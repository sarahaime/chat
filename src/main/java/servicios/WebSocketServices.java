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

    private static void deleteChatMessages(int chatID){
        List<Mensaje> aux= new ArrayList<>();
        for (Mensaje msj : mensajes){
            if(msj.getChatID() == chatID) continue;
            aux.add(msj);
        }

        mensajes = aux;
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


    public static void deleteChat(String address){
        for(Chat chat: chats){
            if (chat.getUserAddress().equals(address) || chat.getAdminAddress().equals(address)){
                deleteChatMessages(chat.getId());
                chats.remove(chat);
                break;
            }
        }
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



    public static List<Chat> getListChats(){
        List<Chat> temp = new ArrayList<>();
        for(Chat chat: chats){
            if (chat.getAdminAddress() == null){
                temp.add(chat);
            }
        }
        getLastMessage(temp);
        return temp;
    }

    public static void createChatMessage(Mensaje sms){
        mensajes.add(sms);
    }

    public static List<Mensaje> getMessages(int chatID){
        List<Mensaje> aux = new ArrayList<>();
        for(Mensaje mensaje: mensajes){
            if(mensaje.getChatID() == chatID){
                aux.add(mensaje);
            }
        }
        return aux;
    }

    private static void getLastMessage(List<Chat> chats){
        for(Chat chat: chats){
            List<Mensaje> ownMessages = getMessages(chat.getId());
            chat.setLastMessage(ownMessages.get(0));
        }
    }


}
