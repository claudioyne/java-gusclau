package biblioteca.emprestimo;

public class ProfessorMulta implements MultaStrategy {
	private final double valorPorDia;

	public ProfessorMulta() {
		this.valorPorDia = 0.50;
	}

	public ProfessorMulta(double valorPorDia) {
		this.valorPorDia = valorPorDia;
	}

	@Override
	public double calcularMulta(long diasAtraso) {
		return diasAtraso <= 0 ? 0.0 : diasAtraso * valorPorDia;
	}
}


