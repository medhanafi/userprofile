package com.kmsoft.userprofile.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity (name="pictures")
public class Picture  {

	private String large;
	private String medium;
	private String thumbnail;
	
	@OneToMany(mappedBy = "picture")
	private Set<Picture> picture = new HashSet<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getLarge() {
		return large;
	}
	public void setLarge(String large) {
		this.large = large;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((large == null) ? 0 : large.hashCode());
		result = prime * result + ((medium == null) ? 0 : medium.hashCode());
		result = prime * result + ((thumbnail == null) ? 0 : thumbnail.hashCode());
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
		Picture other = (Picture) obj;
		if (large == null) {
			if (other.large != null)
				return false;
		} else if (!large.equals(other.large))
			return false;
		if (medium == null) {
			if (other.medium != null)
				return false;
		} else if (!medium.equals(other.medium))
			return false;
		if (thumbnail == null) {
			if (other.thumbnail != null)
				return false;
		} else if (!thumbnail.equals(other.thumbnail))
			return false;
		return true;
	}
	
	
}
