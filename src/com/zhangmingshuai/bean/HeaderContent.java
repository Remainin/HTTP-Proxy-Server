package com.zhangmingshuai.bean;

/**
 * CreateDate��2017-5-4 time��01:22:59
 * Location��HIT
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
