package modelos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class LikeComentario implements Serializable {

    @Id
    private long comentarioId;

    @Id
    private long usuarioId;

    private int val;

    public LikeComentario() {}

    public long getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(long comentarioId) {
        this.comentarioId = comentarioId;
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
