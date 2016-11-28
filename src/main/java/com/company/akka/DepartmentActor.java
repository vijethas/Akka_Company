package com.company.akka;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DepartmentActor extends Worker{
	private Map<String,Method> departmentMethods = new HashMap<String, Method>();
	private final static String service = "com.company.services.DepartmentService";
	
	public Class<?> getClassObject(){
		Class<?> c = null;
		try {
			c = Class.forName(service);
			Method[] methods = c.getMethods();
			for(Method m: methods){
				departmentMethods.put(m.getName(), m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public Method getMethod(String methodName){
		return departmentMethods.get(methodName);
	}

}
