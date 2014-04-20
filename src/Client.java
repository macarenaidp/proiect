import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class Client {

	public static final int BUF_SIZE = 4;
	public static boolean running = true;
	public static String file_name = "";
	public String destdir = "";

	public Client(User source, String fileName, String destDir) {
		super();
		Selector selector			= null;
		SocketChannel socketChannel	= null;
		this.file_name = fileName;
		this.destdir = destDir;

		try {
			selector = Selector.open();

			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(source.getIP(), source.getPort()));
			socketChannel.register(selector, SelectionKey.OP_CONNECT);

			while (running) {
				selector.select();

				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();

					if (key.isConnectable())
						connect(key);
					else if (key.isReadable())
						read(key);
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

			if (socketChannel != null)
				try {
					socketChannel.close();
				} catch (IOException e) {}
		}
	}


	public static void connect(SelectionKey key) throws IOException {

		System.out.print("CONNECT: ");

		SocketChannel socketChannel = (SocketChannel)key.channel();
		if (! socketChannel.finishConnect()) {
			System.err.println("Eroare finishConnect");
			running = false;
		}

		key.interestOps(SelectionKey.OP_WRITE);
	}

	public void write(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel)key.channel();

		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer send_buffer = encoder.encode(CharBuffer.wrap(this.file_name));

		while (socketChannel.write(send_buffer) > 0);

		socketChannel.register(key.selector(), SelectionKey.OP_READ);
	}

	public void read(SelectionKey key) throws IOException {

		int bytes = 0;
		SocketChannel socketChannel = (SocketChannel)key.channel();

		RandomAccessFile raf	= null;		// file
		FileChannel fc			= null;		// associated file channel
		MappedByteBuffer memBuf	= null;		// associated memory mapping

		try {
			ByteBuffer lengthByteBuffer = ByteBuffer.allocate(4);
			socketChannel.read(lengthByteBuffer);
			int file_size = lengthByteBuffer.getInt(0);
			System.out.println("File size: " + file_size);

			//my file
			raf = new RandomAccessFile(this.destdir + this.file_name, "rw");
			raf.setLength(0);
			fc = raf.getChannel();
			memBuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, file_size);

			while ((bytes = socketChannel.read(memBuf)) > 0);

			// check for EOF
			if (bytes == -1)
				throw new IOException("EOF");

			fc.close();
			raf.close();
			running = false;

		} catch (IOException e) {
			socketChannel.close();
			fc.close();
			raf.close();
			running = false;
		}
	}

}
