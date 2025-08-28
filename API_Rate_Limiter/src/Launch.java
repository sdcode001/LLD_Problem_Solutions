
public class Launch {

	public static void main(String[] args) {
		//RateLimiter rl = new FixedWindowRateLimiter();
		//RateLimiter rl = new SlidingWindowRateLimiter();
		//RateLimiter rl = new LeakyBucketRateLimiter();
		RateLimiter rl = new TokenBucketRateLimiter();
		
		rl.start();
	}

}
