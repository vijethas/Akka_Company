package com.company.services;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.company.cql.EmployeeCQL;
import com.company.traverse.EmployeeTraverser;

public class EmployeeService {
	EmployeeCQL cql;
	EmployeeTraverser et;
	
	public EmployeeService() {
		et = new EmployeeTraverser(Neo4jDBFactory.getGraphDb());
	}

	public Response createEmployee(Map<String,Object> params){
		String name = params.get("name").toString();
		int age = (int) params.get("age");
		Integer supervisorId = (Integer) params.get("supervisorId");
		int departmentId = (int) params.get("departmentId");
		String result = et.createEmployee(name, age, supervisorId, departmentId);
		return Response.status(200).entity(result).build();
	}
	
	
	public Response addSupervisor(Map<String,Object> params) {
		int empId = (int) params.get("empId");
		int supervisorId = (int) params.get("supervisorId");
		String result = et.addSupervisor(empId, supervisorId);
		return Response.status(200).entity(result).build();
	}
	
	public Response changeSupervisor(Map<String,Object> params) {
		int empId = (int) params.get("empId");
		int newSupervisorId = (int) params.get("supervisorId");
		String result = et.changeSupervisor(empId, newSupervisorId);
		return Response.status(200).entity(result).build();
	}

}
