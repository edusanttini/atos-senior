package com.atos.util;

import java.time.LocalDateTime;

public class UniqueDate {
	
	LocalDateTime now = LocalDateTime.now();
	
	private String getData() {
		String day = ""+now.getDayOfMonth();
		String month = ""+now.getMonthValue();
		String year = ""+now.getYear();
		if(month.length() == 1)
			month = "0"+month;
		String fullDate = year.concat("-").concat(month).concat("-").concat(day);
		return fullDate;
	}

	public boolean isEOW(int day, int month) {
		LocalDateTime wen;
		if(month == 12)
			wen = LocalDateTime.of(now.getYear()-1, month, day, 10, 12);
		else
			wen = LocalDateTime.of(now.getYear(), month, day, 10, 12);

		if(wen.getDayOfWeek().toString().contentEquals("SATURDAY")
				|| wen.getDayOfWeek().toString().contentEquals("SUNDAY"))
			return true;
		return false;
	}

	public String getMonth() {
		return this.getData().substring(5, 7);
	}

	public String getDay() {
		return this.getData().substring(8);
	}

	public String getYear() {
		return this.getData().substring(0, 4);
	}
}