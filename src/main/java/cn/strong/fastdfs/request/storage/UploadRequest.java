/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.FDFS_FILE_EXT_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static cn.strong.fastdfs.core.Consts.FDFS_STORE_PATH_INDEX_LEN;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.request.payload.Payload;

/**
 * 上传请求
 * 
 * @author liulongbiao
 *
 */
public class UploadRequest implements Request {

	public final Payload payload;
	public final String ext;
	public final byte storePathIndex;

	public UploadRequest(Payload payload, String ext, byte storePathIndex) {
		this.payload = Objects.requireNonNull(payload);
		this.ext = ext;
		this.storePathIndex = storePathIndex;
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_UPLOAD_FILE;
	}

	@Override
	public long length() {
		return FDFS_STORE_PATH_INDEX_LEN + FDFS_PROTO_PKG_LEN_SIZE
				+ FDFS_FILE_EXT_LEN + payload.length();
	}

	@Override
	public void writeBody(ByteBuf buf) {
		buf.writeByte(storePathIndex);
		buf.writeLong(payload.length());
		writeFixLength(buf, ext, FDFS_FILE_EXT_LEN);
		payload.write(buf);
	}

}
