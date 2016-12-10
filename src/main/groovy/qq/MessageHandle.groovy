package qq

import com.alibaba.fastjson.JSON
import controls.BaiduControl
import controls.BaikeControl
import controls.TranslateControl
import controls.TulingControl
import controls.WeatherControl
import controls.WhatIsControl
import controls.WuBiControl
import exceptions.ArgumentsNotCorrectException
import org.apache.log4j.Logger
import server.QQServer
import utils.LogUtils

import java.util.regex.Matcher

/**
 *
 * @author: weiwei
 * create: 2016-11-23 19:39.
 * description:
 */
class MessageHandle {

    final static Logger logger = LogUtils.getLogger(MessageHandle)

    final static String START_CHAR = ""

    final static Long adminQQ = 4699495

    static String handleAll(JSON msgObject) {
        FromMessageType returnType = null
        int messageType = msgObject.get("Type")
        int messageSubType = msgObject.get("SubType")
        Long fromQQNum = msgObject.get("QQ")
        Long fromGroupNum = msgObject.get("Group")
        Long fromDiscussNum = msgObject.get("Discuss")
        String qqMsg=msgObject.get("Msg")
        if (qqMsg) {
            String message = URLDecoder.decode(qqMsg, "UTF-8")
            switch (messageType) {
            //私聊信息 11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
                case 1:
                    switch (messageSubType) {
                        case 11:
                            returnType = FromMessageType.FRIEND
                            break
                        case 1:
                            break
                        case 2:
                            returnType = FromMessageType.GROUP_FRIEND
                            break
                        case 3:
                            returnType = FromMessageType.DISCUSS_FRIEND
                            break
                        default:
                            throw new ArgumentsNotCorrectException("未知的子消息类型[$messageSubType]")

                    }
                    logger.debug("收到消息,来自[${fromQQNum}],内容:${message}")


                    return dealMessageQQ(fromQQNum, message)
                    break
            //群消息 1/普通消息 2/匿名消息 3/系统消息
                case 2:
                    switch (messageSubType) {
                        case 1:
                            returnType = FromMessageType.GROUP
                            break
                        case 2:
                            returnType = FromMessageType.ANONYMOUS
                            break
                        case 3:
                            break
                        default:
                            throw new ArgumentsNotCorrectException("未知的子消息类型[$messageSubType]")
                    }
                    return dealMessageGroup(fromGroupNum, fromQQNum, message)
                    break
            //讨论组信息 目前固定为1
                case 4:

                    return dealMessageDiscuss(fromDiscussNum, fromQQNum, message)

                    break
            //上传群文件 目前固定为1
                case 11:
                    break
            //群管理员变动 1/被取消管理员 2/被设置管理员
                case 101:
                    break
            //群成员减少 1/群员离开 2/群员被踢 3/自己(即登录号)被踢
                case 102:
                    break
            //群成员增加 1/管理员已同意 2/管理员邀请
                case 103:
                    break
            //好友已添加 目前固定为1
                case 201:
                    break
            //请求添加好友 目前固定为1
                case 301:
                    break
            //请求添加群 1/他人申请入群 2/自己(即登录号)受邀入群
                case 302:
                    break
                default:
                    throw new ArgumentsNotCorrectException("未知的主消息类型[$messageType]")
            }
        }
        return null
    }

    static String dealMessageQQByHttp(Long fromQQNum, String message) {
        String dealMessage = checkActions(message)
        return dealMessage ?
                "{" +
                        "data:[" +
                        "{" +
                        "Type:1," +
                        "QQ:$fromQQNum," +
                        "Msg:\"$dealMessage\"," +
                        "Send:1" +
                        "}" +
                        "]" +
                        "}" : null
    }

    static String dealMessageQQByWebSocket(Long fromQQNum, String message) {
        return "{" +
                "Fun:'sendPrivateMsg'," +
                "QQ:$fromQQNum," +
                "Msg:\"$message\"," +
                "Send:1" +
                "}"
    }


    static String dealMessageQQ(Long fromQQNum, String message, boolean isWebSocket = false) {
        return isWebSocket ? dealMessageQQByWebSocket(fromQQNum, message) : dealMessageQQByHttp(fromQQNum, message)
    }


    static String dealMessageGroupByHttp(Long fromGroupNum, Long fromQQNum, String message) {
        String dealMessage = checkActions(message, fromGroupNum, fromQQNum)
        return dealMessage ?
                "{data:[" +
                        "{" +
                        "Type:-7," +
                        "Skn1:$fromQQNum" +
                        "}," +
                        "{" +
                        "Type:2," +
                        "Group:$fromGroupNum," +
                        "Msg:\"\\n$dealMessage\"," +
                        "Send:1" +
                        "}" +
                        "]}" : null
    }

