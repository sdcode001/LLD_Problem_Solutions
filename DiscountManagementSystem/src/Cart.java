import java.util.HashMap;
import java.util.List;

public class Cart {
   private int id;
   private HashMap<Integer, Product> products;
   private int totalPrice;
   private HashMap<Integer, Coupon> appliedCoupons;
   
   public Cart(int id) {
	   this.id = id;
   }
   
   public int getId() {
	   return this.id;
   }
   
   public void removeAllProducts() {
	   this.products.clear();
   }
   
   public void removeAllCoupons() {
	   this.appliedCoupons.clear();
   }
   
   public List<Product> getProducts(){
	   return (List<Product>)this.products.values();
   }
   
   public Product getProductWithId(int id) {
	   return this.products.get(id);
   }
   
   public Coupon getCouponWithId(int id) {
	   return this.appliedCoupons.get(id);
   }
   
   public List<Coupon> getCoupons(){
	   return (List<Coupon>)this.appliedCoupons.values();
   }
   
   public void addProduct(Product p) {
	   if(!this.products.containsKey(p.getId())) {
		   this.products.put(p.getId(), p);
		   this.calculateTotalPrice();
	   }
   }
   
   public void removeProduct(int productId) {
	   if(this.products.containsKey(productId)) {
		   this.products.remove(productId);
		   this.calculateTotalPrice();
	   }
	   else {
		   System.out.println("Product "+productId+" is not available to remove from Cart "+this.id);
	   }
   }
   
   public void addCoupon(Coupon c) {
	   if(!this.appliedCoupons.containsKey(c.getCode())) {
		   this.appliedCoupons.put(c.getCode(), c);
	   }
   }
   
   public void removeCoupon(int couponId) {
	   if(this.appliedCoupons.containsKey(couponId)) {
		   this.appliedCoupons.remove(couponId);
	   }
	   else {
		   System.out.println("Coupon "+couponId+" is not available to remove from Cart "+this.id);
	   }
   }
   
   private void calculateTotalPrice() {
	   this.totalPrice=0;
	   this.products.values().forEach(v -> {
		   this.totalPrice += v.getPrice()*v.getQuantity();
	   });
   }
   
   public int getTotalPrice() {
	   this.calculateTotalPrice();
	   return this.totalPrice;
   }
   
   public void setTotalPrice(int price) {
	   this.totalPrice=price;
   }
   
}
