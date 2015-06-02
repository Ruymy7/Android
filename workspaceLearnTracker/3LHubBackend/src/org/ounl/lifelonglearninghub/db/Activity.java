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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Activity {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String id_user;
	private String id_subject;
	private long activity_date_checkin;
	private long activity_date_checkout;
	private double activity_location_latitude;
	private double activity_location_longitude;
	private int activity_record_mode;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getId_user() {
		return id_user;
	}
	public void setId_user(String id_user) {
		this.id_user = id_user;
	}
	public String getId_subject() {
		return id_subject;
	}
	public void setId_subject(String id_subject) {
		this.id_subject = id_subject;
	}
	public long getActivity_date_checkin() {
		return activity_date_checkin;
	}
	public void setActivity_date_checkin(long activity_date_checkin) {
		this.activity_date_checkin = activity_date_checkin;
	}
	public long getActivity_date_checkout() {
		return activity_date_checkout;
	}
	public void setActivity_date_checkout(long activity_date_checkout) {
		this.activity_date_checkout = activity_date_checkout;
	}
	public double getActivity_location_latitude() {
		return activity_location_latitude;
	}
	public void setActivity_location_latitude(double activity_location_latitude) {
		this.activity_location_latitude = activity_location_latitude;
	}
	public double getActivity_location_longitude() {
		return activity_location_longitude;
	}
	public void setActivity_location_longitude(double activity_location_longitude) {
		this.activity_location_longitude = activity_location_longitude;
	}
	public int getActivity_record_mode() {
		return activity_record_mode;
	}
	public void setActivity_record_mode(int activity_record_mode) {
		this.activity_record_mode = activity_record_mode;
	}
	



}