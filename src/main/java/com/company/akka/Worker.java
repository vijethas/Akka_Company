package com.company.akka;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

public class Worker extends UntypedActor{

		private ActorRef worker;
		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof Request) {
				Request request = (Request) message;
				getActor(request);
            } else if (message instanceof CustomRequest) {
    			CustomRequest cr = (CustomRequest) message;
    			Response response = callOperation(cr,getSelf());
    			getSender().tell(response, getSelf());
    			getContext().stop(getSelf());
    			//getContext().stop(getSender());
    		}else {
				unhandled(message);
			}
		}
		

		private Response callOperation(CustomRequest cr, ActorRef worker) {
			Response res = null;
			try {
				Class<?> className = getClassObject();
				Method method = getMethod(cr.getOperation());
				if (null != cr.getParams()) {
					res = (Response) method.invoke(className.newInstance(), cr.getParams());
				} else {
					res = (Response) method.invoke(className.newInstance());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			return res;
		}
		
		void getActor(Request req) {
			switch (req.getService()) {
			case "Department": {
				worker = this.getContext().actorOf(new RoundRobinPool(1).props(Props.create(DepartmentActor.class)),"Department");
				break;
			}
			case "Employee": {
				worker = this.getContext().actorOf(new RoundRobinPool(1).props(Props.create(EmployeeActor.class)),"Employee");
				break;
			}
			case "Common": {
				worker = this.getContext().actorOf(new RoundRobinPool(1).props(Props.create(CommonActor.class)),"Common");
				break;
			}
			}
			worker.tell(new CustomRequest(req.getOperation(), req.getParams()), getSender());
		}
		
		Class<?> getClassObject() {
			return null;
		}

		Method getMethod(String methodName) {
			return null;
		}
}
