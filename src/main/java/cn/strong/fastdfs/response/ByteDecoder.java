/**
 * 
 */
package cn.strong.fastdfs.response;

import io.netty.buffer.ByteBuf;

/**
 * 字节数组解码器
 * 
 * @author liulongbiao
 *
 */
public enum ByteDecoder implements ResponseDecoder<byte[]> {
	INSTANCE;

	@Override
	public long expectLength() {
		return -1;
	}

	@Override
	public byte[] decode(ByteBuf buf) {
		int length = buf.readableBytes();
		byte[] result = new byte[length];
		buf.readBytes(result);
		return result;
	}

}
