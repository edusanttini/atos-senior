package com.atos.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.atos.actions.DSL;
import com.atos.model.Xpath;
import com.atos.resources.SeleniumEndpoint;
import com.atos.util.RandomDate;
import com.atos.util.UniqueDate;

public class WebController extends DSL {
	
	private UniqueDate ud = new UniqueDate();
	
	public void run(SeleniumEndpoint se) {
		try {
			se.setupChromeDriver();
			se.openBrowser();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void quit(SeleniumEndpoint se) {
		se.tearDown();
	}
	
	public void logIn(SeleniumEndpoint se, String user, String pwd) throws InterruptedException {
		this.clear(se, By.id(Xpath.inputUserLogIn.get()));
		this.type(se, By.id(Xpath.inputUserLogIn.get()), user);
		this.clear(se, By.id(Xpath.inputUserPwd.get()));
		this.type(se, By.id(Xpath.inputUserPwd.get()), pwd);
		se.getDriver().findElement(By.id(Xpath.btnLogIn.get())).click();
	}

	public void goToCurrentMonth(SeleniumEndpoint se) {
		this.waitFor(se, Xpath.divNextMonth, 5);
		this.moveToElement(se, By.id(Xpath.btnNextMonth.get()));
		WebElement we = se.driver.findElement(By.id(Xpath.divMonthArrows.get()));
		WebElement nextArrow = se.getDriver().findElement(By.id(Xpath.divNextMonth.get()));
		String divContent = "class=\"disabled\"";
		while(!we.getAttribute("innerHTML").contains(divContent)) {
			this.waitFor(se, Xpath.divNextMonth, 5);
			nextArrow.click();
		}
	}

	private String getFormatedData(String data) {
		return "dia_"+data+"_InserirMarcacao";
	}
	
	private String getDataIds(String day, boolean isNewMonth) {
		String fullDate = "";
		int dayInt = Integer.parseInt(ud.getDay());
		if(day.length() == 1)
			day = "0"+day;
		if(dayInt >= 20 || isNewMonth)
			fullDate = ud.getYear().concat("-").concat(ud.getMonth().concat("-").concat(day));
		else {
			int month = Integer.parseInt(ud.getMonth());
			month = month - 1;
			fullDate = ud.getYear().concat("-").concat("0" + month + "").concat("-").concat(day);
		}
		return fullDate;
	}
	
	private void enterDataEdition(SeleniumEndpoint se, String xpath) throws InterruptedException {
		Thread.sleep(3000);
		WebElement we = se.getDriver().findElement(By.id(xpath));
		JavascriptExecutor ex = (JavascriptExecutor)se.getDriver();
		ex.executeScript("arguments[0].click();", we);
		this.waitFor(se, Xpath.btnAddData, 5);
	}
	
	private void addNewLine(SeleniumEndpoint se) {
		WebElement we = se.getDriver().findElement(By.id(Xpath.btnAddData.get()));
		we.click();
	}
	
	private void fillHour(SeleniumEndpoint se) {
		String entryTime;
		String exitTime;
		RandomDate rd = new RandomDate();
		int initialHour = rd.getRandomInitialHour();
		int initalMinute = rd.getRandomMinutes(initialHour);
		int exitHour = rd.getRandomExitHour();
		int exitMinute = rd.getRandomMinutes(exitHour);
		
		if(initialHour == 7)
			entryTime = Integer.toString(initialHour).concat(":").concat(""+initalMinute);
		else 
			entryTime = Integer.toString(initialHour).concat(":").concat("0"+initalMinute);
		if(exitHour == 16) 
			exitTime = Integer.toString(exitHour).concat(":").concat(""+exitMinute);
		else
			exitTime = Integer.toString(exitHour).concat(":").concat("0"+exitMinute);

		this.type(se, By.id(Xpath.inputFirstDateLine.get()), entryTime);
		this.type(se, By.id(Xpath.inputSecDateLine.get()), exitTime);
	}
	
	private void saveDateEdition(SeleniumEndpoint se) {
		WebElement savebtn = se.getDriver().findElement(By.id(Xpath.btnSaveDateEditionTable.get()));
		savebtn.click();
	}
	
	private void selectJustification(SeleniumEndpoint se) throws InterruptedException {
		WebElement justify1 = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass0.get()));
		justify1.click();
		Thread.sleep(2000);
		if(this.elementExists(se, Xpath.btnHomeOfficeJustifyReason)) {
			WebElement homeOffice = se.getDriver().findElement(By.id(Xpath.btnHomeOfficeJustifyReason.get()));
			homeOffice.click();	
		}
		Thread.sleep(2000);
		WebElement justify2 = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass1.get()));
		justify2.click();
		Thread.sleep(2000);
		if(this.elementExists(se, Xpath.btnHomeOfficeJustifyReason)) {
			WebElement homeOffice2 = se.getDriver().findElement(By.id(Xpath.btnHomeOfficeJustifyReason.get()));
			homeOffice2.click();
		}
	}
	
	private int getMonthDays() {
		int month = Integer.parseInt(ud.getMonth());
		int year = Integer.parseInt(ud.getYear());
		if(month == 1 || month == 3 || month == 5 || month == 7 
				|| month == 8 || month == 10 || month ==12)
			return 31;
		else if(month == 2)
			if(year == 2024 || year == 2028 || year == 2032 || year == 2036
				|| year == 2040 || year == 2044 || year == 2048 || year == 2052)
				return 29;
			else
				return 28;
		else
			return 30;
	}
	
	private int getMonthDays(int month) {
		int year = Integer.parseInt(ud.getYear());
		if(month == 1 || month == 3 || month == 5 || month == 7 
				|| month == 8 || month == 10 || month == 12)
			return 31;
		else if(month == 2)
			if(year == 2024 || year == 2028 || year == 2032 || year == 2036
				|| year == 2040 || year == 2044 || year == 2048 || year == 2052)
				return 29;
			else
				return 28;
		else
			return 30;
	}

	private boolean checkIfAlreadyFilled(SeleniumEndpoint se, String xpath) throws InterruptedException {
		Thread.sleep(1000);
		if(!elementExists(se, xpath))
			return true;
		return false;
	}

	private void fillDataLoop(SeleniumEndpoint se, int startDay) throws InterruptedException {
		for(int day = startDay; day <=Integer.parseInt(ud.getDay()); day++) {
			if(ud.isEOW(day, Integer.parseInt(ud.getMonth())))
				continue;
			if(this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, true))))
				continue;
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+day, true)));
			this.enterDataEdition(se, this.getFormatedData(this.getDataIds(""+day, true)));
			this.addNewLine(se);
			this.waitFor(se, Xpath.inputFirstDateLine, 5);
			this.addNewLine(se);
			this.fillHour(se);
			this.selectJustification(se);
			this.saveDateEdition(se);
		}
	}
	
	private void fillExclusiveDataLoop(SeleniumEndpoint se, int startDay) throws InterruptedException {
		int lastMonth = Integer.parseInt(ud.getMonth()) - 1;
		for(int day = startDay; day <=this.getMonthDays(lastMonth); day++) {
			if(ud.isEOW(day, lastMonth))
				continue;
			if(this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, false))))
				continue;
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+day, false)));
			this.enterDataEdition(se, this.getFormatedData(this.getDataIds(""+day, false)));
			this.addNewLine(se);
			this.waitFor(se, Xpath.inputFirstDateLine, 5);
			this.addNewLine(se);
			this.fillHour(se);
			this.selectJustification(se);
			this.saveDateEdition(se);
		}
	}
	
	public void fillData(SeleniumEndpoint se) throws InterruptedException {
		if(Integer.parseInt(ud.getDay()) >= 20) {
			this.fillDataLoop(se, 20);
		} else {
			this.fillExclusiveDataLoop(se, 20);
			this.fillDataLoop(se, 1);
		}
	}
}