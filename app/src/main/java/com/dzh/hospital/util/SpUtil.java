package com.dzh.hospital.util;

import com.blankj.utilcode.util.SPUtils;

public class SpUtil {
    private static final String IP = "IP";
    private static final String PATH = "PATH";
    /**
     * 序列号
     */
    private static final String SN = "SN";
    private static final String DELAY = "DELAY";


    public static void setIp(String ip) {
        SPUtils.getInstance(IP).put(IP, ip);
    }

    public static String getIp() {
        return SPUtils.getInstance(IP).getString(IP);
    }

    public static void setPath(String path) {
        SPUtils.getInstance(PATH).put(PATH, path);
    }

    public static String getPath() {
        return SPUtils.getInstance(PATH).getString(PATH);
    }

    public static void setSn(String Sn) {
        SPUtils.getInstance(SN).put(SN, Sn);
    }

    public static String getSn() {
        return SPUtils.getInstance(SN).getString(SN);
    }
    public static void setDelay(int Sn) {
        SPUtils.getInstance(DELAY).put(DELAY, Sn);
    }

    public static int getDelay() {
        return SPUtils.getInstance(DELAY).getInt(DELAY,20);
    }
}
