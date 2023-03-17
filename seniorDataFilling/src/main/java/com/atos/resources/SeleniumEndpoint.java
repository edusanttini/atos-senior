package com.atos.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumEndpoint {

	 public WebDriver driver;
	 
	    @BeforeAll
	    public void setupChromeDriver() {
	    	WebDriverManager.chromedriver().setup();
	    }

	    @BeforeEach
	    public void openBrowser() throws InterruptedException {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--remote-allow-origins=*"); 
			driver = WebDriverManager.chromedriver().capabilities(options).create();//workaround for https://github.com/bonigarcia/webdrivermanager/issues/1007
			//driver = new ChromeDriver(options);//uncomment when issue fixed
	        driver.get("https://seniorx.myatos.net:8181/gestaoponto-frontend/login");
	    }

	    @AfterEach
	    public void tearDown() {
	        //driver.quit();
	    }

		public WebDriver getDriver() {
			return this.driver;
		}
}
