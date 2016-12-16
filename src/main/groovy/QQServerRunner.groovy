import server.QQServer
import server.ServerRunner
import server.WebSocket

/**
 *
 * @author: Assilzm
 * create: 2016-11-23 18:10.
 * description:
 */
class QQServerRunner {
    static int port = 9093


    public static void main(String[] arg) {
        QQServer qqServer = new QQServer(port)
        ServerRunner.executeInstance(qqServer)
    }
}
