package info.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;

import info.model.ClaimCodes;
import info.model.ResultData;
import info.model.UserData;

public class CountData {

	// summary of OT hours
	double otAmountInSummary = 0;

	// name of the file with data
	String fileToCheck;

	// weekday names in format: Mon, Tue...
	String day1Name;
	String day2Name;

	// days difference between 2 provided dates
	int dayDifference = 0;

	// start and end date of OT in format yyyy/MM/dd
	String otStartDate;
	String otEndDate;

	// start and end hours of OT in format HH:mm
	String otStartHour;
	String otEndHour;

	// # of standby hours within 1st and 2nd day
	String sb1Day;
	String sb2Day;

	// formated start and end date (yyyy/MM/dd HH:mm)
	String fStartDate;
	String fEndDate;

	// date formats which will be used
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	SimpleDateFormat fDays = new SimpleDateFormat("yyyy/MM/dd");
	DecimalFormat df = new DecimalFormat("#.##");

	// start and end dates in format yyyy/MM/dd HH:mm
	Date dStartDate;
	Date dEndDate;

	// start and end dates in format yyyy/MM/dd
	Date ddStartDate;
	Date ddEndDate;

	// start and end dates in format DateTime
	DateTime dtStartDate;
	DateTime dtEndDate;
	DateTime ddtStartDate;
	DateTime ddtEndDate;

	// ses3 i seot times
	double ses30 = 0.00f;
	double seot0 = 0.00f;

	// additional variables in case when there is a time difference and OT times
	// for both days are needed
	double ses31 = 0.00f;
	double seot1 = 0.00f;

	// collection with names of weekdays
	final HashMap<Integer, String> weekdayNames = new HashMap<>();

	// claim code of selected account
	String cCode;

	// collections
	public List<ResultData> resDataObj = new ArrayList<>();
	public List<UserData> userDataObj = new ArrayList<>();
	public List<ClaimCodes> claimCodesObj = new ArrayList<>();

	// variable which helps to avoid calculation errors
	double oneMin = 0.017;

	// constructor sets file to check provided by the user
	public CountData(String ftc) {
		this.fileToCheck = ftc;
	}

