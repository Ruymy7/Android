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

@Api(name = "enrolmentendpoint", namespace = @ApiNamespace(ownerDomain = "ounl.org", ownerName = "ounl.org", packagePath = "lifelonglearninghub.db"))
public class EnrolmentEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listEnrolment")
	public CollectionResponse<Enrolment> listEnrolment(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Enrolment> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Enrolment.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Enrolment>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Enrolment obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Enrolment> builder().setItems(execute)
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
	@ApiMethod(name = "getEnrolment")
	public Enrolment getEnrolment(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Enrolment enrolment = null;
		try {
			enrolment = mgr.getObjectById(Enrolment.class, id);
		} finally {
			mgr.close();
		}
		return enrolment;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param enrolment
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertEnrolment")
	public Enrolment insertEnrolment(Enrolment enrolment) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (enrolment.getId() != null) {
				if (containsEnrolment(enrolment)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(enrolment);
		} finally {
			mgr.close();
		}
		return enrolment;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param enrolment
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateEnrolment")
	public Enrolment updateEnrolment(Enrolment enrolment) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsEnrolment(enrolment)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(enrolment);
		} finally {
			mgr.close();
		}
		return enrolment;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeEnrolment")
	public void removeEnrolment(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Enrolment enrolment = mgr.getObjectById(Enrolment.class, id);
			mgr.deletePersistent(enrolment);
		} finally {
			mgr.close();
		}
	}

	private boolean containsEnrolment(Enrolment enrolment) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Enrolment.class, enrolment.getId());
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
