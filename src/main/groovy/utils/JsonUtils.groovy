package utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import exceptions.JsonParseException
import org.apache.log4j.Logger
/**
 *
 * @author: weiwei
 * create: 16:00.
 * description:
 */
class JsonUtils {

    private final static Logger logger = LogUtils.getLogger(JsonUtils)

    static JSON stringToJson(String str) {
        try {
            return JSON.parse(str)
        } catch (Exception e) {
            throw new JsonParseException("转换Json出错，错误信息：",e)
        }
    }

    static String objectToJsonString(Object obj){
        JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue)
    }

    static String formatJsonString(String str){
        if (str) {
            str = str.replaceAll(/(?<=["'])(\d{4}-\d{2}-\d{2})\s+(\d{2}:\d{2}:\d{2})(?=["'])/, "\$1T\$2.000Z")
            str= stringToJson(str).toJSONString()
        }
        return str
    }
}
