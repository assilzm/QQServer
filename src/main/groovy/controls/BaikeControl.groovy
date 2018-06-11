package controls

import http.HttpBase
import okhttp3.Response
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import utils.LogUtils

/**
 *
 * User: Assilzm
 * Date: 13-12-12
 * Time: 下午7:16
 */
class BaikeControl {

    private final static Logger logger = LogUtils.getLogger(BaikeControl.class)

    final static String baikeUrl = "http://baike.baidu.com"
    static HttpBase httpBase = new HttpBase(false)

    static String getBaike(String baikeName) {
        def sortContent = ""
        String baikeNameUTF = URLEncoder.encode(baikeName, "UTF8")
        String baikeArg = "/search/word?word=${baikeNameUTF}&enc=utf8"
//        Map<String, List<String>> res = httpBase.get(baikeUrl + baikeArg)
        Response response = httpBase.get(baikeUrl + baikeArg)
        if (response.code() in [302, 301]) {
            String pageLink = response.header("Location")
            pageLink = pageLink =~ /^https?:\/\/baike\.baidu\.com/ ? pageLink : baikeUrl + pageLink
//            println pageLink
            if (!pageLink.contains("none?")) {
                Document doc = Jsoup.connect(pageLink).get()
                def baikeLinks = getBaikeList(doc)
                if (baikeLinks.size()) {
                    baikeLinks.keySet().each {
                        sortContent += it + "\n"
                        String sortContentTemp = getBaikeSortContent(Jsoup.connect(baikeUrl + baikeLinks.get(it)).get())
                        if (sortContentTemp)
                            sortContent += sortContentTemp + "\n"
                        sortContent += baikeUrl + baikeLinks.get(it) + "\n\n"
                    }
                } else {
                    sortContent = getBaikeSortContent(doc)
                    if (sortContent == null)
                        sortContent = "未找到百科词条[$baikeName]，您可以参考搜索结果页：" + "\n" + doc.baseUri()
                    else
                        sortContent = baikeName + "\n" + sortContent + "\n" + doc.baseUri()
                }
                response.close()
                return sortContent
            }
        }
        response.close()
        return "未找到百科词条[$baikeName]"
    }

    private static String getBaikeSortContent(Document doc) {
        Element sortContentDiv = doc.select("div.lemma-summary").first()
        if (sortContentDiv)
            return sortContentDiv.text()
        else {
            sortContentDiv = doc.select("div.lemma-main-content").first()
            if (sortContentDiv)
                return sortContentDiv.text()
            else
                return null
        }

    }


    private static Map<String, String> getBaikeList(Document doc) {
        Map<String, String> urlList = new HashMap<>()
        Elements baikeList = doc.select("div#lemma-list>ul.custom_dot>li>p>a")
        if (baikeList) {
            baikeList.each {
                urlList.put(it.text(), it.attr("href"))
            }
            logger.debug("百科列表" + urlList)
        }
        urlList
    }

}
