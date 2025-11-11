package biblioteca.notificacao;

import biblioteca.Usuario;

public interface Observador {
	void notificar(String mensagem, Usuario usuario);
}


