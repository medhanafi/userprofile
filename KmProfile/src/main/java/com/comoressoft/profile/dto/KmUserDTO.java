package com.comoressoft.profile.dto;

public class KmUserDTO {
	private int kmUserId;
	public int getKmUserId() {
		return kmUserId;
	}

	public void setKmUserId(int kmUserId) {
		this.kmUserId = kmUserId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public PictureDTO getPicture() {
		return picture;
	}

	public void setPicture(PictureDTO picture) {
		this.picture = picture;
	}

	public UserLocationDTO getLocation() {
		return location;
	}

	public void setLocation(UserLocationDTO location) {
		this.location = location;
	}

	private String gender;
	private String name;
	private String firstname;
	private String email;
	private String birth;
	private String cell;
	private String city;

	private PictureDTO picture;

	private UserLocationDTO location;

}
