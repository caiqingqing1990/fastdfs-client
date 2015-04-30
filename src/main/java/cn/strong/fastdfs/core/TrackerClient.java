/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL;
import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE;
import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cn.strong.fastdfs.exception.FastdfsException;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.request.tracker.GetUploadStorageRequest;
import cn.strong.fastdfs.request.tracker.StoragePathRequest;
import cn.strong.fastdfs.response.ResponseDecoder;
import cn.strong.fastdfs.response.StorageServerInfoDecoder;
import cn.strong.fastdfs.response.StorageServerInfoListDecoder;

/**
 * Tracker 客户端
 * 
 * @author liulongbiao
 *
 */
public class TrackerClient {

	private static final Random RDM = new Random();
	private List<InetSocketAddress> addresses;
	private FastdfsExecutor executor;

	public TrackerClient(List<InetSocketAddress> addresses,
			FastdfsExecutor executor) {
		if (Helpers.isEmpty(addresses)) {
			throw new FastdfsException("tracker addresses is empty.");
		}
		this.addresses = Collections.unmodifiableList(addresses);
		this.executor = Objects.requireNonNull(executor);
	}

	/*
	 * 选取一个地址
	 */
	private InetSocketAddress pickOne() {
		int size = addresses.size();
		int idx = size == 1 ? 0 : RDM.nextInt(size);
		return addresses.get(idx);
	}

	/**
	 * 请求 tracker 获取一个可用的上传 storage 地址
	 * 
	 * @return
	 */
	public StorageServerInfo getUploadStorage() {
		return getUploadStorage(null);
	}

	/**
	 * 请求 tracker 获取一个可用的上传 storage 地址
	 * 
	 * @param group
	 * @return
	 */
	public StorageServerInfo getUploadStorage(String group) {
		return exec(new GetUploadStorageRequest(group),
				StorageServerInfoDecoder.INSTANCE);
	}

	/**
	 * 请求 tracker 获取一个可用的下载 storage 地址
	 * 
	 * @param path
	 * @return
	 */
	public StorageServerInfo getDownloadStorage(StoragePath path) {
		List<StorageServerInfo> servers = exec(new StoragePathRequest(path,
				TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE),
				StorageServerInfoListDecoder.INSTANCE);
		return Helpers.first(servers);
	}

	/**
	 * 请求 tracker 获取一个可用的更新 storage 地址
	 * 
	 * @param path
	 * @return
	 */
	public StorageServerInfo getUpdateStorage(StoragePath path) {
		List<StorageServerInfo> servers = exec(new StoragePathRequest(path,
				TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE),
				StorageServerInfoListDecoder.INSTANCE);
		return Helpers.first(servers);
	}

	/**
	 * 请求 tracker 获取所有可用的下载 storage 地址列表
	 * 
	 * @param path
	 * @return
	 */
	public List<StorageServerInfo> findDownloadStorages(StoragePath path) {
		return exec(new StoragePathRequest(path,
				TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL),
				StorageServerInfoListDecoder.INSTANCE);
	}

	/**
	 * 在随机的服务器上执行命令，并获取相应内容
	 * 
	 * @param cmd
	 * @return
	 */
	public <T> T exec(Request request, ResponseDecoder<T> decoder) {
		return executor.exec(request, decoder, pickOne());
	}
}
