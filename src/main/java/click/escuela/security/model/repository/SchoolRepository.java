package click.escuela.security.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.security.model.School;


public interface SchoolRepository extends JpaRepository<School, UUID> {

}
