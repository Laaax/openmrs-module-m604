package org.openmrs.data;

import java.util.ArrayList; 
import java.util.Date;   
import java.util.List;

import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.log.myLogger;



public class SampleData {
	
	private static PersonService service;  
	private static ArrayList<Person> people;
	private static ArrayList<Relationship> newRelations;
	
	
	public static void generate()
	{
		myLogger.print("Generating sample data...");
		people = new ArrayList<Person>();
		service = Context.getPersonService();
		newRelations = new ArrayList<Relationship>();
		createPersons();
		introduceRelationships();
	
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
		
		saveAllRelationshipsToDB();
		writeAllrelationshipsInDBToLog();
	}


	/*create some N new person objects*/
	public static void createPersons()
	{
		/*Format for create person createPerson(FirstName, MiddleName, LastName, DOByy, DOBmm, DOBdd)*/
		people.add(createPerson("Brittney", "Keel", "Bellow", "Female", 1909, 12, 12));
		people.add(createPerson("Eric", "KS " , "Lane"  , "Female" ,1997 ,11, 8 ));
		people.add(createPerson("Daphen", "Well" , "Moone"  ,"Female" ,1987 ,03 , 12));
		people.add(createPerson("Fraser", "Martin" , "Crane"  ,"Male" ,1987 ,03 , 12));
		people.add(createPerson("Katie", "Miliband" , "Black"  ,"Female" ,1987 ,03 , 12));
		people.add(createPerson("Peter", "L" , "Lowesnky"  ,"Male" ,1987 ,03 , 12));
		
		
		saveAllPeopletoDB();
		//writeAllPeopleInDBTolog();	
	}
	
			/*for debugging*/
	public static void writeAllPeopleInDBTolog() {
		
		List<Person> newPersons;
		Person p;
		for(int i=0;i<people.size();i++)
		{
			p = people.get(i);
						myLogger.print("Searching in DB for person with name " + p.getFamilyName());
			newPersons = service.getPeople(p.getFamilyName(), null);
			for(Person p2 : newPersons)
				myLogger.print("Person with ID " + p2.getPersonId() + " name  " + p2.getFamilyName()+"  in DB");
		}
		
	}

	public static void writeAllrelationshipsInDBToLog()
	{
		List<Relationship> existingRelationships = service.getAllRelationships();
		myLogger.print("Existing relations");
		for(Relationship rs : existingRelationships)
		{ 
			myLogger.print( rs.getRelationshipId()+ " " + rs.getPersonA().getFamilyName() + "  " +
							rs.getPersonB().getFamilyName() );
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
		
		
		for(Person p : people)
		{
			try
			{
			service.savePerson(p);			
		}catch(Exception ae)
			{
				myLogger.print("Unable to save " + p.getFamilyName() +" to Database");
				myLogger.print(ae.getStackTrace().toString());
			}
		}
	}

	private static void saveAllRelationshipsToDB()
	{

		for(Relationship rs: newRelations)
		{
			service.saveRelationship(rs);
		}
		
	}
	
	
	public static Person createPerson(String givenName, String middleName, String familyName, String gender,
			 int birthDateyy, int birthDatemm, int birthDatedd) 
				{
					
					Person tempPersonObj = new Person();
					/*Id generated automatically for the person*/
					PersonName tempPersonNameObj = new PersonName(givenName, middleName, familyName);
					tempPersonNameObj.setFamilyName(familyName);
					tempPersonObj.setGender(gender);
					tempPersonObj.addName(tempPersonNameObj);
										//myLogger.print(tempPersonObj.getFamilyName() + " , " + tempPersonObj.getGivenName());
					tempPersonObj.setBirthdate(new Date(birthDateyy, birthDatemm, birthDatedd));
					
					return tempPersonObj;
					
				}	
		
		public static void deleteAllRecordsCreatedInThisSession()
		{
			service = Context.getPersonService();
			myLogger.print("Deleting all records");
			for(Relationship rs : newRelations)
				service.purgeRelationship(rs);
			newRelations.clear();
			for(Person p : people)
			{
				myLogger.print("deleting record of  " + p.getFamilyName());
				service.purgePerson(p);
			}
			people.clear();
			
		}
	

}
