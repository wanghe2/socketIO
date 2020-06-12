package org.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用IO （3大件   Channel 、Buffer 、Selector）
 * 一个select可以监管多个channel
 * 其实非阻塞模型就用到了 channel 、buffer去实现非阻塞
 * @author wanghe
 *
 */
public class SocketClientNIO {
	
	public final static String HOST="127.0.0.1";
	public final static int PORT=8080;
	static Selector selector;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		selector=Selector.open();
		SocketChannel socketChannel=SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress(HOST, PORT));
		socketChannel.register(selector, SelectionKey.OP_CONNECT);//注册一个连接事件
		while(true) {
			selector.select();//会阻塞，直到有管道出现请求
			Set<SelectionKey>selectionKeys= selector.selectedKeys();//一系列的请求
			Iterator<SelectionKey>selectIterator= selectionKeys.iterator();//因为请求到达后，处理请求后要清掉该请求，所以使用迭代器
			while(selectIterator.hasNext()) {
				SelectionKey selectionKey=selectIterator.next();
				selectIterator.remove();
				if(selectionKey.isConnectable()) {//如果是连接事件
					handleConnect(selectionKey);
				}
				if(selectionKey.isReadable()) {//有读的管道请求
					handleRead(selectionKey);
				}
			}
		}
		
	}
	
	public static void handleConnect(SelectionKey selectionKey) throws IOException, InterruptedException {//这里已经连接到服务端，可以写数据了
		 SocketChannel socketChannel=(SocketChannel) selectionKey.channel();//这其实是新的一个管道
		 if(socketChannel.isConnectionPending()) {//查看这个管道上还有没有连接的进程
			 socketChannel.finishConnect();
		 }
		 socketChannel.configureBlocking(false);//非阻塞
		 socketChannel.write(ByteBuffer.wrap("你好呀，我是客户端".getBytes()));
		 Thread.sleep(1000);
		 System.err.println("客户端发送数据完毕，准备接收数据");
		 socketChannel.register(selector, SelectionKey.OP_READ);//写完数据，等待服务器返回数据，所以注册一个读的事件
	}

	private static void handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel=(SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        System.out.println("客户端接收到消息:"+new String(byteBuffer.array()));
	}
}
