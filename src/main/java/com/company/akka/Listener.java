package com.company.akka;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

public class Listener extends UntypedActor{
	
	public static void main(String[] args) {
		
		ActorSystem actorSystem = ActorSystem.create("Company");
		
		final ActorRef listener = actorSystem.actorOf(Props.create(Listener.class), "Listner");
		Map<String, Object> params = new HashMap<>();
		params.put("name", "DA");
		params.put("description", "DA");
		params.put("headId", null);
		Map<String, Object> params1 = new HashMap<>();
		params1.put("name", "Tom");
		params1.put("age", 40);
		params1.put("supervisorId", null);
		params1.put("departmentId", 0);
		ActorRef master = actorSystem.actorOf(new RoundRobinPool(2).props(Props.create(Master.class)), "Master");
		master.tell(new Request("Department", "createDepartment", params), listener);
		master.tell(new Request("Employee", "createEmployee", params1),listener);
		master.tell(new Request("Common", "getAllEmployees", null), listener);
		System.out.println("Completed");

	}
	@Override
	public void onReceive(Object message) throws Exception {
		try{
		if (message instanceof FinalResponse) {
            FinalResponse response = (FinalResponse) message;
            System.out.println("Time taken: "+ response.getTime()+"ms");
            Response res = (Response) response.getRes();
            System.out.println("Response: " +res.getEntity());
        } else {
        	unhandled(message);
        }
		}finally {
			//getContext().system().terminate();
		}
	}

}
