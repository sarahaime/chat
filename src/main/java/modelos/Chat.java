package modelos;

import javax.persistence.*;
import java.util.Set;
@Entity
public class Chat {
    @Id
    @GeneratedValue
    private long id;
    private String userAddress;
    private String adminAddress = null;
    public static int nextChatID = 0;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Mensaje> mensajes;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public static int getNextChatID() {
        return nextChatID;
    }

    public static void setNextChatID(int nextChatID) {
        Chat.nextChatID = nextChatID;
    }

    public Set<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }


}
