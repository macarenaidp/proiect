import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static final int BUF_SIZE	= 4;
	public static final String IP		= "127.0.0.1";
	public static final int PORT		= 40002;
	public static long file_size = 0;

	public Server() {
		super();

		ServerSocketChannel serverSocketChannel	= null;
		Selector selector						= null;

		try {
			selector = Selector.open();

			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(IP, PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				selector.select();

				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();

					if (key.isAcceptable())
						accept(key);
					else if (key.isWritable())
						write(key);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {}

			if (serverSocketChannel != null)
				try {
					serverSocketChannel.close();
				} catch (IOException e) {}
		}
	}
	


	public static void write(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel)key.channel();
		ByteBuffer buf = (ByteBuffer)key.attachment();

		ByteBuffer wrap = ByteBuffer.allocate(4);
		wrap.putInt((int)file_size);
		wrap.flip();
		socketChannel.write(wrap);

		while (socketChannel.write(buf) > 0);

		if (! buf.hasRemaining()) {
			socketChannel.close();
		}
	}

	public static void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		RandomAccessFile raf	= null;		// file
		FileChannel fc			= null;		// associated file channel
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);
		raf = new RandomAccessFile("/home/camelia/Desktop/ceva.txt", "r");
		fc = raf.getChannel();

		while(fc.read(buf) > 0);
		file_size = fc.size();
		buf.flip();

		socketChannel.register(key.selector(), SelectionKey.OP_WRITE, buf);
	}

}
