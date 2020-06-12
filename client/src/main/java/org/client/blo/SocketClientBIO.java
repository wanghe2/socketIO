package org.client.blo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * 阻塞IO
 * @author wanghe
 *
 */
public class SocketClientBIO {
	
	public final static String HOST="127.0.0.1";
	public final static int PORT=8080;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socketclient=new Socket(HOST,PORT);
		String outdata="hello,我是客户端哦\n";
		System.err.println("客户端要发出的信息："+outdata);
		BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socketclient.getOutputStream()));
        out.write(outdata);
        out.flush();
		System.err.println("客户端写完毕，准备读入");
		BufferedReader in=new BufferedReader(new InputStreamReader(socketclient.getInputStream()));
		String getData=in.readLine();
		System.err.println("客户端接收信息："+getData);
		in.close();
		out.close();
		Thread.sleep(15000);
		socketclient.close();
	}
}
