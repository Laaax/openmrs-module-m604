package org.openmrs.module.changerelationships;


public class UpdateRecordForm {

	private String toRelationshipType;
	private String toName;

	public UpdateRecordForm() {
		this.toName = "";
		this.toRelationshipType = "";
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
