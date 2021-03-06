package com.sigeyi.http;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 *
 * 解析框架中的网络请求和响应结果,并以日志形式输出
 * <p>
 * Created by huangchangguo on 2018.2.20
 *
 */
public class RequestInterceptor implements Interceptor {
    private GlobalHttpHandler mHandler;
    private final Level printLevel;

    public enum Level {
        NONE,       //不打印log
        REQUEST,    //只打印请求信息
        RESPONSE,   //只打印响应信息
        ALL         //所有数据全部打印
    }

    public RequestInterceptor(@Nullable GlobalHttpHandler handler, @Nullable Level level) {
        this.mHandler = handler;
        if (level == null)
            printLevel = Level.ALL;
        else
            printLevel = level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //添加统一的消息头
        //request = setGlobalHeader(request);
        if (mHandler != null)
            request= mHandler.onHttpRequestBefore(request);
        boolean logRequest = printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.REQUEST);
        if (logRequest) {
            //打印请求信息
            if (request.body() != null && isParseable(request.body().contentType())) {
                FormatPrinter.printJsonRequest(request, parseParams(request));
            } else {
                FormatPrinter.printFileRequest(request);
            }
        }
        boolean logResponse = printLevel == Level.ALL || (printLevel != Level.NONE && printLevel == Level.RESPONSE);

        long t1 = logResponse ? System.nanoTime() : 0;
        Response originalResponse = null;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Log.d("hcg-intercept","Http Error: " + e);
            throw e;
        }
        long t2 = logResponse ? System.nanoTime() : 0;

        //打印响应结果
        String bodyString = null;
        ResponseBody responseBody = null;
        if (originalResponse != null)
            responseBody = originalResponse.body();

        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = printResult(request, originalResponse, logResponse);
        }

        if (logResponse && responseBody != null) {
            final List<String> segmentList = request.url().encodedPathSegments();
            final String header = originalResponse.headers().toString();
            final int code = originalResponse.code();
            final boolean isSuccessful = originalResponse.isSuccessful();
            final String message = originalResponse.message();
            final String url = originalResponse.request().url().toString();

            if (responseBody != null && isParseable(responseBody.contentType())) {
                FormatPrinter.printJsonResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        isSuccessful, code, header,
                        isJson(responseBody.contentType()) ?
                                CharacterHandler.jsonFormat(bodyString) : isXml(responseBody.contentType()) ?
                                CharacterHandler.xmlFormat(bodyString) : bodyString, segmentList, message, url);
            } else {
                FormatPrinter.printFileResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        isSuccessful, code, header, segmentList, message, url);
            }

        }
        //服务器返回的结果,可以做token超时重新获取
        if (mHandler != null)
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        return originalResponse;
    }

    private Request setGlobalHeader(Request request) {
        Request.Builder builder = request.newBuilder();

        List<String> headerValues = request.headers(Api.HEADER_KEY);

        if (headerValues != null && headerValues.size() > 0) {
            HttpUrl baseUrl = request.url();
            HttpUrl newBaseUrl = baseUrl;
            HttpUrl newFullUrl = baseUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();
            return builder.url(newFullUrl).build();
        } else {
            return request;
        }
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param response
     * @param logResponse
     * @return
     * @throws IOException
     */
    @Nullable
    private String printResult(Request request, Response response, boolean logResponse) {
        try {
            //读取服务器返回的结果
            ResponseBody responseBody = response.newBuilder().build().body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body
            Buffer buffer = source.buffer();

            //获取content的压缩类型
            String encoding = response
                    .headers()
                    .get("Content-Encoding");
            Buffer clone = buffer.clone();
            //解析response content
            return parseContent(responseBody, encoding, clone);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }



    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));//解压
        } else {//content没有被压缩
            return clone.readString(charset);
        }
    }

    /**
     * 解析请求服务器的请求参数
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String parseParams(Request request) {
        try {
            RequestBody body = request.newBuilder().build().body();
            if (body == null) return "";
            Buffer requestbuffer = new Buffer();
            body.writeTo(requestbuffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            return CharacterHandler.jsonFormat(URLDecoder.decode(requestbuffer.readString(charset), convertCharset(charset)));
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * 是否可以解析
     *
     * @param mediaType
     * @return
     */
    public static boolean isParseable(MediaType mediaType) {
        return isText(mediaType) || isPlain(mediaType)
                || isJson(mediaType) || isForm(mediaType)
                || isHtml(mediaType) || isXml(mediaType);
    }

    public static boolean isText(MediaType mediaType) {
        if (mediaType == null || mediaType.type() == null) return false;
        return mediaType.type().equals("text");
    }

    public static boolean isPlain(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("plain");
    }

    public static boolean isJson(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("json");
    }

    public static boolean isXml(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("xml");
    }

    public static boolean isHtml(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("html");
    }

    public static boolean isForm(MediaType mediaType) {
        if (mediaType == null || mediaType.subtype() == null) return false;
        return mediaType.subtype().toLowerCase().contains("x-www-form-urlencoded");
    }

    public static String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1)
            return s;
        return s.substring(i + 1, s.length() - 1);
    }

}
