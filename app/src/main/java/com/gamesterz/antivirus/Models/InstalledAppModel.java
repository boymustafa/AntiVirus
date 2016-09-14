package com.gamesterz.antivirus.Models;

/**
 * Created by Boy Mustafa on 13/06/16.
 */
public class InstalledAppModel {

    String pkgName;
    long appSize;

    public InstalledAppModel(){

    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

}
