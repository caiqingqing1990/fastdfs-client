/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.core.CommandCodes.FDFS_PROTO_CMD_RESP;
import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.exception.FastdfsResponseException;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.response.ResponseDecoder;

/**
 * FastDFS 客户端处理器
 * 
 * @author liulongbiao
 *
 */
public class FastdfsClientHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory
			.getLogger(FastdfsClientHandler.class);

	private volatile Channel channel;
	private final BlockingQueue<ByteBuf> responses = new LinkedBlockingQueue<ByteBuf>();

	/**
	 * 执行请求，获取响应结果
	 * 
	 * @param request
	 *            请求
	 * @param decoder
	 *            响应解码器
	 * @return 响应结果
	 */
	public <V> V exec(Request request, ResponseDecoder<V> decoder) {
		Helpers.requireNonNull(request);
		Helpers.requireNonNull(decoder);
		send(request);
		ByteBuf response = waitForResponse();
		return decode(response, decoder);
	}

	/*
	 * 发送请求
	 */
	private void send(Request request) {
		long length = request.length();
		ByteBuf buf = channel.alloc().buffer((int) length + 10);
		buf.writeLong(length);
		buf.writeByte(request.cmd());
		buf.writeByte(ERRNO_OK);
		request.writeBody(buf);
		channel.writeAndFlush(buf);
	}

	/*
	 * 等待响应
	 */
	private ByteBuf waitForResponse() {
		ByteBuf response = null;
		boolean interrupted = false;
		for (;;) {
			try {
				response = responses.take();
				break;
			} catch (InterruptedException ignore) {
				interrupted = true;
			}
		}
		if (interrupted) {
			Thread.currentThread().interrupt();
		}
		return response;
	}

	/*
	 * 解码响应
	 */
	private <V> V decode(ByteBuf out, ResponseDecoder<V> decoder) {
		try {
			long length = out.readLong();
			byte cmd = out.readByte();
			byte errno = out.readByte();
			if (errno != 0) {
				throw new FastdfsResponseException(
						"Fastdfs responsed with an error, errno is " + errno);
			}
			if (cmd != FDFS_PROTO_CMD_RESP) {
				throw new FastdfsResponseException(
						"Expect response command code error : " + cmd);
			}
			long expectLength = decoder.expectLength();
			if (expectLength >= 0) {
				if (length != expectLength) {
					throw new FastdfsResponseException(
							"Expect response length : " + expectLength
									+ " , but receive length : " + length);
				}
			}

			try {
				return decoder.decode(out);
			} catch (Exception e) {
				throw new FastdfsResponseException("response decode error", e);
			}
		} finally {
			out.release();
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		logger.info("new channel registered.");
		channel = ctx.channel();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		responses.add((ByteBuf) msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage(), cause);
		ctx.close();
	}

}
