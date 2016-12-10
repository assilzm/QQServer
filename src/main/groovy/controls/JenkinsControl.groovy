package controls

import org.apache.log4j.Logger
import qq.MessageHandle
import server.QQServer
import server.WebSocket
import utils.LogUtils

/**
 *
 * @author: weiwei
 * create: 2016-11-24 18:21.
 * description:
 */
class JenkinsControl {
    private final static Logger logger = LogUtils.getLogger(JenkinsControl.class)

    final static Long GROUP_NUM=294384179


    static String jenkinsActions(Map<String, String> parms){
        List<String> to = parms.get("to")?.split(",")
        String message = parms.get("message")
        List<String> sendUsers = new ArrayList<>()
        to.each {
            if (it) {
                logger.debug("通知[$it]")
                WebSocket.send(MessageHandle.dealMessageGroup(GROUP_NUM,it.toLong(),message,true))
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
