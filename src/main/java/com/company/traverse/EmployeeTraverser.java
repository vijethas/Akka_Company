package com.company.traverse;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

import com.company.common.RelationTypes;

public class EmployeeTraverser {
	private GraphDatabaseService graphDb;
	
	public EmployeeTraverser(GraphDatabaseService graphDb) {
	    this.graphDb = graphDb;
	}
	public String createEmployee(String name, int age, Integer supervisorId, int departmentId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			// Create employee node
			Label label = Label.label("Employee");
			Index<Node> index = graphDb.index().forNodes("Employee");
			Node employee = graphDb.createNode(label);
			Integer employeeId = (int) employee.getId();
			employee.addLabel(label);
			employee.setProperty("empId", employeeId);
			employee.setProperty("name", name);
			employee.setProperty("age", age);
			// Creating relationship with Supervior
			if(null!=supervisorId){
				employee.setProperty("supervisorId", supervisorId);
				Node supervisor = graphDb.getNodeById(supervisorId);
				Label supervisorLabel = Label.label("Supervisor");
				supervisor.addLabel(supervisorLabel);
				supervisor.createRelationshipTo(employee, RelationTypes.SUPERVISOR_OF);
			}
			employee.setProperty("departmentId", departmentId);
			index.add(employee, "empId", employeeId);
			//Creating relationship with Department Node 
			Node dept = graphDb.getNodeById(departmentId);
			dept.createRelationshipTo(employee, RelationTypes.CONTAINS);
			result.append("Created employee "+name);
			tx.success();
		} catch (Exception e) {
		
		}
		return result.toString();
	}

	public String addSupervisor(int empId, int supervisorId) {
		StringBuilder result = new StringBuilder();
		try (Transaction tx = graphDb.beginTx()) {
			Node employee = graphDb.getNodeById(empId);
			// Get supervisor node
			Node supervisor = graphDb.getNodeById(supervisorId);
			Traverser sups = getSupervisors(supervisor);
			List<Node> supervisors = new ArrayList<>();
			for(Path sup:sups){
				supervisors.add(sup.endNode());
			}
			if(supervisors.contains(employee)){
				System.out.println("Cannot add "+supervisor+ "as a supervisor to "+employee);
				return result.append("Cannot add "+supervisor+ "as a supervisor to "+employee).toString();
			}
			employee.setProperty("supervisorId", supervisorId);
			Label supervisorLabel = Label.label("Supervisor");
			supervisor.addLabel(supervisorLabel);
			// Create relationship SUPERVISOR_OF
			supervisor.createRelationshipTo(employee, RelationTypes.SUPERVISOR_OF);
			System.out.println("Added Supervisor");
			result.append("Added Supervisor");
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String changeSupervisor(int empId, int newSupervisorId) {
		StringBuilder result = new StringBuilder(); 
		try (Transaction tx = graphDb.beginTx()) {
			Node employee = graphDb.getNodeById(empId);
			int oldSupervisorId = (int)employee.getProperty("supervisorId");
			
			// Get supervisor node
			Node oldSupervisor = graphDb.getNodeById(oldSupervisorId);
			int noOfSupervisee = oldSupervisor.getDegree(RelationTypes.SUPERVISOR_OF, Direction.OUTGOING);
			if(noOfSupervisee<1){
				oldSupervisor.removeLabel(Label.label("Supervisor"));
			}
			
			Relationship rel = employee.getSingleRelationship(RelationTypes.SUPERVISOR_OF, Direction.INCOMING);
			rel.delete();
			employee.setProperty("supervisorId", newSupervisorId);
			Node supervisor = graphDb.getNodeById(newSupervisorId);
			// Create relationship SUPERVISOR_OF
			Label supervisorLabel = Label.label("Supervisor");
			supervisor.addLabel(supervisorLabel);
			supervisor.createRelationshipTo(employee, RelationTypes.SUPERVISOR_OF);
			result.append("Updated Changes");
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	private Traverser getSupervisors(Node supervisor){
		TraversalDescription traverser = graphDb.traversalDescription().breadthFirst().relationships(RelationTypes.SUPERVISOR_OF,Direction.INCOMING).evaluator(Evaluators.excludeStartPosition());
		return traverser.traverse(supervisor);
	}

	/*public void deleteEmployee() {

	}*/
}
