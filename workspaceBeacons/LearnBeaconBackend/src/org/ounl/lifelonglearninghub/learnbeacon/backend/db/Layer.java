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
package org.ounl.lifelonglearninghub.learnbeacon.backend.db;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Layer {
	
	public static final int LEVEL_ATTRACT = 2;
	public static final int LEVEL_REACT = 1;
	public static final int LEVEL_INTERACT = 0;

	public static final int SIGNAL_BEEP = 1;
	public static final int SIGNAL_VIBRATE = 0;
	
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	private String 	beacon_id; // Major, minor ... tienes que mmirar el estandar para ver cual es el valor a asignar aqui
	private String 	layer_cc; // Creative commons license for the beacon
	private int level; // 0 1 2 
	private int distance; // Distance in meters for which the content will be valid
	private int signal; // Type of alert to be triggered when this layer takes the focus
	private Text content; // Content for this layer
	private String tag; // Field to identify a group of layers across levels i.e. author name btb
	private long timestamp_update; // Timestamp when the item was last created or updated
	private long timestamp_validity_start; // Timestamp tange in which the timestamp is valid
	private long timestamp_validity_end;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBeacon_id() {
		return beacon_id;
	}
	public void setBeacon_id(String beacon_id) {
		this.beacon_id = beacon_id;
	}
	public String getLayer_cc() {
		return layer_cc;
	}
	public void setLayer_cc(String layer_cc) {
		this.layer_cc = layer_cc;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getSignal() {
		return signal;
	}
	public void setSignal(int signal) {
		this.signal = signal;
	}
	public Text getContent() {
		return content;
	}
	public void setContent(Text content) {
		this.content = content;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public long getTimestamp_update() {
		return timestamp_update;
	}
	public void setTimestamp_update(long timestamp_update) {
		this.timestamp_update = timestamp_update;
	}
	public long getTimestamp_validity_start() {
		return timestamp_validity_start;
	}
	public void setTimestamp_validity_start(long timestamp_validity_start) {
		this.timestamp_validity_start = timestamp_validity_start;
	}
	public long getTimestamp_validity_end() {
		return timestamp_validity_end;
	}
	public void setTimestamp_validity_end(long timestamp_validity_end) {
		this.timestamp_validity_end = timestamp_validity_end;
	} 
	

}