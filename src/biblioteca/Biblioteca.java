package biblioteca;

import biblioteca.emprestimo.Emprestimo;
import biblioteca.notificacao.Observador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Biblioteca {
	private static Biblioteca instance;

	private final Map<String, Livro> idParaLivro;
	private final Map<String, Usuario> idParaUsuario;
	private final Map<String, Emprestimo> idParaEmprestimo;
	private final List<String> historicoOperacoes;
	private final List<Observador> observadores;
	private final int prazoPadraoDias;

	private Biblioteca() {
		this.idParaLivro = new HashMap<>();
		this.idParaUsuario = new HashMap<>();
		this.idParaEmprestimo = new HashMap<>();
		this.historicoOperacoes = new ArrayList<>();
		this.observadores = new ArrayList<>();
		this.prazoPadraoDias = 7;
	}

	public static synchronized Biblioteca getInstance() {
		if (instance == null) {
			instance = new Biblioteca();
		}
		return instance;
	}

	public void registrarObservador(Observador observador) {
		this.observadores.add(observador);
	}

	public void removerObservador(Observador observador) {
		this.observadores.remove(observador);
	}

	private void notificarTodos(String mensagem, Usuario usuario) {
		for (Observador o : observadores) {
			o.notificar(mensagem, usuario);
		}
	}

	public void cadastrarLivro(Livro livro) {
		idParaLivro.put(livro.getId(), livro);
		historicoOperacoes.add("Cadastrado livro: " + livro);
	}

	public void cadastrarUsuario(Usuario usuario) {
		idParaUsuario.put(usuario.getId(), usuario);
		historicoOperacoes.add("Cadastrado usuário: " + usuario);
	}

	public Optional<Livro> buscarLivroPorId(String id) {
		return Optional.ofNullable(idParaLivro.get(id));
	}

	public Optional<Usuario> buscarUsuarioPorId(String id) {
		return Optional.ofNullable(idParaUsuario.get(id));
	}

	public Emprestimo emprestar(String idLivro, String idUsuario) {
		Livro livro = idParaLivro.get(idLivro);
		Usuario usuario = idParaUsuario.get(idUsuario);
		if (livro == null) throw new IllegalArgumentException("Livro não encontrado");
		if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado");
		if (!livro.isDisponivel()) throw new IllegalStateException("Livro indisponível");

		livro.setDisponivel(false);
		Emprestimo emprestimo = new Emprestimo(livro, usuario, prazoPadraoDias);
		idParaEmprestimo.put(emprestimo.getId(), emprestimo);
		historicoOperacoes.add("Emprestado: " + livro.getTitulo() + " para " + usuario.getNome());
		return emprestimo;
	}

	public void devolver(String idEmprestimo) {
		Emprestimo emp = idParaEmprestimo.get(idEmprestimo);
		if (emp == null) throw new IllegalArgumentException("Empréstimo não encontrado");
		emp.registrarDevolucao();
		emp.getLivro().setDisponivel(true);
		double multa = emp.calcularMulta();
		historicoOperacoes.add("Devolução: " + emp.getLivro().getTitulo() + " por " + emp.getUsuario().getNome() + " | Multa: " + multa);
		if (multa > 0) {
			notificarTodos("Multa de R$ " + String.format("%.2f", multa) + " por atraso no livro " + emp.getLivro().getTitulo(), emp.getUsuario());
		}
	}

	public void notificarAtrasos() {
		for (Emprestimo emp : idParaEmprestimo.values()) {
			if (emp.estaAtrasado()) {
				notificarTodos("Seu empréstimo do livro '" + emp.getLivro().getTitulo() + "' está atrasado em " + emp.getDiasAtraso() + " dia(s).", emp.getUsuario());
			}
		}
	}

	public void notificarNovoLancamento(Livro livro) {
		for (Usuario usuario : idParaUsuario.values()) {
			notificarTodos("Novo lançamento disponível: " + livro.getTitulo() + " de " + livro.getAutor(), usuario);
		}
	}

	public List<String> getHistoricoOperacoes() {
		return new ArrayList<>(historicoOperacoes);
	}

	public List<Livro> listarLivros() {
		return new ArrayList<>(idParaLivro.values());
	}

	public List<Usuario> listarUsuarios() {
		return new ArrayList<>(idParaUsuario.values());
	}

	public List<Emprestimo> listarEmprestimos() {
		return new ArrayList<>(idParaEmprestimo.values());
	}
}


