package de.studycard.studycard.service;

import de.studycard.studycard.model.Role;
import de.studycard.studycard.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Speichert ein Role-Objekt.
     *
     * @param role die zu speichernde Role.
     * @return gespeichertes Role-Objekt inkl. generierter ID.
     */
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Gibt das Role-Objekt für einen bestimmten Rollennamen zurück.
     *
     * @param roleName der Name.
     * @return role-Objekt.
     */
    public Role getRoleByName(String roleName) {
        return roleRepository.findByRolename(roleName);
    }
}
