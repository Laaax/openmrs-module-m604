/**
 * The contents of this file are subject to the OpenMRS Public License/
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.changerelationships.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.RelationshipType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.log.myLogger;
import org.openmrs.module.changerelationships.PatientSearch;
import org.openmrs.module.changerelationships.UpdateRecord;
import org.openmrs.module.changerelationships.api.ChangeRelationshipsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.openmrs.module.changerelationships.ChangeRelationships;
//ChangeRelationshipsService;
//import org.openmrs.module.changerelationships.api.impl.ChangeRelationshipsServiceImpl;

/**
 * The main controller.
 */
@Controller
public class  ChangeRelationshipsManageController{
	
	private int noOfRelations;
	private List<RelationshipType> relationshipTypesInDB;
	private PersonService personService;
	private String fromPerson, fromPersonRelation, toPerson, toRelationshipType;
	private ChangeRelationshipsService changeRelationshipService;
	private int recordUpdateStatus;
	private boolean areAllUpdatesSuccessful;
	private final int RECORDS_UPDATED_SUCCESFULLY, NO_RELATED_PEOPLE_ERROR, INITAL_RECORD_STATUS, OLD_PERSON_NOT_FOUND_ERROR, 
							NEW_PERSON_NOT_FOUND_ERROR, UNABLE_TO_UPATE_ALL_RECORDS_ERROR;  
	
	protected final Log log = LogFactory.getLog(getClass());
	
		
	public ChangeRelationshipsManageController() {
		this.noOfRelations = 0;
		this.fromPerson  = new String("");
		this.fromPersonRelation = new String("");
		this.areAllUpdatesSuccessful = false;
		this.personService = Context.getPersonService();
		this.changeRelationshipService = Context.getService(ChangeRelationshipsService.class);
		this.INITAL_RECORD_STATUS = 0;
		this.RECORDS_UPDATED_SUCCESFULLY = 1;
		this.OLD_PERSON_NOT_FOUND_ERROR = 2;
		this.NEW_PERSON_NOT_FOUND_ERROR  = 3;
		this.NO_RELATED_PEOPLE_ERROR = 4;
		this.UNABLE_TO_UPATE_ALL_RECORDS_ERROR = 5;
		this.recordUpdateStatus = 99;
	}


	@ModelAttribute("patientSearch")
	public PatientSearch getPatientSearchObject(){
		return new PatientSearch();
	}
	
	@ModelAttribute("updateRecord")
	public UpdateRecord getUpdateRecordObject(){
		return new UpdateRecord();
	}
	
 


	@RequestMapping(value = "/module/changerelationships/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
		relationshipTypesInDB = personService.getAllRelationshipTypes();
		ArrayList<String> relationshipTypesString = new ArrayList<String>();
		
		//this.noOfRelations = 0;
		//myLogger.print("Existing relations at Controller ");
		for(RelationshipType rt : relationshipTypesInDB)
		{
			relationshipTypesString.add(rt.getaIsToB() + "/" +  rt.getbIsToA());
		}
		ArrayList<String> relationshipTypesStringIncludingAll = new ArrayList<String>(relationshipTypesString);
		relationshipTypesStringIncludingAll.add("All");	//In case all relations of a person have to be changed
		
		model.addAttribute("existingRelationshipTypesName", relationshipTypesString);
		model.addAttribute("existingRelationshipTypesNameIncludingAll", relationshipTypesStringIncludingAll);
		model.addAttribute("fromPerson", fromPerson);
		model.addAttribute("fromPersonRelation", fromPersonRelation);
		
		model.addAttribute("recordUpdateStatus", this.recordUpdateStatus);
		model.addAttribute("noOfRelations", noOfRelations);
		this.recordUpdateStatus = this.INITAL_RECORD_STATUS;

		
	}
	
