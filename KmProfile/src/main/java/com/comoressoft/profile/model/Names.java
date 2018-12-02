package com.comoressoft.profile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity (name="names")
public class Names{
   

    private String gender;
    private String name;
    private String pronouce;
    
    
    
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;



	public Names() {
		super();
	}

	public Names(String gender, String name, String pronouce) {
		super();
		this.gender = gender;
		this.name = name;
		this.pronouce = pronouce;
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



	public String getPronouce() {
		return pronouce;
	}



	public void setPronouce(String pronouce) {
		this.pronouce = pronouce;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}

	

}