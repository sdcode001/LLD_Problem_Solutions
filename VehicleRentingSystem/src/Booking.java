
public class Booking {
	private int id;
	private BookingStatus status;
	private Slot bookingSlot;
	private String vehicleType;
	private BookingStrategy bookingStrategy;
	
	public Booking(BookingStrategy strategy) {
		this.bookingStrategy = strategy;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BookingStatus getStatus() {
		return status;
	}
	public void setStatus(BookingStatus status) {
		this.status = status;
	}
	public Slot getBookingSlot() {
		return bookingSlot;
	}
	public void setBookingSlot(Slot bookingSlot) {
		this.bookingSlot = bookingSlot;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public BookingStrategy getBookingStrategy() {
		return bookingStrategy;
	}
	
	
}
