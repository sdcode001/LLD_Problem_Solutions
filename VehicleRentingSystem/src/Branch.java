import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
	private String name;
	private Map<String, List<Vehicle>> vehicleMap = new HashMap();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, List<Vehicle>> getVehicleMap() {
		return vehicleMap;
	}

	public void addVehicle(String vehicleType, Vehicle vehicle) {
		if(this.vehicleMap.containsKey(vehicleType)) {
			List<Vehicle> list = this.vehicleMap.get(vehicleType);
			list.add(vehicle);
			this.vehicleMap.put(vehicleType, list);
		}
		else {
			List<Vehicle> list = new ArrayList();
			list.add(vehicle);
			this.vehicleMap.put(vehicleType, list);
		}
	}

}
