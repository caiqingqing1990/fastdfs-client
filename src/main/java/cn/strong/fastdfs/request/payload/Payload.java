/**
 * 
 */
package cn.strong.fastdfs.request.payload;

import io.netty.buffer.ByteBuf;

/**
 * 上传文件内容
 * 
 * @author liulongbiao
 *
 */
public interface Payload {

	/**
	 * 文件大小
	 * 
	 * @return
	 */
	long length();

	/**
	 * 将文件内容写到 ByteBuf
	 * 
	 * @param buf
	 */
	void write(ByteBuf buf);
}
