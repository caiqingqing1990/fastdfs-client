/**
 * 
 */
package cn.strong.fastdfs.exception;

/**
 * Fastdfs 响应异常
 * 
 * @author liulongbiao
 *
 */
public class FastdfsResponseException extends FastdfsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5323316025855985731L;

	public FastdfsResponseException() {
		super();
	}

	public FastdfsResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastdfsResponseException(String message) {
		super(message);
	}

	public FastdfsResponseException(Throwable cause) {
		super(cause);
	}

}
