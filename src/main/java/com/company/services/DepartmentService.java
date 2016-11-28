package com.company.services;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.company.cql.DepartmentCQL;
import com.company.traverse.DepartmentTraverser;

public class DepartmentService {
	DepartmentCQL cql;
	DepartmentTraverser dt;

	public DepartmentService() {
		dt = new DepartmentTraverser(Neo4jDBFactory.getGraphDb());
	}

	public Response createDepartment(Map<String,Object> params) {
		String name = params.get("name").toString();
		String description = params.get("description").toString();
		Integer headId = (Integer) params.get("headId");
		String result = dt.createDepartment(name, description, headId);
		return Response.status(200).entity(result).build();
	}

	public Response editDepartment(Map<String,Object> params) {
		String name = params.get("name").toString();
		String description = params.get("description").toString();
		Integer headId = (Integer) params.get("headId");
		Integer deptId = (Integer) params.get("deptId");
		String result = dt.editDepartment(deptId, name, description, headId);
		return Response.status(200).entity(result).build();
	}

	public Response addEmployeesToDepartment(Map<String,Object> params) {
		@SuppressWarnings("unchecked")
		List<Integer> idList = (List<Integer>)params.get("idList"); 
		Integer deptId = (Integer) params.get("deptId");
		String result = dt.addEmployeesToDepartment(idList, deptId);
		return Response.status(200).entity(result).build();
	}

	public Response removeEmployeesFromDepartment(Map<String,Object> params) {
		@SuppressWarnings("unchecked")
		List<Integer> idList = (List<Integer>)params.get("idList"); 
		Integer deptId = (Integer) params.get("deptId");
		String result = dt.removeEmployeeesFromDepartment(idList, deptId);
		return Response.status(200).entity(result).build();
	}

}
