import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import servicios.WebSocketServices;
import java.io.IOException;

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
        System.out.println("Recibiendo del cliente: "+usuario.getLocalAddress().getAddress().toString()+" - Mensaje"+message);
        try {
            //Enviar un simple mensaje al cliente que mando al servidor..
            usuario.getRemote().sendString("Mensaje enviado al Servidor: "+message);
            //mostrando a todos los clientes
            WebSocketServices.enviarMensaje(message,"abc123");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
