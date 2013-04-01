<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>


<p>Hello ${user.systemId}!</p>

<h2>Information</h2>
<table>
	<tr>
	<form modelAttribute="patientSearchForm" method="POST" action="patientSearch.form" >
   
    	<table>
    	<td>
        <label path="fromName">Change Person from</label>
        <input path="fromName" name = "fromName" value = "${fromPerson}" required/>
        </td>
        
        <td>
  	 		<label path="fromRelationshipType">Relationship Type</label>
  	 		<select path="fromRelationshipType" name = "fromRelationshipType">
          	<c:forEach var = "relationshiptype"  items = "${existingRelationshipTypesNameIncludingAll}" >
        	 
        		<option value = "${relationshiptype}">  ${relationshiptype}   	</option>
        	
           	</c:forEach> 
    		</select>  
    	</td>  	
    	 
	    <td>
    	<input type="submit" value="Search"/>
    	</td>
    	
    	</table>
    </form>     
	</tr>
	
	<tr>
    	<p> The Search criteria for ${fromPerson} as a ${fromPersonRelation} has ${noOfRelations} relations </p> 
    </tr>
    
    <tr>
    <form modelAttribute="updateRecordForm" method="POST" action="updateRecord.form">
    	<table>
    	<td>
    		<label path="toName">Change Person to</label>
        	<input path="toName" name = "toName" required/>
        </td>

		<td>
  	 	<label path="toRelationshipType">New Relationship Type</label>
  	 	<select path="toRelationshipType" name = "toRelationshipType">
          <c:forEach var = "relationshiptype"  items = "${existingRelationshipTypesName}" >
        	 
        	<option value = "${relationshiptype}">  ${relationshiptype}   	</option>
        </c:forEach> 
        
    	</select>
    	</td>
    	
    	<td>
    		<input type="submit" value="Change"/>
    	</td>
    	</table>
    
    </form>	
    </tr>
    
  </table>

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