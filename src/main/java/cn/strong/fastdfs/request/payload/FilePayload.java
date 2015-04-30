/**
 * 
 */
package cn.strong.fastdfs.request.payload;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.strong.fastdfs.core.Helpers;
import cn.strong.fastdfs.exception.FastdfsException;

/**
 * @author liulongbiao
 *
 */
public class FilePayload implements Payload {

	public final File file;

	public FilePayload(File file) {
		if (file == null || !file.exists()) {
			throw new FastdfsException("file doesnot exist.");
		}
		this.file = file;
	}

	@Override
	public long length() {
		return file.length();
	}

	@Override
	public void write(ByteBuf buf) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			buf.writeBytes(in, (int) length());
		} catch (IOException e) {
			throw new FastdfsException("write file payload error : "
					+ e.getMessage(), e);
		} finally {
			Helpers.closeQuietly(in);
		}
	}
}
