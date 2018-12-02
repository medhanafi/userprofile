package com.comoressoft.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages= {"com.comoressoft.profile.repository"})
public class KmProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmProfileApplication.class, args);
	}
}
