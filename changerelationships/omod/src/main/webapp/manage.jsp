<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<p>Hello ${user.systemId}!</p>

<h2>Information</h2>
<form modelAttribute="patientSearch" method="POST" action="patientSearch.form" >
   
    
        <label path="fromName">Change Person from</label>
        <input path="fromName" name = "fromName" value = "${fromPerson}" required/>
        &nbsp&nbsp&nbsp
  	 	<label path="fromRelationshipType">Relationship Type</label>
  	 	<select path="fromRelationshipType" name = "fromRelationshipType">
          <c:forEach var = "relationshiptype"  items = "${existingRelationshipTypesNameIncludingAll}" >
        	 
        	<option value = "${relationshiptype}">  ${relationshiptype}   	</option>
        </c:forEach> 
        
    	<select>
    	
    	&nbsp &nbsp 

    	<input type="submit" value="Search"/>
    
    <br/>
    <br/>	
    <br/>
    <br/>	
	
    
    <p> The Search criteria for &nbsp${fromPerson}&nbsp as a &nbsp ${fromPersonRelation} &nbsp  has ${noOfRelations} relations </p> 
       
</form>

    <br/>
    <br/>	
    <br/>
    <br/>	
	

<form modelAttribute="updateRecord"  method="POST" action="updateRecord.form">
	<label path="toName">Change Person to</label>
        <input path="toName" name = "toName" required/>
        &nbsp&nbsp&nbsp
  	 	<label path="toRelationshipType">New Relationship Type</label>
  	 	<select path="toRelationshipType" name = "toRelationshipType">
          <c:forEach var = "relationshiptype"  items = "${existingRelationshipTypesName}" >
        	 
        	<option value = "${relationshiptype}">  ${relationshiptype}   	</option>
        </c:forEach> 
        
    	<select>
    	
    	&nbsp &nbsp 

    	<input type="submit" value="Change"/>
	</select>
	</form>

    <br/>
    <br/>	




<%
final int INITAL_RECORD_STATUS = 0, RECORDS_UPDATED_SUCCESFULLY = 1, OLD_PERSON_NOT_FOUND_ERROR = 2,
     NEW_PERSON_NOT_FOUND_ERROR  = 3, NO_RELATED_PEOPLE_ERROR = 4, UNABLE_TO_UPATE_ALL_RECORDS_ERROR = 5;
	
	int updateStatus = Integer.parseInt(request.getAttribute("recordUpdateStatus").toString());
			//out.print("Update Status : " + updateStatus );
			
	switch(updateStatus)
	{
	case  INITAL_RECORD_STATUS : break; 
	
	case	RECORDS_UPDATED_SUCCESFULLY : out.print("All Records updated successfully");
			break;
	case	OLD_PERSON_NOT_FOUND_ERROR : out.print("No record found for person whose relation has to be changed");
			break;
	case	NEW_PERSON_NOT_FOUND_ERROR :  out.print("No record found for new person to who relation has to be changed");
			break;
	case	NO_RELATED_PEOPLE_ERROR : out.print("No person related to person whose relationship has to be changed");
			break;
	case	UNABLE_TO_UPATE_ALL_RECORDS_ERROR : out.print("Unable to update all records. Some were updated though."+
															"See logs for more details");
			break;
																	
	default : break;
	}
%>


<%@ include file="/WEB-INF/template/footer.jsp"%>