package com.atos.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.atos.model.Xpath;
import com.atos.resources.SeleniumEndpoint;
import com.atos.util.RandomDate;

public class WebController {
	
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
	
	public void type(SeleniumEndpoint se, By by, String txt) {
		se.getDriver().findElement(by).sendKeys(txt);
	}
	
	public void clear(SeleniumEndpoint se, By by) {
		se.getDriver().findElement(by).clear();
	}
	
	public boolean elementExists(SeleniumEndpoint se, Xpath xpath) {
		try {
			return se.getDriver().findElements(By.id(xpath.get())).size() > 0;
		} catch(NoSuchElementException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void waitFor(SeleniumEndpoint se, Xpath xpath, int time) {
		try {
			@SuppressWarnings("deprecation")
			WebDriverWait wait = new WebDriverWait(se.getDriver(), time);
			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(
					By.id(xpath.get()))));
		} catch(TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public void waitFor(SeleniumEndpoint se, String xpath, int time) {
		try {
			@SuppressWarnings("deprecation")
			WebDriverWait wait = new WebDriverWait(se.getDriver(), time);
			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(
					By.id(xpath))));
		} catch(TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public void setImplicityWait(SeleniumEndpoint se, int timeout) {
		try {
			se.getDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public WebElement findElement(SeleniumEndpoint se, By by) {
		try {
			return se.getDriver().findElement(by);
		} catch(NoSuchElementException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void moveToElement(SeleniumEndpoint se, By by) {
		try {
			Actions builder = new Actions(se.getDriver());
			WebElement element = findElement(se, by);
			builder.moveToElement(element).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	private String getData() {
		String day;
		String month;
		String year;
		String fullDate;
		LocalDateTime now = LocalDateTime.now();
		day = ""+now.getDayOfMonth();
		month = ""+now.getMonthValue();
		year = ""+now.getYear();
		if(month.length() == 1)
			month = "0"+month;
		fullDate = year.concat("-").concat(month).concat("-").concat(day);
		return fullDate;
	}
	
	private String getMonth() {
		return this.getData().substring(5, 7);
	}
	
	private String getDay() {
		return this.getData().substring(8);
	}
	
	private String getYear() {
		return this.getData().substring(0, 4);
	}
	
	private String getFormatedData(String data) {
		return "dia_"+data+"_InserirMarcacao";
	}
	
	private String getDataIds(String day, boolean isNewMonth) {
		String fullDate = "";
		int dayInt = Integer.parseInt(this.getDay());
		if(dayInt >= 20 || isNewMonth)
			fullDate = this.getYear().concat("-").concat(this.getMonth().concat("-").concat(day));
		else {
			int month = Integer.parseInt(this.getMonth());
			month = month + 1;
			fullDate = this.getYear().concat("-").concat(month+"").concat("-").concat(day);
		}
		return fullDate;
	}
	
	@SuppressWarnings("deprecation")
	private void enterDataEdition(SeleniumEndpoint se, String xpath) throws InterruptedException {
		Thread.sleep(2000);
		WebElement we = se.getDriver().findElement(By.id(xpath));
		we.click();
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
		int month = Integer.parseInt(this.getMonth());
		int year = Integer.parseInt(this.getYear());
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
	
	private void fillDataLoop(SeleniumEndpoint se, int startDay) throws InterruptedException {
		for(int i = startDay; i <=Integer.parseInt(this.getDay()); i++) {
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+i, false)));
			this.enterDataEdition(se, this.getFormatedData(this.getDataIds(""+i, false)));
			this.addNewLine(se);
			this.waitFor(se, Xpath.inputFirstDateLine, 5);
			this.addNewLine(se);
			this.fillHour(se);
			this.selectJustification(se);
			this.saveDateEdition(se);
		}
	}
	
	private void fillExclusiveDataLoop(SeleniumEndpoint se, int startDay) throws InterruptedException {
		for(int i = startDay; i <=this.getMonthDays(); i++) {
			//System.out.println("-----------------: " + this.getFormatedData(this.getDataIds(""+i, true)));
			this.enterDataEdition(se, this.getFormatedData(this.getDataIds(""+i, true)));
			this.addNewLine(se);
			this.waitFor(se, Xpath.inputFirstDateLine, 5);
			this.addNewLine(se);
			this.fillHour(se);
			this.selectJustification(se);
			this.saveDateEdition(se);
		}
	}
	
	public void fillData(SeleniumEndpoint se) throws InterruptedException {
		if(Integer.parseInt(this.getDay()) >= 20) {
			this.fillDataLoop(se, 20);
		} else {
			this.fillExclusiveDataLoop(se, 20);
			this.fillDataLoop(se, 1);
		}
	}
}