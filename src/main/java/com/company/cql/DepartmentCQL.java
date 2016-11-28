package com.company.cql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class DepartmentCQL {
	private GraphDatabaseService graphDb;

	public DepartmentCQL(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
			try (Transaction tx = graphDb.beginTx()) {
				graphDb.execute("CREATE INDEX ON :Department(deptId)");
				graphDb.execute("CREATE CONSTRAINT ON (d:Department) ASSERT d.deptId IS UNIQUE");
				tx.success();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public String createDepartment(String name, String description, Integer headId) {
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			Map<String, Object> params = new HashMap<>();
			params.put("name", name);
			params.put("description", description);
			params.put("headId", headId);
			String query = "CREATE (d:Department{deptName:{name},description:{description},headId:{headId}}) RETURN d.id";
			rs = graphDb.execute(query, params);
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	public String editDepartment(String name, String description, Integer headId, Integer deptId) {
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			Map<String, Object> params = new HashMap<>();
			params.put("deptId", deptId);
			params.put("name", name);
			params.put("description", description);
			params.put("headId", headId);
			String query = "MATCH (d:Department) WHERE d.deptId={deptId} SET d.deptName={name},d.description={description},d.headId={headId} RETURN d";
			rs = graphDb.execute(query, params);
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	public String addEmployeesToDepartment(List<Integer> idList, Integer deptId) {
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH (d:Department) WHERE d.deptId={deptId} MATCH (e:Employee{e.Id={empId}})"
					+ "MERGE d-[:{relation}]->e";
			Map<String, Object> params;
			for (int empId : idList) {
				params = new HashMap<>();
				params.put("deptId", deptId);
				params.put("empId", empId);
				params.put("relation", "CONTAINS");
				rs = graphDb.execute(query, params);
			}
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

	public String removeEmployeesFromDepartment(List<Integer> idList, Integer deptId) {
		Result rs = null;
		try (Transaction tx = graphDb.beginTx()) {
			String query = "MATCH (d:Department) WHERE d.deptId={deptId} MATCH (e:Employee{e.Id={empId}})"
					+ "DELETE d-[:{relation}]->e";
			Map<String, Object> params;
			for (int empId : idList) {
				params = new HashMap<>();
				params.put("deptId", deptId);
				params.put("empId", empId);
				params.put("relation", "CONTAINS");
				rs = graphDb.execute(query, params);
			}
			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs.toString();
	}

}
