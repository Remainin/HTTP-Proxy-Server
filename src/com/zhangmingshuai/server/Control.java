package com.zhangmingshuai.server;

/**
 * CreateDate£º2017-5-4 time£º02:58:42
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
import com.zhangmingshuai.bean.HeaderContent;
import java.io.IOException;
import java.io.OutputStream;

public class Control {
    final static String[] gfw_url_list = {
        "baidu.com"
    };
    final static String[] gfw_ip_list = {
           "127.0.0.1"
    };
    final static String[] gfw_redirect_list = {
            "hit.edu.cn"
    };
    final static String redirect = "HTTP/1.1 200 OK\n" +
                                   "Server: nginx\n" +
                                   "Content-Type: text/html\n\n" +
                                   "hello world";
    public static boolean a(HeaderContent HeaderContent){
        for(String url:gfw_url_list){
            if(HeaderContent.url.contains(url)){
                return false;
            }
        }
        return true;
    }

    public static boolean b(HeaderContent HeaderContent){
        for(String ip:gfw_ip_list){
            if(HeaderContent.ip.contains(ip)){
                return false;
            }
        }
        return true;
    }

    public static boolean c(HeaderContent HeaderContent,OutputStream osIn) throws IOException {
        for(String url:gfw_redirect_list){
            if(HeaderContent.url.contains(url)){
                byte[] bytes = redirect.getBytes();
                osIn.write(bytes,0,bytes.length);
                osIn.flush();
                return false;
            }
        }
        return true;
    }
}
