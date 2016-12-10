package controls

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import http.HttpBase
import org.apache.log4j.Logger
import utils.LogUtils

/**
 *
 * User: WeiWei
 * Date: 13-12-30
 * Time: 下午4:29
 */
class TranslateControl {


    private final static Logger logger = LogUtils.getLogger(TranslateControl.class)

    final
    static String transUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=sunwenhao&key=1603772286&type=data&doctype=json&version=1.1&q="

    static String translate(String words) {
        HttpBase httpBase = new HttpBase()
        String retString = ""
        JSON json = httpBase.getAndReturnJson(transUrl + words)
        String translation = json.get("translation").get(0)
        if (translation) retString += translation + "\t"
        JSONObject basic = json.get("basic")
        if (basic) {
            String phonetic = basic.getString("phonetic")
            JSONArray explains = basic.getJSONArray("explains")
            retString += "[$phonetic]\r\n"
            explains.each {
                if (it)
                    retString += it.toString() + "\t"
            }
        }
        if (retString)
            return retString
        else
            return '我是翻译不出来了 你自己去翻 http://translate.google.com/?q=' + words
    }
}
