openmrs-module-m604
===================

Guidelines to test the functionality of the code

1)Download the package openmrs-standalone-1.8.2-with-demo-data from http://en.flossmanuals.net/openmrs-guide/installation-and-initial-setup/
   and follow the setup procedure as described.
2) Navigate to the folder where the directory has been placed and run the program.
3)Open a web browser and enter in the URL   http:localhost:8081/openmrs-standalone
4)A login page should open up. Login witht the details
		Username : admin
		Password : Admin123
5)Go to the "Administration" section by clickin on the "Administration" tab.
6)Go to "Manage Module" section. 
7) Clone the git package from    https://github.com/ykolesn/openmrs-module-m604.git      to your local machine.
8)Run the cloned git package as a maven project
	nonte that both steps 7 and 8 are easier to perform using Eclipse
9)The maven build will generate a changerelationship.omod file in the  openmrs-module-m604/changerelationship/omod/target folder.
10)On the "Manage module " page upload the .omod file using the "Add module" function
11)Once uploaded go back to the administration page and refresh. You must be able to see a "Change relationship" link. Clicking on the link will take you to the page of 		our module. 


User guide.
1)To test the code some sample data is needed. To generate these, you will have to modify the source code. If however the Database has enough records, then this is not 	necessary
2)Navigate to openmrs-module-m604/changerelationship/api/src/main/java/org.openmrs.module.changerelationships/ChangeRelationshipModuleActivator.java
3)Comment out the line 49 to allow the execution of SampleDate.generate()
4)Then open the file  openmrs-module-m604/changerelationship/api/src/main/java/org.openmrs.data/SampleData.java
 	In this class create new people using the same format as the one used from line 111 to 116 on the createPersons()
	Similarly create new realtionships(line 83-87 in the function createNewRelationships)
5)Once the data   is input you are free to test the code to change relationships of different people using the web UI.
6) The first text box takes the name of the person who already is related to several people. The drop down menu allows the user to select the relationship of the person 	that has to be changed
7)The second text box takes the name of the person against whom new relations need to be created. The drop down menu allows the user to select the relationship of the person 	to which it has to be changed.
8)Press "Change", to update the records. A notification about the success status of the update will be displayed.
