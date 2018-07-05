package servicios;

import java.sql.SQLException;

import org.h2.tools.Server;

public class BootStrapServices {

    public static void startDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

}
