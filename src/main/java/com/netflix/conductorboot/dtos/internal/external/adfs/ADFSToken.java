package com.netflix.conductorboot.dtos.internal.external.adfs;

import java.util.Arrays;

public class ADFSToken {
    private String ver;
    private String appType;
    private String aud;
    private String iss;
    private String[] Role;
    private String auth_time;
    private String authmethod;
    private String exp;
    private String iat;

    public ADFSToken(String ver, String appType, String aud, String iss, String[] role, String auth_time, String authmethod, String exp, String iat) {
        this.ver = ver;
        this.appType = appType;
        this.aud = aud;
        this.iss = iss;
        Role = role;
        this.auth_time = auth_time;
        this.authmethod = authmethod;
        this.exp = exp;
        this.iat = iat;
    }

    public ADFSToken() {

    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String[] getRole() {
        return Role;
    }

    public void setRole(String[] role) {
        Role = role;
    }

    public String getAuth_time() {
        return auth_time;
    }

    public void setAuth_time(String auth_time) {
        this.auth_time = auth_time;
    }

    public String getAuthmethod() {
        return authmethod;
    }

    public void setAuthmethod(String authmethod) {
        this.authmethod = authmethod;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    @Override
    public String toString() {
        return "ADFSToken{" +
                "ver='" + ver + '\'' +
                ", appType='" + appType + '\'' +
                ", aud='" + aud + '\'' +
                ", iss='" + iss + '\'' +
                ", Role=" + Arrays.toString(Role) +
                ", auth_time='" + auth_time + '\'' +
                ", authmethod='" + authmethod + '\'' +
                ", exp='" + exp + '\'' +
                ", iat='" + iat + '\'' +
                '}';
    }
}
