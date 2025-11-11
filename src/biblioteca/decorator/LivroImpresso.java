package biblioteca.decorator;

import biblioteca.Livro;

public class LivroImpresso extends LivroDecorator {
	public enum TipoCapa { DURA, COMUM }
	private final TipoCapa tipoCapa;

	public LivroImpresso(Livro livroBase, TipoCapa tipoCapa) {
		super(livroBase);
		this.tipoCapa = tipoCapa;
	}

	public TipoCapa getTipoCapa() {
		return tipoCapa;
	}

	@Override
	public String toString() {
		return "LivroImpresso{" + getTitulo() + ", capa=" + tipoCapa + "}";
	}
}


