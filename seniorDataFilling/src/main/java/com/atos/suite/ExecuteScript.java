package com.atos.suite;

import com.atos.controller.WebController;
import com.atos.resources.SeleniumEndpoint;

public class ExecuteScript {
	
	private WebController wc = new WebController();
	private SeleniumEndpoint se = new SeleniumEndpoint();
	
	public void run(String user, String pwd, String justificative) {
		try {
			wc.run(se);
			wc.logIn(se, user, pwd);
			wc.goToCurrentMonth(se);
			wc.fillData(se, justificative);
			wc.quit(se);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}