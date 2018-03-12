package com.application.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateUID {

	public static void main(String[] args) {

		List<String> numbers = new ArrayList<>();
		numbers.add("20180131_01");
		numbers.add("20180131_02");
		numbers.add("20180131_03");
		numbers.add("20180131_04");
		numbers.add("20180131_05");
		numbers.add("20180131_06");
		numbers.add("20180131_07");
		numbers.add("20180131_08");
		numbers.add("20180131_09");
		numbers.add("20180131_02");

		String uid = getUniqueId(numbers);
		System.out.println(uid);
	}

	public static String getUniqueId(List<String> numbers) {

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddMMyyyy");
		Date today = new Date();
		String date = DATE_FORMAT.format(today);
		String uid = date + "_01";

		if (numbers.contains(uid))
			while (true) {
				uid = getNextUniqueId(uid);
				if (!numbers.contains(uid))
					break;
			}

		return uid;

	}

	private static String getNextUniqueId(String uid) {
		String date = uid.substring(0, 8);
		String nr = uid.substring(9, 11);
		int nrInt = Integer.parseInt(nr);
		nrInt++;

		return date + "_" + String.format("%02d", nrInt);

	}

}
