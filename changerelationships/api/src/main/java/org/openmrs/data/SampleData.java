package org.openmrs.data;

import java.util.ArrayList;
import java.util.Date;   
import java.util.List;

import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.log.myLogger;



public class SampleData {
	
	private static PersonService service;  
	private static ArrayList<Person> people;
	private static ArrayList<PersonName> peopleName;
	private static ArrayList<Relationship> newRelations;
	private static ArrayList<RelationshipType> newRelationshipTypes;
	
	public static void generate()
	{
		myLogger.print("Generating sample data...");
		people = new ArrayList<Person>();
		peopleName = new ArrayList<PersonName>();
		service = Context.getPersonService();
		newRelations = new ArrayList<Relationship>();
		createPersons();
		introduceRelationships();
		createNewRelationShipTypes();
	}
	
	
	private static void createNewRelationShipTypes() {
		
		newRelationshipTypes = new ArrayList();
		newRelationshipTypes.add(createNewRelationType("R1", "R2"));
		writeAllRelationTypesInDBTolog();
	}


	private static RelationshipType createNewRelationType(String string, String string2) {
		
		RelationshipType tempType = new RelationshipType();
		String desc = new String(string + string2);
		tempType.setDescription(desc);
		tempType.setaIsToB(string);
		tempType.setbIsToA(string2);
		service.saveRelationshipType(tempType);
		return tempType;
	}


	private static void introduceRelationships() {
		
		List<RelationshipType> existingRelationshipType = service.getAllRelationshipTypes();
		RelationshipType doctorPatient = new RelationshipType();
		RelationshipType sibling = new RelationshipType();
		RelationshipType parent = new RelationshipType();
		RelationshipType  auntUncle = new RelationshipType();
		
		myLogger.print("Existing relationshiptypes");
		for(RelationshipType rt : existingRelationshipType)
		{
			if(rt.getaIsToB().equals("Doctor"))
				doctorPatient = rt;
			if(rt.getaIsToB().equals("Sibling"))
				sibling = rt;
			if(rt.getaIsToB().equals("Parent"))
				parent = rt;
			if(rt.getaIsToB().equals("Aunt/Uncle"))
				auntUncle = rt;
			myLogger.print(rt.getRelationshipTypeId().toString() + rt.getaIsToB() + "/" + rt.getbIsToA());
		}
		
		//myLogger.print("new relation : " + doctorPatient.getaIsToB());
		newRelations.add(new Relationship(people.get(0), people.get(1), doctorPatient));
		newRelations.add(new Relationship(people.get(0), people.get(2), doctorPatient));
		newRelations.add(new Relationship(people.get(0), people.get(3), sibling));
		newRelations.add(new Relationship(people.get(0), people.get(4), parent));
		newRelations.add(new Relationship(people.get(0), people.get(5), auntUncle));
		
		for(Relationship rs: newRelations)
		{
			service.saveRelationship(rs);
		}
		
		
		List<Relationship> existingRelationships = service.getAllRelationships();
		myLogger.print("Existing relations");
		for(Relationship rs : existingRelationships)
		{ 
			myLogger.print( rs.getRelationshipId()+ " " + rs.getPersonA().getFamilyName() + "  " +
							rs.getPersonB().getFamilyName() );
		}
		
		
		
	}


	/*create some N new person objects*/
	public static void createPersons()
	{
		people.add(createPerson("Beaty", "Charles", "Model", "Male", 1909, 12, 12));
		people.add(createPerson("Tori", "KS " , "Brown"  , "Female" ,1997 ,11, 8 ));
		people.add(createPerson("Sam", "Will" , "Axe"  ,"Male" ,1987 ,03 , 12));
		people.add(createPerson("Roger", "Will" , "Bilwalk"  ,"Male" ,1987 ,03 , 12));
		people.add(createPerson("Karen", "Will" , "Mcd"  ,"Female" ,1987 ,03 , 12));
		people.add(createPerson("Pearson", "For" , "Pearson"  ,"Male" ,1987 ,03 , 12));
		
		
		saveAllPeopletoDB();
		writeAllPeopleInDBTolog();	
	}
	
			/*for debugging*/
	public static void writeAllPeopleInDBTolog() {
		
		List<Person> newPersons;
		for(Person p : people)
		{
			newPersons = service.getPeople(p.getFamilyName(), null);
			for(Person p2 : newPersons)
				myLogger.print("New person with ID " + p2.getPersonId() + " name  " + p2.getFamilyName() +
						" familyName2 : "+ p2		+ "  inserted to DB");
		}
		
	}

	
	/*for debugging*/
	public static void writeAllRelationTypesInDBTolog() {
	
		myLogger.print("List of all relationship types in DB");
	List<RelationshipType> temp = service.getAllRelationshipTypes();
	for( RelationshipType rt : temp)
	{
		myLogger.print(rt.getaIsToB() +  "/" + rt.getbIsToA());
	}
	
}


	private static void saveAllPeopletoDB() {
		
		try
		{
		for(Person p : people)
			service.savePerson(p);
		}catch(Exception ae)
			{
				myLogger.print("Unable to save a person to DB");
			}
	}


	public static Person createPerson(String givenName, String middleName, String familyName, String gender,
			 int birthDateyy, int birthDatemm, int birthDatedd) 
				{
					
					Person tempPersonObj = new Person();
					PersonName tempPersonNameObj = new PersonName(givenName, middleName, familyName);
					tempPersonNameObj.setFamilyName2(familyName);
					
					//tempPersonObj.setPersonId(new Integer(id));
					tempPersonObj.setGender(gender);
					tempPersonObj.addName(tempPersonNameObj);
					tempPersonObj.setBirthdate(new Date(birthDateyy, birthDatemm, birthDatedd));
					/*try{
					 service.savePerson(tempPersonObj);
					
					}catch(Exception ae)
					{
					
						myLogger.print("Unable to save person record to DB");
						//ae.printStackTrace();
						
					}
					
					people = service.getPeople(familyName, null);
					for(Person p : people)
						myLogger.print("New person with ID " + p.getPersonId() + " name  " + p.getFamilyName()
								+ "  inserted to DB");
					
					/*
					service.savePerson(tempPersonObj);
					//myLogger.print("New person " + tempPersonObj.getFamilyName() + " created");*/
					
					
					return tempPersonObj;
					
				}	
		
		public static void deleteAllRecordsCreatedInThisSession()
		{
			myLogger.print("Deleting all records");
			for(Person p : people)
				service.purgePerson(p);
		}
	

}
