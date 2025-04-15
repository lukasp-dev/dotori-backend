package com.example.ucamcodeapi;

import com.example.ucamcodeapi.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;

@SpringBootTest
class UcamcodeapiApplicationTests {

    @Autowired
    private JwtService jwtService;

	@Test
	void contextLoads() {
	}

	@Test
	void tokenCreate(){
		var claims = new HashMap<String, Object>();
		claims.put("user_id", 923); //user no.923

		var expireAt = LocalDateTime.now().plusMinutes(10);

		var jwtToken = jwtService.create(claims, expireAt);

		System.out.println(jwtToken);
	}

	/**
	 * JWT Token Validation
	 * before this test, please run function tokenCreate first,
	 * as the token might have expired by the time you run this test.
	 */
	@Test
	void tokenValidate(){
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5MjMsImV4cCI6MTczMDE2NTExMn0.fMFTxHwE483mCnYmtLSHLDWr8bRUDnBPtBKQoxxOklg";// change
		jwtService.validation(token);
	}
}
