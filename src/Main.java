import biblioteca.Biblioteca;
import biblioteca.Livro;
import biblioteca.Usuario;
import biblioteca.UsuarioFactory;
import biblioteca.decorator.LivroDigital;
import biblioteca.decorator.LivroImpresso;
import biblioteca.notificacao.NotificadorEmail;
import biblioteca.notificacao.NotificadorPush;
import biblioteca.notificacao.NotificadorSMS;
import biblioteca.emprestimo.Emprestimo;

public class Main {
	public static void main(String[] args) {
		Biblioteca bib = Biblioteca.getInstance();

		// Observers
		bib.registrarObservador(new NotificadorEmail());
		bib.registrarObservador(new NotificadorSMS());
		bib.registrarObservador(new NotificadorPush());

		// Usuários via Factory
		Usuario admin = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.ADMIN, "Ana Admin", "ana@exemplo.com", "11999990000");
		Usuario aluno = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.ALUNO, "Bruno Aluno", "bruno@exemplo.com", "11911112222");
		Usuario professor = UsuarioFactory.criarUsuario(UsuarioFactory.TipoUsuario.PROFESSOR, "Carla Prof", "carla@exemplo.com", "11933334444");
		bib.cadastrarUsuario(admin);
		bib.cadastrarUsuario(aluno);
		bib.cadastrarUsuario(professor);

		// Livros
		Livro l1 = new Livro("Padrões de Projeto", "GoF", 1994);
		Livro l2 = new Livro("Java Efetivo", "Joshua Bloch", 2018);
		bib.cadastrarLivro(l1);
		bib.cadastrarLivro(l2);

		// Decorator
		Livro digital = new LivroDigital(l1, "https://acervo.exemplo.com/livro-gof");
		Livro impresso = new LivroImpresso(l2, LivroImpresso.TipoCapa.DURA);
		System.out.println("Exemplos Decorator: " + digital + " | " + impresso);

		// Empréstimo (7 dias padrão). Forçamos atraso manipulando data apenas via simulação de notificação
		Emprestimo e1 = bib.emprestar(l1.getId(), aluno.getId());
		System.out.println("Emprestado e1: " + e1.getId());

		// Notificar novo lançamento
		Livro novo = new Livro("DDD", "Eric Evans", 2003);
		bib.cadastrarLivro(novo);
		bib.notificarNovoLancamento(novo);

		// Simulação de atrasos (neste exemplo, não alteramos datas, apenas chamamos método que verifica estado atual)
		bib.notificarAtrasos(); // provavelmente nenhum atraso imediato

		// Devolução (sem atraso real neste fluxo simples)
		bib.devolver(e1.getId());

		// Histórico
		System.out.println("Histórico de operações:");
		for (String h : bib.getHistoricoOperacoes()) {
			System.out.println(" - " + h);
		}
	}
}


