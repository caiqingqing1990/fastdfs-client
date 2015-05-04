/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 删除文件请求
 * 
 * @author liulongbiao
 *
 */
public class DeleteRequest extends AbstractStoragePathRequest {

	public DeleteRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_DELETE_FILE;
	}
	
}
