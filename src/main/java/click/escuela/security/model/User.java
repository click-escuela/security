package click.escuela.security.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
public class User {

	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2") 
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column
	private String name;

	@Column
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_role", nullable = false)
	private Role role;
}
