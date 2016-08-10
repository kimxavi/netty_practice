package com.pooq.project.server.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.netty.channel.Channel;

@Repository
public class ChannelRepository {
	private final Multimap<String,Channel> multimap = ArrayListMultimap.create();
	
	public List<Channel> getChannels(String roomNumber){
		return (List<Channel>)multimap.get(roomNumber);
	}
	public ChannelRepository putChannels(String roomNumber, Channel channel) {
		multimap.put(roomNumber, channel);
		return this;
	}
	public void removeChannel(String roomNumber, Channel channel){
		multimap.remove(roomNumber, channel);
	}
}
