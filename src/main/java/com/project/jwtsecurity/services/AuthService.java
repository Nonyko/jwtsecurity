package com.project.jwtsecurity.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.jwtsecurity.enums.RoleName;
import com.project.jwtsecurity.jwt.JwtProvider;
import com.project.jwtsecurity.message.request.SignUpForm;
import com.project.jwtsecurity.model.Role;
import com.project.jwtsecurity.model.UserCustom;
import com.project.jwtsecurity.repository.RoleRepository;
import com.project.jwtsecurity.repository.UserRepository;

@Service
public class AuthService {
	
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
    public void criarRole(RoleName role) {
    	Role roleCreated = new Role(role);
    	roleRepository.save(roleCreated);
    }
	public void criarUsuario(SignUpForm signUpRequest) {
		// Creating user's account
        UserCustom user = new UserCustom(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));
 
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
 
        strRoles.forEach(role -> {
          switch(role) {
          case "admin":
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(adminRole);
            
            break;
          
          default:
              Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
              roles.add(userRole);              
          }
        });
        
        user.setRoles(roles);
        userRepository.save(user);
	}

}
