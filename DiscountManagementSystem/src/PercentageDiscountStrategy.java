
public class PercentageDiscountStrategy implements CouponDiscountStrategy {

	@Override
	public int getDiscountAmount(Cart cart, int couponCode) {
		Coupon c = cart.getCouponWithId(couponCode);
		int percentageAmount = (cart.getTotalPrice()*c.getDiscountPercentage())/100;
		return Math.min(percentageAmount, c.getMaxDiscountPrice());
	}

}
