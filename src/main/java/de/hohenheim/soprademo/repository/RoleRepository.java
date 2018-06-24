package de.hohenheim.soprademo.repository;

import de.hohenheim.soprademo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Role findByRolename(String roleName);

}
