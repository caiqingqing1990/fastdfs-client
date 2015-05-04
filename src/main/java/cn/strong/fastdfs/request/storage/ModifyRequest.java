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
 * 修改文件内容请求
 * 
 * @author liulongbiao
 *
 */
public class ModifyRequest implements Request {

	public final StoragePath spath;
	public final Payload payload;
	public final int offset;

	public ModifyRequest(StoragePath spath, Payload payload, int offset) {
		this.spath = Helpers.requireNonNull(spath);
		this.payload = Helpers.requireNonNull(payload);
		this.offset = offset;
	}

	@Override
	public long length() {
		return 3 * Consts.FDFS_PROTO_PKG_LEN_SIZE
				+ spath.path.getBytes(UTF_8).length
				+ payload.length();
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_MODIFY_FILE;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(offset);
		buf.writeLong(payload.length());
		buf.writeBytes(pathBytes);
		payload.write(buf);
	}
}
