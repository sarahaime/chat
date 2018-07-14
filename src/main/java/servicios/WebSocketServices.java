package servicios;

import modelos.Chat;
import modelos.Mensaje;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WebSocketServices {

    public static List<Session> usuariosEnLinea = new ArrayList<>();
    public static List<Chat> chats = new ArrayList<>();
    public static List<Mensaje> mensajes = new ArrayList<>();

    public static void enviarMensaje(String mensaje, String addresDestino){
        for(Session sesionConectada : usuariosEnLinea){
            try {
                if( sesionConectada.getRemote().toString().equalsIgnoreCase(addresDestino)){
                    sesionConectada.getRemote().sendString(mensaje);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Chat getChatByID(int id){
        for(Chat chat: chats){
            if (chat.getId() == id)
                return chat;
        }
        return null;
    }


    public static void enviarBroadcast(String mensaje){
        for(Session sesionConectada : usuariosEnLinea){
            try {
                sesionConectada.getRemote().sendString(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateChat(Chat ch){
        for(Chat chat: chats){
            if (chat.getId() == ch.getId()){
                chat.setAdminAddress(ch.getAdminAddress());
                return;
            }
        }
    }

    public static Chat getChatByUserAdd(String clientAddress){
        Chat ch = null;
        for(Chat chat: chats){
            if (chat.getUserAddress().equals(clientAddress)){
                ch = chat;
                break;
            }
        }
        return ch;
    }

    public static Chat getChatByAdminAdd(String administratorAddress){
        Chat ch = null;
        for(Chat chat: chats){
            if (chat.getAdminAddress().equals(administratorAddress)){
                ch = chat;
                break;
            }
        }
        return ch;
    }

    public static int createChat(String clientAddress, String username){

        for(Chat chat: chats){
            if (chat.getUserAddress() == clientAddress){
                chat.setUsername(username);
                return chat.getId();
            }
        }

        Chat chat = new Chat(clientAddress);
        chat.setId(Chat.nextChatID++);
        chat.setUsername(username);
        chats.add(chat);

        return chat.getId();
    }


//esto se va a usar en una ruta
    public static List<Chat> getListChats(  String adminAdd ){
        List<Chat> temp = new ArrayList<>();
        for(Chat chat: chats){
            if (chat.getAdminAddress() == "-1" || chat.getAdminAddress().equalsIgnoreCase(adminAdd)){
                temp.add(chat);
            }
        }
        System.out.println(temp.size());
        return temp;
    }

    public static String getMessages(int chatID){
        String aux = "";
        for(Mensaje mensaje: mensajes){
            if(mensaje.getChatID() == chatID){
                aux = aux + mensaje.getMensaje() + "<br>";
            }
        }
        return aux;
    }

}
