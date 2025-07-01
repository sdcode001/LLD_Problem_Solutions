
public class Executor {

	public static void main(String[] args) {
		BranchService branchService = BranchService.getInstance();
		BookingService bookingService = BookingService.getInstance();
		
		branchService.addBranch("Madhapur", new String[] {
				"1 suv for Rs.12 per hour",
				"3 sedan for Rs.10 per hour",
				"3 bike for Rs.20 per hour"
		});
		
		branchService.addBranch("Kondapur", new String[] {
				"3 sedan for Rs.11 per hour",
				"3 bike for Rs.30 per hour",
				"4 hatchback for Rs.8 per hour"
		});
		
		branchService.addBranch("Jubilee Hill", new String[] {
				"1 suv for Rs.11 per hour",
				"10 bike for Rs.3 per hour",
				"3 sedan for Rs.10 per hour"
		});
		
		branchService.addVehicle("Madhapur", "1 sedan");
		
		bookingService.createBooking("suv", "20th Feb 10:00 AM", "20th Feb 12:00 PM");
		
		bookingService.createBooking("suv", "20th Feb 10:00 AM", "20th Feb 12:00 PM");
		
		bookingService.createBooking("suv", "20th Feb 10:00 AM", "20th Feb 12:00 PM");
	}

}
