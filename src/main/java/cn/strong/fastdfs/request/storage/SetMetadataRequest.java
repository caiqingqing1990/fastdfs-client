/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_LONG_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_RECORD_SEPERATOR;
import static cn.strong.fastdfs.core.Helpers.requireNonNull;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;

import java.util.Map;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Request;

/**
 * 设置文件属性请求
 * 
 * @author liulongbiao
 *
 */
public class SetMetadataRequest implements Request {

	private static byte[] toBytes(Map<String, String> metadata) {
		if (metadata == null || metadata.isEmpty()) {
			return new byte[0];
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : metadata.entrySet()) {
			if (!first) {
				sb.append(FDFS_RECORD_SEPERATOR);
			}
			sb.append(entry.getKey());
			sb.append(FDFS_FIELD_SEPERATOR);
			sb.append(entry.getValue());
			first = false;
		}
		return sb.toString().getBytes(UTF_8);
	}

	public final StoragePath spath;
	public final byte[] metadatas;
	public final byte flag;

	public SetMetadataRequest(StoragePath spath,
			Map<String, String> metadata, byte flag) {
		this(spath, toBytes(metadata), flag);
	}

	public SetMetadataRequest(StoragePath spath, byte[] metadatas, byte flag) {
		this.spath = requireNonNull(spath);
		this.metadatas = metadatas;
		this.flag = flag;
	}

	@Override
	public long length() {
		return 2 * FDFS_LONG_LEN + 1 + FDFS_GROUP_LEN
				+ spath.path.getBytes(UTF_8).length
				+ metadatas.length;
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_SET_METADATA;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(metadatas.length);
		buf.writeByte(flag);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		buf.writeBytes(pathBytes);
		buf.writeBytes(metadatas);
	}
}
