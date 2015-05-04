/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Helpers.readString;
import io.netty.buffer.ByteBuf;

import cn.strong.fastdfs.exception.FastdfsException;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 存储路径解码器
 * 
 * @author liulongbiao
 *
 */
public enum StoragePathDecoder implements ResponseDecoder<StoragePath> {

	INSTANCE;

	@Override
	public long expectLength() {
		return -1;
	}

	@Override
	public StoragePath decode(ByteBuf in) {
		int length = in.readableBytes();
		if (length <= FDFS_GROUP_LEN) {
			throw new FastdfsException("body length : " + length
					+ ", is lte required group name length 16.");
		}
		String group = readString(in, FDFS_GROUP_LEN);
		String path = readString(in);
		return new StoragePath(group, path);
	}

}
