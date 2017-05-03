
package com.zhangmingshuai.server;

/**
 * CreateDate£º2017-5-4 time£º03:39:35
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */

import com.zhangmingshuai.Socket.SocketThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
    private ServerSocket server=null;

    public Server(int port) throws IOException {
        server=new ServerSocket(port);
        while(true){
            Socket socket=null;
            try{
                socket=server.accept();
                new SocketThread(socket).start();
            }catch(Exception e) {
                System.out.println("Error."+e);
            }
        }
    }

    public void close() throws IOException {
        if(server!=null) server.close();
    }
}
