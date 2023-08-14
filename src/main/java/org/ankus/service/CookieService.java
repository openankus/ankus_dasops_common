package org.ankus.service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieService {

    public void cookieCreate(String name, String data, HttpServletResponse response){

        Cookie cookie = new Cookie(name,data);
        cookie.setComment("접속 유지 여부");
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);       // 30일 유지
        response.addCookie(cookie);
    }

    public void cookieDelete(String name, HttpServletRequest request, HttpServletResponse response){
        Cookie[] list = request.getCookies();
        for(Cookie cookie : list){
            if(cookie.getName().equals(name)){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public String cookieSelect(String name, HttpServletRequest request) {
        Cookie[] list = request.getCookies();
        if(list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }


}
