package com.pooq.project.server;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

@Component
public class NettyServer {
	
	@Autowired
	private ServerBootstrap serverBootstrap;
	@Autowired
    private InetSocketAddress port;

    private Channel channel;

	public void start() throws Exception {
        channel =  serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        channel.close();
        channel.parent().close();
    }
}
