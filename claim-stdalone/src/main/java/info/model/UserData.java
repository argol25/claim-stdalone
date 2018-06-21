package info.model;

public class UserData {

	// account for which OT were done
	private String accountName;

	// OT start date - YYYY/MM/DD
	private String startDate;

	// OT end date - YYYY/MM/DD
	private String endDate;

	// exact hour when OT started - HH:MM
	private String stHour;

	// exact hour when OT ended - HH:MM
	private String endHour;

	// number of standby hours within startTime date
	private String sb1Day;

	// number of standby hours within endTime date
	private String sb2Day;

	// getters and setters
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStHour() {
		return stHour;
	}

	public void setStHour(String stHour) {
		this.stHour = stHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public String getSb1Day() {
		return sb1Day;
	}

	public void setSb1Day(String sb1Day) {
		this.sb1Day = sb1Day;
	}

	public String getSb2Day() {
		return sb2Day;
	}

	public void setSb2Day(String sb2Day) {
		this.sb2Day = sb2Day;
	}
}
