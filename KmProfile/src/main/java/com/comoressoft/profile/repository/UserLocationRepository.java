package com.comoressoft.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.comoressoft.profile.model.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Integer>{

	@Query(value = "select * from user_location WHERE city=?1 ", nativeQuery = true)
	UserLocation findByCity(String city);

}
