package com.dotori.backend;

import com.dotori.backend.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;

@SpringBootTest
class DotoriBackendApplicationTests {

    @Autowired
    private JwtService jwtService;

    private String generatedToken;

	@Test
	void contextLoads() {
	}

	@Test
	void tokenCreate(){
		var claims = new HashMap<String, Object>();
		claims.put("user_id", 923); //user no.923

		var expireAt = LocalDateTime.now().plusMinutes(10);

		generatedToken = jwtService.create(claims, expireAt);
		System.out.println("Generated token: " + generatedToken);
	}

	/**
	 * JWT Token Validation
	 * before this test, please run function tokenCreate first,
	 * as the token might have expired by the time you run this test.
	 */
	@Test
	void tokenValidate(){
		// First create a token
		tokenCreate();
		// Then validate the generated token
		jwtService.validation(generatedToken);
	}
}
