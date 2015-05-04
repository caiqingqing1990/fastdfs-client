/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.strong.fastdfs.SimpleFastdfsClient;

/**
 * @author liulongbiao
 *
 */
public class SimpleFastdfsClientIT {

	public static void main(String[] args) {
		FastdfsExecutor executor = new FastdfsExecutor();
		executor.init();

		try {
			SimpleFastdfsClient dfs = getSimpleFastdfsClient(executor);

			String uri = testUpload(dfs);
			testDownload(dfs, uri);
			testSetMetadata(dfs, uri);
			testGetMetadata(dfs, uri);

			uri = testUploadAppender(dfs);
			testAppend(dfs, uri);
			testTruncate(dfs, uri);
		} finally {
			executor.shutdown();
		}

	}

	private static void testAppend(SimpleFastdfsClient dfs, String uri) {
		byte[] bytes = "appender again.".getBytes(CharsetUtil.UTF_8);
		dfs.append(uri, bytes);
		testDownload(dfs, uri);
	}

	private static String testUploadAppender(SimpleFastdfsClient dfs) {
		byte[] bytes = "Hello appender.".getBytes(CharsetUtil.UTF_8);
		String uri = dfs.uploadAppender(bytes, "txt");
		System.out.println("uploaded: ");
		System.out.println(uri);

		testDownload(dfs, uri);
		return uri;
	}

	private static void testTruncate(SimpleFastdfsClient dfs, String uri) {
		dfs.truncate(uri, 2);
		testDownload(dfs, uri);
	}

	private static void testGetMetadata(SimpleFastdfsClient dfs, String uri) {
		Map<String, String> metadata = dfs.getMetadata(uri);
		System.out.println("metadatas: ");
		for (Map.Entry<String, String> entry : metadata.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	private static void testSetMetadata(SimpleFastdfsClient dfs, String uri) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("createdBy", "401");
		metadata.put("createdOn", "2015-03-23");
		dfs.setMetadata(uri, metadata);
	}

	private static void testDownload(SimpleFastdfsClient dfs, String uri) {
		byte[] downloads = dfs.download(uri);
		String content = new String(downloads, CharsetUtil.UTF_8);
		System.out.println("downloaded: ");
		System.out.println(content);
	}

	private static String testUpload(SimpleFastdfsClient dfs) {
		byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
		String uri = dfs.upload(bytes, "txt");
		System.out.println("uploaded: ");
		System.out.println(uri);
		return uri;
	}

	private static SimpleFastdfsClient getSimpleFastdfsClient(
			FastdfsExecutor client) {
		List<InetSocketAddress> trackers = Arrays.asList(new InetSocketAddress(
				"192.168.20.68", 22122));
		return new SimpleFastdfsClient(client, trackers);
	}

}
