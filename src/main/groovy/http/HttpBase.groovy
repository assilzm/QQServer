package http

import com.alibaba.fastjson.JSON
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT
import okhttp3.*
import exceptions.NoSuchRequestTypeException
import utils.JsonUtils

import static http.RequestMethod.DELETE
import static http.RequestMethod.GET
import static http.RequestMethod.POST
import static http.RequestMethod.PUT


/**
 *
 * @author: Assilzm
 * create: 14:02.
 * description:
 */
class HttpBase {

    public static final MediaType JSON = MediaType.parse("application/json");
    public static final MediaType TEXT = MediaType.parse("application/x-www-form-urlencoded")

    Map<String, String> headerToAdd = new HashMap<>()

    OkHttpClient client

    OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
            .cookieJar(
            new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                    cookieStore.put(url.host(), cookies)
                }

                @Override
                List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })

    Headers defaultHeaders = new Headers.Builder()
            .add("Accept", "*/*")
            .add("Accept-Charset", "utf-8")
            .add("Accept-Language", "zh-CN,zh")
            .build()


    HttpBase(boolean followRedirects = true) {
        client = httpBuilder.followRedirects(followRedirects).build()
    }

    RequestBody buildBody(String body, MediaType mediaType = TEXT) {
        RequestBody requestBody=FormBody.create(TEXT, "")
        switch (mediaType) {
            case TEXT:
                if (body!=null)
                    requestBody= FormBody.create(TEXT, body)
                break
            case JSON:
                if (body==null)
                    requestBody= FormBody.create(JSON, "{}")
                else
                    requestBody= FormBody.create(JSON, JsonUtils.formatJsonString(body))
                break
            default:
                break
        }
        return requestBody
    }


    Request request(String url, RequestMethod type, String body = null, MediaType mediaType = TEXT) {
        HttpUrl httpUrl = HttpUrl.parse(createUrl(url))
        request(httpUrl, type, buildBody(body,mediaType))
    }


    Request request(String url, RequestMethod type, RequestBody body) {
        HttpUrl httpUrl = HttpUrl.parse(createUrl(url))
        request(httpUrl, type, body)
    }


    Request request(HttpUrl httpUrl, RequestMethod type, RequestBody body) {
        Request.Builder builder = new Request.Builder().url(httpUrl).headers(defaultHeaders)
        if (headerToAdd.keySet().size() > 0) headerToAdd.each { key, value -> builder.header(key, value) }
        switch (type) {
            case GET:
                break
            case POST:
                builder.post(body)
                break
            case PUT:
                builder.put(body)
                break
            case DELETE:
                builder.delete(body)
                break
            default:
                throw new NoSuchRequestTypeException("错误的请求方式：" + type)

        }
        builder.build()
    }

    Response doRequest(Request request) {
        client.newCall(request).execute()
    }

    Response get(String url) {
        doRequest(request(url, GET))
    }


    Response post(String url, String data, MediaType mediaType = TEXT) {
        doRequest(request(url, POST, data, mediaType))
    }

    Response delete(String url, String data, MediaType mediaType = TEXT) {
        doRequest(request(url, DELETE, data, mediaType))
    }

    Response put(String url, String data, MediaType mediaType = TEXT) {
        doRequest(request(url, PUT, data, mediaType))
    }

    String getAndReturnBody(String url) {
        get(url).body().string()
    }

    JSON getAndReturnJson(String url) {
        responseFormat(getAndReturnBody(url))
    }

    String postAndReturnBody(String url, String data, MediaType mediaType = TEXT) {
        post(url, data,mediaType).body().string()
    }

    JSON postAndReturnJson(String url, String arguments, MediaType mediaType = TEXT) {
        responseFormat(postAndReturnBody(url, arguments,mediaType))
    }

    String postAndReturnBody(String url, Map<String, String> data, MediaType mediaType = TEXT) {
        post(url, JsonUtils.objectToJsonString(data),mediaType).body().string()
    }

    String putAndReturnBody(String url, String data, MediaType mediaType = TEXT) {
        put(url, data,mediaType).body().string()
    }

    String putAndReturnBody(String url, Map<String, String> data, MediaType mediaType = TEXT) {
        put(url, JsonUtils.objectToJsonString(data),mediaType).body().string()
    }

    JSON putAndReturnJson(String url, String arguments, MediaType mediaType = TEXT) {
        responseFormat(putAndReturnBody(url, arguments,mediaType))
    }

    String deleteAndReturnBody(String url, String data, MediaType mediaType = TEXT) {
        delete(url, data,mediaType).body().string()
    }

    String deleteAndReturnBody(String url, Map<String, String> data, MediaType mediaType = TEXT) {
        delete(url, JsonUtils.objectToJsonString(data),mediaType).body().string()
    }

    JSON deleteAndReturnJson(String url, String arguments, MediaType mediaType = TEXT) {
        responseFormat(deleteAndReturnBody(url, arguments,mediaType))
    }


    String createUrl(String urlString) {
        urlString
    }

    private static JSON responseFormat(String responseString) {
        return JsonUtils.stringToJson(responseString)
    }


}
