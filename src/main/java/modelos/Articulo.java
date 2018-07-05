package modelos;

import org.hibernate.annotations.CreationTimestamp;
import servicios.LikeArticuloServices;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Articulo {
    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "text")
    private String cuerpo;
    private String titulo;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    private Usuario autor;

    //para que incluya los comentarios sin problemas
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Comentario> comentarios;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Etiqueta> etiquetas;

    //campos que son computados
    @Transient
    private int likesCount;

    @Transient
    private int dislikesCount;



    //todo: estas son las funciones para que el articulo traiga los likes dislikes
    public long getLikesCount() {
        return LikeArticuloServices.getInstancia().getLikesByArticuloID(id, 1);
    }

    public long getDislikesCount() {
        return LikeArticuloServices.getInstancia().getLikesByArticuloID(id, 2);
    }

    public Articulo() { }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, Set<Comentario> comentarios, Set<Etiqueta> etiquetas) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.comentarios = comentarios;
        this.etiquetas = etiquetas;
    }






    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Set<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Set<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void agregarEtiqueta(Etiqueta e){
        etiquetas.add(e);
    }
    
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
}
