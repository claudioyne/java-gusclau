package biblioteca;

public class Aluno extends Usuario {
	public Aluno(String nome, String email, String telefone) {
		super(nome, email, telefone);
	}

	@Override
	public String getTipo() {
		return "Aluno";
	}
}


