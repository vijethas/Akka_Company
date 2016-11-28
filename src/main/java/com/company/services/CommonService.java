package com.company.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.neo4j.graphdb.Node;

import com.company.cql.CommonCQL;
import com.company.traverse.CommonTraverser;

public class CommonService {
	CommonCQL cql;
	CommonTraverser ct;
	
	public CommonService() {
		ct = new CommonTraverser(Neo4jDBFactory.getGraphDb());
	}

	public Response getAllEmployees() {
		List<Object> result = ct.getAllEmployees();
		return Response.status(200).entity(result).build();
	}


	public Response getAllSupervisors() {
		List<Object> result = ct.getAllSupervisors();
		return Response.status(200).entity(result).build();
	}

	public Response getAllNonSupervisors() {
		Set<Node> result = ct.getAllNonSupervisors();
		return Response.status(200).entity(result).build();
	}

	public Response getAllPeers(Map<String,Object> params) {
		int empId = (int) params.get("empId");
		Set<Node> result = ct.getAllPeers(empId);
		return Response.status(200).entity(result).build();
	}

	
	public Response getAllRepoteesBasedOnLevel(Map<String,Object> params) {
		int supervisorId = (int) params.get("supervisorId");
		int level = (int) params.get("level");
		List<Object> result = ct.getAllRepoteesBasedOnLevel(level, supervisorId);
		return Response.status(200).entity(result).build();
	}

	public Response getAllEmpInDept(Map<String,Object> params) {
		int deptId = (int) params.get("deptId");
		List<Object> result = ct.getAllEmpInDept(deptId);
		return Response.status(200).entity(result).build();
	}
	
}
