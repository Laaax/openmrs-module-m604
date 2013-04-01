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
import org.openmrs.api.context.Context;
import org.openmrs.log.myLogger;
import org.openmrs.module.changerelationships.ChangeRelationshipsData;
import org.openmrs.module.changerelationships.ChangeRelationshipsData.RecordUpdateStatus;
import org.openmrs.module.changerelationships.PatientSearchForm;
import org.openmrs.module.changerelationships.UpdateRecordForm;
import org.openmrs.module.changerelationships.api.ChangeRelationshipsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * The main controller.
 */
@Controller
@SessionAttributes({"changeRelationshipsData", "patientSearchForm", "updateRecordForm"})
public class  ChangeRelationshipsManageController{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	//Spring annotations to create these session objects on a per-session basis
	// if they do not exist already
	
	@ModelAttribute("changeRelationshipsData")
	public ChangeRelationshipsData getChangeRelationshipDataObject(){
		System.out.println("CREATING NEW ChangeRelationshipsData OBJECT");
		return new ChangeRelationshipsData();
	}
	
	@ModelAttribute("patientSearchForm")
	public PatientSearchForm getPatientSearchFormObject(){
		System.out.println("CREATING NEW PatientSearchForm OBJECT");
		return new PatientSearchForm();
	}
	
	@ModelAttribute("updateRecordForm")
	public UpdateRecordForm getPatientSearchObject(){
		System.out.println("CREATING NEW UpdateRecordForm OBJECT");
		return new UpdateRecordForm();
	}

	//Controller code to handle the management page data
	@RequestMapping(value = "/module/changerelationships/manage", method = RequestMethod.GET)
	public void manage(ModelMap model, @ModelAttribute("changeRelationshipsData") ChangeRelationshipsData formData,
			@ModelAttribute("patientSearchForm") PatientSearchForm searchForm) {
		
		model.addAttribute("user", Context.getAuthenticatedUser());
		List<RelationshipType> relationshipTypesInDB = Context.getPersonService().getAllRelationshipTypes();
		ArrayList<String> relationshipTypesString = new ArrayList<String>();
		

		//myLogger.print("Existing relations at Controller ");
		for(RelationshipType rt : relationshipTypesInDB)
		{
			relationshipTypesString.add(rt.getaIsToB() + "/" +  rt.getbIsToA());
		}
		ArrayList<String> relationshipTypesStringIncludingAll = new ArrayList<String>(relationshipTypesString);
		relationshipTypesStringIncludingAll.add("All");	//In case all relations of a person have to be changed
		
		model.addAttribute("existingRelationshipTypesName", relationshipTypesString);
		model.addAttribute("existingRelationshipTypesNameIncludingAll", relationshipTypesStringIncludingAll);
		model.addAttribute("fromPerson", searchForm.getFromName());
		model.addAttribute("fromPersonRelation", searchForm.getFromRelationshipType());
		
		model.addAttribute("recordUpdateStatus", formData.getRecordUpdateStatus());
		model.addAttribute("noOfRelations", formData.getNumberOfRelationshipsFound());
		model.addAttribute("changeRelationshipsForm", formData);
		
	}
	
	 
	 /*Finds number of related people and displays it to the user */
	@RequestMapping(value="/module/changerelationships/patientSearch", method = RequestMethod.POST)
	public String checkPatients(@ModelAttribute("changeRelationshipsData") ChangeRelationshipsData formData, 
		@ModelAttribute("patientSearchForm") PatientSearchForm searchForm){
		
		ChangeRelationshipsService crService = Context.getService(ChangeRelationshipsService.class);
			
		Person fromPersonObject = crService.getPersonObjectFromInputname(searchForm.getFromName());
			
		//handle the error case if the person object is null
		if(fromPersonObject == null){
			//Reason could be the person's name was not entered or no one with that name exists
			myLogger.print("unable to find a match for given person name");
											
			formData.setRecordUpdateStatus(RecordUpdateStatus.OLD_PERSON_NOT_FOUND_ERROR);
			formData.setNumberOfRelationshipsFound(0);
			return "redirect:/module/changerelationships/manage.form";
		}
				 
		//in case all relations of a person have to be changed call 
		if(searchForm.getFromRelationshipType().equals("All")){		
			formData.setNumberOfRelationshipsFound(crService.numberOfRelationships(fromPersonObject));	
		} else {
			RelationshipType fromPersonOldRelationshipTypeObject = crService.findRelationshipTypeFromInput(searchForm.getFromRelationshipType());
				
			formData.setNumberOfRelationshipsFound(crService.numberOfRelationships(fromPersonObject, fromPersonOldRelationshipTypeObject));
		}
		
		return "redirect:/module/changerelationships/manage.form";
		
	}

	/*
	 * On clicking change, this function is called and this in turn calls
	 * service methods to update corresponding records
	 */
	@RequestMapping(value = "/module/changerelationships/updateRecord", method = RequestMethod.POST)
	public String updateRecord(@ModelAttribute("updateRecordForm") UpdateRecordForm updateRecord, 
			@ModelAttribute("changeRelationshipsData") ChangeRelationshipsData formData) {

		myLogger.print("In updateRecods java code");
		ChangeRelationshipsService crService = Context.getService(ChangeRelationshipsService.class);
		//toPerson = updateRecord.getToName();
		myLogger.print("Trying to match name of new person " + updateRecord.getToName());
		Person toPersonObject = crService.getPersonObjectFromInputname(updateRecord.getToName());

		if (formData.getNumberOfRelationshipsFound() == 0) {
			formData.setRecordUpdateStatus(RecordUpdateStatus.NO_RELATED_PEOPLE_ERROR);
		} else {
			if (toPersonObject != null) {
				//toRelationshipType = new String(updateRecord.getToRelationshipType());
				RelationshipType toRelationshipTypeObject = crService.findRelationshipTypeFromInput(updateRecord.getToRelationshipType());
				if (toRelationshipTypeObject != null) {
					boolean areAllUpdatesSuccessful = crService.updateRelativesToNewPerson(toPersonObject,toRelationshipTypeObject);
					
					if (areAllUpdatesSuccessful)
						formData.setRecordUpdateStatus(RecordUpdateStatus.RECORDS_UPDATED_SUCCESFULLY);
					else
						formData.setRecordUpdateStatus(RecordUpdateStatus.UNABLE_TO_UPATE_ALL_RECORDS_ERROR);
				} else {
					myLogger.print("Unable to find new Relationship to which records have to be updated");
				}
			} else {
				// Reason could be no one with that name exists
				myLogger.print("unable to find a match for given name");
																		
				formData.setRecordUpdateStatus(RecordUpdateStatus.NEW_PERSON_NOT_FOUND_ERROR);
			}

		}
		return "redirect:/module/changerelationships/manage.form";
	}

}