package biblioteca.emprestimo;

import biblioteca.Aluno;
import biblioteca.Admin;
import biblioteca.Livro;
import biblioteca.Professor;
import biblioteca.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Emprestimo {
	private final String id;
	private final Livro livro;
	private final Usuario usuario;
	private final LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;
	private final int prazoDias;

	public Emprestimo(Livro livro, Usuario usuario, int prazoDias) {
		this.id = UUID.randomUUID().toString();
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.prazoDias = prazoDias;
	}

	public String getId() {
		return id;
	}

	public Livro getLivro() {
		return livro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void registrarDevolucao() {
		this.dataDevolucao = LocalDate.now();
	}

	public LocalDate getDataPrevistaDevolucao() {
		return dataEmprestimo.plusDays(prazoDias);
	}

	public long getDiasAtraso() {
		LocalDate dataReferencia = dataDevolucao != null ? dataDevolucao : LocalDate.now();
		long dias = ChronoUnit.DAYS.between(getDataPrevistaDevolucao(), dataReferencia);
		return Math.max(0, dias);
	}

	public boolean estaAtrasado() {
		return getDiasAtraso() > 0;
	}

	public double calcularMulta() {
		long atraso = getDiasAtraso();
		MultaStrategy strategy;
		if (usuario instanceof Admin) {
			return 0.0;
		} else if (usuario instanceof Aluno) {
			strategy = new AlunoMulta();
		} else if (usuario instanceof Professor) {
			strategy = new ProfessorMulta();
		} else {
			strategy = diasAtraso -> diasAtraso; // default fallback
		}
		return strategy.calcularMulta(atraso);
	}
}


