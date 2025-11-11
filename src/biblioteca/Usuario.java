package biblioteca;

import java.util.Objects;
import java.util.UUID;

public abstract class Usuario {
	private final String id;
	private String nome;
	private String email;
	private String telefone;

	protected Usuario(String nome, String email, String telefone) {
		this.id = UUID.randomUUID().toString();
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}

	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public abstract String getTipo();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return Objects.equals(id, usuario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return getTipo() + "{id='" + id + "', nome='" + nome + "', email='" + email + "', telefone='" + telefone + "'}";
	}
}


