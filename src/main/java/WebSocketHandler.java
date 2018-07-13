import modelos.Chat;
import modelos.Mensaje;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import servicios.WebSocketServices;
import java.io.IOException;
import java.util.Date;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void conectando(Session usuario){
        System.out.println("Conectando Usuario: "+usuario.getRemoteAddress().toString());
        WebSocketServices.usuariosEnLinea.add(usuario);
    }

    //hay que dejar los 2 parametros ultimos, son parte del protocolo, si no se ponen el programa explota
    @OnWebSocketClose
    public void cerrandoConexion(Session usuario, int statusCode, String reason) {
        System.out.println("Desconectando el usuario: " + usuario.getRemoteAddress().toString());
        WebSocketServices.usuariosEnLinea.remove(usuario);
    }

    @OnWebSocketMessage
    public void recibiendoMensaje(Session usuario, String message) {
        System.out.println(message);
        try {
            String comando = message.split(":chat:")[0];
            String val = message.split(":chat:")[1];

            System.out.println(comando);
            System.out.println(val);

            if (comando.equals("tomar")){
                int chatID = Integer.parseInt(val);
                Chat chat = WebSocketServices.getChatByID(chatID);
                chat.setAdminAddress(usuario.getRemoteAddress().toString());
                chat.setOpen(true);
                WebSocketServices.updateChat(chat);
                WebSocketServices.enviarMensaje(WebSocketServices.getMessages(chatID),chat.getAdminAddress());
            }

            if (comando.equals("soltar")){
                Chat chat = WebSocketServices.getChatByID(Integer.parseInt(val));
                chat.setOpen(false);
                WebSocketServices.updateChat(chat);
            }

            //usuario pide iniciar chat
            if (comando.equals("abrir")){
                WebSocketServices.createChat(usuario.getRemoteAddress().toString(), val);
                usuario.getRemote().sendString("<strong>Bot: </strong>  Hola " + val + "<br>");
            }

            //mensaje desde el usuario
            if (comando.equals("user")){
                Chat chat = WebSocketServices.getChatByUserAdd(usuario.getRemoteAddress().toString());
                String msj = "<strong> "+ chat.getUsername() + " dice: </strong>" + val;
                WebSocketServices.mensajes.add(new Mensaje(chat.getId(), msj, new Date(), false) );
                if(chat.isOpen()) WebSocketServices.enviarMensaje(msj,chat.getAdminAddress());
            }

            //un admin manda un mesaje
            if (comando.equals("admin")){
                Chat chat = WebSocketServices.getChatByAdminAdd(usuario.getRemoteAddress().toString());
                String msj =  "<strong>El Bogger dice: <strong>" + val;
                WebSocketServices.mensajes.add(new Mensaje(chat.getId(),msj, new Date(), true));
                WebSocketServices.enviarMensaje(msj,chat.getUserAddress());
            }

            //Enviar un simple mensaje al cliente que mando al servidor..
        //    usuario.getRemote().sendString("Mensaje enviado al Servidor: " + message);
            //mostrando a todos los clientes
          //  WebSocketServices.enviarMensaje(message,"abc123");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
