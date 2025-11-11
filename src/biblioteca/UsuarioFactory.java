package biblioteca;

public class UsuarioFactory {
	public enum TipoUsuario {
		ADMIN, ALUNO, PROFESSOR
	}

	public static Usuario criarUsuario(TipoUsuario tipo, String nome, String email, String telefone) {
		switch (tipo) {
			case ADMIN:
				return new Admin(nome, email, telefone);
			case ALUNO:
				return new Aluno(nome, email, telefone);
			case PROFESSOR:
				return new Professor(nome, email, telefone);
			default:
				throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
		}
	}
}


