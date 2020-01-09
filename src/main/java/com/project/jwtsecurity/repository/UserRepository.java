package com.project.jwtsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.jwtsecurity.model.UserCustom;

@Repository
public interface UserRepository extends JpaRepository<UserCustom, Long>{

	Optional <UserCustom> findByUsername(String username);
	Boolean existsByUsername(String username);

}
