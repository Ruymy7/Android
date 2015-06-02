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
public class Subject {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String subject_desc;
	private String subject_task_desc;
	private String subject_task_alternative_desc;
	private Long subject_task_date_start;
	private Long subject_task_time_duration;
	private int subject_task_level;
	private int subject_task_order;
	
	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject_desc() {
		return subject_desc;
	}
	public void setSubject_desc(String subject_desc) {
		this.subject_desc = subject_desc;
	}
	public String getSubject_task_desc() {
		return subject_task_desc;
	}
	public void setSubject_task_desc(String subject_task_desc) {
		this.subject_task_desc = subject_task_desc;
	}
	public Long getSubject_task_date_start() {
		return subject_task_date_start;
	}
	public void setSubject_task_date_start(Long subject_task_date_start) {
		this.subject_task_date_start = subject_task_date_start;
	}
	public Long getSubject_task_time_duration() {
		return subject_task_time_duration;
	}
	public void setSubject_task_time_duration(Long subject_task_time_duration) {
		this.subject_task_time_duration = subject_task_time_duration;
	}
	public int getSubject_task_level() {
		return subject_task_level;
	}
	public void setSubject_task_level(int subject_task_level) {
		this.subject_task_level = subject_task_level;
	}
	public int getSubject_task_order() {
		return subject_task_order;
	}
	public void setSubject_task_order(int subject_task_order) {
		this.subject_task_order = subject_task_order;
	}
	public String getSubject_task_alternative_desc() {
		return subject_task_alternative_desc;
	}
	public void setSubject_task_alternative_desc(
			String subject_task_alternative_desc) {
		this.subject_task_alternative_desc = subject_task_alternative_desc;
	}



}