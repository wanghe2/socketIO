package org.server.nobio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
/**
 * 非阻塞IO 利用 configureBlocking（false）,默认是阻塞的
 * @author wanghe
 *
 */
public class SockerServerNOBIO {

	
	public final static int PORT=8080;

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
		serverSocketChannel.configureBlocking(false);//设置非阻塞
		while(true) {
			SocketChannel socketChannel= serverSocketChannel.accept();
			if(socketChannel!=null) {
				System.err.println("服务端接到客户端请求");
				ByteBuffer buffer=ByteBuffer.allocate(1024);
				socketChannel.read(buffer);
				System.err.println("服务端接收到消息："+new String(buffer.array()));
				buffer.clear();
				System.err.println("服务端准备发送数据");
				socketChannel.write(ByteBuffer.wrap("你好呀，我是服务端".getBytes()));
				Thread.sleep(3000);
				socketChannel.close();
			}else {
				Thread.sleep(1000);
				System.err.println("没有客户端请求，这里要轮询，睡一秒");
			}
		}
	}
}
