package info.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import info.model.ClaimCodes;
import info.model.ResultData;
import info.model.UserData;

public class GetSetFileData {

	public List<UserData> uData = new ArrayList<>();
	public List<ClaimCodes> cCodes = new ArrayList<>();
	String fileName;

	public GetSetFileData(String fileToCheck) {
		this.fileName = fileToCheck;
	}

	public List<UserData> getUserData() {

		// open file with user data
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {
			Workbook workbook = new HSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet("UserData");
			Iterator<Row> iterator = sheet.iterator();
			int count = 0;
			DataFormatter df = new DataFormatter();
			String hour;

			// gets user data
			while (iterator.hasNext()) {

				UserData ud = new UserData();
				Row currentRow = iterator.next();

				// add access element to the table
				if (count > 0) {

					if (currentRow.getCell(0).toString().equals(""))
						break;

					// imports account name
					ud.setAccountName(currentRow.getCell(0).toString().trim());

					// imports start date
					ud.setStartDate(currentRow.getCell(1).toString());

					// imports start hour
					hour = df.formatCellValue(currentRow.getCell(2));
					ud.setStHour(hour);

					// imports # of standby hours from day 1
					ud.setSb1Day(currentRow.getCell(3).toString().trim());

					// imports end date
					ud.setEndDate(currentRow.getCell(4).toString());

					// imports end hour
					hour = df.formatCellValue(currentRow.getCell(5));
					ud.setEndHour(hour);

					// imports # of standby hours from day 2
					ud.setSb2Day(currentRow.getCell(6).toString().trim());

					// adds new user data object to the list
					uData.add(ud);
				}

				/*
				 * if(uData.isEmpty()) System.out.println("uData is empty");
				 * else System.out.println("Size of uData: " + uData.size());
				 */

				// avoids import of headers
				count++;

			}

		} catch (IOException exc) {
			System.out.println(exc);
		}

		return uData;
	}

	// imports claim codes from a file
	public List<ClaimCodes> getClaimCodes() {

		// open file with account data
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {
			Workbook workbook = new HSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet("AccountData");
			Iterator<Row> iterator = sheet.iterator();
			int count = 0;

			// gets user data
			while (iterator.hasNext()) {

				ClaimCodes cc = new ClaimCodes();
				Row currentRow = iterator.next();

				// add access element to the table
				if (count > 0) {
					// imports account name
					cc.setAccountName(currentRow.getCell(0).toString().trim());

					// import claim codes
					cc.setClaimCode(currentRow.getCell(1).toString().trim());

					// adds new account data object to the list
					cCodes.add(cc);
				}

				// avoids import of headers
				count++;

			}

		} catch (IOException exc) {
			System.out.println(exc);
		}

		return cCodes;
	}

	// sets results data counted based on data from user - CATS data
	public void setResultData(List<ResultData> rd) {
		System.out.println("Starting setResultData");
		System.out.println("File name: " + fileName);

		// open file with account data
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {

			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheet("HowToClaim - CATS");

			int rowNum = sheet.getLastRowNum();
			System.out.println("Last row number: " + rowNum);

			for (ResultData rdata : rd) {
				Row row = sheet.createRow(++rowNum);
				System.out.println("Last row number: " + rowNum);
				for (int colNum = 0; colNum < 9; colNum++) {
					if (colNum == 0) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getWbsEl());
					}
					if (colNum == 1) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getOtType());
					}
					if (colNum == 2) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getSat());
					}
					if (colNum == 3) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getSun());
					}
					if (colNum == 4) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getMon());
					}
					if (colNum == 5) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getTue());
					}
					if (colNum == 6) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getWen());
					}
					if (colNum == 7) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getThu());
					}
					if (colNum == 8) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(rdata.getFri());
					}

				}
				rowNum++;

			}
			
			System.out.println("Write data to the file.");
			try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
				workbook.write(fos);
			} catch (IOException exc) {
				System.out.println(exc);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		
	}

	/*
	 * // sets results data counted based on data from user - CATS data public
	 * void setResultData(List<ResultData> rd) {
	 * System.out.println("Starting setResultData");
	 * System.out.println("File name: " + fileName);
	 * 
	 * Workbook workbook = new HSSFWorkbook(); Sheet sheet =
	 * workbook.getSheet("HowToClaim - CATS"); int rowNum = 0;
	 * 
	 * if (rowNum == 0) { rowNum++; } else { for (ResultData rdata : rd) { Row
	 * row = sheet.createRow(rowNum);
	 * 
	 * for (int colNum = 0; colNum < 9; colNum++) { if (colNum == 0) { Cell cell
	 * = row.createCell(colNum); cell.setCellValue(rdata.getWbsEl()); } if
	 * (colNum == 1) { Cell cell = row.createCell(colNum);
	 * cell.setCellValue(rdata.getOtType()); } if (colNum == 2) { Cell cell =
	 * row.createCell(colNum); cell.setCellValue(rdata.getSat()); } if (colNum
	 * == 3) { Cell cell = row.createCell(colNum);
	 * cell.setCellValue(rdata.getSun()); } if (colNum == 4) { Cell cell =
	 * row.createCell(colNum); cell.setCellValue(rdata.getMon()); } if (colNum
	 * == 5) { Cell cell = row.createCell(colNum);
	 * cell.setCellValue(rdata.getTue()); } if (colNum == 6) { Cell cell =
	 * row.createCell(colNum); cell.setCellValue(rdata.getWen()); } if (colNum
	 * == 7) { Cell cell = row.createCell(colNum);
	 * cell.setCellValue(rdata.getThu()); } if (colNum == 8) { Cell cell =
	 * row.createCell(colNum); cell.setCellValue(rdata.getFri()); }
	 * 
	 * } rowNum++;
	 * 
	 * }
	 * 
	 * try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
	 * workbook.write(fos); } catch (IOException exc) { System.out.println(exc);
	 * } } }
	 */
}
