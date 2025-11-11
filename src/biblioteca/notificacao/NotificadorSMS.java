package biblioteca.notificacao;

import biblioteca.Usuario;

public class NotificadorSMS implements Observador {
	@Override
	public void notificar(String mensagem, Usuario usuario) {
		System.out.println("[SMS] Para: " + usuario.getTelefone() + " | " + mensagem);
	}
}


