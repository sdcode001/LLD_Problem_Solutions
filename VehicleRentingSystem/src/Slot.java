import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Slot {
    private Date startTime;
    private Date endTime;
    
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = this.processDateTime(startTime);
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = this.processDateTime(endTime);
	}
    
	public static Date processDateTime(String datetime) {
		//Example- "20th Feb 10:00 AM"
		Date result = new Date();
		datetime = datetime.replaceAll("(st|nd|rd|th)", "");
		SimpleDateFormat sdf = new SimpleDateFormat("d MMM hh:mm a");
        try {
			return sdf.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
	}
    
}
