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

@Api(name = "activityendpoint", namespace = @ApiNamespace(ownerDomain = "ounl.org", ownerName = "ounl.org", packagePath = "lifelonglearninghub.db"))
public class ActivityEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listActivity")
	public CollectionResponse<Activity> listActivity(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Activity> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Activity.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Activity>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Activity obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Activity> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method lists all the entities inserted in datastore for a given
	 * courseId and userId. It uses HTTP GET method and paging support.
	 * 
	 * @param subject_desc
	 * @param userId
	 * 
	 * @return A CollectionResponse class containing the list of entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listActivityCourseUser", path = "activity/course/{subject_desc}/user/{user_id}")
	public CollectionResponse<Activity> listActivityCourseUser(
			@Named("subject_desc") String courseId,
			@Named("user_id") String userId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Activity> activityList = null;
		List<Activity> activityFilteredtList = new ArrayList<Activity>();

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Activity.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			activityList = (List<Activity>) query.execute();

			// Filter Activity by subject_desc and user_id from params
			for (Activity obj : activityList) {

				// TODO here you have to filter by course id
				// TOFIX Issue
				// https://code.google.com/p/lifelong-learning-hub/issues/detail?id=11
				String sSubjectDesc = obj.getId_subject();
				String sU = obj.getId_user();

				if (userId.compareToIgnoreCase(sU) == 0) {
					activityFilteredtList.add(obj);
				}
			}

			cursor = JDOCursorHelper.getCursor(activityFilteredtList);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

		} finally {
			mgr.close();
		}

		return CollectionResponse.<Activity> builder()
				.setItems(activityFilteredtList).setNextPageToken(cursorString)
				.build();
	}
	
	
	/**
	 * This method lists all the entities inserted in datastore for a given
	 * courseId. It uses HTTP GET method and paging support.
	 * 
	 * @param subject_desc
	 * 
	 * @return A CollectionResponse class containing the list of entities
	 *         persisted and a cursor to the next page.
	 */	
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listActivityCourse", path = "activity/course/{subject_desc}")
	public CollectionResponse<Activity> listActivityCourse(
			@Named("subject_desc") String courseId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Subject> subjectList = null;
		List<String> subjectIdFilteredtList = new ArrayList<String>();

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


			for (Subject obj : subjectList) {

				String sSubjectDesc = obj.getSubject_desc();

				if (sSubjectDesc.compareToIgnoreCase(courseId) == 0) {
					subjectIdFilteredtList.add(""+obj.getId());
				}
			}

			cursor = JDOCursorHelper.getCursor(subjectIdFilteredtList);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			

		} finally {
			mgr.close();
		}
		
		
		List<Activity> activityList = null;
		List<Activity> activityFilteredtList = new ArrayList<Activity>();		
		
		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Activity.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			activityList = (List<Activity>) query.execute();


			for (Activity obj : activityList) {

				String sIdSubject = obj.getId_subject();

				if(subjectIdFilteredtList.contains(sIdSubject)){
					activityFilteredtList.add(obj);
				}
			}

			cursor = JDOCursorHelper.getCursor(activityFilteredtList);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

		} finally {
			mgr.close();
		}
		

		return CollectionResponse.<Activity> builder()
				.setItems(activityFilteredtList).setNextPageToken(cursorString)
				.build();
	}
	
	
	
	

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getActivity")
	public Activity getActivity(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Activity activity = null;
		try {
			activity = mgr.getObjectById(Activity.class, id);
		} finally {
			mgr.close();
		}
		return activity;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param activity
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertActivity")
	public Activity insertActivity(Activity activity) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (activity.getId() != null) {
				if (containsActivity(activity)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(activity);
		} finally {
			mgr.close();
		}
		return activity;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param activity
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateActivity")
	public Activity updateActivity(Activity activity) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsActivity(activity)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(activity);
		} finally {
			mgr.close();
		}
		return activity;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeActivity")
	public void removeActivity(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Activity activity = mgr.getObjectById(Activity.class, id);
			mgr.deletePersistent(activity);
		} finally {
			mgr.close();
		}
	}

	/**
	 * This method removes the entity for given user and checkin. It uses HTTP
	 * DELETE method.
	 * 
	 * @param userId
	 *            String name of the user dateCheckIn long date in milliseconds
	 *            when checkin was performed
	 */
	@ApiMethod(name = "removeActivityCheckInUser", path = "activity/checkin/{activity_date_checkin}/user/{user_id}")
	public void removeActivityCheckInUser(@Named("user_id") String userId,
			@Named("activity_date_checkin") Long dateCheckIn,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Activity> activityList = null;
		int iRowsRemoved = 0;
		List<Activity> activityFilteredtList = new ArrayList<Activity>();

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Activity.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			activityList = (List<Activity>) query.execute();

			// Filter Activity by activity_date_checkin and user_id from params
			for (Activity obj : activityList) {

				long lCheckIn = obj.getActivity_date_checkin();
				String sUser = obj.getId_user();

				if ((userId.compareToIgnoreCase(sUser) == 0)
						&& (lCheckIn == dateCheckIn)) {
					activityFilteredtList.add(obj);

					Activity activity = mgr.getObjectById(Activity.class,
							obj.getId());
					mgr.deletePersistent(activity);
					iRowsRemoved++;

				}
			}

		} finally {
			mgr.close();
		}

		// iRowsRemoved contains the number of rows removed

	}

	private boolean containsActivity(Activity activity) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Activity.class, activity.getId());
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
