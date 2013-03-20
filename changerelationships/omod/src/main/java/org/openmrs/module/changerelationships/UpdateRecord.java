package org.openmrs.module.changerelationships;

import org.openmrs.RelationshipType;

public class UpdateRecord {

	String  toRelationshipType, toName;

	public UpdateRecord() {
		this.toName = new String();
		this.toRelationshipType = new String();
	}

	public String getToRelationshipType() {
		return toRelationshipType;
	}

	public void setToRelationshipType(String toRelationshipType) {
		this.toRelationshipType = toRelationshipType;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}
	
}
