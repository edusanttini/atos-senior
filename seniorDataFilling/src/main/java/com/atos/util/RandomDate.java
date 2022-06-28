package com.atos.util;

import java.util.Random;

public class RandomDate {
	
	public int getRandomInitialHour() {
		Random r = new Random();
		return r.ints(1, 7, 9).findFirst().getAsInt();
	}
	
	public int getRandomExitHour() {
		Random r = new Random();
		return r.ints(1, 16, 18).findFirst().getAsInt();
	}
	
	public int getRandomMinutes(int randomSelector) {
		Random r = new Random();
		int randomLowerNumber = r.ints(1, 0, 6).findFirst().getAsInt();
		int randomHigherNumber = r.ints(1, 55, 60).findFirst().getAsInt();
		if(randomSelector == 7 || randomSelector == 16)
			return randomHigherNumber;
		else
			return randomLowerNumber;
	}
}
