import java.util.Map;

public interface BookingStrategy {
    BookingStatus booking(String vehicleType, String startTime, String endTime, Map<String, Branch> branchMap);
}
