package biblioteca.notificacao;

import biblioteca.Usuario;

public class NotificadorEmail implements Observador {
	@Override
	public void notificar(String mensagem, Usuario usuario) {
		System.out.println("[EMAIL] Para: " + usuario.getEmail() + " | " + mensagem);
	}
}


