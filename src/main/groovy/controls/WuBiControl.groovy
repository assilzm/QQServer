package controls

import http.HttpBase
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import utils.LogUtils

/**
 *
 * @author Assilzm
 * createTime 2014-8-22 17:13.
 */
class WuBiControl {
    private final static Logger logger = LogUtils.getLogger(WuBiControl)

    final static String searchUrl = "http://www.52wubi.com/wbbmcx/search.php"

    static String getWuBi(String hanzi) {
        HttpBase httpBase=new HttpBase()
        httpBase.headerToAdd.put("Referer","http://www.52wubi.com/wbbmcx/search.php")
        def res =  httpBase.post(searchUrl, "hzname=${URLEncoder.encode(hanzi, "GBK")}",HttpBase.TEXT)
        Elements codes=[]
        byte[] b = res.body().bytes()
        String bodyString= new String(b, "GBK")
        if (res.isSuccessful()) {
            def body = Jsoup.parse(bodyString)
            codes = body.select("#zgbg .tbhover")
        }
        if (codes.size()>0){
            String returnMessage=""
            codes.each {
                def tds=it.select("td")
                returnMessage+=tds.get(0).text()+" "+tds.get(2).text()+"\n"
            }
            return returnMessage
        }
        else
            return "找不到[$hanzi]的五笔编码。"
    }

}
