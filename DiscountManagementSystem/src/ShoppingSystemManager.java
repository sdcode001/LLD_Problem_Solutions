import java.util.HashMap;

//Singleton
public class ShoppingSystemManager {
	private static ShoppingSystemManager instance = new ShoppingSystemManager();
	
	private HashMap<Integer, Product> storedProducts = new HashMap();
	private HashMap<Integer, Cart> carts = new HashMap();
	private HashMap<Integer, Coupon> coupons= new HashMap();
	private Object lock = new Object();
	
    private ShoppingSystemManager() { }
	
	public static ShoppingSystemManager getInstance() {
		return instance;
	}
	
	public void addProduct(Product p) {
		this.storedProducts.put(p.getId(), p);
	}
	
	public void addCoupon(Coupon c) {
		this.coupons.put(c.getCode(), c);
	}
    
	public void addNewCart(Cart ct) {
		this.carts.put(ct.getId(), ct);
	}
	
	public void addProductToCart(int productId, int cartId, int quantity) {
		//Handling concurrent product addition safely
		synchronized (lock) {
			if(this.carts.containsKey(cartId) && this.storedProducts.containsKey(productId)) {
				Product p = this.storedProducts.get(productId);
				if(quantity <= p.getQuantity()) {
					Product cartProduct = new Product(p.getId(), p.getName(), p.getPrice(), quantity);
					Cart c = this.carts.get(cartId);
					c.addProduct(cartProduct);
					this.carts.replace(c.getId(), c);
					p.setQuantity(p.getQuantity() - quantity);
					System.out.println(quantity+" Product "+productId+" added to cart "+cartId);
				}
				else {
					System.out.println("Product "+productId+" can't be added with quantity "+quantity);
				}
			}
			else {
				throw new CartOrProductNotFoundException("Either Cart or Product with this Id not available in system");
			}
		}
	}
	
	public void addCouponToCart(int couponId, int cartId) {
		if(this.carts.containsKey(cartId) && this.coupons.containsKey(couponId)) {
			Coupon c = this.coupons.get(couponId);
			Coupon cartCoupon = new Coupon(c.getCode(), c.getType(), c.getMaxDiscountPrice(), c.getMinDiscountPrice(), c.getDiscountPercentage());
			Cart ct = this.carts.get(cartId);
			ct.addCoupon(cartCoupon);
			this.carts.replace(ct.getId(), ct);
		}
		else {
			throw new CartOrCouponNotFoundException("Either Cart or Coupon with this Id not available in system");
		}
	}
	
	public void removeProductFromCart(int productId, int cartId, int quantity) {
		//Handling concurrent product remove safely
		synchronized (lock) {
			if(this.carts.containsKey(cartId)) {
				Cart c = this.carts.get(cartId);
				Product p = c.getProductWithId(productId);
				if(p!=null && quantity <= p.getQuantity()) {
					p.setQuantity(p.getQuantity()-quantity);
					if(p.getQuantity()==0) {
						c.removeProduct(productId);
					}
					Product storeProduct = this.storedProducts.get(productId);
					storeProduct.setQuantity(storeProduct.getQuantity()+quantity);
				}
				this.carts.replace(c.getId(), c);
				System.out.println(quantity+" Product "+productId+" removed from cart "+cartId);
			}
			else {
				throw new CartNotFoundException("Cart with : "+cartId+" not available in system");
			}
		}
	}
	
	public void removeCouponFromCart(int couponId, int cartId) {
		if(this.carts.containsKey(cartId)) {
			Cart c = this.carts.get(cartId);
			c.removeCoupon(couponId);
			this.carts.replace(c.getId(), c);
		}
		else {
			throw new CartNotFoundException("Cart with : "+cartId+" not available in system");
		}
	}
	
	public void applyCoupon(int cartId, int couponId) {
		if(this.carts.containsKey(cartId)) {
			Cart ct = this.carts.get(cartId);
			Coupon c = ct.getCouponWithId(couponId);
			if(c!=null) {
				CouponDiscountStrategy strategy = null;
				if(c.getType() == CouponType.FLAT) {
					strategy = new FlatDiscountStrategy();
				}
				else {
					strategy = new PercentageDiscountStrategy();
				}
				
				DiscountContext context = new DiscountContext();
				context.setStrategy(strategy);
				int discountAmount = context.getDiscountAmount(ct, couponId);
				
				if(ct.getTotalPrice() >= discountAmount && c.getStatus() == CouponStatus.NOT_APPLIED) {
					ct.setTotalPrice(ct.getTotalPrice() - discountAmount);
					c.setStatus(CouponStatus.APPLIED);
					c.setDiscountAmount(discountAmount);
					System.out.println("Coupon with Id: "+couponId+" applied on cart "+cartId+". Now Cart total price = "+ct.getTotalPrice());
				}
				else {
					System.out.println("Coupon with Id: "+couponId+" can't be applied in Cart: "+cartId);
				}
			}
			else {
				System.out.println("Coupon with Id: "+couponId+" not available in Cart: "+cartId);
			}
		}
		else {
			throw new CartNotFoundException("Cart with : "+cartId+" not available in system");
		}
		
	}
	
	
	public void removeAppliedCoupon(int cartId, int couponId) {
		if(this.carts.containsKey(cartId)) {
			Cart ct = this.carts.get(cartId);
			Coupon c = ct.getCouponWithId(couponId);
			if(c!=null) {
				if(c.getStatus() == CouponStatus.APPLIED) {
					ct.setTotalPrice(ct.getTotalPrice() + c.getDiscountAmount());
					c.setStatus(CouponStatus.NOT_APPLIED);
					c.setDiscountAmount(0);
					System.out.println("Applied Coupon: "+couponId+" removed from cart "+cartId+". Now Cart total price = "+ct.getTotalPrice());
				}
				else {
					System.out.println("Coupon with Id: "+couponId+" not applied in cart "+cartId);
				}
			}
			else {
				System.out.println("Coupon with Id: "+couponId+" not available in Cart: "+cartId);
			}
		}
		else {
			throw new CartNotFoundException("Cart with : "+cartId+" not available in system");
		}
	}
	
	
	public void completePurchase(int cartId) {
		//Handling concurrent purchase safely
		synchronized (lock) {
			if(this.carts.containsKey(cartId)) {
				Cart ct = this.carts.get(cartId);
				if(this.processPayment()) {
					//remove products from system product store.
					ct.getProducts().forEach(v -> {
					   Product p = this.storedProducts.get(v.getId());
					   p.setQuantity(p.getQuantity()-v.getQuantity());
					   if(p.getQuantity()==0) {this.storedProducts.remove(p.getId());}
					});
					ct.removeAllProducts();
					
					ct.setTotalPrice(0);
					
					//removed all applied coupons from cart
					ct.removeAllCoupons();
					
					System.out.println("Purches completed for Cart: "+cartId);
				}
				else {
					System.out.println("Payment failed for order of Cart: "+cartId);
				}
			}
			else {
				throw new CartNotFoundException("Cart with : "+cartId+" not available in system");
			}
		}
	}
	
	private boolean processPayment() {
		System.out.println("Payment processing......please wait");
		//Randomly get payment processing decision...
		if(Math.random()>=0.5) {
			return true;
		}
		return false;
	}
	
	public void showAvailableProducts() {
		System.out.println("Products in System store.....");
		this.storedProducts.values().forEach(v -> {
			System.out.println(v);
		});
	}
	
}
