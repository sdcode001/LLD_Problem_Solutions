# Designing a Shopping Coupon & Discount Management System

## Requirements:
1. Add products to the system with quantity.
2. Add or remove products from Shopping Cart.
3. Apply Coupons to Cart. Coupons should be two types- 1)Falt discount, 2)Percentage discount(e.g- 60% off upto 200)
4. Process payment on a Cart after applying Coupons.
5. On successful order, that should reflect on product store.
6. System should able to handel concurrent orders safely.
7. Add Exception handling

## Classes, Interfaces and Enumerations:
1. The Product class represents a product in the system. with properties- id, name, price, quantity.
2. The Cart class represents a Cart in the system. with properties- id, list of products, total price, coupons applied.
3. The Coupon class represents a Coupon in the system, with properties- code, type, max discount price, min discount price,
discount percentage, status and calculated discount amount for a cart.
4. Discount calculation process based on coupon types is implemented using Strategy pattern.
5. The CouponDiscountStrategy interface, FlatDiscountStrategy class, PercentageDiscountStrategy class and DiscountContext class is used for discount calculation Strategy pattern.
6. The CouponType is constant exporting class, represents coupon types.
7. The CouponStatus is constant exporting class, represents coupon status(applied or not).
8. The ShoppingSystemManager class is the central class which manages carts, products, coupons and payments process. It's implemented using singleton pattern.
9. The Startup class is used to perform all the operations.
10. Added synchronized locking to handel concurrent stored product changes safely while adding or removing products to cart and purchase of cart products.
   
## Design Patterns Used:
1. Singleton pattern is used for ShoppingSystemManager class.
2. Strategy pattern is used to implement discount calculation process based on coupon types

## Implementation
#### [Java Implementation](./src/) 

