package controls

import org.jsoup.Jsoup

/**
 *
 * @author: Assilzm
 * create: 2016-12-3 15:51.
 * description:
 */
class BaiduControl {

    final static String baiduUrl = "http://www.baidu.com/s?wd="


    static String getBaiduResult(String keyword) {
        String result = null
        try {
            def response = Jsoup.connect(baiduUrl + URLEncoder.encode(keyword, "UTF-8")).get()
//            println response.html()
            result = ""
            response.select("div.result.c-container").each {
//                println it.html()
                def content = it.select(">div.c-abstract")
                def a = it.select(">h3.t>a").get(0)
//                content.select("a").remove()
//                content.select("font").last().remove()
                result += a.text() + "\n"
                if (content.size() > 0) {
                    result += content.get(0).text().replaceAll(/- *$/, "") + "\n"
                }
                result += a.attr("href") + "\n"

            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return result
    }
}
