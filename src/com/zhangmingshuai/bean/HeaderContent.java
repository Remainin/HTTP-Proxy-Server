package com.zhangmingshuai.bean;

/**
 * CreateDate£º2017-5-4 time£º01:22:59
 * Location£ºHIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
public class HeaderContent {
    public String method;
    public String host;
    public int port = 80;
    public String protocol;
    public String cookie;
    public String url;
    public byte[] bytes;
    public int len;
    public String ip;
    
    public String toString()
    {
    	return method + " " +  url + " " + protocol;
    	
    }
}
