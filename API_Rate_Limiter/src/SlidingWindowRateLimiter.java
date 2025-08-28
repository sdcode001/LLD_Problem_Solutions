import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


//This rate limiter limits API request with respect to User-Id
public class SlidingWindowRateLimiter implements RateLimiter {
	//maximum number of requests allowed in a period
    private static final int THRESHOLD = 3;
    //this is the period, if it is 1 it means you are gonna allow 3 request in 1 second
    private static final int PERIOD_IN_SECONDS = 1;
    
    //this will be populated by ClientRequestSender and used for simulating incoming request.
    private Deque<Request> requestQueue = new ArrayDeque<>();
    
    //maintain state of each user request.
    private Map<Integer, Deque<Long>> hashMap = new HashMap<>();
    
    private Object lock = new Object();
	
	@Override
	public boolean processRequest(Request request) {
		Deque<Long> timeStamps = hashMap.get(request.getUserId());
		Long currentTimeMills = System.currentTimeMillis();
		//request form user comes for the first time.
		if(timeStamps==null) {
			Deque<Long> newTimeStamps = new ArrayDeque<>();
			newTimeStamps.addLast(currentTimeMills);
			hashMap.put(request.getUserId(), newTimeStamps);
			return true;
		}
		
		//keep on removing the timestamps that are no longer the t to t - PERIOD_IN_SECONDS window
		while(!timeStamps.isEmpty() && timeStamps.getFirst() < (currentTimeMills - PERIOD_IN_SECONDS*1000L)) {
			timeStamps.removeFirst();
		}
		
		//if that window has more than the THRESHOLD requests, drop it
		if(timeStamps.size() >= THRESHOLD) {
			return false;
		}
		
		//otherwise pass it
		timeStamps.addLast(currentTimeMills);
		return true;
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
