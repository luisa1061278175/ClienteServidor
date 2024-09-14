package co.edu.uniquindio.model;



public class Libro {
    private String id;
    private String titulo;
    private String autor;
    private String tema;
    private boolean disponible;


    public Libro(String id, String titulo, String autor, String tema, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tema = tema;
        this.disponible = disponible;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", tema='" + tema + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}