    static String dealMessageGroupByWebSocket(Long fromGroupNum, Long fromQQNum, String message) {
        return "{" +
                "Fun:'sendGroupMsg'," +
                "Group:$fromGroupNum," +
                "Msg:\"[CQ:at,qq=$fromQQNum]\\n$message\"," +
                "Send:1" +
                "}"
    }


    static String dealMessageGroup(Long fromGroupNum, Long fromQQNum, String message, boolean isWebSocket = false) {
        return isWebSocket ? dealMessageGroupByWebSocket(fromGroupNum, fromQQNum, message) : dealMessageGroupByHttp(fromGroupNum, fromQQNum, message)
    }

    static String dealMessageDiscussByHttp(Long fromDiscussNum, Long fromQQNum, String message) {
        String dealMessage = checkActions(message, fromDiscussNum, fromQQNum)
        return dealMessage ?
                "{\"data\":[" +
                        "{" +
                        "Type:-7," +
                        "Skn1:$fromQQNum" +
                        "}," +
                        "{" +
                        "Type:3," +
                        "Group:$fromDiscussNum," +
                        "Msg:\"\\n$dealMessage\"," +
                        "Send:1" +
                        "}" +
                        "]}" : null
    }

    static String dealMessageDiscussByWebSocket(Long fromDiscussNum, Long fromQQNum, String message) {
        return "{" +
                "Fun:'sendDiscussMsg'," +
                "Discuss:$fromDiscussNum," +
                "Msg:\"[CQ:at,qq=$fromQQNum]\\n$message\"," +
                "Send:1" +
                "}"
    }


    static String dealMessageDiscuss(Long fromDiscussNum, Long fromQQNum, String message, boolean isWebSocket = false) {
        return isWebSocket ? dealMessageDiscussByWebSocket(fromDiscussNum, fromQQNum, message) : dealMessageDiscussByHttp(fromDiscussNum, fromQQNum, message)

    }

    static String checkActions(String messages, Long groupOrDiscussNum = null, Long fromQQNum = null) {
        def m = messages =~ /^${START_CHAR}?\s*(\S+)(?:\s+([\s\S]*))?/
        def returnMessage = null
        try {
            if (m.find()) {
                String cmd = m.group(1).trim()
                String value = ""
                if (m.group(2))
                    value = m.group(2).trim()
                logger.debug("收到请求功能[$cmd],内容[$value]")
                if (cmd == "允许该群" && fromQQNum == adminQQ) {
                    if (groupOrDiscussNum != null) {
                        if (groupOrDiscussNum in QQServer.disableGroupNum)
                            QQServer.disableGroupNum -= groupOrDiscussNum
                        return "该群已允许消息发送，继续接受该群请求"

                    }
                }
                if (groupOrDiscussNum in QQServer.disableGroupNum) {
                    return null
                }
                switch (cmd) {
                    case ["运势", "笑话"]:
                        returnMessage = TulingControl.send(messages).replaceAll(/[:;]/, "\\\\n")
                        break
                    case "天气":
                        returnMessage = WeatherControl.getWeather(value)
                        break
                    case "wiki地址":
                        returnMessage = "http://open.seeyon.com:4567/Home"
                        break
                    case "翻译":
                        returnMessage = TranslateControl.translate(value)
                        break
                    case "百科":
                        returnMessage = BaikeControl.getBaike(value)
                        break
                    case "百度":
                        returnMessage = BaiduControl.getBaiduResult(value)
                        break
                    case "五笔":
                        returnMessage = WuBiControl.getWuBi(value)
                        break
                    case ["屏蔽该群", "闭嘴"]:
                        if (groupOrDiscussNum != null && fromQQNum == adminQQ) {
                            returnMessage = "该群已屏蔽，不再往该群发送信息"
                            if (!(groupOrDiscussNum in QQServer.disableGroupNum))
                                QQServer.disableGroupNum.add(groupOrDiscussNum)
                        }
                        break
                    case ~/什么是(.+)/:
                        returnMessage = WhatIsControl.getDescription(Matcher.lastMatcher[0][1])
                        break
                    default:
                        break
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (returnMessage)
            returnMessage = returnMessage.replaceAll(/\r\n|\n\r|[\n\r]/, "\\\\n").replaceAll(/(["'])/, "\\\$1")
        return returnMessage
    }
}