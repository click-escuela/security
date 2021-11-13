package click.escuela.security.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2") 
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column
	private String userName;

	@Column
	private String password;
	
	@Column 
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_role", nullable = false)
	private Role role;
	
	@ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="id_school")
	private School school;
	
	@Column
	private String name;
	
	@Column
	private UUID userId;
}
