/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Helpers.isEmpty;
import static cn.strong.fastdfs.core.Helpers.writeFixLength;
import io.netty.buffer.ByteBuf;

import cn.strong.fastdfs.request.Request;

/**
 * 获取可上传存储服务器请求
 * 
 * @author liulongbiao
 *
 */
public class GetUploadStorageRequest implements Request {

	private final String group;

	public GetUploadStorageRequest(String group) {
		this.group = group;
	}

	@Override
	public byte cmd() {
		return isEmpty(group) ? TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE
				: TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
	}

	@Override
	public long length() {
		return isEmpty(group) ? 0 : FDFS_GROUP_LEN;
	}

	@Override
	public void writeBody(ByteBuf buf) {
		if (!isEmpty(group)) {
			writeFixLength(buf, group, FDFS_GROUP_LEN);
		}
	}

}
