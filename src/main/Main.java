package main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;

public class Main {

	public static void main(String[] args) {

		String line = "";
		String fileName = "C:/trial/data.xls";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm.ss.SSSSSS");
		LocalTime endTime = LocalTime.of(17, 30);

		long count;

		List<String> list = new ArrayList<>();

		String[] dateArray = { "2017-03-07" };

		try (BufferedReader br = new BufferedReader(new FileReader("C:/trial/bigfile.csv"))) {
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

		ExcelCreation excel = new ExcelCreation(fileName);

		HSSFSheet sheet = excel.createSheet("data");

		HSSFRow row = sheet.createRow(0);

		row.createCell(0, CellType.STRING).setCellValue("Date");
		row.createCell(1, CellType.STRING).setCellValue("Total records");
		row.createCell(2, CellType.STRING).setCellValue("Agent transfer");
		row.createCell(3, CellType.STRING).setCellValue("Service provided on Cards");
		row.createCell(4, CellType.STRING).setCellValue("Automated on Cards");
		row.createCell(5, CellType.STRING).setCellValue("Service provided on Loans");
		row.createCell(6, CellType.STRING).setCellValue("Automated on Loans");
		row.createCell(7, CellType.STRING).setCellValue("Loan errors");
		row.createCell(8, CellType.STRING).setCellValue("LOAN_NO_INPUT | LOAN_NO_MATCH | LOAN_ANI_FAILED");
		row.createCell(9, CellType.STRING).setCellValue("LOAN_ANI_FAILED");
		row.createCell(10, CellType.STRING).setCellValue("Question on card/loan");
		row.createCell(11, CellType.STRING).setCellValue("Automation on card/loan");
		row.createCell(12, CellType.STRING).setCellValue("Passed identification on card/loan");
		row.createCell(13, CellType.STRING).setCellValue("AskOperator");
		row.createCell(14, CellType.STRING).setCellValue("PromoTransfer");

		for (int i = 0; i < dateArray.length; i++) {

			row = sheet.createRow(i + 1);
			row.createCell(0, CellType.STRING).setCellValue(dateArray[i]);

			String date = dateArray[i];

			System.out.println(dateArray[i] + ".csv");

			List<String> shortList = list.stream().filter(a -> a.matches("^.*" + date + ".*$")).filter(a -> {
				String phoneTime = a.substring(a.length() - 16, a.length() - 1);
				LocalTime time = LocalTime.parse(phoneTime, formatter);
				return time.isBefore(endTime);
			}).collect(Collectors.toList());

			count = shortList.stream().count();

			row.createCell(1, CellType.NUMERIC).setCellValue(count);
			System.out.println("Total records: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(Hell|NeedHelp|Transfer).*$")).count();

			row.createCell(2, CellType.NUMERIC).setCellValue(count);
			System.out.println("Agent transfer: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*?Input_Card.*Announce_Card.*$")).count();

			row.createCell(3, CellType.NUMERIC).setCellValue(count);
			System.out.println("Service provided on Cards: " + count);

			count = shortList.stream()
					.filter(a -> a.matches("^.*?Input_Card.*ANI_OK.*Announce_.*(HUP|EXIT|TEARDOWN).*$")).count();

			row.createCell(4, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automated on cards: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*?Input_Loan.*Announce_Loan.*$")).count();

			row.createCell(5, CellType.NUMERIC).setCellValue(count);
			System.out.println("Service provided on Loans: " + count);

			count = shortList.stream()
					.filter(a -> a.matches("^.*?Input_Loan.*ANI_OK.*Announce_.*(HUP|EXIT|TEARDOWN).*$")).count();

			row.createCell(6, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automated on loans: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LoanWS_Error.*$")).count();

			row.createCell(7, CellType.NUMERIC).setCellValue(count);
			System.out.println("Loan errors: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LOAN_NO_INPUT.*LOAN_NO_MATCH.*LOAN_ANI_FAILED.*$"))
					.count();

			row.createCell(8, CellType.NUMERIC).setCellValue(count);
			System.out.println("LOAN_NO_INPUT | LOAN_NO_MATCH | LOAN_ANI_FAILED: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LOAN_ANI_FAILED.*$")).count();

			row.createCell(9, CellType.NUMERIC).setCellValue(count);
			System.out.println("LOAN_ANI_FAILED: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(slCP:Card|slCP:Loan).*$")).count();

