/**
 * The contents of this file are subject to the OpenMRS Public License
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
package org.openmrs.module.changerelationships.api.impl;

 
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.log.myLogger;
import org.openmrs.module.changerelationships.api.ChangeRelationshipsService;
import org.openmrs.module.changerelationships.api.db.ChangeRelationshipsDAO;

/**
 * It is a default implementation of {@link ChangeRelationshipsService}.
 */
public class ChangeRelationshipsServiceImpl extends BaseOpenmrsService implements ChangeRelationshipsService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PersonService personService;
	
	private ChangeRelationshipsDAO dao;
	private List<Person> people;
	private List<Relationship> allRelatedPeople;
	private List<RelationshipType> existingRelationshipTypes;
	private int noOfRelatedPeople;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ChangeRelationshipsDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ChangeRelationshipsDAO getDao() {
	    return dao;
    }
    
 
    @Override
    /*Matches the input string to an existing person object and returns the first person*/
	public Person getPersonObjectFromInputname(String fromPerson) {
    	
    	personService = Context.getPersonService();
		people = personService.getPeople(fromPerson, null);
		
		if(people.isEmpty())
			return null;
		
		myLogger.print("People objects with names matching " + fromPerson);
		for(Person p : people)
			myLogger.print( p.getGivenName() + " " + p.getFamilyName());
		
		
		return people.get(0);		//Return the first person matching the name
		
	}

    @Override
	/*Matches the input string to an existing relationship type*/
	public RelationshipType findRelationshipTypeFromInput(String relation) {
    	
    	personService = Context.getPersonService();
		myLogger.print("Trying to find a match for " + relation);
		RelationshipType fromRelationshipTypeSelected = null;
		existingRelationshipTypes = personService.getAllRelationshipTypes();
		for(RelationshipType rt : existingRelationshipTypes)
		{
			if( (rt.getaIsToB() + "/" + rt.getbIsToA()).equals(relation) )
			{
				myLogger.print( " Match found for relationshipType selected " +  rt.getaIsToB() + "/" + rt.getbIsToA());
				fromRelationshipTypeSelected = rt;
				break;
			}
		}
		
		return fromRelationshipTypeSelected;
	}

	
    

/*Function returns the number of people who are related to fromPerson as a fromPersonRelationshiptype */
@Override
public int numberOfRelationships(Person fromPerson, RelationshipType fromPersonRelationship) {
	
	personService = Context.getPersonService();
	
	allRelatedPeople = personService.getRelationships(fromPerson, null, fromPersonRelationship);
	
	myLogger.print("Details of related People where " + fromPerson.getFamilyName() + " is related as " + 
									fromPersonRelationship.getaIsToB() );
	for(Relationship r : allRelatedPeople)
		{
		myLogger.print("Person A : "  + r.getPersonA().getFamilyName() + "  "+ r.getRelationshipType().getaIsToB()+ 
					"/" + r.getRelationshipType().getbIsToA() + " Person B : " + r.getPersonB().getFamilyName() );
		}
	
	noOfRelatedPeople = allRelatedPeople.size();
	myLogger.print("No of People found related = " + noOfRelatedPeople);
	return noOfRelatedPeople;
}



/*Function matches all the people related to fromPerson in any way*/
	@Override
	public int numberOfRelationships(Person fromPerson) {
		personService = Context.getPersonService();
		
		allRelatedPeople = personService.getRelationships(fromPerson, null, null);
		printAllRelatedPeopleDetails();
		noOfRelatedPeople = allRelatedPeople.size();
		myLogger.print("No of People found related = " + noOfRelatedPeople);
		return noOfRelatedPeople;

	}

	
	/*Updates relations of all relatives of old person to new toRelationshipType of toPerson*/
	/*If unable to update the records of any of the relatives, the failure message is logged and the rest of the 
	  records are updated*/
	public boolean updateRelativesToNewPerson(Person toPerson, RelationshipType toRelationshipType)
	{
		personService = Context.getPersonService();
		boolean areAllUpdatesSuccessful = true;
      	for(Relationship relationship : allRelatedPeople)
      	{
      			if(toPerson.equals(relationship.getPersonB()))	//if new person is same as the person on the other side 
       			{												//	of the relationship, it would result in a relationship
      				areAllUpdatesSuccessful = false;				//as Person1 related to Person1, so avoid by skipping. Boundary case.
      				myLogger.print("Unable to update record for " + toPerson + " " +
      						"as it would result in the same person being on either side of the relationship. " +
      						"In this case " + toPerson.getFamilyName() + "related as " + relationship.getRelationshipType().getaIsToB()
      						+ " to" + relationship.getPersonB().getFamilyName());
      				continue;
      			}
      			relationship.setPersonA(toPerson);
      			relationship.setRelationshipType(toRelationshipType);
      			try
      			{
      			personService.saveRelationship(relationship);
      			myLogger.print("Updating " + relationship.getPersonB().getFamilyName() + "'s relation to new person " 
      											+ toPerson.getFamilyName() + " as a " + toRelationshipType.getaIsToB() );
      			}catch(APIException ae)
      			{
      				myLogger.print("Error while updating record for " + relationship.getPersonA().getFamilyName());
      				ae.printStackTrace();
      				//return false;
      			}
      	
    	}
      	myLogger.print("Printing records after update ");
      	printAllRelatedPeopleDetails();
    	return areAllUpdatesSuccessful;
	}

	
	
	private void printAllRelatedPeopleDetails() {
		
		for(Relationship r : allRelatedPeople)
		{
			myLogger.print(r.getPersonB().getFamilyName() + " relationshiptype " 
								+ r.getRelationshipType().getaIsToB() + "/" + r.getRelationshipType().getbIsToA());
		}
		
	}
	
	
}