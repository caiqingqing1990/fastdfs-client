/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;

/**
 * 删除文件请求
 * 
 * @author liulongbiao
 *
 */
public class DeleteRequest implements Request {

	public final StoragePath spath;

	public DeleteRequest(StoragePath spath) {
		this.spath = spath;
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_DELETE_FILE;
	}

	@Override
	public long length() {
		return FDFS_GROUP_LEN + spath.path.getBytes(UTF_8).length;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		ByteBufUtil.writeUtf8(buf, spath.path);
	}
	
}
