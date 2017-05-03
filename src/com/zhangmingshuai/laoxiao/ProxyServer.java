package com.zhangmingshuai.laoxiao;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProxyServer {
	
	public static void  syncStreams(InputStream in,OutputStream out)
	{
		int data;
		int written=0;
		try {
			while( (data=in.read()) != -1)
			{
				if(written==1024)
				{
					out.flush();
					written=0;
				}
//				System.out.print((char)data);
//				System.out.println("sending:"+(char)data);
				out.write(data);
				written++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}finally{
			try {
				out.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}

	public static void process(final Socket client,final Socket server)
	{
		Thread t1=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ProxyServer.syncStreams(client.getInputStream(),server.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		});
		Thread t2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ProxyServer.syncStreams(server.getInputStream(),client.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
		
		System.out.println("t1,t2 ended.");
	}
	public static void build2(Socket client,String host,int port)
	{
		System.out.println("host is "+host+" ,port is "+port);
		try {
			Socket server=new Socket(host,port);
			client.getOutputStream().write("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
			client.getOutputStream().flush();
			System.out.println("sent 200");
			ProxyServer.process(client, server);
			server.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			try {
				System.out.println("sending 500");
				client.getOutputStream().write("HTTP/1.1 500 Internal Error\r\n\r\n".getBytes());
				client.getOutputStream().flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
//				e1.printStackTrace();
			}
		}

		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	public static void buildConnection(Socket client,String data)
	{
		System.out.println("request data is :"+data);
		String host=null;
		int port=443;
		int i="CONNECT".length();
		int ihostend=data.indexOf(":");
		if(ihostend<0)
		{
			host=data.substring(i+1,data.indexOf(" ",i+1));
		}else{
			host=data.substring(i+1,ihostend);
			port = Integer.valueOf(data.substring(ihostend+1,data.indexOf(" ",ihostend+1)));
		}
		ProxyServer.build2(client, host, port);
		
	}
	public static void main22(String[]args)
	{
		try {
			ServerSocket socket=new ServerSocket(8080);
			byte[] data=new byte[1500];
			while(true)
			{
				final Socket client=socket.accept();
				int len=client.getInputStream().read(data);
				System.out.println("len is "+len);
				if(len==-1)continue;
				final String strdata=new String(data,0,len);
				if(strdata.startsWith("CONNECT"))
				{
					Thread t1=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ProxyServer.buildConnection(client, strdata);
						}
					});
					t1.start();
				}else{
					client.getOutputStream().write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
					client.close();
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
