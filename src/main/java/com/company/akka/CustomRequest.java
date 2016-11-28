package com.company.akka;

import java.io.Serializable;
import java.util.Map;

public class CustomRequest implements Serializable{
	
	private static final long serialVersionUID = 4637754210594537300L;
	private final String operation;
	private final Map<String,Object> params;
	
	CustomRequest(String operation, Map<String,Object> params){
		this.operation = operation;
		this.params = params;
	}
	
	public String getOperation() {
		return operation;
	}
	public Map<String, Object> getParams() {
		return params;
	}

}