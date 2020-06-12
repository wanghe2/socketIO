package org.server.blo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * 1.singleHandle（）方法这种方式只能一次处理一个客户端请求；
 * 2.threadpool（） 方式如果想处理多个，可以使用线程池；当serversocket.accept检查到客户端请求，将请求放到线程池里运行
 * @author wanghe
 *
 */
public class ServerSocketBIO {
	
	public final static int PORT=8080;
	public static int TIMES=1;
	static Executor executor=Executors.newFixedThreadPool(30);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocketBIO serverSocket=new ServerSocketBIO();
		if("single".equals(args[0])) {
			serverSocket.singleHandle();
		}else {
			serverSocket.multiHandler();
		}
	}
	
	public void singleHandle() throws IOException {
		ServerSocket serverSocket=new ServerSocket(PORT);
		try {
			while(true) {
				Socket socket= serverSocket.accept();
				BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String getData=in.readLine();
				System.err.println("服务端接收到信息："+getData);
				System.err.println("服务端：接收完毕，准备写入");
				BufferedWriter out= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String serverData="服务端第"+TIMES+"次发出的信息 \n";
				out.write(serverData);
				out.flush();
				out.close();
				in.close();
				System.err.println("服务端写入完毕\n===================\n");
				TIMES++;
				Thread.sleep(3000);
				socket.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			serverSocket.close();
		}
	}
	
	void multiHandler() throws IOException {
		ServerSocket serverSocket=new ServerSocket(PORT);
		try {
			while(true) {
				Socket socket= serverSocket.accept();
			    Runnable runnable=	new SocketServerThread(socket);
				executor.execute(runnable);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			serverSocket.close();
		}
	}
	
	
	
	public  class SocketServerThread implements Runnable{
 
		private Socket socket;
		
		public SocketServerThread(Socket socket) {
			this.socket=socket;
		}
		
		@Override
		public void run() {
			BufferedReader in=null;
			BufferedWriter out=null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String getData=in.readLine();
				System.err.println("服务端接收到信息："+getData);
				System.err.println("服务端：接收完毕，准备写入");
				out= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String serverData="服务端第"+TIMES+"次发出的信息 \n";
				out.write(serverData);
				out.flush();
				out.close();
				in.close();
				System.err.println("服务端写入完毕\n===================\n");
				TIMES++;
				Thread.sleep(3000);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				try {
					if(in!=null) {
						in.close();
					}
					if(out!=null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	} 
	
}
