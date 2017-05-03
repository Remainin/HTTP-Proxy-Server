package com.zhangmingshuai.server;

/**
 * CreateDate£º2017-5-4 time£º02:31:17
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
import com.zhangmingshuai.bean.HeaderContent;

public class HeaderParse {
	public static HeaderContent parse(byte[] buffer, int len) {
		String str = new String(buffer, 0, len);
		HeaderContent HC = new HeaderContent();
		//System.out.println("&& Now Line="+str);
//		if (str.charAt(0) == 'C')
//		{
////			return null; // call connect
////			System.out.println("#%$%$&%&^%"+str.substring(0,7));
////			HC.port = 80;
////			str = "GET" + str.substring(7);
//		}
		String[] source = str.split("\\r\\n");

		//HeaderContent HC = new HeaderContent();

		String[] firstLine = source[0].split(" ");
		HC.method = firstLine[0];
		HC.protocol = firstLine[2];
		HC.url = firstLine[1];
		for (String line : source) {
			//System.out.println("$$$line = "+line);
			String[] map = line.split(":");
			if (map[0].equals("Host")) {
				int i = 0;
				if (map[i + 1].startsWith("http"))
					i++;
				HC.host = map[i + 1].substring(1).replace("/", "");
				if (map.length > i + 2){
					HC.port = Integer.parseInt(map[i + 2]);
//					System.out.println("HC.PORT="+HC.port);
				}
			} else if (map[0].equals("Cookie")) {
				HC.cookie = map[1].substring(1);
			}
		}
		HC.bytes = str.getBytes();
		HC.len = HC.bytes.length;
		return HC;
	}
}