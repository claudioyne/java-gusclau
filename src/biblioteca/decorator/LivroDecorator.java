package biblioteca.decorator;

import biblioteca.Livro;

public abstract class LivroDecorator extends Livro {
	protected final Livro livroBase;

	protected LivroDecorator(Livro livroBase) {
		super(livroBase.getTitulo(), livroBase.getAutor(), livroBase.getAno());
		this.livroBase = livroBase;
	}

	@Override
	public String getTitulo() {
		return livroBase.getTitulo();
	}

	@Override
	public void setTitulo(String titulo) {
		livroBase.setTitulo(titulo);
	}

	@Override
	public String getAutor() {
		return livroBase.getAutor();
	}

	@Override
	public void setAutor(String autor) {
		livroBase.setAutor(autor);
	}

	@Override
	public int getAno() {
		return livroBase.getAno();
	}

	@Override
	public void setAno(int ano) {
		livroBase.setAno(ano);
	}

	@Override
	public boolean isDisponivel() {
		return livroBase.isDisponivel();
	}

	@Override
	public void setDisponivel(boolean disponivel) {
		livroBase.setDisponivel(disponivel);
	}
}


