package com.atos.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
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
			int month = 0;
			if (ud.getMonth().contentEquals("01"))
				month = 12;
			else
				month = Integer.parseInt(ud.getMonth()) -1;
			if(month == 10 || month == 11)
				return ud.getYear().concat("-").concat(month + "").concat("-").concat(day);
			if(month == 12) {
				int lastYear = Integer.parseInt(ud.getYear()) -1;
				return Integer.toString(lastYear).concat("-").concat(month + "").concat("-").concat(day);
			}
			fullDate = ud.getYear().concat("-").concat("0" + month + "").concat("-").concat(day);
		}
		return fullDate;
	}
	

	private boolean enterDataEdition(SeleniumEndpoint se, String xpath) throws InterruptedException {
		Thread.sleep(5000);
		try {
			WebElement we = se.getDriver().findElement(By.id(xpath));


			//WebElement parent = we.findElement(By.xpath("./../../.."));
			//System.out.println("------------enterDataEdition: "+ parent.getAttribute("innerHTML").contains("ng-scope time-adjustment-dia-horario-especial"));
			//System.out.println("------------enterDataEdition1: "+ parent.getAttribute("innerHTML"));
			//WebElement gParent = we.findElement(By.xpath("//td/parent::div[@class=\"ng-scope time-adjustment-dia-horario-especial\"]"));
			//System.out.println("------------enterDataEdition1: "+ gParent.getAttribute("innerHTML"));
			//if(gParent.getAttribute("innerHTML").contains("ng-scope time-adjustment-dia-horario-especial"))
				//return false;//TODO apparently !isClockPending is present in all parents attributes, need to find an exclusive id or string for when people is on vacation


			JavascriptExecutor ex = (JavascriptExecutor)se.getDriver();
			ex.executeScript("arguments[0].click();", we);
			this.waitFor(se, Xpath.btnAddData, 5);	
		} catch (Exception e) {
			e.printStackTrace();
			throw new InterruptedException("error - " + e);
		}
		return true;
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
	
	private void saveDateEdition(SeleniumEndpoint se, String xpath) {
		WebElement savebtn = se.getDriver().findElement(By.id(Xpath.btnSaveDateEditionTable.get()));
		savebtn.click();
		@SuppressWarnings("deprecation")
		WebDriverWait wait = new WebDriverWait(se.getDriver(), 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(Xpath.btnSaveDateEditionTable.get())));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(xpath)));
	}
	
	private void selectJustification(SeleniumEndpoint se, String justification) throws InterruptedException {
		WebElement justify1 = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass0.get()));
		justify1.click();
		Thread.sleep(2000);
		switch (justification) {
		case "1":
			if(this.elementExists(se, Xpath.btnForgotjustifyReason)) {
				WebElement forgot = se.getDriver().findElement(By.id(Xpath.btnForgotjustifyReason.get()));
				forgot.click();
			
			}
			Thread.sleep(2000);
			WebElement justify2 = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass1.get()));
			justify2.click();
			Thread.sleep(2000);
			if(this.elementExists(se, Xpath.btnForgotjustifyReason)) {
				WebElement forgot2 = se.getDriver().findElement(By.id(Xpath.btnForgotjustifyReason.get()));
				forgot2.click();
			}
			break;
		case "2":
			if(this.elementExists(se, Xpath.btnNoConectionJustifyReason)) {
				WebElement noConnection = se.getDriver().findElement(By.id(Xpath.btnNoConectionJustifyReason.get()));
				noConnection.click();
			
			}
			Thread.sleep(2000);
			WebElement justify2a = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass1.get()));
			justify2a.click();
			Thread.sleep(2000);
			if(this.elementExists(se, Xpath.btnNoConectionJustifyReason)) {
				WebElement noConnection2 = se.getDriver().findElement(By.id(Xpath.btnNoConectionJustifyReason.get()));
				noConnection2.click();
			}
			break;
		case "4":
			if(this.elementExists(se, Xpath.btnHomeOfficeJustifyReason)) {
				WebElement homeOffice = se.getDriver().findElement(By.id(Xpath.btnHomeOfficeJustifyReason.get()));
				homeOffice.click();
			
			}
			Thread.sleep(2000);
			WebElement justify2b = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass1.get()));
			justify2b.click();
			Thread.sleep(2000);
			if(this.elementExists(se, Xpath.btnHomeOfficeJustifyReason)) {
				WebElement homeOffice2 = se.getDriver().findElement(By.id(Xpath.btnHomeOfficeJustifyReason.get()));
				homeOffice2.click();
			}
			break;
		case "5":
			if(this.elementExists(se, Xpath.btnManuelMarkJustifyReason)) {
				WebElement manual = se.getDriver().findElement(By.id(Xpath.btnManuelMarkJustifyReason.get()));
				manual.click();
			}
			Thread.sleep(2000);
			WebElement justify2c = se.getDriver().findElement(By.id(Xpath.dropDownJustifyClass1.get()));
			justify2c.click();
			Thread.sleep(2000);
			if(this.elementExists(se, Xpath.btnManuelMarkJustifyReason)) {
				WebElement manual2 = se.getDriver().findElement(By.id(Xpath.btnManuelMarkJustifyReason.get()));
				manual2.click();
			}
			break;
		default:
			System.out.println("Justificative" + justification + "is not on the list. "
					+ "Please inform a valid justificative number(1, 2, 4 or 5)");
			break;
		}
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

	private void fillSpecificData(SeleniumEndpoint se, int day, boolean isNewMonth, String justificative) throws InterruptedException {
		if(this.enterDataEdition(se, this.getFormatedData(this.getDataIds(""+day, isNewMonth)))) {
			this.addNewLine(se);
			this.waitFor(se, Xpath.inputFirstDateLine, 5);
			this.addNewLine(se);
			this.fillHour(se);
			this.selectJustification(se, justificative);
			this.saveDateEdition(se, this.getFormatedData(this.getDataIds(""+day, isNewMonth)));
		}
	}


	private void fillDataLoop(SeleniumEndpoint se, int startDay, String justificative) throws InterruptedException {
		for(int day = startDay; day <=Integer.parseInt(ud.getDay()); day++) {
			if(ud.isEOW(day, Integer.parseInt(ud.getMonth())))
				continue;
			if(this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, true))))
				continue;
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+day, true)));
			while(!this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, true))))
				this.fillSpecificData(se, day, true, justificative);
		}
	}
	
	private void fillExclusiveDataLoop(SeleniumEndpoint se, int startDay, String justificative) throws InterruptedException {

		int lastMonth = 0;
		if(ud.getMonth().contentEquals("01"))
			lastMonth = 12;
		else
			lastMonth = Integer.parseInt(ud.getMonth()) - 1;

		for(int day = startDay; day <=this.getMonthDays(lastMonth); day++) {
			if(ud.isEOW(day, lastMonth))
				continue;
			if(this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, false))))
				continue;
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+day, false)));
			while(!this.checkIfAlreadyFilled(se, this.getFormatedData(this.getDataIds(""+day, false))))
				this.fillSpecificData(se, day, false, justificative);
		}
	}
	
	public void fillData(SeleniumEndpoint se, String justificative) throws InterruptedException {
		if(Integer.parseInt(ud.getDay()) >= 20) {
			this.fillDataLoop(se, 20, justificative);
		} else {
			this.fillExclusiveDataLoop(se, 20, justificative);
			this.fillDataLoop(se, 1, justificative);
		}
	}
}