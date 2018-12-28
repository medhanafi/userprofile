package com.comoressoft.profile.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comoressoft.profile.model.KmUser;
import com.comoressoft.profile.repository.KmUserRepository;

@RestController
public class ProfileController {

	@Autowired
	private KmUserRepository kmUserRepo;

	public ProfileController() {
		super();
	}

	@GetMapping(value = "/profile")
	List<KmUser> getProfile(HttpServletResponse response) throws IOException {
		List<KmUser> users = this.kmUserRepo.findAll();
		return users;
	}

	@GetMapping(value = "/fxdata")
	List<String> getData(HttpServletResponse response) throws IOException {
		return Arrays.asList("What Is JavaFX", "Get Started with JavaFX", "Get Acquainted with JavaFX Architecture",
				"Deployment Guide", "Getting Started with JavaFX 3D Graphics", "Use the Image Ops API",
				"Work with Canvas");
	}

}
