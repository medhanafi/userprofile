package com.comoressoft.profile.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comoressoft.profile.dto.KmUserDTO;
import com.comoressoft.profile.mapper.GlobalMapper;
import com.comoressoft.profile.model.KmUser;
import com.comoressoft.profile.repository.KmUserRepository;

@RestController
public class ProfileController {

	@Autowired
	private KmUserRepository kmUserRepo;
	
	GlobalMapper mapper = Mappers.getMapper(GlobalMapper.class);

	public ProfileController() {
		super();
	}

	@GetMapping(value = "/profile")
	List<KmUserDTO> getProfile(HttpServletResponse response) throws IOException {
		List<KmUserDTO> users = getUsers();
		return users;
	}

	private List<KmUserDTO> getUsers() {
		List<KmUserDTO> users=new ArrayList<>();
		for(KmUser user:this.kmUserRepo.findAll()) {
			users.add(mapper.kmUserToKmUserDTO(user));
		}
		return users;
	}

	@GetMapping(value = "/fxdata")
	List<String> getData(HttpServletResponse response) throws IOException {
		return Arrays.asList("What Is JavaFX", "Get Started with JavaFX", "Get Acquainted with JavaFX Architecture",
				"Deployment Guide", "Getting Started with JavaFX 3D Graphics", "Use the Image Ops API",
				"Work with Canvas");
	}

}
