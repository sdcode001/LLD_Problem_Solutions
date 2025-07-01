import java.util.Date;
import java.util.List;
import java.util.Map;

public class LowestCostBookingStrategy implements BookingStrategy{

	@Override
	public BookingStatus booking(String vehicleType, String startTime, String endTime, Map<String, Branch> branchMap) {
		String targetBranch = null;
		Vehicle targetVehicle = null;
		double lowestPrice = Double.MAX_VALUE;
		Date bookingStart = Slot.processDateTime(startTime);
		Date bookingEnd = Slot.processDateTime(endTime);
		
		for(Branch branch: branchMap.values()) {
			for(List<Vehicle> vehicleTypeList: branch.getVehicleMap().values()) {
				for(Vehicle vehicle: vehicleTypeList) {
					if(vehicle.getType() == vehicleType) {
						//check if vehicle is available for the given slot or not
						if(vehicle.getBookingSlot()==null || 
						   (bookingStart.compareTo(vehicle.getBookingSlot().getEndTime())>0 || bookingEnd.compareTo(vehicle.getBookingSlot().getStartTime())<0)
						){
							if(vehicle.getCostPerHour() < lowestPrice) {
								lowestPrice = vehicle.getCostPerHour();
								targetVehicle = vehicle;
								targetBranch = branch.getName();
							}
						}
					}
				}
			}
		}
		
		if(targetVehicle != null) {
			Slot newSlot = new Slot();
			newSlot.setStartTime(startTime);
			newSlot.setEndTime(endTime);
			targetVehicle.setBookingSlot(newSlot);
			System.out.println("Booked "+vehicleType+" from branch- "+targetBranch+" for per hour cost- "+targetVehicle.getCostPerHour());
			return BookingStatus.BOOKED;
		}
		
		System.out.println("No "+vehicleType+" available for the given slot!");
		return BookingStatus.NOT_BOOKED;
	}

}
