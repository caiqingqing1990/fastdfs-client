/**
 * 
 */
package cn.strong.fastdfs.core;


import io.netty.util.CharsetUtil;

import java.util.concurrent.ExecutionException;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * @author liulongbiao
 *
 */
public class StorageClientIT {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		FastdfsExecutor client = new FastdfsExecutor();
		try {
			client.init();
			StorageClient sc = new StorageClient(client);
			StorageServerInfo stg = new StorageServerInfo("group1",
					"192.168.20.68", 23000, 0);
			byte[] bytes = "Hello fastdfs".getBytes(CharsetUtil.UTF_8);
			StoragePath path = sc.upload(stg, bytes, "txt");
			System.out.println(path);
		} finally {
			client.shutdown();
		}
	}

}
