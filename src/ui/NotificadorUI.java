package ui;

import biblioteca.Usuario;
import biblioteca.notificacao.Observador;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class NotificadorUI implements Observador {
	private final DefaultListModel<String> notificacoesModel;

	public NotificadorUI(DefaultListModel<String> notificacoesModel) {
		this.notificacoesModel = notificacoesModel;
	}

	@Override
	public void notificar(String mensagem, Usuario usuario) {
		String registro = (usuario != null ? usuario.getNome() : "Sistema") + ": " + mensagem;
		SwingUtilities.invokeLater(() -> notificacoesModel.addElement(registro));
	}
}


