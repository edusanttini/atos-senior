package com.atos.seniorDataFilling;
import com.atos.suite.ExecuteScript;

public class seniorDataFillingApp {
    public static void main( String[] args ){
    	String user = args[0];
    	String pwd = args[1];
    	String justificative = args[2];
    	try {
    		ExecuteScript es = new ExecuteScript();
    		es.run(user, pwd, justificative);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}