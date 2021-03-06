package modelos;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

public class Mensaje {

    private int chatID;
    private String mensaje;
    private Date fechaEnvio;
    private boolean admin;

    public Mensaje(int chatID, String mensaje) {
        this.chatID = chatID;
        this.mensaje = mensaje;
    }

    public Mensaje() { }

    public void setChatID(int chatID) {
        this.chatID = chatID;
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

    public int getChatID() {
        return chatID;
    }

}
