
public class DiscountContext {
   private CouponDiscountStrategy strategy;
   
   public void setStrategy(CouponDiscountStrategy strategy) {
	   this.strategy = strategy;
   }
   
   public int getDiscountAmount(Cart cart, int couponCode) {
	   return this.strategy.getDiscountAmount(cart, couponCode);
   }
}
