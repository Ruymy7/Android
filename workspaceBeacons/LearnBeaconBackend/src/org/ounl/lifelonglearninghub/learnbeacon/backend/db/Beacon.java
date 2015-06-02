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

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Beacon {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	private String artefact_name; // Name of the point of interest
	private double artefact_latitude;
	private double artefact_longitude;

	private String beacon_id; // Major, minor ... tienes que mmirar el estandar
								// para ver cual es el valor a asignar aqui
	private String licence_cc; // Creative commons license for the content de
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getArtefact_name() {
		return artefact_name;
	}
	public void setArtefact_name(String artefact_name) {
		this.artefact_name = artefact_name;
	}
	public double getArtefact_latitude() {
		return artefact_latitude;
	}
	public void setArtefact_latitude(double artefact_latitude) {
		this.artefact_latitude = artefact_latitude;
	}
	public double getArtefact_longitude() {
		return artefact_longitude;
	}
	public void setArtefact_longitude(double artefact_longitude) {
		this.artefact_longitude = artefact_longitude;
	}
	public String getBeacon_id() {
		return beacon_id;
	}
	public void setBeacon_id(String beacon_id) {
		this.beacon_id = beacon_id;
	}
	public String getLicence_cc() {
		return licence_cc;
	}
	public void setLicence_cc(String licence_cc) {
		this.licence_cc = licence_cc;
	}

}