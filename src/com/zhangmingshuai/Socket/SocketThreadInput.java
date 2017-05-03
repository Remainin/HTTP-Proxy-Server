/**
 * 
 */
package com.zhangmingshuai.Socket;

/**
 * CreateDate£º2017-5-4 time£º05:48:01
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

public class SocketThreadInput extends Thread {
    private InputStream isOut;
    private OutputStream osIn;
    private String url;
    public SocketThreadInput(InputStream isOut, OutputStream osIn, String url) {
        this.isOut = isOut;
        this.osIn = osIn;
        this.url = url;

    }

    private byte[] buffer = new byte[1024];

    public void run() {
        try {
            int len;
            int flag = 0;
            File my = new File("cache/"+url+".txt");
            if(!my.exists()){
            	System.out.println("Error happening when open 'cache' file!");
            	my.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(my);
            StringBuffer temp = new StringBuffer();
            while ((len = isOut.read(buffer)) != -1) {
                if (len > 0) {
                	String sb = new String(buffer,0,len);
                	if(-1!=sb.indexOf("\r\n\r\n")&&flag ==0){
                		sb = sb.substring(sb.indexOf("\r\n\r\n")+4);
                		flag = 1;
                	}
                 //  System.out.println("Send to client:"+sb);
                    osIn.write(buffer, 0, len);
                    osIn.flush();
                    if(flag ==1 ){
                    	temp.append(sb);
                    	temp.append("\r\n");
                    }
                    
                }
            }
          //  System.out.println("^^SocketInput_TEMP:"+temp+"^^^^");
            fs.write(temp.toString().getBytes("utf-8"));
            fs.close();
        } catch (Exception e) {
            System.out.println("SocketThreadInput leave");
        }
    }
}