	 @RequestMapping(value="/module/changerelationships/patientSearch", method = RequestMethod.GET)
	 public String handleRequest(ModelMap model) {
		 myLogger.print("IN GET METHOD OF PATIENTSEARCH!!!!!");
		 System.out.println("IN GET METHOD OF PATIENTSEARCH!!!!!");
		 PatientSearch patientSearch = new PatientSearch();
		 model.addAttribute("patientSearch", patientSearch); 
		return "patientSearch";
	 }
	
	 
	 /*Finds number of related people and displays it to the user */
	@RequestMapping(value="/module/changerelationships/patientSearch", method = RequestMethod.POST)
	public String checkPatients(@ModelAttribute("patientSearch") PatientSearch patientSearch){

		
		fromPerson = patientSearch.getFromName();
		
		//myLogger.print("Trying to match name " + fromPerson);
		Person fromPersonObject = changeRelationshipService.getPersonObjectFromInputname(fromPerson);
		
		if(fromPersonObject != null)
		{
			fromPersonRelation = new String(patientSearch.getFromRelationshipType());
			 
			if(fromPersonRelation.equals("All"))	//in case all relations of a person have to be changed call 
			{										// a different overloaded function 
				noOfRelations = changeRelationshipService.numberOfRelationships(fromPersonObject);
			}
			else
			{
			RelationshipType fromPersonOldRelationshipTypeObject = 
					changeRelationshipService.findRelationshipTypeFromInput(fromPersonRelation);
			
			noOfRelations = changeRelationshipService.numberOfRelationships(fromPersonObject, 
																	fromPersonOldRelationshipTypeObject);
			}
		}
		else
		{
			myLogger.print("unable to find a match for given person name");//Reason could be person's 
											//name was not entered or no one with that name exists
			this.recordUpdateStatus = this.OLD_PERSON_NOT_FOUND_ERROR;
			this.noOfRelations = 0 ;
		}
		
		return "redirect:/module/changerelationships/manage.form";
		
	}


	@RequestMapping(value="/module/changerelationships/updateRecord", method = RequestMethod.GET)
	public String handleUpdateRecordRequest(ModelMap model)
	{
		myLogger.print("Update Record called");
		UpdateRecord updateRecord = new UpdateRecord();
		 model.addAttribute("updateRecord", updateRecord);
		 return "updateRecord";
	}
	
	/*On clicking change, this function is called and this in turn calls service methods to update corresponding records*/	
	@RequestMapping(value="/module/changerelationships/updateRecord", method = RequestMethod.POST)
	public String updateRecord(@ModelAttribute("updateRecord")UpdateRecord updateRecord ){
		
		myLogger.print("In updateRecods java code");
		
		toPerson = updateRecord.getToName();
		myLogger.print("Trying to match name of new person " + toPerson);
		Person toPersonObject = changeRelationshipService.getPersonObjectFromInputname(toPerson);
		
		if(this.noOfRelations == 0)
		{
			this.recordUpdateStatus = this.NO_RELATED_PEOPLE_ERROR;
		}
		else
		{
			if(toPersonObject != null)
			{
				toRelationshipType = new String(updateRecord.getToRelationshipType());
				RelationshipType toRelationshipTypeObject = changeRelationshipService.findRelationshipTypeFromInput(toRelationshipType);
				if(toRelationshipTypeObject !=  null)
					{
						areAllUpdatesSuccessful  = this.changeRelationshipService.updateRelativesToNewPerson(toPersonObject, toRelationshipTypeObject);
						if(areAllUpdatesSuccessful)
							this.recordUpdateStatus = this.RECORDS_UPDATED_SUCCESFULLY;
						else
							this.recordUpdateStatus = this.UNABLE_TO_UPATE_ALL_RECORDS_ERROR;
					}
				else
				{
					myLogger.print("Unable to find new Relationship to which records have to be updated");
				}
			}
			else
			{
				myLogger.print("unable to find a match for given name");//Reason could be  no one with that name exists
				this.recordUpdateStatus = this.NEW_PERSON_NOT_FOUND_ERROR;
			}
			
		}
		return "redirect:/module/changerelationships/manage.form";
	}

}
