
public class FlatDiscountStrategy implements CouponDiscountStrategy {

	@Override
	public int getDiscountAmount(Cart cart, int couponCode) {
		Coupon c = cart.getCouponWithId(couponCode);
		return c.getMinDiscountPrice();
	}

}
