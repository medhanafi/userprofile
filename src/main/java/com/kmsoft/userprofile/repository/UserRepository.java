package com.kmsoft.userprofile.repository;

import org.springframework.data.repository.CrudRepository;

import com.kmsoft.userprofile.model.User;

public  interface UserRepository extends CrudRepository<User, Long>{

	User findByEmailAndPhone(String email, String phone);

	

}
