/**
 * The contents of this file are subject to the OpenMRS Public icense
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

import org.openmrs.Person;
import org.openmrs.RelationshipType;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(ChangeRelationshipsService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface ChangeRelationshipsService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */
	
	public Person getPersonObjectFromInputname(String fromPerson);
	
	public RelationshipType findRelationshipTypeFromInput(String relation);
	
	int numberOfRelationships(Person fromPerson,RelationshipType fromPersonOldRelationshipTypeObject);
	
	int numberOfRelationships(Person fromPerson);
	
	public boolean updateRelativesToNewPerson(Person p, RelationshipType rst);
	
}