package controls

import exceptions.ArgumentsNotCorrectException
import http.HttpBase
import server.QQServer
import utils.FileUtils
import utils.JsonUtils

/**
 *
 * User: WeiWei
 * Date: 13-12-6
 * Time: 上午9:43
 */
class WeatherControl {

    final static String KEY_STRING = "weatherkey"


    static String getWeather(String cityName) {
        Properties properties = FileUtils.getProperties(QQServer.CONFIG_FILE)
        String key = properties.get(KEY_STRING)
        if (key) {
            String URL = "https://free-api.heweather.com/v5/weather?key=${key}&city="
            HttpBase httpBase = new HttpBase()
            def res = httpBase.getAndReturnBody(URL + URLEncoder.encode(cityName, "utf-8"))
            def json = JsonUtils.stringToJson(res)
            def data = json['HeWeather5'][0]
            if (data.status != 'ok')
                return "找不到该城市的天气：" + cityName
            def ret = "$cityName\n"
            def tomorrow = data.daily_forecast[1];
            def theDayAfterTomorrow = data.daily_forecast[2];
            if (data.aqi != null)
                ret += 'AQI: ' + data.aqi.city.aqi + ' PM2.5: ' + data.aqi.city.pm25 + ' 空气质量: ' + data.aqi.city.qlty + '\n';
            ret += '当前: ' + data.now.tmp + '℃ ' + data.now.cond.txt + '\n';
            if (data.suggestion != null)
                ret += data.suggestion.drsg.txt + '\n';
            ret += '明天: ' + tomorrow.tmp.min + '／' + tomorrow.tmp.max + '℃ ' + tomorrow.cond.txt_d + '\n';
            ret += '后天: ' + theDayAfterTomorrow.tmp.min + '／' + theDayAfterTomorrow.tmp.max + '℃ ' + theDayAfterTomorrow.cond.txt_d + '\n';
            ret += '发布时间: ' + data.basic.update.loc;
            return ret
        }else {
            throw new ArgumentsNotCorrectException("未设置天气API的密钥，请检查${QQServer.CONFIG_FILE.getName()}中的${KEY_STRING}属性。")
        }
    }

}
