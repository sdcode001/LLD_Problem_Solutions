import java.util.ArrayDeque;
import java.util.Deque;


//This rate limiter limits API request irrespective of User-Id
public class LeakyBucketRateLimiter implements RateLimiter{
	private static final int BUCKET_SIZE = 5;
	//Number of requests that can be processed at a fixed rate by the FixedRateRequestProcessor(i.e- 3 requests per second)
	private static final int OUTFLOW_RATE = 3;
	private Deque<Request> bucket = new ArrayDeque<>();
	
	
	//this will be populated by ClientRequestSender and used for simulating incoming request.
    private Deque<Request> requestQueue = new ArrayDeque<>();
    
    private Object lock = new Object();
    private Object bucketLock = new Object();
    
    
	@Override
	public boolean processRequest(Request request) {
		boolean result = false;
		synchronized(bucketLock) {
			if(bucket.size() < BUCKET_SIZE) {
				bucket.addLast(request);
				result = true;
			}
		}
		return result;
	}

	@Override
	public void start() {
		//start the ClientRequestSender and FixedRateRequestProcessor in new Threads concurrently.
		Thread clientRequestSender = new Thread(new ClientRequestSender());
		clientRequestSender.start();
		Thread fixedRateRequestProcessor = new Thread(new FixedRateRequestProcessor());
		fixedRateRequestProcessor.start();
		
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
		fixedRateRequestProcessor.interrupt();
	}
	
	
	//This task will simulate- popping request from bucket at fixed rate.
	private class FixedRateRequestProcessor implements Runnable {
		@Override
		public void run() {
			while(!Thread.currentThread().interrupted()) {
				synchronized(bucketLock) {
					if(!bucket.isEmpty()) {
						Request request = bucket.pollFirst();
						//send request to API Gateway Server
					}
				}
				
				//process request in every (1/OUTFLOW_RATE) second. 
				try {
					Thread.currentThread().sleep((1000/OUTFLOW_RATE));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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
				
				//Add random delay between 0-500 ms.
                try {
					Thread.currentThread().sleep((long)(Math.random()*500));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
