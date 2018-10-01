package com.kmsoft.userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserprofileApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserprofileApplication.class, args);
	}
}
