package com.atos.model;

public enum Xpath {
	
	inputUserLogIn("index-vm-username"),
	inputUserPwd("index-vm-password"),
	btnLogIn("index-1500385519648"),
	divNextMonth("codCalc__navegacao_proximo"),
	btnNextMonth("_content-options-1500385512480"),
	divMonthArrows("_content-options-1500385512543"),
	deleteMe("dia_2022-06-27_InserirMarcacao"),
	btnAddData("addMarcacao"),
	inputFirstDateLine("marcacaoTime-0"),
	inputSecDateLine("marcacaoTime-1"),
	divInsertDate("{{::'coluna-marcacoes-'+$index}}"),
	btnSaveDateEditionTable("saveAppointment"),
	dropDownJustifyClass0("selectJustificative-0"),
	dropDownJustifyClass1("selectJustificative-1"),
	btnHomeOfficeJustifyReason("justificative_4"),
	
	
	
	
	
	deleteMee("");
	
	private String xPathValue;
	
	Xpath(String xPathValue) {
		this.xPathValue = xPathValue;
	}
	
	public String get() {
        return xPathValue;
    }
    
    public String getXpathTable() {
    	return "//*[@id='" + xPathValue + "']/div/div/table/tbody";
    }
    
    @Deprecated
    public String getXpathId() {
        return "//*[@id='" + xPathValue + "']";
    }
    
    public String getXpathClass() {
    	return "//*[@class='" + xPathValue + "']";
    }
    
    public String getXpathImage() {
    	return "//img[@src='" + xPathValue + "']"; 
    }
    
    public String getXPathIdPlusInput() {
    	return "//*[@id='" + xPathValue + "']/input";
    }
    
    public String getXPathInputid() {
    	return "//*[@inputid='" + xPathValue + "']";
    }

}
