package com.quizzz.quizzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.quizapp")
public class QuizzzApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzzApplication.class, args);
	}

}
