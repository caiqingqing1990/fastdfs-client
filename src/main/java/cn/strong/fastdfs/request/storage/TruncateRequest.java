/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;

/**
 * 截取文件请求
 * 
 * @author liulongbiao
 *
 */
public class TruncateRequest implements Request {

	public final StoragePath spath;
	public final int truncatedSize;

	public TruncateRequest(StoragePath spath, int truncatedSize) {
		this.spath = spath;
		this.truncatedSize = truncatedSize;
	}

	public long length() {
		return 2 * FDFS_PROTO_PKG_LEN_SIZE + spath.path.getBytes(UTF_8).length;
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_TRUNCATE_FILE;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(truncatedSize);
		buf.writeBytes(pathBytes);
	}
}
