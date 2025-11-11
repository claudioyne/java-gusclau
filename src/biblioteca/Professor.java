package biblioteca;

public class Professor extends Usuario {
	public Professor(String nome, String email, String telefone) {
		super(nome, email, telefone);
	}

	@Override
	public String getTipo() {
		return "Professor";
	}
}


