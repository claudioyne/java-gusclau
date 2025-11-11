package biblioteca;

import java.util.Objects;
import java.util.UUID;

public class Livro {
	private final String id;
	private String titulo;
	private String autor;
	private int ano;
	private boolean disponivel;

	public Livro(String titulo, String autor, int ano) {
		this.id = UUID.randomUUID().toString();
		this.titulo = titulo;
		this.autor = autor;
		this.ano = ano;
		this.disponivel = true;
	}

	public String getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Livro livro = (Livro) o;
		return Objects.equals(id, livro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Livro{id='" + id + "', titulo='" + titulo + "', autor='" + autor + "', ano=" + ano + ", disponivel=" + disponivel + "}";
	}
}


