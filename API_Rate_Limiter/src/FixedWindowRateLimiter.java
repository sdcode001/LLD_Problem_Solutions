import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


//This rate limiter limits API request with respect to User-Id
public class FixedWindowRateLimiter implements RateLimiter {
	//maximum number of requests allowed in a window
    private static final int THRESHOLD = 3;
    private static final long WINDOW_LENGHT_MILLISECONDS = 1000;
    
    //this will be populated by ClientRequestSender and used for simulating incoming request.
    private Deque<Request> requestQueue = new ArrayDeque<>();
    
    //maintain state of each user request.
    private Map<Integer, FixedWindow> hashMap = new HashMap<>();
    
    private Object lock = new Object(); 
	
	private class FixedWindow {
		public int count;
		public long timeStampMills;
		
		public FixedWindow(int count, long timeStampMills) {
			this.count = count;
			this.timeStampMills = timeStampMills;
		}
	}
	
	
	@Override
	public boolean processRequest(Request request) {
		FixedWindow fixedWindow = hashMap.get(request.getUserId());
		Long currentTimeMills = System.currentTimeMillis();
		//request form user comes for the first time.
		if(fixedWindow==null) {
			hashMap.put(request.getUserId(), new FixedWindow(1,currentTimeMills));
			return true;
		}
		//check request exceed the window
		if(currentTimeMills-fixedWindow.timeStampMills > WINDOW_LENGHT_MILLISECONDS) {
			fixedWindow.count = 1;
			fixedWindow.timeStampMills = currentTimeMills;
			hashMap.put(request.getUserId(), fixedWindow);
			return true;
		}
		//check if we have breached the THRESHOLD already
		if(fixedWindow.count < THRESHOLD) {
			fixedWindow.count++;
			hashMap.put(request.getUserId(), fixedWindow);
			return true;
		}
		
		return false;
	}
	

	@Override
	public void start() {
		//start the ClientRequestSender in a new Thread concurrently
		Thread clientRequestSender = new Thread(new ClientRequestSender());
		clientRequestSender.start();
		
		while(!Thread.currentThread().interrupted()) {
			//pop request from requestQueue and process
			synchronized(lock) {
				if(!requestQueue.isEmpty()) {
					Request request = requestQueue.pollFirst();
					System.out.print("Request came from user - "+request.getUserId());
					boolean result = processRequest(request);
					System.out.println(", Sent to API Gateway Server? " + (result ? "Yes" : "No"));
				}
			}
			
		}
		
		clientRequestSender.interrupt();
	}
	
	
	//This task will simulate- request sending from client side and will send new request with random delay
	private class ClientRequestSender implements Runnable {
		@Override
		public void run() {
			while(!Thread.currentThread().interrupted()) {
				synchronized(lock) {
					//get random userId between 1000 - 1002
					int userId = (int)(Math.random() * 3) + 1000;
					requestQueue.addLast(new Request(userId));
				}
				
				//Add random delay between 0-100 ms.
                try {
					Thread.currentThread().sleep((long)(Math.random()*100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
	    }
    }

}
