package de.zeroco.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import de.zeroco.core.security.User;
import de.zeroco.core.security.UserRepository;

@SpringBootApplication
public class ContactMangementSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactMangementSpringbootApplication.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
		return args->{
			userRepository.save(new User("nani",passwordEncoder.encode("nani123"),"ROLE_USER"));
			userRepository.save(new User("sujwal",passwordEncoder.encode("sujwal@123"),"ROLE_ADMIN"));
		};
	}

}
