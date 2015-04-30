/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.exception.FastdfsConnectionException;
import cn.strong.fastdfs.exception.FastdfsException;
import cn.strong.fastdfs.exception.FastdfsResponseException;
import cn.strong.fastdfs.request.Request;
import cn.strong.fastdfs.response.ResponseDecoder;

/**
 * Fastdfs 执行器
 * 
 * @author liulongbiao
 *
 */
public class FastdfsExecutor {

	private static Logger logger = LoggerFactory
			.getLogger(FastdfsExecutor.class);

	private EventLoopGroup group;
	private KeyedObjectPool<InetSocketAddress, Channel> pool;
	private GenericKeyedObjectPoolConfig poolConfig;

	public GenericKeyedObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericKeyedObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	@PostConstruct
	public void init() {
		if (poolConfig == null) {
			poolConfig = new GenericKeyedObjectPoolConfig();
		}
		group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class)
				.group(group).handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline()
								.addLast(new LengthFieldBasedFrameDecoder(2647046, 0, 8, 2, 0))
								.addLast(new FastdfsClientHandler());
					}
				});
		PooledChannelFactory factory = new PooledChannelFactory(bootstrap);
		pool = new GenericKeyedObjectPool<InetSocketAddress, Channel>(factory,
				poolConfig);
	}

	@PreDestroy
	public void shutdown() {
		if (pool != null) {
			pool.close();
		}
		if (group != null) {
			group.shutdownGracefully();
		}
	}

	/**
	 * 执行命令
	 * 
	 * @param command
	 * @param addr
	 * @return
	 */
	public <V> V exec(Request request, ResponseDecoder<V> decoder,
			InetSocketAddress addr) {
		Helpers.requireNonNull(request);
		Helpers.requireNonNull(decoder);
		Channel channel = null;
		try {
			channel = connect(addr);
			FastdfsClientHandler handler = channel.pipeline().get(
					FastdfsClientHandler.class);
			return handler.exec(request, decoder);
		} catch (Exception e) {
			// if exception is not FastdfsResponseException
			// then it should not return to pool
			if (!(e instanceof FastdfsResponseException)) {
				invalidateChannel(addr, channel);
				channel = null;
			}
			throw wrapAsRuntimeException(e);
		} finally {
			returnChannel(addr, channel);
		}
	}

	private void invalidateChannel(InetSocketAddress addr, Channel channel) {
		try {
			if (channel != null) {
				pool.invalidateObject(addr, channel);
			}
		} catch (Exception e1) {
			logger.warn(e1.getMessage(), e1);
		}
	}

	private void returnChannel(InetSocketAddress addr, Channel channel) {
		try {
			if (channel != null) {
				pool.returnObject(addr, channel);
			}
		} catch (Exception e) {
			logger.warn("return channel error.", e);
		}
	}

	private Channel connect(InetSocketAddress addr) {
		try {
			return pool.borrowObject(addr);
		} catch (Exception e) {
			throw new FastdfsConnectionException(e);
		}
	}

	private RuntimeException wrapAsRuntimeException(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new FastdfsException(e);
		}
	}
}
