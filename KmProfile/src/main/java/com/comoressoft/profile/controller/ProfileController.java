package com.comoressoft.profile.controller;

import java.io.IOException;
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

}
