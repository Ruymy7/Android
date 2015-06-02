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

@Api(name = "goalendpoint", namespace = @ApiNamespace(ownerDomain = "ounl.org", ownerName = "ounl.org", packagePath = "lifelonglearninghub.db"))
public class GoalEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listGoal")
	public CollectionResponse<Goal> listGoal(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Goal> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Goal.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Goal>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Goal obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Goal> builder().setItems(execute)
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
	@ApiMethod(name = "getGoal")
	public Goal getGoal(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Goal goal = null;
		try {
			goal = mgr.getObjectById(Goal.class, id);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param goal
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertGoal")
	public Goal insertGoal(Goal goal) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (goal.getId() != null) {
				if (containsGoal(goal)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(goal);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param goal
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateGoal")
	public Goal updateGoal(Goal goal) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsGoal(goal)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(goal);
		} finally {
			mgr.close();
		}
		return goal;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeGoal")
	public void removeGoal(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Goal goal = mgr.getObjectById(Goal.class, id);
			mgr.deletePersistent(goal);
		} finally {
			mgr.close();
		}
	}

	private boolean containsGoal(Goal goal) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Goal.class, goal.getId());
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
