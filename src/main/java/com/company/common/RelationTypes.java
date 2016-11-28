package com.company.common;

import org.neo4j.graphdb.RelationshipType;

public enum RelationTypes implements RelationshipType {
	SUPERVISOR_OF,
	PEER_OF,
	CONTAINS,
	HAS_HEAD
}
