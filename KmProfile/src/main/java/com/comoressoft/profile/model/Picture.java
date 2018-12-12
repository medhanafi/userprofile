package com.comoressoft.profile.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mha14633
 *
 *         6 déc. 2018
 *
 */
@Entity(name = "picture")
public class Picture {

	private String fileName;
	private byte[] pictureData;

	@JsonIgnore
	@OneToMany(mappedBy = "picture")
	private Set<KmUser> users = new HashSet<>();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getPictureData() {
		return pictureData;
	}

	public void setPictureData(byte[] pictureData) {
		this.pictureData = pictureData;
	}

	public Set<KmUser> getUsers() {
		return users;
	}

	public void setUsers(Set<KmUser> users) {
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
