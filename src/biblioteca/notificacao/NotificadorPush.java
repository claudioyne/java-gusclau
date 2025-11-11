package biblioteca.notificacao;

import biblioteca.Usuario;

public class NotificadorPush implements Observador {
	@Override
	public void notificar(String mensagem, Usuario usuario) {
		System.out.println("[PUSH] Para: " + usuario.getNome() + " | " + mensagem);
	}
}


