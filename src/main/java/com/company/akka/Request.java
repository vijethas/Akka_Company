package com.company.akka;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable{
	
	private static final long serialVersionUID = -6995504747548212185L;
	private final String service;
	private final String operation;
	private final Map<String,Object> params;
	
	public String getService() {
		return service;
	}
	public String getOperation() {
		return operation;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	Request(String service, String operation,Map<String,Object> params){
		this.service = service;
		this.operation = operation;
		this.params = params;
	}
}
