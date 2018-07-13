package modelos;

import javax.persistence.*;
import java.util.Set;
//@Entity
public class Chat {
    @Id
    @GeneratedValue
    private int id;
    private String userAddress;
    private String username;
    private String adminAddress = "-1";
    public static int nextChatID = 0;
    private boolean isOpen = false;
    private Mensaje lastMessage;
//    @OneToMany(fetch = FetchType.EAGER)
//    private Set<Mensaje> mensajes;


    public Chat(){}

    public Mensaje getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Mensaje lastMessage) {
        this.lastMessage = lastMessage;
    }



    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

//    public Set<Mensaje> getMensajes() {
//        return mensajes;
//    }
//
//    public void setMensajes(Set<Mensaje> mensajes) {
//        this.mensajes = mensajes;
//    }

    public Chat (String userAddress){
        this.userAddress = userAddress;
    }

    public Chat(int id, String userAddress, String adminAddress){
        this.adminAddress = adminAddress;
        this.userAddress= userAddress;
        this.id = id;
    }





}
