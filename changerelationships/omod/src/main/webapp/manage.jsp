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



<c:if test="${updateSuccessful == true}">
<p> Records updated successfully </p>
</c:if>



<%@ include file="/WEB-INF/template/footer.jsp"%>