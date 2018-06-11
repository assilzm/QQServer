package server


import org.apache.log4j.Logger
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import qq.MessageHandle
import utils.JsonUtils
import utils.LogUtils


/**
 *
 * @author: Assilzm
 * create: 2016-11-24 14:05.
 * description:
 */
class WebSocket {

    private final static Logger logger = LogUtils.getLogger(WebSocket.class)


    static WebSocketClient wc


    static void connect(String url) {
        wc = new WebSocketClient(new URI("ws://$url")) {



            @Override
            void onOpen(ServerHandshake handshakedata) {
                System.out.println(handshakedata.getHttpStatusMessage())
            }

            @Override
            void onMessage(String message) {
//                System.out.println(message)
                MessageHandle.handleAll(JsonUtils.stringToJson(message.trim()))
            }


            @Override
            void onClose(int code, String reason, boolean remote) {
                System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) )
                //断线重连
                connect(url)
            }

            @Override
            void onError(Exception ex) {
                ex.printStackTrace()
            }

        }
        wc.connectBlocking()
    }

    static void send(String message) {
        try {
            wc.send(message)
        } catch (Exception e) {
            logger.error("发送消息出错,错误信息:", e)
        }
    }

    static void close() {
        wc.close()
    }
}
