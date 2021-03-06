package com.kmsoft.userprofile.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kmsoft.userprofile.model.User;
import com.kmsoft.userprofile.repository.UserRepository;
import com.kmsoft.userprofile.utils.JDException;

@Service
public class UserProfileService {

	@Autowired
	private UserProfileParser parser;

	public static String URL = "https://randomuser.me/api/";

	@Autowired
	private UserRepository userRepository;

	@Scheduled(cron = "0/1 * * * * ?")
	public void process() {
		User user = new User();
		try {
			parser.jsonParser(user, URL);
			if (user.getName() != null) {
				User oldUser = userRepository.findByEmailAndPhone(user.getEmail(), user.getPhone());
				if (oldUser == null) {
					userRepository.save(user);
				}
			}
		} catch (IOException | JDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
