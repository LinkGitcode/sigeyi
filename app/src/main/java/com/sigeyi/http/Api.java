package com.sigeyi.http;

/**
 * 存放一些与常量有关的东西,如请求地址,请求码等
 * <p>
 * Created by huangchangguo 2018.2.20
 */
public interface Api {

    String testFirmwareURL1 = "https://appmail.mail.10086.cn/RmWeb3/view.do?func=attach%3Adownload&mid=8d01079ea2b242d600000001&offset=4126&size=45184&sid=00UyOTU5OTUyOTAwMDA0Mjg102CAACC3000008&type=attach&encoding=1&name=PM01T181P01.zip";
    String NAVI_DOMAIN = "http://test.fyp.szprize.cn";//导航栏baseUrl

    /**************************用以区分请求的字段***********************/
    String HEADER_KEY = "header_key";

    String UMENG_APP_KEY = "5b3c70e4f29d985901000076";//友盟key

    /**************************用以区分请求的字段***********************/
}
