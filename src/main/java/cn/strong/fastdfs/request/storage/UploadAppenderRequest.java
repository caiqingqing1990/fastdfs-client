/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.request.payload.Payload;

/**
 * 上传 Appender 文件请求
 * 
 * @author liulongbiao
 *
 */
public class UploadAppenderRequest extends UploadRequest {

	public UploadAppenderRequest(Payload payload, String ext,
			byte storePathIndex) {
		super(payload, ext, storePathIndex);
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE;
	}
}
