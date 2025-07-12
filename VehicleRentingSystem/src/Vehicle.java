
public class Vehicle {
    private int id;
    private String type;
    private double costPerHour;
    private Slot bookingSlot = null;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getCostPerHour() {
		return costPerHour;
	}
	public void setCostPerHour(double costPerHour) {
		this.costPerHour = costPerHour;
	}
	public Slot getBookingSlot() {
		return bookingSlot;
	}
	public void setBookingSlot(Slot bookingSlot) {
		this.bookingSlot = bookingSlot;
	}
	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", type=" + type + ", costPerHour=" + costPerHour + ", bookingSlot=" + bookingSlot
				+ "]";
	}
    
    
}
