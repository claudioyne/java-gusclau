package biblioteca.decorator;

import biblioteca.Livro;

public class LivroDigital extends LivroDecorator {
	private final String urlAcesso;

	public LivroDigital(Livro livroBase, String urlAcesso) {
		super(livroBase);
		this.urlAcesso = urlAcesso;
	}

	public String getUrlAcesso() {
		return urlAcesso;
	}

	@Override
	public String toString() {
		return "LivroDigital{" + getTitulo() + ", url='" + urlAcesso + "'}";
	}
}


