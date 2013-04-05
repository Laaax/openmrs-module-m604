package org.openmrs.module.changerelationships;

import java.util.List;

import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.PersonService;
import org.openmrs.module.changerelationships.api.ChangeRelationshipsService;

public class ChangeRelationshipsData {
	
	public enum RecordUpdateStatus {
		RECORDS_UPDATED_SUCCESFULLY(1), 
		NO_RELATED_PEOPLE_ERROR(2), 
		INITAL_RECORD_STATUS(3), 
		OLD_PERSON_NOT_FOUND_ERROR(4), 
		NEW_PERSON_NOT_FOUND_ERROR(5), 
		UNABLE_TO_UPATE_ALL_RECORDS_ERROR(6);

		int code;

		private RecordUpdateStatus(int status) {
			code = status;
		}

		public int getStatus() {
			return code;
		}
	}
	
	private List<Relationship> allRelatedPeople;
	private int numberOfRelationshipsFound;
	private RecordUpdateStatus recordUpdateStatus;
	private boolean areAllUpdatesSuccessful;
	
	public int getRecordUpdateStatus(){
		return recordUpdateStatus.getStatus();
	}
	
	public void setRecordUpdateStatus(RecordUpdateStatus status){
		this.recordUpdateStatus=status;
	}
	
	public ChangeRelationshipsData(){
		//myLogger.print("Patient Search constructor called");
		this.numberOfRelationshipsFound = 0;
		this.recordUpdateStatus = RecordUpdateStatus.INITAL_RECORD_STATUS;
		this.setAreAllUpdatesSuccessful(false);
	}
	
	public int getNumberOfRelationshipsFound() {
		return numberOfRelationshipsFound;
	}

	public void setNumberOfRelationshipsFound(int numberOfRelationshipsFound) {
		this.numberOfRelationshipsFound = numberOfRelationshipsFound;
	}

	public boolean isAreAllUpdatesSuccessful() {
		return areAllUpdatesSuccessful;
	}

	public void setAreAllUpdatesSuccessful(boolean areAllUpdatesSuccessful) {
		this.areAllUpdatesSuccessful = areAllUpdatesSuccessful;
	}

	public List<Relationship> getAllRelatedPeople() {
		return allRelatedPeople;
	}

	public void setAllRelatedPeople(List<Relationship> allRelatedPeople) {
		this.allRelatedPeople = allRelatedPeople;
	}

}
