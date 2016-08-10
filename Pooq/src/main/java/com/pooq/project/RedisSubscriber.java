package com.pooq.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pooq.project.server.repository.ChannelRepository;

import io.netty.channel.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Component
public class RedisSubscriber {
	@Autowired
	private Jedis jedis;
	@Autowired
    private ChannelRepository channelRepository;
	
	private final JedisPubSub jedisPubSub = new JedisPubSub() {
		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
		}
		
		@Override
		public void onMessage(String channel, String message) {
			
			String roomNumber = "1";
			for(Channel c : channelRepository.getChannels(roomNumber)){
				if(!c.isActive()){
					channelRepository.removeChannel(roomNumber, c);
					continue;
				}
				c.writeAndFlush(message + System.lineSeparator());
			}
		}
	};
	public void start() {
		new Thread(()->{
			try {
				jedis.subscribe(jedisPubSub, "chat");
				jedis.quit();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}, "subscriberThread").start();
	}	
	public void close() {
		jedisPubSub.unsubscribe();
	}
}
