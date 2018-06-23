package de.studycard.studycard.repository;

import de.studycard.studycard.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRolename(String roleName);
}
