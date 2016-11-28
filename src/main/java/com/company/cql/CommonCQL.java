package com.company.cql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

@SuppressWarnings("unused")
public class CommonCQL {

	private GraphDatabaseService graphDb;
	
	public CommonCQL(GraphDatabaseService graphDb) {
	    this.graphDb = graphDb;
	    try (Transaction tx = graphDb.beginTx()) { 
			graphDb.execute("CREATE INDEX ON :Department(deptId)"); 
			graphDb.execute("CREATE CONSTRAINT ON (d:Department) ASSERT d.deptId,d.deptName IS UNIQUE");
			tx.success(); 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Object> getAllEmployees() {
		List<Object> res = new ArrayList<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH(n:Employee) RETURN n";
			rs = graphDb.execute(query);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<Object> getAllSupervisors() {
		List<Object> res = new ArrayList<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH(n:Supervisor) RETURN n";
			rs = graphDb.execute(query);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public Set<Node> getAllNonSupervisors() {
		Set<Node> res = new HashSet<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH(n:Employee) WHERE NOT n:Supervisor RETURN n";
			rs = graphDb.execute(query);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public Set<Node> getAllPeersOfEmployee(int empId){
		Set<Node> res = new HashSet<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			Map<String, Object> params = new HashMap<>();
			params.put("empId",empId);
			String query = "MATCH (e:Employee)<-[:SUPERVISOR_OF]-(s:Supervisor)-[:SUPERVISOR_OF]->(p:Employee) WHERE e.empId={empId} RETURN p";
			rs = graphDb.execute(query,params);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public List<Object> getAllRepoteesBasedOnLevel(int level,int empId) {
		List<Object> res = new ArrayList<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			Map<String, Object> params = new HashMap<>();
			params.put("empId",empId);
			params.put("level", level);
			String query = "MATCH (e:Employee)<-[:SUPERVISOR_OF*{level}]-(s:Supervisor) WHERE s.empId={empId} return e";
			rs = graphDb.execute(query,params);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	public List<Object> getAllEmpInDept(String dept) {
		List<Object> res = new ArrayList<>();
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			Map<String, Object> params = new HashMap<>();
			params.put("dept",dept);
			String query = "MATCH (d:Department)-[:CONTAINS]->(e:Employee)where d.deptName={dept} RETURN e";
			rs = graphDb.execute(query,params);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
