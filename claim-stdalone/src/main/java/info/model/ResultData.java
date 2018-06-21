package info.model;

public class ResultData {

	// wbs element for a specific account
	private String wbsEl;

	// type of OT (ses3 or seot)
	private String otType;

	// week days
	private String sat, sun, mon, tue, wen, thu, fri;

	// week number withint specific year
	private int weekNo;

	public ResultData() {
		wbsEl = null;
		otType = null;
		sat = null;
		sun = null;
		mon = null;
		tue = null;
		wen = null;
		thu = null;
		fri = null;
	}

	// getters and setters
	public String getWbsEl() {
		return wbsEl;
	}

	public void setWbsEl(String wbsEl) {
		this.wbsEl = wbsEl;
	}

	public String getOtType() {
		return otType;
	}

	public void setOtType(String otType) {
		this.otType = otType;
	}

	public String getSat() {
		return sat;
	}

	public void setSat(String sat) {
		this.sat = sat;
	}

	public String getSun() {
		return sun;
	}

	public void setSun(String sun) {
		this.sun = sun;
	}

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public String getTue() {
		return tue;
	}

	public void setTue(String tue) {
		this.tue = tue;
	}

	public String getWen() {
		return wen;
	}

	public void setWen(String wen) {
		this.wen = wen;
	}

	public String getThu() {
		return thu;
	}

	public void setThu(String thu) {
		this.thu = thu;
	}

	public String getFri() {
		return fri;
	}

	public void setFri(String fri) {
		this.fri = fri;
	}

	public int getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(int weekNo) {
		this.weekNo = weekNo;
	}

}
