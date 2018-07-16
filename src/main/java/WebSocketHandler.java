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
       // System.out.println("Conectando Usuario: "+usuario.getRemoteAddress().toString());
        WebSocketServices.usuariosEnLinea.add(usuario);
    }

    //hay que dejar los 2 parametros ultimos, son parte del protocolo, si no se ponen el programa explota
    @OnWebSocketClose
    public void cerrandoConexion(Session usuario, int statusCode, String reason) {
       // System.out.println("Desconectando el usuario: " + usuario.getRemoteAddress().toString());
        WebSocketServices.usuariosEnLinea.remove(usuario);
    }

    @OnWebSocketMessage
    public void recibiendoMensaje(Session usuario, String message) {
        try {
            String comando = message.split(":chat:")[0];
            String val = message.split(":chat:")[1];

            if (comando.equals("tomar")){
                int chatID = Integer.parseInt(val);
                Chat chat = WebSocketServices.getChatByID(chatID);
                chat.setAdminAddress(usuario.getRemoteAddress().toString());
                WebSocketServices.cerrarChats( usuario.getRemoteAddress().toString() );
                chat.setOpen(true);
                WebSocketServices.updateChat(chat);
                WebSocketServices.enviarMensaje(WebSocketServices.getMessages(chatID),chat.getAdminAddress());
                WebSocketServices.enviarBroadcast(":-:actualizar:-:");
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
                WebSocketServices.enviarBroadcast(":-:actualizar:-:");
            }

            //mensaje desde el usuario
            if (comando.equals("user")){
                Chat chat = WebSocketServices.getChatByUserAdd(usuario.getRemoteAddress().toString());
                String msj =  "           <div class=\"row message-body\">" +
                        "                    <div class=\"col-sm-12 message-main-receiver\">" +
                        "                        <div class=\"receiver\">" +
                        "                            <div class=\"message-text\">" +
                                                            val +
                        "                            </div>" +
                        "                        </div>" +
                        "                    </div>" +
                        "                </div>";

                Mensaje mensaje = new Mensaje();

                mensaje.setChatID(chat.getId());
                mensaje.setMensaje(msj);
                mensaje.setFechaEnvio(new Date());
                System.out.println("Enviando mensaje al chat # " + mensaje.getChatID());

                WebSocketServices.mensajes.add(mensaje);

                if(chat.isOpen()) WebSocketServices.enviarMensaje(msj,chat.getAdminAddress());

            }

            //un admin manda un mesaje
            if (comando.equals("admin")){
                Chat chat = WebSocketServices.getChatByAdminAdd(usuario.getRemoteAddress().toString());
                String msj =  "           <div class=\"row message-body\">" +
                        "                    <div class=\"col-sm-12 message-main-sender\">" +
                        "                        <div class=\"sender\">" +
                        "                            <div class=\"message-text\">" +
                                                            val +
                        "                            </div>" +
                        "                        </div>" +
                        "                    </div>" +
                        "                </div>";

                System.out.println("El admin mensajea a chat # " + chat.getId() + ' ' + chat.getUsername());
                WebSocketServices.mensajes.add(new Mensaje(chat.getId(),msj));
                WebSocketServices.enviarMensaje(msj,chat.getUserAddress());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
