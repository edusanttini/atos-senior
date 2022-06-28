package com.atos.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumEndpoint {

	 public WebDriver driver;
	 
	    @BeforeAll
	    public void setupChromeDriver() {
	    	WebDriverManager.chromedriver().setup();
	    }

	    @BeforeEach
	    public void openBrowser() throws InterruptedException {
	        driver = new ChromeDriver();
	        driver.get("https://seniorx.myatos.net:8181/gestaoponto-frontend/login");
	    }

	    @AfterEach
	    public void tearDown() {
	        driver.quit();
	    }

	    @Test
	    public void verify() {
	        // Exercise
	        String title = driver.getTitle();
	        System.out.println("string title: " + title);

	        // Verify
	        assertThat(title).contains("Google");
	    }

		public WebDriver getDriver() {
			return this.driver;
		}
}
