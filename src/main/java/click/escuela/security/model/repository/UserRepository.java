package click.escuela.security.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import click.escuela.security.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	public Optional<User> findByUserName(String userName);

	public Optional<User> findByUserId(UUID userId);

}
