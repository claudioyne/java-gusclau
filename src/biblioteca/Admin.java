package biblioteca;

public class Admin extends Usuario {
	public Admin(String nome, String email, String telefone) {
		super(nome, email, telefone);
	}

	@Override
	public String getTipo() {
		return "Admin";
	}
}


