package com.company.traverse;

import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import com.company.common.RelationTypes;

public class DepartmentTraverser {
	private GraphDatabaseService graphDb;
	
	public DepartmentTraverser(GraphDatabaseService graphDb) {
	    this.graphDb = graphDb;
	}

	public String createDepartment(String name, String description, Integer headId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			// Create department node
			Index<Node> index = graphDb.index().forNodes("Department");
			//Label label = DynamicLabel.label("Department");
			Label label = Label.label("Department");
			Node department = graphDb.createNode(label);
			Integer deptId = (int) department.getId();
			department.setProperty("departmentId", deptId);
			department.setProperty("deptName", name);
			department.setProperty("description", description);
			if(null!=headId){
				department.setProperty("headId", headId);
			}
			index.add(department, "departmentId", deptId);
			department.addLabel(label);
			//System.out.println("Created Node -> ");
			//System.out.println(deptId+":"+name);
			result.append("Dept "+name+" has been created -> "+deptId);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String editDepartment(int deptid, String name, String description, Integer headId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			// Edit department
			Node department = graphDb.getNodeById(deptid);
			if(null!=name){
				department.setProperty("deptName", name);
			}
			if(null!=description){
				department.setProperty("description", description);
			}
			if(null!=headId){
				department.setProperty("headId", headId);
			}
			result.append("Department "+department.getProperty("name")+" is updated");
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String addEmployeesToDepartment(List<Integer> employeeIds, int deptId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			Node dept = graphDb.getNodeById(deptId);
			for (int empId : employeeIds) {
				Node employee = graphDb.getNodeById(empId);
				dept.createRelationshipTo(employee, RelationTypes.CONTAINS);
			}
			result.append("Employees are added to the department successfully");
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String removeEmployeeesFromDepartment(List<Integer> employeeIds, int deptId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			for (int empId : employeeIds) {
				Node employee = graphDb.getNodeById(empId);
				Relationship rel = employee.getSingleRelationship(RelationTypes.CONTAINS, Direction.INCOMING);
				rel.delete();
			}
			result.append("Removed Employees from the Department");
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
