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
package org.openmrs.module.changerelationships.api;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.log.myLogger;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link ${ChangeRelationshipsService}}.
 */
public class  ChangeRelationshipsServiceTest extends BaseModuleContextSensitiveTest {
	
	private PersonService personService;
	private ChangeRelationshipsService testCangeRelationshipService;
	private ArrayList<Person> testPeople;
	private ArrayList<Relationship> testRelations;
	private ArrayList<RelationshipType> testRelationshipTypes;
	
	private void setup()
	{
		personService = Context.getPersonService();
		this.testCangeRelationshipService = Context.getService(ChangeRelationshipsService.class);
		this.testPeople = new ArrayList();
		this.testRelations = new ArrayList();
		this.testRelationshipTypes = new ArrayList();
	}
	
	
	private void createTestPeopleAndRelations()
	{
		setup();
		testPeople.add(createAndSavePerson("Person1", "For1", "Testing1", "Male", 1967, 8,3 ));
		testPeople.add(createAndSavePerson("Person2", "For2", "Testing2", "Male", 1967, 8,3 ));
		testPeople.add(createAndSavePerson("Person3", "For3", "Testing3", "Male", 1967, 8,3 ));
		testPeople.add(createAndSavePerson("Person4", "For4", "Testing4", "Male", 1967, 8,3 ));
		testPeople.add(createAndSavePerson("Person5", "For5", "Testing5", "Male", 1967, 8,3 ));
		
		testRelationshipTypes.add(createNewRelationType("TestRType1", "TestRType2"));
		testRelationshipTypes.add(createNewRelationType("TestRType3", "TestRType4"));
		testRelationshipTypes.add(createNewRelationType("TestRType5", "TestRType6"));
		testRelationshipTypes.add(createNewRelationType("TestRType7", "TestRType8"));
		
		testRelations.add(new Relationship(testPeople.get(0), testPeople.get(1), testRelationshipTypes.get(0) ));
		testRelations.add(new Relationship(testPeople.get(0), testPeople.get(2), testRelationshipTypes.get(0) ));
		testRelations.add(new Relationship(testPeople.get(0), testPeople.get(3), testRelationshipTypes.get(0) ));
		testRelations.add(new Relationship(testPeople.get(0), testPeople.get(4), testRelationshipTypes.get(1) ));
		saveAllRelations();
	}
	
	private void saveAllRelations() {

			for(Relationship r : testRelations)
				personService.saveRelationship(r);
	}


	private RelationshipType createNewRelationType(String string, String string2) {
			
		RelationshipType tempType = new RelationshipType();
		String desc = new String(string + string2);
		tempType.setDescription(desc);
		tempType.setaIsToB(string);
		tempType.setbIsToA(string2);
		personService.saveRelationshipType(tempType);
		return tempType;

	}

	private Person createAndSavePerson(String givenName, String middleName, String familyName, String gender,
			 int birthDateyy, int birthDatemm, int birthDatedd) 
				{
					
					Person tempPersonObj = new Person();
					PersonName tempPersonNameObj = new PersonName(givenName, middleName, familyName);
				
					tempPersonObj.setGender(gender);
					tempPersonObj.addName(tempPersonNameObj);
					tempPersonObj.setBirthdate(new Date(birthDateyy, birthDatemm, birthDatedd));
					try{
					 personService.savePerson(tempPersonObj);
					
					}catch(Exception ae)
					{
					
						myLogger.print("Unable to save person record to DB in Test Function");
						
					}
					return tempPersonObj;
				}	
	

	private void deleteDataCreatedForTests() {
		
		/*Purge all relations*/
		for(Relationship rs : testRelations)
		{
			personService.purgeRelationship(rs);
		}
		
		/*Purge all relationshipTypes*/
		for(RelationshipType rt : testRelationshipTypes)
		{
			personService.purgeRelationshipType(rt);
		}
		
		/*Purge all persons*/
		for(Person p : testPeople)
		{
			personService.purgePerson(p);
		}
		
		}
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(Context.getService(ChangeRelationshipsService.class));
	}
	
	@Test
	public void shouldSetupPersonService(){
		setup();
		assertNotNull(personService);
	}

	@Test
	public void getPersonObjectFromInputnameTest()
	{
		setup();
		Person testPerson = createAndSavePerson("Person4", "For4", "Testing4", "Male", 1909, 12, 12);
		Person testPerson2 = this.testCangeRelationshipService.getPersonObjectFromInputname(testPerson.getFamilyName());
		assertNotNull(testPerson2);
		assertArrayEquals(testPerson2.getFamilyName().toCharArray(), testPerson.getFamilyName().toCharArray());
		
		testPerson2 = this.testCangeRelationshipService.getPersonObjectFromInputname(testPerson.getGivenName());
		assertNotNull(testPerson2);
		assertArrayEquals(testPerson2.getGivenName().toCharArray(), testPerson.getGivenName().toCharArray());
		
		testPerson2 = this.testCangeRelationshipService.getPersonObjectFromInputname(testPerson.getMiddleName());
		assertNotNull(testPerson2);
		assertArrayEquals(testPerson2.getMiddleName().toCharArray(), testPerson.getMiddleName().toCharArray());
		
		//List<Person> testPeople = personService.getPeople("Testing2", null);
		//System.out.println("Saved " + testPeople.get(0).getFamilyName() + " to DB");
		personService.purgePerson(testPerson);	//delete  person created for testing
		//testPeople = personService.getPeople("Testing2", null);
		//if(testPeople.isEmpty())
			//System.out.println(" Person deleted");
	}
	
	@Test
	public void findRelationshipTypeFromInputTest()
	{
		setup();
		String relationName;
		List<RelationshipType> testTypes = personService.getAllRelationshipTypes();
		for(RelationshipType trstype : testTypes)
		{
			relationName = trstype.getaIsToB() +  "/" + trstype.getbIsToA();
			assertNotNull(this.testCangeRelationshipService.findRelationshipTypeFromInput(relationName));
		}
	}
	
	
	/*Test assertions depends on type of input provided in createTestPeopleAndRelations()*/
	@Test
	public void numberOfRelationshipGivenPersonAndRelationshipTypeTest()
	{
		setup();
		createTestPeopleAndRelations();
		 														//relationship that exists in the DB
		/*System.out.println("Test person : " + testPeople.get(0).getFamilyName() +
				            "Test RType : " + testRelationshipTypes.get(0).getaIsToB());*/
		assertEquals(3, this.testCangeRelationshipService.numberOfRelationships(testPeople.get(0), 
				 													testRelationshipTypes.get(0)));
		deleteDataCreatedForTests();
	}


	/*Test assertions depends on type of input provided in createTestPeopleAndRelations()*/
	@Test
	public void numberOfRelationshipsWhenAllSelected()
	{
		setup();
		createTestPeopleAndRelations();
		assertEquals(4, this.testCangeRelationshipService.numberOfRelationships(testPeople.get(0)));
		deleteDataCreatedForTests();
	}

	
	
}
