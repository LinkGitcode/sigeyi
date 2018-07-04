package com.sigeyi.http;

/**
 * 存放一些与常量有关的东西,如请求地址,请求码等
 * <p>
 * Created by huangchangguo 2018.2.20
 */
public interface Api {
    //下载固件的测试地址
    String testFirmwareURL1 = "https://appmail.mail.10086.cn/RmWeb/view.do?func=attach:download&type=attach&encoding=1&sid=00UzMDcxNzU1MTAwMTgzNDM5006852EB000008&mid=8d01079ea2b242d600000001&offset=4126&size=45184&name=PM01T181P01.zip";
    String NAVI_DOMAIN = "http://test.fyp.szprize.cn";//baseUrl

    /**************************用以区分请求的字段***********************/
    String HEADER_KEY = "header_key";

    String UMENG_APP_KEY = "5b3c70e4f29d985901000076";//友盟统计key,简单的统计一下崩溃等信息

    /**************************用以区分请求的字段***********************/

    /**************************用以区分设备的字段***********************/
    String SIGEYI_TYPE1 = "SIGEYI";
    String SIGEYI_TYPE2 = "AXPOWSER";
    String SIGEYI_TYPE3 = "AXO";
    String SIGEYI_TYPE4 = "DLS";
    /**************************用以区分设备的字段***********************/
}
