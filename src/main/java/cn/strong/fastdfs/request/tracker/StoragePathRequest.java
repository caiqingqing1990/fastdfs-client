/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 发送内容为存储路径的请求
 * 
 * @author liulongbiao
 *
 */
public class StoragePathRequest extends AbstractStoragePathRequest {

	private final byte cmd;

	public StoragePathRequest(StoragePath spath, byte cmd) {
		super(spath);
		this.cmd = cmd;
	}

	@Override
	public byte cmd() {
		return cmd;
	}

}
