/**
 * 
 */
package cn.strong.fastdfs.request;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Helpers.requireNonNull;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import cn.strong.fastdfs.model.StoragePath;

/**
 * 内容为存储路径的抽象请求
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractStoragePathRequest implements Request {

	private final StoragePath spath;

	public AbstractStoragePathRequest(StoragePath spath) {
		this.spath = requireNonNull(spath);
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
