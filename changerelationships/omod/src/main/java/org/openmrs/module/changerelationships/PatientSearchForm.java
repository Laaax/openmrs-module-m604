package org.openmrs.module.changerelationships;

import org.openmrs.log.myLogger;

public class PatientSearchForm {

	private String fromName;
	private String fromRelationshipType;

	
	
	public PatientSearchForm()
	{
		//myLogger.print("Patient Search constructor called");
		this.fromName = "";
		this.fromRelationshipType = "";
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
