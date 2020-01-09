package com.project.jwtsecurity.config;



import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


import com.project.jwtsecurity.enums.RoleName;
import com.project.jwtsecurity.message.request.SignUpForm;
import com.project.jwtsecurity.model.Role;
import com.project.jwtsecurity.services.AuthService;

@Component
public class InitialConfig implements
ApplicationListener<ContextRefreshedEvent>{
	//private static final Logger LOG = Logger.getLogger(InitialConfig.class);
	private boolean configRealizada = true;
	@Autowired
    AuthService authService;
	@Value("${jwtsecurity.app.usernameadmin}")
	  String usernameAdmin;
	  
	  @Value("${jwtsecurity.app.passadmin}")
	  String passAdmin;
  
  @Override public void onApplicationEvent(ContextRefreshedEvent event) {
      //LOG.info("Increment counter");
	  
	  if(!configRealizada) {
		 
		  
		//Criar roles iniciais
		  authService.criarRole(RoleName.ROLE_ADMIN);
		  authService.criarRole(RoleName.ROLE_USER);
		  
		  //Criar user inicial
		  SignUpForm form = new SignUpForm();
		  form.setUsername(usernameAdmin);
		  form.setPassword(passAdmin);
		  Set<String> roles = new HashSet<>();
		  roles.add(RoleName.ROLE_ADMIN.name());
		  form.setRole(roles);
		  authService.criarUsuario(form);
		  
		  this.configRealizada = true;
	  }
     
  }
}
