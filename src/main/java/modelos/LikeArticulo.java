package modelos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LikeArticulo implements Serializable {

    @Id
    private long articuloId;

    @Id
    private long usuarioId;

    private int val;

    public LikeArticulo() {}



    public long getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(long articuloId) {
        this.articuloId = articuloId;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
