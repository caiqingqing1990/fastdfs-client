/**
 * 
 */
package cn.strong.fastdfs.request;

import io.netty.buffer.ByteBuf;

/**
 * Fastdfs 请求
 * 
 * @author liulongbiao
 *
 */
public interface Request {

	/**
	 * 命令编码
	 * 
	 * @return
	 */
	byte cmd();

	/**
	 * body 长度
	 * 
	 * @return
	 */
	long length();

	/**
	 * 将 body 写到 ByteBuf 中
	 * 
	 * @param buf
	 */
	void writeBody(ByteBuf buf);
}
