package controls

import org.apache.log4j.Logger
import qq.FromMessageType
import qq.MessageHandle
import server.QQServer
import server.WebSocket
import utils.FileUtils
import utils.JsonUtils
import utils.LogUtils

/**
 *
 * @author: Assilzm
 * create: 2016-11-24 18:21.
 * description:
 */
class JenkinsControl {
    private final static Logger logger = LogUtils.getLogger(JenkinsControl.class)

    final static String GROUP_STRING = "v5group"


    static String jenkinsActions(Map<String, String> parms) {

        Properties properties = FileUtils.getProperties(QQServer.CONFIG_FILE)
//        Long groupNum = Long.parseLong(properties.get(GROUP_STRING).toString())
        String to = parms.get("to")
        String groups = parms.get("group")
        if (groups.trim().empty) {
            logger.error("必须设置要发送的群群号!")
            return null
        }
        else {
            String message = parms.get("message")
//        List<String> sendUsers = new ArrayList<>()
            if (!(to.trim() in ["", ","])) {
                logger.debug("通知[$to]")
                MessageHandle.sendMessageByWebSocket(message, groups, to, FromMessageType.GROUP)
//            sendUsers.add("$to")
                return "已通知$to:$message"

            } else {
                logger.debug("好友[$to]不存在，跳过。")
                return null
            }
        }


    }
}
