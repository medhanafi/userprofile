package com.comoressoft.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comoressoft.profile.model.KmUser;


public interface KmUserRepository extends JpaRepository<KmUser, Integer> {

}
