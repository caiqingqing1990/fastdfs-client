/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_LONG_LEN;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.Objects;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;

/**
 * 下载请求
 * 
 * @author liulongbiao
 *
 */
public class DownloadRequest implements Request {

	public static final int DEFAULT_OFFSET = 0;
	public static final int SIZE_UNLIMIT = 0;

	public final StoragePath spath;
	public final int offset;
	public final int size;

	public DownloadRequest(StoragePath spath, int offset, int size) {
		this.spath = Objects.requireNonNull(spath);
		this.offset = offset;
		this.size = size;
	}

	public DownloadRequest(StoragePath spath) {
		this(spath, DEFAULT_OFFSET, SIZE_UNLIMIT);
	}

	@Override
	public long length() {
		return 2 * FDFS_LONG_LEN + FDFS_GROUP_LEN
				+ spath.path.getBytes(UTF_8).length;
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_DOWNLOAD_FILE;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		buf.writeLong(offset);
		buf.writeLong(size);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		ByteBufUtil.writeUtf8(buf, spath.path);
	}
}
