import java.util.Map;

public class BookingStrategyContext implements BookingStrategy{
	private BookingStrategy strategy;
	
	public void setStrategy(BookingStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public BookingStatus booking(String vehicleType, String startTime, String endTime, Map<String, Branch> branchMap) {
		return this.strategy.booking(vehicleType, startTime, endTime, branchMap);
	}

}
