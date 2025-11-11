package biblioteca.emprestimo;

public class AlunoMulta implements MultaStrategy {
	private final double valorPorDia;

	public AlunoMulta() {
		this.valorPorDia = 1.00;
	}

	public AlunoMulta(double valorPorDia) {
		this.valorPorDia = valorPorDia;
	}

	@Override
	public double calcularMulta(long diasAtraso) {
		return diasAtraso <= 0 ? 0.0 : diasAtraso * valorPorDia;
	}
}


