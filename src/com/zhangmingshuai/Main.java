package com.zhangmingshuai;

/**
 * CreateDate£º2017-5-5 time£º07:54:50
 * Location:HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */

import com.zhangmingshuai.server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		Server server = null;
        try {
            server = new Server(10240);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
