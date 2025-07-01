import java.util.HashMap;
import java.util.Map;


//singleton pattern
public class BranchService {
    private static BranchService instance = new BranchService();
    private Map<String, Branch> branchMap = new HashMap();
    private int vechicleId = 1000;
    
    private BranchService(){}
    
    public static BranchService getInstance() {
    	return instance;
    }
    
    public Map<String, Branch> getBranchMap() {
    	return this.branchMap;
    }
    
    public void addBranch(String branchName, String[] items) {
    	Branch branch = new Branch();
    	branch.setName(branchName);
    	
    	for(String item: items) {
    		//Example items[i]- "3 sedan for Rs.12 per hour"
    		String[] details = item.split(" ");
    		int quantity = Integer.parseInt(details[0]);
    		String vehicleType = details[1];
    		int perHourCost = Integer.parseInt(details[3].substring(3));
    		for(int i=0;i<quantity;i++) {
    			Vehicle v = new Vehicle();
    			v.setCostPerHour(perHourCost);
    			v.setId(this.vechicleId++);
    			v.setType(vehicleType);
    			v.setBookingSlot(null);
    			branch.addVehicle(vehicleType, v);
    		}
    	}
    	
    	branchMap.put(branchName, branch);
    }
    
    public void addVehicle(String branchName, String item) {
    	//Example item- "3 sedan"
    	if(this.branchMap.containsKey(branchName)) {
    		Branch targetBranch = this.branchMap.get(branchName);
    		String[] details = item.split(" ");
    		int quantity = Integer.parseInt(details[0]);
    		String vehicleType = details[1];
    		
    		if(targetBranch.getVehicleMap().containsKey(vehicleType)) {
    			Vehicle commonVehicle = targetBranch.getVehicleMap().get(vehicleType).get(0);
    			
    			for(int i=0;i<quantity;i++) {
        			Vehicle v = new Vehicle();
        			v.setCostPerHour(commonVehicle.getCostPerHour());
        			v.setId(this.vechicleId++);
        			v.setType(vehicleType);
        			v.setBookingSlot(null);
        			targetBranch.addVehicle(vehicleType, v);
        		}
    			System.out.println("Added "+quantity+" "+vehicleType+ " to "+branchName);
    		}
    		else {
    			System.out.println("Vehicle type "+vehicleType+" not found!");
    		}
    	}
    	else {
    		System.out.println("Branch "+branchName+" not found!");
    	}
    }
    
    
}
