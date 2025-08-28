import java.util.ArrayDeque;
import java.util.Deque;


//This rate limiter limits API request irrespective of User-Id
public class TokenBucketRateLimiter implements RateLimiter {
	private static final int BUCKET_SIZE = 5;
	//Number of tokens that can be added at a fixed rate by the FixedRateTokenAdder(i.e- 3 requests per second)
	private static final int BUCKET_REFILL_RATE = 3;
	private volatile int availableTokens = BUCKET_SIZE;
	
	//this will be populated by ClientRequestSender and used for simulating incoming request.
    private Deque<Request> requestQueue = new ArrayDeque<>();
	
	private Object lock = new Object();
    private Object bucketLock = new Object();

    
	@Override
	public boolean processRequest(Request request) {
		boolean result = false;
		synchronized(bucketLock) {
			if(availableTokens > 0) {
				availableTokens--;
				result = true;
			}
		}
		return result;
	}

	
	@Override
	public void start() {
		//start the ClientRequestSender and FixedRateTokenAdder in new Threads concurrently.
		Thread clientRequestSender = new Thread(new ClientRequestSender());
		clientRequestSender.start();
		Thread fixedRateTokenAdder = new Thread(new FixedRateTokenAdder());
		fixedRateTokenAdder.start();
		
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
		fixedRateTokenAdder.interrupt();
	}
	
	
	//This task will simulate- adding token to bucket at fixed rate.
	private class FixedRateTokenAdder implements Runnable {
		@Override
		public void run() {
			while(!Thread.currentThread().interrupted()) {
				synchronized(bucketLock) {
					if(availableTokens < BUCKET_SIZE) {
						availableTokens++;
					}
				}
				
				//refill token in every (1/BUCKET_REFILL_RATE) second. 
				try {
					Thread.currentThread().sleep((1000/BUCKET_REFILL_RATE));
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
