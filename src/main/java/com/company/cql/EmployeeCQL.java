package com.company.cql;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class EmployeeCQL {
	private GraphDatabaseService graphDb;
	
	public EmployeeCQL(GraphDatabaseService graphDb){
	     this.graphDb = graphDb;
	     try (Transaction tx = graphDb.beginTx()) { 
				graphDb.execute("CREATE INDEX ON :Department(deptId)"); 
				graphDb.execute("CREATE CONSTRAINT ON (d:Department) ASSERT d.deptId,d.deptName IS UNIQUE");
				tx.success(); 
			}catch (Exception e) {
				e.printStackTrace();
			}
	}

	public String createEmployee(String name, int age, Integer supervisorId, int departmentId){
		Result rs = null;
		 try (Transaction tx = graphDb.beginTx()) {
			 	String query = "CREATE(e:Employee{name:{name},age:{age},supervisorId:{supervisorId},departmentId:{departmentId}})RETURN e.Id";
			 	Map<String, Object> params = new HashMap<>();
			 	params.put("name", name);
			 	params.put("age", age);
			 	if(null!=supervisorId){
			 		params.put("supervisorId", supervisorId);
			 	}
			 	params.put("departmentId", departmentId);
		    	rs =  graphDb.execute(query,params);
		    	tx.success();
		      }catch (Exception e) {
		    	  e.printStackTrace();
			}
		 return rs.toString();
	}
	
	public String addSupervisor(int empId, int supervisorId){
		 Result rs = null;
		 try (Transaction tx = graphDb.beginTx()) {
			 String query = "MATCH (e:Employee) WHERE e.Id={empId}MATCH (s:Employee) WHERE s.Id={supervisorId}MERGE s-[:SUPERVISOR_OF]->x";
			 	Map<String, Object> params = new HashMap<>();
			 	params.put("empId", empId);
			 	params.put("supervisorId", supervisorId);
			 	rs = graphDb.execute(query,params);
			 tx.success();
		      }catch (Exception e) {
		    	  e.printStackTrace();
			}
		 return rs.toString();
	}
	
	public String changeSupervisor(int empId, int newSupervisorId){
		Result rs = null;
		 try (Transaction tx = graphDb.beginTx()) {
			 String query = "MATCH (e:Employee) WHERE e.employeeId={empId}MATCH (s:Employee) WHERE s.employeeId={newSupervisorId}MERGE s-[:SUPERVISOR_OF]->x";
			 	Map<String, Object> params = new HashMap<>();
			 	params.put("empId", empId);
			 	params.put("newSupervisorId", newSupervisorId);
			 	rs = graphDb.execute(query,params);
			 tx.success();
		      }catch (Exception e) {
		    	  e.printStackTrace();
			}
		 return rs.toString();
	}
	
	/*public void deleteEmployee(){
		 try (Transaction tx = graphDb.beginTx()) {
			 Result rs =  graphDb.execute("CREATE (e:Employee{name:"+name+",age:"+age+",supervisorId:"+supervisorId+",departmentId"+departmentId+"})");
		    	tx.success();
		      }
	}
*/
}
