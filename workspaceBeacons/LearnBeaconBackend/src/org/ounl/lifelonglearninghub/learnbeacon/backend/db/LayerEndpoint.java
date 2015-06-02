package org.ounl.lifelonglearninghub.learnbeacon.backend.db;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.ounl.lifelonglearninghub.learnbeacon.backend.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "layerendpoint", namespace = @ApiNamespace(ownerDomain = "ounl.org", ownerName = "ounl.org", packagePath = "lifelonglearninghub.learnbeacon.backend.db"))
public class LayerEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listLayer")
	public CollectionResponse<Layer> listLayer(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Layer> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Layer.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Layer>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Layer obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Layer> builder().setItems(execute)
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
	@ApiMethod(name = "getLayer")
	public Layer getLayer(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Layer layer = null;
		try {
			layer = mgr.getObjectById(Layer.class, id);
		} finally {
			mgr.close();
		}
		return layer;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param layer
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertLayer")
	public Layer insertLayer(Layer layer) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (layer.getId() != null) {
				if (containsLayer(layer)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(layer);
		} finally {
			mgr.close();
		}
		return layer;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param layer
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateLayer")
	public Layer updateLayer(Layer layer) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsLayer(layer)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(layer);
		} finally {
			mgr.close();
		}
		return layer;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeLayer")
	public void removeLayer(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Layer layer = mgr.getObjectById(Layer.class, id);
			mgr.deletePersistent(layer);
		} finally {
			mgr.close();
		}
	}

	private boolean containsLayer(Layer layer) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Layer.class, layer.getId());
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
