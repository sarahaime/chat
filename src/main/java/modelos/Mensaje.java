package modelos;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Mensaje {

    @Id
    @GeneratedValue
    private long id;

    @Column(columnDefinition = "text")
    private String mensaje;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

}
