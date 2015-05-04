/**
 * 
 */
package cn.strong.fastdfs.core;


import java.io.File;
import java.util.Map;
import java.util.Objects;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.request.payload.BytePayload;
import cn.strong.fastdfs.request.payload.FilePayload;
import cn.strong.fastdfs.request.payload.Payload;
import cn.strong.fastdfs.request.storage.AppendRequest;
import cn.strong.fastdfs.request.storage.DeleteRequest;
import cn.strong.fastdfs.request.storage.DownloadRequest;
import cn.strong.fastdfs.request.storage.GetMetadataRequest;
import cn.strong.fastdfs.request.storage.ModifyRequest;
import cn.strong.fastdfs.request.storage.SetMetadataRequest;
import cn.strong.fastdfs.request.storage.TruncateRequest;
import cn.strong.fastdfs.request.storage.UploadAppenderRequest;
import cn.strong.fastdfs.request.storage.UploadRequest;
import cn.strong.fastdfs.response.ByteDecoder;
import cn.strong.fastdfs.response.EmptyDecoder;
import cn.strong.fastdfs.response.MetadataDecoder;
import cn.strong.fastdfs.response.ResponseDecoder;
import cn.strong.fastdfs.response.StoragePathDecoder;

/**
 * Storage 客户端
 * 
 * @author liulongbiao
 *
 */
public class StorageClient {

	private FastdfsExecutor executor;

	public StorageClient(FastdfsExecutor executor) {
		this.executor = Objects.requireNonNull(executor);
	}

	/**
	 * 上传文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param bytes
	 *            上传的字节内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public StoragePath upload(StorageServerInfo storage, byte[] bytes,
			String ext) {
		Payload payload = new BytePayload(bytes);
		UploadRequest request = new UploadRequest(payload, ext,
				(byte) storage.storePathIndex);
		return exec(request, StoragePathDecoder.INSTANCE, storage);
	}

	/**
	 * 上传本地文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public StoragePath upload(StorageServerInfo storage, File file) {
		Payload payload = new FilePayload(file);
		String ext = Helpers.getFileExt(file.getName());
		UploadRequest request = new UploadRequest(payload, ext,
				(byte) storage.storePathIndex);
		return exec(request, StoragePathDecoder.INSTANCE, storage);
	}

	/**
	 * 上传 appender 文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param bytes
	 *            上传的字节内容
	 * @param ext
	 *            文件扩展名
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(StorageServerInfo storage,
			byte[] bytes, String ext) {
		Payload payload = new BytePayload(bytes);
		UploadAppenderRequest request = new UploadAppenderRequest(payload, ext,
				(byte) storage.storePathIndex);
		return exec(request, StoragePathDecoder.INSTANCE, storage);
	}

	/**
	 * 上传本地文件为 appender 文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param file
	 *            本地文件
	 * @return 服务器存储路径
	 */
	public StoragePath uploadAppender(StorageServerInfo storage, File file) {
		Payload payload = new FilePayload(file);
		String ext = Helpers.getFileExt(file.getName());
		UploadAppenderRequest request = new UploadAppenderRequest(payload, ext,
				(byte) storage.storePathIndex);
		return exec(request, StoragePathDecoder.INSTANCE, storage);
	}

	/**
	 * 追加文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param bytes
	 *            追加的字节内容
	 */
	public void append(StorageServerInfo storage, StoragePath spath,
			byte[] bytes) {
		AppendRequest request = new AppendRequest(spath, new BytePayload(bytes));
		exec(request, EmptyDecoder.INSTANCE, storage);
	}

	/**
	 * 修改文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param bytes
	 *            修改的字节内容
	 * @param offset
	 *            偏移地址
	 */
	public void modify(StorageServerInfo storage, StoragePath spath,
			byte[] bytes, int offset) {
		ModifyRequest request = new ModifyRequest(spath,
				new BytePayload(bytes),
				offset);
		exec(request, EmptyDecoder.INSTANCE, storage);
	}

	/**
	 * 删除文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 */
	public void delete(StorageServerInfo storage, StoragePath spath) {
		DeleteRequest request = new DeleteRequest(spath);
		exec(request, EmptyDecoder.INSTANCE, storage);
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 */
	public void truncate(StorageServerInfo storage, StoragePath spath) {
		truncate(storage, spath, 0);
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param truncatedSize
	 *            截取字节数
	 */
	public void truncate(StorageServerInfo storage, StoragePath spath,
			int truncatedSize) {
		TruncateRequest request = new TruncateRequest(spath, truncatedSize);
		exec(request, EmptyDecoder.INSTANCE, storage);
	}

	/**
	 * 下载文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @return 文件内容字节数组
	 */
	public byte[] download(StorageServerInfo storage, StoragePath spath) {
		DownloadRequest request = new DownloadRequest(spath);
		return exec(request, ByteDecoder.INSTANCE, storage);
	}

	/**
	 * 下载文件内容
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param offset
	 *            字节偏移量
	 * @param size
	 *            下载字节数
	 * @return 文件内容字节数组
	 */
	public byte[] download(StorageServerInfo storage, StoragePath spath,
			int offset, int size) {
		DownloadRequest request = new DownloadRequest(spath, offset, size);
		return exec(request, ByteDecoder.INSTANCE, storage);
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param spath
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 * @param flag
	 *            设置标识
	 */
	public void setMetadata(StorageServerInfo storage, StoragePath spath,
			Map<String, String> metadata, byte flag) {
		SetMetadataRequest request = new SetMetadataRequest(spath, metadata,
				flag);
		exec(request, EmptyDecoder.INSTANCE, storage);
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param storage
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param path
	 *            服务器存储路径
	 * @return
	 */
	public Map<String, String> getMetadata(StorageServerInfo storage,
			StoragePath path) {
		GetMetadataRequest request = new GetMetadataRequest(path);
		return exec(request, MetadataDecoder.INSTANCE, storage);
	}

	/**
	 * 在指定服务器上执行命令，并获取相应内容
	 * 
	 * @param cmd
	 * @param storage
	 * @return
	 */
	public <T> T exec(Request request, ResponseDecoder<T> decoder,
			StorageServerInfo storage) {
		return executor.exec(request, decoder, storage.getAddress());
	}
}
