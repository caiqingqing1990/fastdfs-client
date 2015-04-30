/**
 * 
 */
package cn.strong.fastdfs.core;

import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 帮助类
 * 
 * @author liulongbiao
 *
 */
public class Helpers {

	public static <T> T requireNonNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
		return obj;
	}

	public static <T> T requireNonNull(T obj, String message) {
		if (obj == null)
			throw new NullPointerException(message);
		return obj;
	}

	/**
	 * 关闭通道
	 * 
	 * @param channel
	 */
	public static void close(Channel channel) {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close().sync();
			} catch (InterruptedException e) {
				// nothing to do
			}
		}
	}

	/**
	 * 关闭
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * 给 ByteBuf 写入定长字符串
	 * <p>
	 * 若字符串长度大于定长，则截取定长字节；若小于定长，则补零
	 * 
	 * @param buf
	 * @param content
	 * @param length
	 */
	public static void writeFixLength(ByteBuf buf, String content, int length) {
		byte[] bytes = content.getBytes(CharsetUtil.UTF_8);
		int blen = bytes.length;
		int wlen = blen > length ? length : blen;
		buf.writeBytes(bytes, 0, wlen);
		if (wlen < length) {
			buf.writeZero(length - wlen);
		}
	}

	/**
	 * 读取固定长度的字符串(修剪掉补零的字节)
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static String readString(ByteBuf in, int length) {
		return in.readBytes(length).toString(UTF_8).trim();
	}

	/**
	 * 读取字符串(修剪掉补零的字节)
	 * 
	 * @param in
	 * @return
	 */
	public static String readString(ByteBuf in) {
		return in.toString(UTF_8);
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isEmpty(String content) {
		return content == null || content.isEmpty();
	}

	/**
	 * 判断列表是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmpty(List<T> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 获取列表头元素
	 * 
	 * @param list
	 * @return
	 */
	public static <T> T first(List<T> list) {
		return isEmpty(list) ? null : list.get(0);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileExt(String filename) {
		if (filename == null) {
			return "";
		}
		int idx = filename.lastIndexOf('.');
		return idx == -1 ? "" : filename.substring(idx + 1).toLowerCase();
	}
}
