package org.client.nobio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 非阻塞IO
 * @author wanghe
 *
 */
public class SocketClientNOBIO {
	
	public final static String HOST="127.0.0.1";
	public final static int PORT=8080;

	public static void main(String[] args) throws IOException, InterruptedException {
		SocketChannel socketChannel=SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(HOST, PORT));//连接这里，其实是会阻塞的（客户端也可以设置成非阻塞，但这里没有必要）
		System.err.println("客户端连接成功，准备发送数据");
		socketChannel.write(ByteBuffer.wrap("你好呀，我是服务端".getBytes()));
		Thread.sleep(1000);
		System.err.println("客户端发送数据完毕，准备接收数据");
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		System.err.println("客户端接收到消息："+new String(buffer.array()));
		System.err.println("客户端接收数据完毕，即将关闭");
		Thread.sleep(3000);
		socketChannel.close();
	}
}
