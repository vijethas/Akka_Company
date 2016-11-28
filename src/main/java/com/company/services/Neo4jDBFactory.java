package com.company.services;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jDBFactory {

	private static GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
	private static GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(new File("/data/graphDB/Company"));
	
	public static GraphDatabaseService getGraphDb(){
		if(graphDb== null){
			graphDb = graphDbFactory.newEmbeddedDatabase(new File("/data/graphDB/Company"));
			registerShutdownHook(graphDb);
		}
		return graphDb;
	}
	
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

}
