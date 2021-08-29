package click.escuela.security.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import click.escuela.security.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

	//Role getRoleById(UUID id);

	public Role findByName(String name);
}
