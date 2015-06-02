/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.ounl.lifelonglearninghub.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "subjectendpoint", namespace = @ApiNamespace(ownerDomain = "ounl.org", ownerName = "ounl.org", packagePath = "lifelonglearninghub.db"))
public class SubjectEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listSubject")
	public CollectionResponse<Subject> listSubject(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Subject> subjectList = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Subject.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			subjectList = (List<Subject>) query.execute();
			cursor = JDOCursorHelper.getCursor(subjectList);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Subject obj : subjectList)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Subject> builder().setItems(subjectList)
				.setNextPageToken(cursorString).build();
	}
	
	/**
	 * This method lists all the entities inserted in datastore for a given subject_desc. 
	 * It uses HTTP GET method and paging support.
	 * 
	 * @param subject_desc
	 * 
	 * @return A CollectionResponse class containing the list of  entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listSubjectCourse", path="subject/course/{subject_desc}")
	public CollectionResponse<Subject> listSubjectCourse(
			@Named("subject_desc") String courseId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Subject> subjectList = null;
		List<Subject> subjecFilteredtList = new ArrayList<Subject>();

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Subject.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			subjectList = (List<Subject>) query.execute();


			// Filter subject by subject_desc from param

			for (Subject obj : subjectList){
				String sSubjectDesc = obj.getSubject_desc();				
				if(courseId.compareToIgnoreCase(sSubjectDesc)==0){
					subjecFilteredtList.add(obj);
				}
			}
			
			cursor = JDOCursorHelper.getCursor(subjecFilteredtList);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

				
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Subject> builder().setItems(subjecFilteredtList)
				.setNextPageToken(cursorString).build();
	}
	

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getSubject")
	public Subject getSubject(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Subject subject = null;
		try {
			subject = mgr.getObjectById(Subject.class, id);
		} finally {
			mgr.close();
		}
		return subject;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param subject
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertSubject")
	public Subject insertSubject(Subject subject) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (subject.getId() != null) {
				if (containsSubject(subject)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(subject);
		} finally {
			mgr.close();
		}
		return subject;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param subject
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateSubject")
	public Subject updateSubject(Subject subject) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsSubject(subject)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(subject);
		} finally {
			mgr.close();
		}
		return subject;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeSubject")
	public void removeSubject(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Subject subject = mgr.getObjectById(Subject.class, id);
			mgr.deletePersistent(subject);
		} finally {
			mgr.close();
		}
	}

	private boolean containsSubject(Subject subject) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Subject.class, subject.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
