package controls

import org.apache.log4j.Logger
import qq.MessageHandle
import server.QQServer
import server.WebSocket
import utils.FileUtils
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


    static String jenkinsActions(Map<String, String> parms){

        Properties properties = FileUtils.getProperties(QQServer.CONFIG_FILE)
        Long groupNum = Long.parseLong(properties.get(GROUP_STRING).toString())
        List<String> to = parms.get("to")?.split(",")
        String message = parms.get("message")
        List<String> sendUsers = new ArrayList<>()
        to.each {
            if (it) {
                logger.debug("通知[$it]")
                WebSocket.send(MessageHandle.dealMessageGroup(groupNum,it.toLong(),message,true))
                sendUsers.add("$it")
            }else {
                logger.debug("好友[$it]不存在，跳过。")
            }
        }
        if (sendUsers.size() > 0)
            return "已通知$sendUsers:$message"
        else
            return null
    }


}
