/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Helpers.requireNonNull;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;

/**
 * 发送内容为存储路径的请求
 * 
 * @author liulongbiao
 *
 */
public class StoragePathRequest implements Request {

	private final StoragePath spath;
	private final byte cmd;

	public StoragePathRequest(StoragePath spath, byte cmd) {
		this.spath = requireNonNull(spath);
		this.cmd = cmd;
	}

	@Override
	public byte cmd() {
		return cmd;
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
