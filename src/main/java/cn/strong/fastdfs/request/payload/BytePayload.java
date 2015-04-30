/**
 * 
 */
package cn.strong.fastdfs.request.payload;

import io.netty.buffer.ByteBuf;

/**
 * 字节上传内容
 * 
 * @author liulongbiao
 *
 */
public class BytePayload implements Payload {

	public final byte[] bytes;

	public BytePayload(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public long length() {
		return bytes.length;
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeBytes(bytes);
	}

}
