package server

import controls.JenkinsControl
import exceptions.ArgumentsNotCorrectException
import fi.iki.elonen.NanoHTTPD
import org.apache.log4j.Logger
import qq.MessageHandle
import utils.FileUtils
import utils.JsonUtils
import utils.LogUtils

/**
 *
 * @author: Assilzm
 * create: 18:00.
 * description:
 */

public class QQServer extends NanoHTTPD {
    final static Logger logger = LogUtils.getLogger(QQServer)

    final static String WEBSOCKET_SERVER_STRING = "websocketserver"

    final static File CONFIG_FILE = new File("config.properties")

    static List<Long> allowGroupNum = []

    static List<Long> disableGroupNum = []

    static Properties properties = new Properties()


    QQServer(int port) {
        super(port)
        logger.debug("服务器已启动，端口[$port]")
        properties=FileUtils.getProperties(CONFIG_FILE)
        String websocketserver = properties.get(WEBSOCKET_SERVER_STRING)
        if (websocketserver) {
            WebSocket.connect(websocketserver)
            logger.debug("WebSocketServer[$websocketserver]已连接")
        }else{
            stop()
            throw new  ArgumentsNotCorrectException("未配置websocket服务器，请检查配置文件${CONFIG_FILE.getName()}")
        }
    }



    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        try {
            NanoHTTPD.Method method = session.getMethod()

            String uri = session.getUri()
            if (NanoHTTPD.Method.POST.equals(session.getMethod())) {
                Map<String, String> files = new HashMap<String, String>()
                session.parseBody(files)
                logger.debug(method.toString() + " '" + uri.toString() + "' ")
                String responseText = URLDecoder.decode(session.getQueryParameterString(), "UTF-8")
                logger.debug(responseText)
                String returnMessage = MessageHandle.handleAll(JsonUtils.stringToJson(responseText))
                if (returnMessage) {
                    logger.debug("返回数据[$returnMessage]")
                    return new NanoHTTPD.Response(returnMessage)

                }
            }
            if (NanoHTTPD.Method.GET.equals(session.getMethod())) {
                Map<String, String> params = session.getParms()
                logger.debug("parms:" + params)
                String returnMessage = null
                if (params.get("from") == "jenkins") {
                    returnMessage = JenkinsControl.jenkinsActions(params)
                }
                logger.debug("发送数据[$returnMessage]")

                return new NanoHTTPD.Response(returnMessage)
            }
        } catch (Exception e) {
            logger.error("发送消息出错,错误信息:", e)

        }
        return new NanoHTTPD.Response("{message:\"error\"}")
    }


}