			row.createCell(10, CellType.NUMERIC).setCellValue(count);
			System.out.println("Question on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(Input_Card|Input_Loan).*$")).count();

			row.createCell(11, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automation on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*ANI_OK.*$")).count();

			row.createCell(12, CellType.NUMERIC).setCellValue(count);
			System.out.println("Passed identification on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*inSS.*AskOperator.*$")).count();

			row.createCell(13, CellType.NUMERIC).setCellValue(count);
			System.out.println("AskOperator: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*promo:Transfer.*$")).count();

			row.createCell(14, CellType.NUMERIC).setCellValue(count);
			System.out.println("PromoTransfer: " + count);

			System.out.println();

			shortList.clear();

		}

		list.clear();

		String[] fileNames = { "Dialogs2017_03_14", "21_03_2017" };

		for (int i = 0; i < fileNames.length; i++) {

			row = sheet.createRow(dateArray.length + i + 1);

			row.createCell(0, CellType.STRING).setCellValue(fileNames[i]);

			String file = "C:/trial/" + fileNames[i] + ".csv";

			System.out.println(fileNames[i] + ".csv");

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				while ((line = br.readLine()) != null) {
					list.add(line);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}

			List<String> shortList = list.stream().filter(a -> {

				int index = a.indexOf(".");
				int startIndex = index - 2;
				int endIndex = a.indexOf(",", startIndex);

				String callTime = a.substring(startIndex, endIndex);

				LocalTime time = LocalTime.parse(callTime, formatter);

				return time.isBefore(endTime);

			}).collect(Collectors.toList());

			count = shortList.stream().count();

			row.createCell(1, CellType.NUMERIC).setCellValue(count);
			System.out.println("Total records: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(Hell|NeedHelp|Transfer).*$")).count();

			row.createCell(2, CellType.NUMERIC).setCellValue(count);
			System.out.println("Agent transfer: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*?Input_Card.*Announce_Card.*$")).count();

			row.createCell(3, CellType.NUMERIC).setCellValue(count);
			System.out.println("Service provided on Cards: " + count);

			count = shortList.stream()
					.filter(a -> a.matches("^.*?Input_Card.*ANI_OK.*Announce_.*(HUP|EXIT|TEARDOWN).*$")).count();

			row.createCell(4, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automated on cards: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*?Input_Loan.*Announce_Loan.*$")).count();

			row.createCell(5, CellType.NUMERIC).setCellValue(count);
			System.out.println("Service provided on Loans: " + count);

			count = shortList.stream()
					.filter(a -> a.matches("^.*?Input_Loan.*ANI_OK.*Announce_.*(HUP|EXIT|TEARDOWN).*$")).count();

			row.createCell(6, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automated on loans: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LoanWS_Error.*$")).count();

			row.createCell(7, CellType.NUMERIC).setCellValue(count);
			System.out.println("Loan errors: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LOAN_NO_INPUT.*LOAN_NO_MATCH.*LOAN_ANI_FAILED.*$"))
					.count();

			row.createCell(8, CellType.NUMERIC).setCellValue(count);
			System.out.println("LOAN_NO_INPUT | LOAN_NO_MATCH | LOAN_ANI_FAILED: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*LOAN_ANI_FAILED.*$")).count();

			row.createCell(9, CellType.NUMERIC).setCellValue(count);
			System.out.println("LOAN_ANI_FAILED: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(slCP:Card|slCP:Loan).*$")).count();

			row.createCell(10, CellType.NUMERIC).setCellValue(count);
			System.out.println("Question on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*(Input_Card|Input_Loan).*$")).count();

			row.createCell(11, CellType.NUMERIC).setCellValue(count);
			System.out.println("Automation on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*ANI_OK.*$")).count();

			row.createCell(12, CellType.NUMERIC).setCellValue(count);
			System.out.println("Passed identification on card/loan: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*inSS.*AskOperator.*$")).count();

			row.createCell(13, CellType.NUMERIC).setCellValue(count);
			System.out.println("AskOperator: " + count);

			count = shortList.stream().filter(a -> a.matches("^.*promo:Transfer.*$")).count();

			row.createCell(14, CellType.NUMERIC).setCellValue(count);
			System.out.println("PromoTransfer: " + count);

			System.out.println();

			shortList.clear();
			list.clear();

		}

		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			excel.getWorkbook().write(fileOut);
			fileOut.close();
			System.out.println("Your excel file has been generated!");

			excel.getWorkbook().close();

			// File myFile = new File(fileName);
			// Desktop.getDesktop().open(myFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
