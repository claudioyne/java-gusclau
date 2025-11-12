package ui;

import biblioteca.Biblioteca;
import biblioteca.Livro;
import biblioteca.Usuario;
import biblioteca.UsuarioFactory;
import biblioteca.decorator.LivroDigital;
import biblioteca.decorator.LivroImpresso;
import biblioteca.emprestimo.Emprestimo;
import biblioteca.notificacao.NotificadorEmail;
import biblioteca.notificacao.NotificadorPush;
import biblioteca.notificacao.NotificadorSMS;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BibliotecaApp {
	private final Biblioteca biblioteca;

	private final DefaultListModel<String> livrosModel = new DefaultListModel<>();
	private final DefaultListModel<Usuario> usuariosModel = new DefaultListModel<>();
	private final DefaultListModel<Emprestimo> emprestimosModel = new DefaultListModel<>();
	private final DefaultListModel<String> historicoModel = new DefaultListModel<>();
	private final DefaultListModel<String> notificacoesModel = new DefaultListModel<>();

	private final JComboBox<Livro> livrosDisponiveisCombo = new JComboBox<>();
	private final JComboBox<Usuario> usuariosCombo = new JComboBox<>();

	private BibliotecaApp() {
		this.biblioteca = Biblioteca.getInstance();
		inicializarObservers();
		inicializarDadosDemonstracao();
	}

	private void inicializarObservers() {
		biblioteca.registrarObservador(new NotificadorEmail());
		biblioteca.registrarObservador(new NotificadorSMS());
		biblioteca.registrarObservador(new NotificadorPush());
		biblioteca.registrarObservador(new NotificadorUI(notificacoesModel));
	}

	private void inicializarDadosDemonstracao() {
		if (!biblioteca.listarLivros().isEmpty() || !biblioteca.listarUsuarios().isEmpty()) {
			return;
		}

		Usuario admin = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.ADMIN, "Ana Admin", "ana@exemplo.com", "11999990000");
		Usuario aluno = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.ALUNO, "Bruno Aluno", "bruno@exemplo.com", "11911112222");
		Usuario professor = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.PROFESSOR, "Carla Prof", "carla@exemplo.com", "11933334444");
		biblioteca.cadastrarUsuario(admin);
		biblioteca.cadastrarUsuario(aluno);
		biblioteca.cadastrarUsuario(professor);

		Livro l1 = new Livro("Padrões de Projeto", "GoF", 1994);
		Livro l2 = new Livro("Java Efetivo", "Joshua Bloch", 2018);
		Livro digital = new LivroDigital(l1, "https://acervo.exemplo.com/livro-gof");
		Livro impresso = new LivroImpresso(l2, LivroImpresso.TipoCapa.DURA);

		biblioteca.cadastrarLivro(l1);
		biblioteca.cadastrarLivro(l2);
		biblioteca.cadastrarLivro(digital);
		biblioteca.cadastrarLivro(impresso);
	}

	private void montarInterface() {
		JFrame frame = new JFrame("Sistema de Biblioteca");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(900, 600));

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Livros", painelLivros());
		tabs.addTab("Usuários", painelUsuarios());
		tabs.addTab("Empréstimos", painelEmprestimos());
		tabs.addTab("Histórico", painelHistorico());
		tabs.addTab("Notificações", painelNotificacoes());

		frame.add(tabs, BorderLayout.CENTER);

		atualizarListas();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private JPanel painelLivros() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JList<String> listaLivros = new JList<>(livrosModel);
		panel.add(new JScrollPane(listaLivros), BorderLayout.CENTER);

		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setBorder(BorderFactory.createTitledBorder("Cadastrar novo livro"));

		JTextField tituloField = new JTextField(20);
		JTextField autorField = new JTextField(20);
		JSpinner anoSpinner = new JSpinner(new SpinnerNumberModel(2024, 1500, 2100, 1));

		JButton adicionarBtn = new JButton("Adicionar livro");
		adicionarBtn.addActionListener(e -> {
			String titulo = tituloField.getText().trim();
			String autor = autorField.getText().trim();
			int ano = (int) anoSpinner.getValue();

			if (titulo.isEmpty() || autor.isEmpty()) {
				mostrarErro("Preencha título e autor.");
				return;
			}

			Livro livro = new Livro(titulo, autor, ano);
			biblioteca.cadastrarLivro(livro);
			tituloField.setText("");
			autorField.setText("");
			anoSpinner.setValue(2024);
			atualizarListas();
			mostrarMensagem("Livro cadastrado com sucesso!");
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		formulario.add(new JLabel("Título:"), gbc);
		gbc.gridx = 1;
		formulario.add(tituloField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		formulario.add(new JLabel("Autor:"), gbc);
		gbc.gridx = 1;
		formulario.add(autorField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formulario.add(new JLabel("Ano:"), gbc);
		gbc.gridx = 1;
		formulario.add(anoSpinner, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		formulario.add(adicionarBtn, gbc);

		panel.add(formulario, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel painelUsuarios() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JList<Usuario> listaUsuarios = new JList<>(usuariosModel);
		listaUsuarios.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Usuario usuario) {
					setText(usuario.getTipo() + " - " + usuario.getNome() + " (" + usuario.getEmail() + ")");
				}
				return this;
			}
		});
		panel.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setBorder(BorderFactory.createTitledBorder("Cadastrar novo usuário"));

		JTextField nomeField = new JTextField(20);
		JTextField emailField = new JTextField(20);
		JTextField telefoneField = new JTextField(15);
		JComboBox<UsuarioFactory.TipoUsuario> tipoCombo = new JComboBox<>(UsuarioFactory.TipoUsuario.values());

		JButton adicionarBtn = new JButton("Adicionar usuário");
		adicionarBtn.addActionListener(e -> {
			String nome = nomeField.getText().trim();
			String email = emailField.getText().trim();
			String telefone = telefoneField.getText().trim();
			UsuarioFactory.TipoUsuario tipo = (UsuarioFactory.TipoUsuario) tipoCombo.getSelectedItem();

			if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || tipo == null) {
				mostrarErro("Preencha todos os campos.");
				return;
			}

			try {
				Usuario usuario = UsuarioFactory.criarUsuario(tipo, nome, email, telefone);
				biblioteca.cadastrarUsuario(usuario);
				nomeField.setText("");
				emailField.setText("");
				telefoneField.setText("");
				atualizarListas();
				mostrarMensagem("Usuário cadastrado com sucesso!");
			} catch (IllegalArgumentException ex) {
				mostrarErro(ex.getMessage());
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		formulario.add(new JLabel("Nome:"), gbc);
		gbc.gridx = 1;
		formulario.add(nomeField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		formulario.add(new JLabel("Email:"), gbc);
		gbc.gridx = 1;
		formulario.add(emailField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formulario.add(new JLabel("Telefone:"), gbc);
		gbc.gridx = 1;
		formulario.add(telefoneField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formulario.add(new JLabel("Tipo:"), gbc);
		gbc.gridx = 1;
		formulario.add(tipoCombo, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		formulario.add(adicionarBtn, gbc);

		panel.add(formulario, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel painelEmprestimos() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JList<Emprestimo> listaEmprestimos = new JList<>(emprestimosModel);
		listaEmprestimos.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Emprestimo emp) {
					String status = emp.getDataDevolucao() == null ? "Em aberto" : "Devolvido";
					setText(emp.getLivro().getTitulo() + " -> " + emp.getUsuario().getNome() + " | " + status);
				}
				return this;
			}
		});
		panel.add(new JScrollPane(listaEmprestimos), BorderLayout.CENTER);

		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setBorder(BorderFactory.createTitledBorder("Gerenciar empréstimo"));

		livrosDisponiveisCombo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Livro livro) {
					setText(livro.getTitulo() + " (" + livro.getAutor() + ")");
				} else if (value == null) {
					setText("Selecione um livro");
				}
				return this;
			}
		});

		usuariosCombo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Usuario usuario) {
					setText(usuario.getTipo() + " - " + usuario.getNome());
				} else if (value == null) {
					setText("Selecione um usuário");
				}
				return this;
			}
		});

		JButton emprestarBtn = new JButton("Realizar empréstimo");
		emprestarBtn.addActionListener(e -> {
			Livro livro = (Livro) livrosDisponiveisCombo.getSelectedItem();
			Usuario usuario = (Usuario) usuariosCombo.getSelectedItem();

			if (livro == null || usuario == null) {
				mostrarErro("Selecione livro e usuário.");
				return;
			}

			try {
				Emprestimo emprestimo = biblioteca.emprestar(livro.getId(), usuario.getId());
				atualizarListas();
				mostrarMensagem("Empréstimo realizado com sucesso! ID: " + emprestimo.getId());
			} catch (Exception ex) {
				mostrarErro(ex.getMessage());
			}
		});

		JButton devolverBtn = new JButton("Registrar devolução");
		devolverBtn.addActionListener(e -> {
			Emprestimo emprestimo = listaEmprestimos.getSelectedValue();
			if (emprestimo == null) {
				mostrarErro("Selecione um empréstimo para devolver.");
				return;
			}

			try {
				biblioteca.devolver(emprestimo.getId());
				atualizarListas();
				mostrarMensagem("Devolução registrada com sucesso!");
			} catch (Exception ex) {
				mostrarErro(ex.getMessage());
			}
		});

		JButton atrasosBtn = new JButton("Verificar atrasos");
		atrasosBtn.addActionListener(e -> biblioteca.notificarAtrasos());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		formulario.add(new JLabel("Livro disponível:"), gbc);
		gbc.gridx = 1;
		formulario.add(livrosDisponiveisCombo, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		formulario.add(new JLabel("Usuário:"), gbc);
		gbc.gridx = 1;
		formulario.add(usuariosCombo, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		formulario.add(emprestarBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		formulario.add(devolverBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		formulario.add(atrasosBtn, gbc);

		panel.add(formulario, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel painelHistorico() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JList<String> lista = new JList<>(historicoModel);
		panel.add(new JScrollPane(lista), BorderLayout.CENTER);
		return panel;
	}

	private JPanel painelNotificacoes() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JList<String> lista = new JList<>(notificacoesModel);
		panel.add(new JScrollPane(lista), BorderLayout.CENTER);
		return panel;
	}

	private void atualizarListas() {
		atualizarLivros();
		atualizarUsuarios();
		atualizarEmprestimos();
		atualizarHistorico();
	}

	private void atualizarLivros() {
		List<Livro> livros = biblioteca.listarLivros().stream()
				.sorted(Comparator.comparing(Livro::getTitulo))
				.collect(Collectors.toList());

		livrosModel.clear();
		for (Livro livro : livros) {
			livrosModel.addElement(livro.getTitulo() + " (" + livro.getAutor() + ") - " + (livro.isDisponivel() ? "Disponível" : "Indisponível"));
		}

		DefaultComboBoxModel<Livro> comboModel = new DefaultComboBoxModel<>();
		for (Livro livro : livros) {
			if (livro.isDisponivel()) {
				comboModel.addElement(livro);
			}
		}
		livrosDisponiveisCombo.setModel(comboModel);
		livrosDisponiveisCombo.setSelectedItem(comboModel.getSize() > 0 ? comboModel.getElementAt(0) : null);
	}

	private void atualizarUsuarios() {
		List<Usuario> usuarios = biblioteca.listarUsuarios().stream()
				.sorted(Comparator.comparing(Usuario::getNome))
				.collect(Collectors.toList());

		usuariosModel.clear();
		for (Usuario usuario : usuarios) {
			usuariosModel.addElement(usuario);
		}

		DefaultComboBoxModel<Usuario> comboModel = new DefaultComboBoxModel<>();
		for (Usuario usuario : usuarios) {
			comboModel.addElement(usuario);
		}
		usuariosCombo.setModel(comboModel);
		usuariosCombo.setSelectedItem(comboModel.getSize() > 0 ? comboModel.getElementAt(0) : null);
	}

	private void atualizarEmprestimos() {
		List<Emprestimo> emprestimos = biblioteca.listarEmprestimos().stream()
				.sorted(Comparator.comparing(Emprestimo::getDataEmprestimo).reversed())
				.collect(Collectors.toList());

		emprestimosModel.clear();
		for (Emprestimo emprestimo : emprestimos) {
			emprestimosModel.addElement(emprestimo);
		}
	}

	private void atualizarHistorico() {
		List<String> historico = biblioteca.getHistoricoOperacoes();
		historicoModel.clear();
		for (String evento : historico) {
			historicoModel.addElement(evento);
		}
	}

	private void mostrarErro(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	private void mostrarMensagem(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void exibir() {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {
			}

			BibliotecaApp app = new BibliotecaApp();
			app.montarInterface();
		});
	}
}


