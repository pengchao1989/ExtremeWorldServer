package com.jixianxueyuan.utils;

import com.google.common.base.Splitter;

import java.util.Map;

/**
 * Created by sheodon on 2017/2/18.
 */
public  class TargetInfo {

    public String device;  // 设备名 iphone 6s
    public String sysName; //系统名  IOS
    public String sysVer; //系统版本 10.0
    public String appVer; //应用版本 1.5.0
    public String appVerCode; //应用版本号 xxx

    public boolean isIOS() {
        if (sysName == null) {
            return false;
        }
        return sysName.equalsIgnoreCase("IOS");
    }

    public boolean isIOSAppVersion(String version) {
        if (!isIOS() || appVer == null) {
            return false;
        }
        return appVer.startsWith(version);
    }

    public TargetInfo(String targetInfoString) {

        Map<String, String> properties = Splitter.on(",").withKeyValueSeparator(":").split(targetInfoString);

        this.device = properties.get("device");
        this.sysName = properties.get("sysName");
        this.sysVer = properties.get("sysVer");
        this.appVer = properties.get("appVer");
        this.appVerCode = properties.get("appVerCode");
    }

    public static boolean isIOSAppVersion(String targetInfo, String version) {

        if (targetInfo == null) {
            return false;
        }
        TargetInfo target = new TargetInfo(targetInfo);
        return target.isIOSAppVersion(version);
    }
}