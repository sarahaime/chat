package modelos;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Etiqueta implements Serializable{

    @Id
    @GeneratedValue
    private long id;

    private String etiqueta;

    @ManyToMany(mappedBy = "etiquetas", fetch = FetchType.EAGER)
    private Set<Articulo> articulos;

    public Etiqueta(){};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Set<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(Set<Articulo> articulos) {
        this.articulos = articulos;
    }


}
