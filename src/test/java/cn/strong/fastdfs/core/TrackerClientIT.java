/**
 * 
 */
package cn.strong.fastdfs.core;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * @author liulongbiao
 *
 */
public class TrackerClientIT {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		FastdfsExecutor client = new FastdfsExecutor();
		ExecutorService service = Executors.newFixedThreadPool(5);
		try {
			client.init();
			final TrackerClient tc = new TrackerClient(
					Arrays.asList(new InetSocketAddress("192.168.20.68", 22122)),
					client);

			Random random = new Random();
			List<Future<?>> fs = new ArrayList<Future<?>>();
			for(int i = 0 ; i < 20; i ++) {
				Future<?> f = service.submit(new Runnable() {

					@Override
					public void run() {
						StorageServerInfo stg = tc.getUploadStorage();
						System.out.println(stg);
					}

				});
				fs.add(f);
				long ms = random.nextInt(500);
				System.out.println("sleep " + ms + " ms.");
				Thread.sleep(ms);
			}
			for (Future<?> f : fs) {
				f.get();
			}
		} finally {
			client.shutdown();
			service.shutdown();
		}
	}

}
