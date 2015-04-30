/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 通道池工厂
 * 
 * @author liulongbiao
 *
 */
public class PooledChannelFactory extends
		BaseKeyedPooledObjectFactory<InetSocketAddress, Channel> {

	private Bootstrap bootstrap;

	public PooledChannelFactory(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public Channel create(InetSocketAddress key) throws Exception {
		ChannelFuture f = bootstrap.connect(key).sync();
		return f.channel();
	}

	@Override
	public PooledObject<Channel> wrap(Channel value) {
		return new DefaultPooledObject<Channel>(value);
	}

	@Override
	public void destroyObject(InetSocketAddress key, PooledObject<Channel> p)
			throws Exception {
		Helpers.close(p.getObject());
	}

	@Override
	public boolean validateObject(InetSocketAddress key, PooledObject<Channel> p) {
		Channel channel = p.getObject();
		return channel.isActive();
	}
}
