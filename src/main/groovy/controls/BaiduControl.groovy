package controls

import org.jsoup.Jsoup

/**
 *
 * @author: weiwei
 * create: 2016-12-3 15:51.
 * description:
 */
class BaiduControl {

    final static String baiduUrl = "http://www.baidu.com/s?tn=baidulocal&wd="


    static String getBaiduResult(String keyword) {
        String result =null
        try {
            def response = Jsoup.connect(baiduUrl + URLEncoder.encode(keyword, "UTF-8")).get()
            result=""
            response.select("td.f").each {
                def content = it.select(">font").get(0)
                def a = it.select(">a").get(0)
                content.select("a").remove()
                content.select("font").last().remove()
                result += a.text() + "\n"
                result += content.text().replaceAll(/- *$/, "") + "\n"
                result += a.attr("href") + "\n"
            }
        }catch (Exception e){
            e.printStackTrace()
        }
        return result
    }
}
