/**
 * 
 */
package cn.strong.fastdfs;

import static cn.strong.fastdfs.core.Consts.STORAGE_SET_METADATA_FLAG_OVERWRITE;
import static cn.strong.fastdfs.core.Helpers.requireNonNull;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import cn.strong.fastdfs.core.FastdfsExecutor;
import cn.strong.fastdfs.core.StorageClient;
import cn.strong.fastdfs.core.TrackerClient;
import cn.strong.fastdfs.exception.FastdfsException;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;


/**
 * Fastdfs 客户端
 * 
 * @author liulongbiao
 *
 */
public class FastdfsClient {

	private TrackerClient trackerClient;
	private StorageClient storageClient;

	public FastdfsClient(TrackerClient trackerClient,
			StorageClient storageClient) {
		this.trackerClient = requireNonNull(trackerClient);
		this.storageClient = requireNonNull(storageClient);
	}

	public FastdfsClient(FastdfsExecutor executor, List<InetSocketAddress> trackers) {
		this.trackerClient = new TrackerClient(trackers, executor);
		this.storageClient = new StorageClient(executor);
	}

	/**
	 * 上传文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public StoragePath upload(byte[] bytes, String ext) {
		return upload(bytes, ext, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public StoragePath upload(byte[] bytes, String ext, String group) {
		StorageServerInfo storage = trackerClient.getUploadStorage(group);
		return storageClient.upload(storage, bytes, ext);
	}

	/**
	 * 上传本地文件
	 * 
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public StoragePath upload(File file) {
		return upload(file, null);
	}

	/**
	 * 上传本地文件
	 * 
	 * @param file
	 *            本地文件
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public StoragePath upload(File file, String group) {
		if (file == null || !file.exists()) {
			throw new FastdfsException("file does not exist.");
		}
		StorageServerInfo storage = trackerClient.getUploadStorage(group);
		return storageClient.upload(storage, file);
	}

	/**
	 * 上传 Appender 文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(byte[] bytes, String ext) {
		return uploadAppender(bytes, ext, null);
	}

	/**
	 * 上传 Appender 文件
	 * 
	 * @param bytes
	 *            文件内容
	 * @param ext
	 *            文件扩展名
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(byte[] bytes, String ext, String group) {
		StorageServerInfo storage = trackerClient.getUploadStorage(group);
		return storageClient.uploadAppender(storage, bytes, ext);
	}

	/**
	 * 上传本地文件为 appender 文件
	 * 
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(File file) {
		return uploadAppender(file, null);
	}

	/**
	 * 上传本地文件为 appender 文件
	 * 
	 * @param file
	 *            本地文件
	 * @param group
	 *            文件分组
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(File file, String group) {
		if (file == null || !file.exists()) {
			throw new FastdfsException("file does not exist.");
		}
		StorageServerInfo storage = trackerClient.getUploadStorage(group);
		return storageClient.uploadAppender(storage, file);
	}

	/**
	 * 下载文件
	 * 
	 * @param path
	 *            服务器存储路径
	 * @return 文件内容
	 */
	public byte[] download(StoragePath path) {
		StorageServerInfo storage = trackerClient.getDownloadStorage(path);
		return storageClient.download(storage, path);
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            服务器存储路径
	 */
	public void delete(StoragePath path) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		storageClient.delete(storage, path);
	}

	/**
	 * 追加文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param bytes
	 *            追加内容
	 */
	public void append(StoragePath path, byte[] bytes) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		storageClient.append(storage, path, bytes);
	}

	/**
	 * 修改文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param bytes
	 *            修改内容
	 * @param offset
	 *            修改起始偏移量
	 */
	public void modify(StoragePath path, byte[] bytes, int offset) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		storageClient.modify(storage, path, bytes, offset);
	}

	/**
	 * 截取文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 */
	public void truncate(StoragePath path) {
		truncate(path, 0);
	}

	/**
	 * 截取文件内容
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param truncatedSize
	 *            截取字节数
	 */
	public void truncate(StoragePath path, int truncatedSize) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		storageClient.truncate(storage, path, truncatedSize);
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 */
	public void setMetadata(StoragePath path, Map<String, String> metadata) {
		setMetadata(path, metadata, STORAGE_SET_METADATA_FLAG_OVERWRITE);
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param path
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 * @param flag
	 *            设置标识
	 */
	public void setMetadata(StoragePath path, Map<String, String> metadata,
			byte flag) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		storageClient.setMetadata(storage, path, metadata, flag);
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param path
	 * @return
	 */
	public Map<String, String> getMetadata(StoragePath path) {
		StorageServerInfo storage = trackerClient.getUpdateStorage(path);
		return storageClient.getMetadata(storage, path);
	}
}
