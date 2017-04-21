package duplicatenumbers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicateNumber {

	public static void main(String[] args) {

		String line = "";

		// It's a target file, change accordingly on your PC.

		List<String> list = new ArrayList<>();

		long count;

		// An array of file names that correspond to the required dates. If
		// needed, you can add a number of dates. The only condition: it must
		// coincide with the file name in the string below
		String[] fileNames = { "12_04_2017" };

		for (int i = 0; i < fileNames.length; i++) {

			// Please change the line accordingly to match your source file
			String file = "C:/trial/" + fileNames[i] + ".csv";

			System.out.println(fileNames[i] + ".csv");

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				while ((line = br.readLine()) != null) {
					list.add(line);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}

			List<String> shortList = list.stream().map(a -> {

				int startIndex = a.indexOf(",\"\"") + 3;
				int endIndex = a.indexOf("\"", startIndex);

				return a.substring(startIndex, endIndex);

			}).collect(Collectors.toList());

			System.out.println("Total: " + shortList.size());

			List<String> distinctList = shortList.stream().distinct().collect(Collectors.toList());

			System.out.println("Unique numbers: " + distinctList.size());

			List<HelperCall> repeatedCalls = new ArrayList<>();

			List<HelperCall> oneTimeCalls = new ArrayList<>();

			for (String value : distinctList) {
				int frequency = Collections.frequency(shortList, value);
				if (frequency > 1)
					repeatedCalls.add(new HelperCall(value, frequency));
				else
					oneTimeCalls.add(new HelperCall(value, frequency));
			}

			System.out.println("One time calls: " + oneTimeCalls.size());

			System.out.println("Repeated calls: " + repeatedCalls.size());

			count = list.stream().filter(a -> a.matches("^.*(Hell|NeedHelp|Transfer).*$")).filter(a -> {
				for (HelperCall call : repeatedCalls) {

					if (a.contains(call.getAin()))
						return true;
				}
				return false;
			}).count();

			System.out.println("Agent transfer for repeated calls: " + count);

			count = list.stream().filter(a -> a.matches("^.*Announce_.*(HUP|EXIT|TEARDOWN).*$")).filter(a -> {
				for (HelperCall call : repeatedCalls) {

					if (a.contains(call.getAin()))
						return true;
				}
				return false;
			}).count();

			System.out.println("Announcements for repeated calls: " + count);

			list.clear();

		}

	}

}
