package com.pooq.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.pooq.project.server.NettyServer;

@SpringBootApplication
public class PooqApplication {


	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(PooqApplication.class, args);
		
		RedisSubscriber subscriber = ctx.getBean(RedisSubscriber.class);
		subscriber.start();

		NettyServer netty = ctx.getBean(NettyServer.class);
		netty.start();
		
		subscriber.close();
	}
}
