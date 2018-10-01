package com.kmsoft.userprofile.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "location")
public class Location {

	private String street;
	private String city;
	private String state;
	private String postcode;
	private Float co_latitude;
	private Float co_longitude;
	private String tz_offset;
	private String tz_description;

	@OneToMany(mappedBy = "location")
	private Set<User> users = new HashSet<>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Float getCoLatitude() {
		return co_latitude;
	}

	public void setCoLatitude(Float co_latitude) {
		this.co_latitude = co_latitude;
	}

	public Float getCoLongitude() {
		return co_longitude;
	}

	public void setCoLongitude(Float co_longitude) {
		this.co_longitude = co_longitude;
	}

	public String getTzOffset() {
		return tz_offset;
	}

	public void setTzOffset(String tz_offset) {
		this.tz_offset = tz_offset;
	}

	public String getTz_description() {
		return tz_description;
	}

	public void setTz_description(String tz_description) {
		this.tz_description = tz_description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((co_latitude == null) ? 0 : co_latitude.hashCode());
		result = prime * result + ((co_longitude == null) ? 0 : co_longitude.hashCode());
		result = prime * result + ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((tz_description == null) ? 0 : tz_description.hashCode());
		result = prime * result + ((tz_offset == null) ? 0 : tz_offset.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (co_latitude == null) {
			if (other.co_latitude != null)
				return false;
		} else if (!co_latitude.equals(other.co_latitude))
			return false;
		if (co_longitude == null) {
			if (other.co_longitude != null)
				return false;
		} else if (!co_longitude.equals(other.co_longitude))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (tz_description == null) {
			if (other.tz_description != null)
				return false;
		} else if (!tz_description.equals(other.tz_description))
			return false;
		if (tz_offset == null) {
			if (other.tz_offset != null)
				return false;
		} else if (!tz_offset.equals(other.tz_offset))
			return false;
		return true;
	}

}
