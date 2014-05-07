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
import org.apache.log4j.*;

public class Client {

	public static final int BUF_SIZE = 4;
	public static boolean running = true;

	public String fileName;
	public String downloadDir;
	public User source;

	public static Logger logger = Logger.getLogger(Client.class);


	public Client(User source, String fileName, String destDir) {
		super();
		Selector selector			= null;
		SocketChannel socketChannel	= null;
		this.fileName = fileName;
		this.downloadDir = destDir;
		this.source = source;

		running = true;

		try {
			selector = Selector.open();

			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			System.out.println(source.getPort());
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
			socketChannel.close();

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
		ByteBuffer send_buffer = encoder.encode(CharBuffer.wrap(this.fileName));

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
			raf = new RandomAccessFile(this.downloadDir + this.fileName, "rw");
			raf.setLength(0);
			fc = raf.getChannel();
			memBuf = fc.map(FileChannel.MapMode.READ_WRITE, 0, file_size);

			int progres = 0;
			int value = 0;

			PropertyConfigurator.configure("log4j.properties");
			String curr_user = this.source.getName();
			Logger user_logger = Logger.getLogger(curr_user);

			while ((bytes = socketChannel.read(memBuf)) > 0){
				System.out.println();
				logger.info("Am primit in pasul asta: " + bytes + " bytes. ");
				user_logger.info("Am primit in pasul asta: " + bytes + " bytes. ");
				progres = progres + bytes;
				logger.info("In total pana acum : " + progres + " bytes. ");
				user_logger.info("In total pana acum : " + progres + " bytes. ");
				value = (int)Math.ceil(((float)progres/file_size)*100);
				logger.info("Procent: "+value);
				user_logger.info("Procent: "+value);
				ProgressWorker.upgradeProgress(value);
				System.out.println();
			}

			// check for EOF
			if (bytes == -1)
				throw new IOException("EOF");

			fc.close();
			raf.close();
			running = false;
			socketChannel.close();

		} catch (IOException e) {
			socketChannel.close();
			running = false;
		}
	}

}
