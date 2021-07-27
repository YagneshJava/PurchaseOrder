package com.purchaseInvoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.purchaseInvoice.models.AuthenticationRequest;
import com.purchaseInvoice.models.AuthenticationResponse;
import com.purchaseInvoice.service.MyUserDetailsService;
import com.purchaseInvoice.util.JWT;

@Controller
public class WelcomeController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWT jwtUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@GetMapping("/hello")
	@ResponseBody
	public String welcomeController() {
		return "Hey there!";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
			final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
			final String jwt = jwtUtil.generateToken(userDetails);
			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		} catch (Exception e) {
			throw new Exception("Incorrect Username or Password", e);
		}
	}
}
