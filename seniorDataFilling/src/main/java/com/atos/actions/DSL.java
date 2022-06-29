package com.atos.actions;

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

public class DSL {
	
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
	
	public boolean elementExists(SeleniumEndpoint se, String xpath) {
		try {
			return se.getDriver().findElements(By.id(xpath)).size() > 0;
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
}