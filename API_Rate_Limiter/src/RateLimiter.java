
public interface RateLimiter {
	//returns false for request drop else true
   boolean processRequest(Request request);
   void start();
}
