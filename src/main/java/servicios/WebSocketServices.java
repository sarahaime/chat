package servicios;

import modelos.Chat;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketServices {

    public static List<Session> usuariosEnLinea = new ArrayList<>();
    public static List<Chat> chats = new ArrayList<>();

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


}
