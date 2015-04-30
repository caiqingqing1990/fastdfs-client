/**
 * 
 */
package cn.strong.fastdfs.exception;

/**
 * Fastdfs 异常
 * 
 * @author liulongbiao
 *
 */
public class FastdfsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6957383397699283454L;

	public FastdfsException() {
		super();
	}

	public FastdfsException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastdfsException(String message) {
		super(message);
	}

	public FastdfsException(Throwable cause) {
		super(cause);
	}

}
