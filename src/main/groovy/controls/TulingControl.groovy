package controls

import com.alibaba.fastjson.JSON
import exceptions.ArgumentsNotCorrectException
import http.HttpBase
import org.apache.log4j.Logger
import server.QQServer
import utils.JsonUtils
import utils.LogUtils

/**
 *
 * @author: weiwei
 * create: 2016-11-24 15:57.
 * description:
 */
class TulingControl {

    final static Logger logger = LogUtils.getLogger(TulingControl)

    static HttpBase httpBase = new HttpBase()

    final static String KEY_STRING = "tulingkey"


    static String send(String message) {
        String key = QQServer.properties.get(KEY_STRING)
        if (key) {
            String responseText = httpBase.postAndReturnBody("http://www.tuling123.com/openapi/api", '{' +
                    'key:"' + key + '",' +
                    'info:"' + message + '",' +
                    'userid:"123456"' +
                    '}', HttpBase.JSON)
            JSON json = JsonUtils.stringToJson(responseText)
            int code = json.get("code")
            if (code != 100000) {
                logger.error("图灵返回数据错误，返回信息:${responseText}")
                return null
            } else {
                String returnMessage = (String) json.get("text")
                logger.debug("图灵求情数据[$message]，返回信息:${returnMessage}")
                return returnMessage
            }
        } else {
            throw new ArgumentsNotCorrectException("未配置tuling服务器的密钥，请检查配置文件${QQServer.CONFIG_FILE.getName()}")
        }
    }

}
