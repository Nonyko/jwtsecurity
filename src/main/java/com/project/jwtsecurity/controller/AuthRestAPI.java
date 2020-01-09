package com.project.jwtsecurity.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.jwtsecurity.enums.RoleName;
import com.project.jwtsecurity.jwt.JwtProvider;
import com.project.jwtsecurity.message.request.LoginForm;
import com.project.jwtsecurity.message.request.SignUpForm;
import com.project.jwtsecurity.message.response.JwtResponse;
import com.project.jwtsecurity.model.Role;
import com.project.jwtsecurity.model.UserCustom;
import com.project.jwtsecurity.repository.RoleRepository;
import com.project.jwtsecurity.repository.UserRepository;
import com.project.jwtsecurity.services.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value="/api/auth")
@Api(value="API REST PARA AUTENTICACAO")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthRestAPI {
	  @Autowired
	    AuthenticationManager authenticationManager;
	 
	    @Autowired
	    UserRepository userRepository;
	 
	    @Autowired
	    RoleRepository roleRepository;
	 
	    @Autowired
	    PasswordEncoder encoder;
	 
	    @Autowired
	    JwtProvider jwtProvider;
	    
	    @Autowired
	    AuthService authService;
	    
	    @PostMapping("/signin")
	    @ApiOperation(value="endpoint para logar um usuario", response=String.class)
	    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
	 
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getUsername(),
	                        loginRequest.getPassword()
	                )
	        );
	 
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	 
	        String jwt = jwtProvider.generateJwtToken(authentication);
	        return ResponseEntity.ok(new JwtResponse(jwt));
	    }
	    
	    @PostMapping("/signup")
	    @ApiOperation(value="endpoint para criar um usuario", response=String.class)
	    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
	        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
	            return new ResponseEntity<String>("Fail -> Username is already taken!",
	                    HttpStatus.BAD_REQUEST);
	        }        
	 
	        // Creating user's account
	        authService.criarUsuario(signUpRequest);
	 
	        return ResponseEntity.ok().body("User registered successfully!");
	    }
	    
	    @PostMapping("/test")
	    @ApiOperation(value="endpoint teste", response=String.class)
	    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	    public ResponseEntity<String> teste(@RequestHeader("Authorization") String Authorizarion) {	 	      
	        return ResponseEntity.ok("Autorizado");
	    }
	    
	   
}
