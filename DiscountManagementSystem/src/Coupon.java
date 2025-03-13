
public class Coupon {
	private int code;
	private String type;
	private int maxDiscountPrice; //percentage discount threshold
	private int minDiscountPrice; //flat discount 
	private int discountPercentage;
	private int discountAmount;
	private String status;
	
	public Coupon(int code, String type, int maxDiscountPrice, int minDiscountPrice, int discountPercentage) {
		this.code = code;
		this.type = type;
		this.maxDiscountPrice = maxDiscountPrice;
		this.minDiscountPrice = minDiscountPrice;
		this.discountPercentage = discountPercentage;
		this.discountAmount = 0;
		this.status = CouponStatus.NOT_APPLIED;
	}
	
	

	public int getCode() {
		return code;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public int getMaxDiscountPrice() {
		return maxDiscountPrice;
	}

	public void setMaxDiscountPrice(int maxDiscountPrice) {
		this.maxDiscountPrice = maxDiscountPrice;
	}

	public int getMinDiscountPrice() {
		return minDiscountPrice;
	}

	public void setMinDiscountPrice(int minDiscountPrice) {
		this.minDiscountPrice = minDiscountPrice;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int amount) {
		this.discountAmount = amount;
	}

}
