package org.openmrs.module.changerelationships;

import org.openmrs.log.myLogger;

public class PatientSearch {

	String fromName, fromRelationshipType, toRelationshipType, toName;
	
	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToRelationshipType() {
		return toRelationshipType;
	}

	public void setToRelationshipType(String to) {
		this.toRelationshipType = to;
	}

	
	
	public PatientSearch()
	{
		myLogger.print("Patient Search constructor called");
		this.fromName = new String();
		this.fromRelationshipType = new String();
		this.toName = new String();
		this.toRelationshipType = new String();
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String name) {
		this.fromName = name;
	}

	public String getFromRelationshipType() {
		return fromRelationshipType;
	}

	public void setFromRelationshipType(String relationshipType) {
		this.fromRelationshipType = relationshipType;
	}
	
}
