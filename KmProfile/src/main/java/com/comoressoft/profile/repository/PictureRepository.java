package com.comoressoft.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comoressoft.profile.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Integer> {

}
