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
 * User: WeiWei
 * Date: 13-12-12
 * Time: 下午7:16
 */
class BaikeControl {

    private final static Logger logger = LogUtils.getLogger(BaikeControl.class)

    final static String baikeUrl = "http://baike.baidu.com"
    static HttpBase httpBase=new HttpBase(false)

    static String getBaike(String baikeName) {
        def sortContent = ""
        String baikeNameUTF = URLEncoder.encode(baikeName, "UTF8")
        String baikeArg = "/search/word?word=${baikeNameUTF}"
//        Map<String, List<String>> res = httpBase.get(baikeUrl + baikeArg)
        Response response= httpBase.get(baikeUrl + baikeArg)
        println response.headers()
        println response.code()
        if (response.code()==302) {
            String pageLink = response.header("Location")
            println  pageLink
            pageLink = pageLink.startsWith(baikeUrl) ? pageLink : baikeUrl + pageLink

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
                } else
                    sortContent = baikeName + "\n" + getBaikeSortContent(doc) + "\n" + pageLink.replaceAll(/\?[^\?]*$/, "")

                return sortContent
            }
        }
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
