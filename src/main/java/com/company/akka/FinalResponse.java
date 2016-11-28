package com.company.akka;

import java.io.Serializable;

import javax.ws.rs.core.Response;

public class FinalResponse implements Serializable{
	
	private static final long serialVersionUID = -5118525888530115856L;
	private final Response res;
	private long time;
	public FinalResponse(Response res,Long time) {
		this.res = res;
		this.time = time;
	}
	public Response getRes() {
		return res;
	}
	public Long getTime() {
		return time;
	}

}
