package com.company.traverse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

import com.company.common.RelationTypes;

public class CommonTraverser {
	
	private GraphDatabaseService graphDb;
	
	public CommonTraverser(GraphDatabaseService graphDb) {
	    this.graphDb = graphDb;
	}
	
	public List<Object> getAllEmployees() {
		List<Object> emps = new ArrayList<>();
		try (Transaction tx = graphDb.beginTx()) {
			ResourceIterator<Node>employees = graphDb.findNodes(Label.label("Employee"));
			while(employees.hasNext()){
				emps.add(employees.next().getProperty("name"));
			}
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}
	
	public List<Object> getAllSupervisors() {
		List<Object> emps = new ArrayList<>();
		try (Transaction tx = graphDb.beginTx()) {
			ResourceIterator<Node>employees = graphDb.findNodes(Label.label("Supervisor"));
			while(employees.hasNext()){
				emps.add(employees.next().getProperty("name"));
			}
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}

	public Set<Node> getAllNonSupervisors() {
		Set<Node> empNotSupervisors = new HashSet<>();
		try (Transaction tx = graphDb.beginTx()) {
			ResourceIterable<Node> nodes = graphDb.getAllNodes();
			for(Node n:nodes){
				if(n.hasLabel(Label.label("Employee"))&&!n.hasLabel(Label.label("Supervisor"))){
					empNotSupervisors.add(n);
				}
			}
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return empNotSupervisors;
	}
	
	public Set<Node> getAllPeers(int empId){
		Set<Node> subEmployees = new HashSet<>();
		try (Transaction tx = graphDb.beginTx()) {
			Node emp = graphDb.getNodeById(empId);
			Relationship r = emp.getSingleRelationship(RelationTypes.SUPERVISOR_OF, Direction.INCOMING);
			Node supervisor = r.getOtherNode(emp);
			Traverser subEmps = getSubEmployees(supervisor);
			for(Path subEmp:subEmps){
				subEmployees.add(subEmp.endNode());
			}
			subEmployees.remove(emp);
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return subEmployees;
	}

	public List<Object> getAllRepoteesBasedOnLevel(Integer level, Integer supervisorId) {
		List<Object> repotees = new ArrayList<>();
		try (Transaction tx = graphDb.beginTx()) {
			Node supervisor = graphDb.getNodeById(supervisorId);
			Traverser repEmps = getRepotees(supervisor, level);
			for(Path rep:repEmps){
				repotees.add(rep.endNode().getProperty("name"));
			}
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return repotees;
	}

	public List<Object> getAllEmpInDept(int deptId) {
		List<Object> employees = new ArrayList<>();
		try (Transaction tx = graphDb.beginTx()) {
			Node department = graphDb.getNodeById(deptId);
			Traverser empsInDept = getEmpInDept(department);
			for(Path empInDept:empsInDept){
				employees.add(empInDept.endNode().getProperty("name"));
			}
			tx.success();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return employees;
	}
	
	private Traverser getEmpInDept(Node department){
		TraversalDescription traverser = graphDb.traversalDescription().breadthFirst().relationships(RelationTypes.CONTAINS,Direction.OUTGOING).evaluator(Evaluators.excludeStartPosition());
		return traverser.traverse(department);
	}
	
	
	private Traverser getRepotees(Node supervisor,int level){
		TraversalDescription traverser = graphDb.traversalDescription().breadthFirst().relationships(RelationTypes.SUPERVISOR_OF,Direction.OUTGOING).evaluator(Evaluators.atDepth(level));
		return traverser.traverse(supervisor);
	}
	
	private Traverser getSubEmployees(Node supervisor){
		TraversalDescription traverser = graphDb.traversalDescription().breadthFirst().relationships(RelationTypes.SUPERVISOR_OF,Direction.OUTGOING).evaluator(Evaluators.excludeStartPosition());
		return traverser.traverse(supervisor);
	}
}
