package com.ukhanov.TwPersonaAI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TwPersonaAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwPersonaAiApplication.class, args);
	}

}
