package com.atos.resources;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumEndpoint {

	 public WebDriver driver;
	 public String chromedriverPath; // Manually assigned ChromeDriver path

    @BeforeAll
    public void setupChromeDriver() {
        // Set the path to your downloaded ChromeDriver based on your preference
        // Replace "C:\\path\\to\\chromedriver.exe" with your actual path
        //chromedriverPath = "C:\\path\\to\\chromedriver.exe";
		chromedriverPath = "C:\\Users\\santi\\.cache\\selenium\\chromedriver\\win32\\121.0.6167.184\\chromedriver.exe";
    }

	@BeforeEach
    public void openBrowser() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--remote-allow-origins=*");
        System.setProperty("webdriver.chrome.driver", chromedriverPath); // Set system property manually
        driver = new ChromeDriver(options);

        //driver.get("https://seniorx.myatos.net:8181/gestaoponto-frontend/login");
        driver.get("https://webp12.seniorcloud.com.br:31171/gestaoponto-frontend/login");
    }

	@AfterEach
	public void tearDown() {
	    //driver.quit();
	}

	public WebDriver getDriver() {
		return this.driver;
	}
}
