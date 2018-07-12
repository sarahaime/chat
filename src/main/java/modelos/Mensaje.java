package modelos;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Mensaje {

    @Id
    @GeneratedValue
    private int id;

    @Column(columnDefinition = "text")
    private String mensaje;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;

    public static int nextMsjID = 0;



    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    private int chatID;

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getChatID() {
        return chatID;
    }

}
