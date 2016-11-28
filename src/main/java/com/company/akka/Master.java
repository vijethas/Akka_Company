package com.company.akka;

import javax.ws.rs.core.Response;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

public class Master extends UntypedActor{
	private ActorRef service = this.getContext().actorOf(new RoundRobinPool(5).props(Props.create(Worker.class)),"Worker");
	private final long start = System.currentTimeMillis();
	private ActorRef listner;
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Request) {
			listner = getSender();
            service.tell(message, getSelf());
        } else if (message instanceof Response) {
        	Response res = (Response) message;
        	Long duration = System.currentTimeMillis()-start;
        	listner.tell(new FinalResponse(res,duration), getSelf());
        	getContext().stop(getSender());
        } else {
        	unhandled(message);
        }
	}
}
