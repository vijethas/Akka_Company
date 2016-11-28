package com.company.akka;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EmployeeActor extends Worker{
	private Map<String,Method> employeeMethods = new HashMap<String, Method>();
	private final static String service = "com.company.services.EmployeeService";
	
	public Class<?> getClassObject(){
		Class<?> c = null;
		try {
			c = Class.forName(service);
			Method[] methods = c.getMethods();
			for(Method m: methods){
				employeeMethods.put(m.getName(), m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public Method getMethod(String methodName){
		return employeeMethods.get(methodName);
	}
}
