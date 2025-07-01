import java.util.ArrayList;
import java.util.List;


//singleton pattern
public class BookingService {
	private static BookingService instance = new BookingService();
	private List<Booking> bookings = new ArrayList();
	private BranchService branchService = BranchService.getInstance();
	private int bookingId = 1000;
   
	private BookingService(){}
	
	public static BookingService getInstance() {
		return instance;
	}
	
	public List<Booking> getBookings(){
		return this.bookings;
	}
	
	public void createBooking(String vehicleType, String startTime, String endTime) {
		//select the booking strategy...
		BookingStrategyContext startegyContext = new BookingStrategyContext();
		BookingStrategy selectedStrategy = new LowestCostBookingStrategy();
		startegyContext.setStrategy(selectedStrategy);
		
		Booking newBooking = new Booking(selectedStrategy);
		newBooking.setId(bookingId++);
		Slot slot = new Slot();
		slot.setStartTime(startTime);
		slot.setEndTime(endTime);
		newBooking.setBookingSlot(slot);
		
		BookingStatus bookVehicleStatus = startegyContext.booking(vehicleType, startTime, endTime, branchService.getBranchMap());
		
		newBooking.setStatus(bookVehicleStatus);
		newBooking.setVehicleType(vehicleType);

		this.bookings.add(newBooking);
	}
	
}
