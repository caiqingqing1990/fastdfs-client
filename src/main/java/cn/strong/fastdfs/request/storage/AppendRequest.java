/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.core.Consts;
import cn.strong.fastdfs.core.Helpers;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.request.payload.Payload;

/**
 * 追加文件请求
 * 
 * @author liulongbiao
 *
 */
public class AppendRequest implements Request {
	public final StoragePath spath;
	public final Payload payload;

	public AppendRequest(StoragePath spath, Payload payload) {
		this.spath = Helpers.requireNonNull(spath);
		this.payload = Helpers.requireNonNull(payload);
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_APPEND_FILE;
	}

	@Override
	public long length() {
		return 2 * Consts.FDFS_PROTO_PKG_LEN_SIZE
				+ spath.path.getBytes(UTF_8).length + payload.length();
	}

	@Override
	public void writeBody(ByteBuf buf) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(payload.length());
		buf.writeBytes(pathBytes);
		payload.write(buf);
	}
}