	// sets imports all data required in further calculations
	public void prepareAndCountData() {

		GetSetFileData gsfd = new GetSetFileData(fileToCheck);

		// fill collection with name of weekdays
		weekdayNames.put(1, "Mon");
		weekdayNames.put(2, "Tue");
		weekdayNames.put(3, "Wen");
		weekdayNames.put(4, "Thu");
		weekdayNames.put(5, "Fri");
		weekdayNames.put(6, "Sat");
		weekdayNames.put(7, "Sun");

		// imports user data and claim codes from file selected by the user
		userDataObj.addAll(gsfd.getUserData());
		claimCodesObj.addAll(gsfd.getClaimCodes());

		for (UserData ud : userDataObj) {
			
			otStartDate = ud.getStartDate();
			System.out.println("otStartDate equals " + ud.getStartDate());
			
			otEndDate = ud.getEndDate();
			System.out.println("otEndDate equals " + otEndDate);
			
			otStartHour = ud.getStHour();
			System.out.println("otStartHour equals " + otStartHour);
			
			otEndHour = ud.getEndHour();
			System.out.println("otEndHour equals " + otEndHour);

			// decrease end hour (00:00) to avoid calculation errors
			if (otEndHour.equals("00:00")) {
				otEndHour = "23:59";
			}

			sb1Day = ud.getSb1Day();
			sb2Day = ud.getSb2Day();

			// change String format to yyyy/MM/dd HH:mm
			fStartDate = otStartDate + " " + otStartHour;
			fEndDate = otEndDate + " " + otEndHour;

			// change String to appropriate format of Date
			try {

				String accName;

				// yyyy/MM/dd HH:mm
				dStartDate = format.parse(fStartDate);
				dEndDate = format.parse(fEndDate);

				// yyyy/MM/dd
				ddStartDate = fDays.parse(fStartDate);
				ddEndDate = fDays.parse(fEndDate);

				// DateTime yyyy/MM/dd HH:mm
				dtStartDate = new DateTime(dStartDate);
				dtEndDate = new DateTime(dEndDate);

				// DateTime yyyy/MM/dd
				ddtStartDate = new DateTime(ddStartDate);
				ddtEndDate = new DateTime(ddEndDate);

				// count day difference between start and end of OT
				this.dayDifference = Days.daysBetween(ddtStartDate, ddtEndDate).getDays();

				// gets account name from user date
				accName = ud.getAccountName().trim().toLowerCase();

				// searches for account name and corresponding claim code
				for (ClaimCodes cc : claimCodesObj) {
					if (accName.equals(cc.getAccountName().trim().toLowerCase())) {
						// sets appropriate claim code
						cCode = cc.getClaimCode();
					} else {
						cCode = "Claim code for provided account not known.";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			claimDataCount();
		}
		System.out.println("\n*****\n Check resDataObj size: " + resDataObj.size());
		gsfd.setResultData(resDataObj);
	}

	public void claimDataCount() {

		// minutes difference between 2 provided dates
		int minDiff = 0;

		// # of day within a week, will help to set its name
		int dayOfWeekNo0 = 0;
		int dayOfWeekNo1 = 0;

		minDiff = Minutes.minutesBetween(dtStartDate, dtEndDate).getMinutes();
		otAmountInSummary = (double) minDiff / 60;

		// start appropriate actions if OT were done within the one calendar day
		if (dayDifference == 0) {
			try {
				// reference date, needed to count yyyy/MM/dd 00:00
				String sRefDate = otStartDate + " 00:00";
				Date sdRefDate = format.parse(sRefDate);
				DateTime sddRefDate = new DateTime(sdRefDate);

				// difference (in minutes) between midnight and OT start time
				int iMinDiff0 = 0;
				double fMinDiff0 = 0;

				// gets name of a weekday based on weekday #
				dayOfWeekNo0 = dtStartDate.getDayOfWeek();
				day1Name = weekdayNames.get(dayOfWeekNo0);

				iMinDiff0 = Minutes.minutesBetween(sddRefDate, dtStartDate).getMinutes();
				fMinDiff0 = (double) iMinDiff0 / 60;

				if (Float.parseFloat(sb1Day) < fMinDiff0) {

					// OT time is not included within standby hours
					seot0 = otAmountInSummary;
				} else if (Float.parseFloat(sb1Day) >= fMinDiff0) {
					// ses3 is equal to difference between amount of standby
					// hours and OT start hour
					ses30 = Float.parseFloat(sb1Day) - fMinDiff0;
					seot0 = otAmountInSummary - ses30;
				}

				// adding data in case when OT occurred
				// ses3
				if (ses30 > 0) {

					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SES3");
					establishAndSetWeekday(ses30, day1Name, rd);
					// sets # of week within specific year
					// if it's Saturday or Sunday add 1 to get Sat -> Friday
					// week period
					if (day1Name.equals("Sat") || day1Name.equals("Sun"))
						rd.setWeekNo(establisWeekNo(ddtStartDate) + 1);
					else
						rd.setWeekNo(establisWeekNo(ddtStartDate));
					resDataObj.add(rd);

				}

				// adding data in case when OT occurred
				// ses0
				if (seot0 > 0) {
					// adds 1 minute in case end hours is equal to 00:00
					if (otEndHour.equals("23:59"))
						seot0 = (seot0 + oneMin);

					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SEOT");
					establishAndSetWeekday(seot0, day1Name, rd);
					rd.setWeekNo(establisWeekNo(ddtStartDate));
					resDataObj.add(rd);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// start appropriate actions if OT were done within the two calendar
		// days
		else if (dayDifference > 0) {
			try {
				// reference date needed to count yyyy/MM/dd 23:59
				String sRefDate = otStartDate + " 23:59";
				Date sdRefDate = format.parse(sRefDate);
				DateTime sddRefDate = new DateTime(sdRefDate);

				// minutes difference between midnight at the end of first day
				// and OT start hour
				int iMinDiff0 = 0;
				double fMinDiff0 = 0;

				// reference time (number of hours within one day)
				final double dayHoursAmount = 24;

				// variable which helps to count amount of hours within standy
				double sbHtoMidnight = 0;

				// reference variable which contains planned standby hours
				// (double format)
				double sb1DayRef = Float.parseFloat(sb1Day);
				double sb2DayRef = Float.parseFloat(sb2Day);

				// gets name of a weekday based on weekday #
				dayOfWeekNo0 = dtStartDate.getDayOfWeek();
				day1Name = weekdayNames.get(dayOfWeekNo0);

				iMinDiff0 = Minutes.minutesBetween(dtStartDate, sddRefDate).getMinutes();

				// adds missing minute to midnight
				iMinDiff0 = iMinDiff0 + 1;
				fMinDiff0 = (double) iMinDiff0 / 60;

				sbHtoMidnight = dayHoursAmount - fMinDiff0;

				// results for 1st day
				if (sb1DayRef > sbHtoMidnight) {
					ses30 = sb1DayRef - sbHtoMidnight;
					seot0 = fMinDiff0 - ses30;
				} else if (sb1DayRef <= sbHtoMidnight) {
					seot0 = fMinDiff0;
				}

				// # of OT for 2nd day
				double day2OT = 0;

				day2OT = otAmountInSummary - (ses30 + seot0);

				if (sb2DayRef < day2OT) {
					ses31 = sb2DayRef;
					seot1 = day2OT - sb2DayRef;
				} else if (sb2DayRef >= day2OT) {
					ses31 = day2OT;
				}

				// gets name of a weekday based on weekday #
				dayOfWeekNo1 = dtEndDate.getDayOfWeek();
				day2Name = weekdayNames.get(dayOfWeekNo1);

				// fills data in case ses3 for 1st day occurred
				if (ses30 > 0) {
					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SES3");
					establishAndSetWeekday(ses30, day1Name, rd);

					// sets # of week within specific year
					// if it's Saturday or Sunday add 1 to get Sat -> Friday
					// week period
					if (day1Name.equals("Sat") || day1Name.equals("Sun"))
						rd.setWeekNo(establisWeekNo(ddtStartDate) + 1);
					else
						rd.setWeekNo(establisWeekNo(ddtStartDate));

					resDataObj.add(rd);

				}

				// fills data in case sesot for 1st day occurred
				if (seot0 > 0) {

					// adds one minute in case when end hour is equal to 00:00
					if (otEndHour.equals("23:59"))
						seot0 = (seot0 + oneMin);

					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SEOT");
					establishAndSetWeekday(seot0, day1Name, rd);

					// sets # of week within specific year
					// if it's Saturday or Sunday add 1 to get Sat -> Friday
					// week period
					if (day1Name.equals("Sat") || day1Name.equals("Sun"))
						rd.setWeekNo(establisWeekNo(ddtStartDate) + 1);
					else
						rd.setWeekNo(establisWeekNo(ddtStartDate));

					resDataObj.add(rd);

				}

				// fills data in case ses3 for 2nd day occurred
				if (ses31 > 0) {
					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SES3");
					establishAndSetWeekday(ses31, day2Name, rd);

					// sets # of week within specific year
					// if it's Saturday or Sunday add 1 to get Sat -> Friday
					// week period
					if (day1Name.equals("Sat") || day1Name.equals("Sun"))
						rd.setWeekNo(establisWeekNo(ddtStartDate) + 1);
					else
						rd.setWeekNo(establisWeekNo(ddtStartDate));
					resDataObj.add(rd);

				}

				// fills data in case sesot for 2nd day occurred
				if (seot1 > 0) {

					// adds 1 minute in case when end hour is equal to 00:00
					if (otEndHour.equals("23:59"))
						seot0 = (seot0 + oneMin);

					ResultData rd = new ResultData();
					rd.setWbsEl(cCode);
					rd.setOtType("SEOT");
					establishAndSetWeekday(seot1, day2Name, rd);

					// sets # of week within specific year
					// if it's Saturday or Sunday add 1 to get Sat -> Friday
					// week period
					if (day1Name.equals("Sat") || day1Name.equals("Sun"))
						rd.setWeekNo(establisWeekNo(ddtStartDate) + 1);
					else
						rd.setWeekNo(establisWeekNo(ddtStartDate));
					resDataObj.add(rd);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public List<ResultData> provideRD() { return resDataObj; }
	 */

	// method responsible for adding appropriate # of hours for reported OT
	void establishAndSetWeekday(double ot, String dName, ResultData rd) {
		if (dName.equals("Mon"))
			rd.setMon(df.format(ot));
		else if (dName.equals("Tue"))
			rd.setTue(df.format(ot));
		else if (dName.equals("Wen"))
			rd.setWen(df.format(ot));
		else if (dName.equals("Thu"))
			rd.setThu(df.format(ot));
		else if (dName.equals("Fri"))
			rd.setFri(df.format(ot));
		else if (dName.equals("Sat"))
			rd.setSat(df.format(ot));
		else if (dName.equals("Sun"))
			rd.setSun(df.format(ot));
	}

	// method responsible for setting appropriate week # within specified year
	// based on specified day
	int establisWeekNo(DateTime date) {
		return date.getWeekOfWeekyear();
	}
}
