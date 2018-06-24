package de.hohenheim.soprademo.repository;

import de.hohenheim.soprademo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

}
