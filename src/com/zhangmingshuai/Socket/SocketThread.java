package com.zhangmingshuai.Socket;

/**
 * CreateDate£º2017-5-4 time£º06:39:44
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;
import com.zhangmingshuai.bean.HeaderContent;
import com.zhangmingshuai.laoxiao.ProxyServer;
import com.zhangmingshuai.server.Control;
import com.zhangmingshuai.server.HeaderParse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketThread extends Thread {
    private Socket socketIn,socketOut;
    private InputStream isIn,isOut;
    private OutputStream osIn,osOut;
    private byte[] bytes = new byte[2048];

    public SocketThread(Socket socket) {
        this.socketIn = socket;
    }
    public static void syncStream(InputStream in,OutputStream out)
    {
    	int data;
    	
    	try{
    		while((data=in.read())!=-1)
    		{
    			out.write(data);
    			System.out.println("sending--->"+(char)data);
    			out.flush();
    		}
    		out.flush();
    	}catch(Exception e){
    		try {
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    }
    
    public static void processConnect(Socket cclient,Socket sserver)
    {
    	final Socket client=cclient;
    	final Socket server=sserver;
		Thread t1=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					SocketThread.syncStream(client.getInputStream(), server.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Thread t2=new Thread(new Runnable() {
			@Override
			public void run() {
//				 TODO Auto-generated method stub
				try {
					SocketThread.syncStream(server.getInputStream(), client.getOutputStream());
				} catch (IOException e) {
//					 TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
			
    }

    public void run() {
        try {
            System.out.println("\n\na client connect ");
            isIn = socketIn.getInputStream();
            osIn = socketIn.getOutputStream();

            int len;
            HeaderContent hc = null;
            if ((len = isIn.read(bytes)) != -1&&len > 0){
                hc = HeaderParse.parse(bytes,len);
                hc.ip = socketIn.getLocalAddress().getHostAddress();
              //System.out.println("%%%ip="+HeaderContent.ip);
            }
            System.out.println("HeaderContent : " + hc.toString());
            if(hc.method.equals("CONNECT"))
            {
            	ProxyServer.build2(socketIn, hc.host,hc.port);
            	return;
            }
//            

            if(Control.c(hc,osIn)){
            	//System.out.println("!!!sockeout_HOST="+hc.host);
            	//System.out.println("!!!sockeout_PORT="+hc.port);
                socketOut = new Socket(hc.host,hc.port);
                isOut = socketOut.getInputStream();
                osOut = socketOut.getOutputStream();
                System.out.println("begin:\n");
                System.out.println(new String(hc.bytes, 0, hc.len));
                System.out.println("end");
//                System.out.println("***END OF THE HEADERCONTENT***");
                osOut.write(hc.bytes, 0, hc.len);
               osOut.flush();

                if(Control.a(hc)&&Control.b(hc)){
//                    SocketThreadOutput out = new SocketThreadOutput(isIn, osOut);
//                    out.start();
//                	File my = new File("cache/"+hc.host+".txt");
//                	if(my.exists()){
//                		System.out.println("Cache Entered");
//                		FileInputStream fs = new FileInputStream(my);
//                		BufferedReader br= BufferedReader(new InputStreamReader(fs));
//                		String line = null;
//                		osIn.write("HTTP/1.0 200 OK\r\n\r\n".getBytes());
//                		while((line=br.readLine())!=null){
//                			osIn.write(line.getBytes());
//                		}
//                		fs.close();
//                		
//                	}else{
                    SocketThreadInput in = new SocketThreadInput(isOut, osIn,hc.host);
                    in.start();

//                    out.join();
                    in.join();
                	
                }
            }
        } catch (Exception e) {
            System.out.println("a client leave?"+e.getMessage());
        } finally {
            try {
                if (socketIn != null) {
                    socketIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	/**
	 * CreateDate£º2017-5-6ÉÏÎç12:56:42
	 * Location£ºHIT
	 * Author: Zhang Mingshuai
	 * TODO
	 * return
	 */
	private BufferedReader BufferedReader(InputStreamReader inputStreamReader) {
		// TODO Auto-generated method stub
		return null;
	}
}