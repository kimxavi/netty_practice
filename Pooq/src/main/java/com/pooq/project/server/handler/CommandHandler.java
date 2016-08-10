package com.pooq.project.server.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pooq.project.server.repository.ChannelRepository;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
@ChannelHandler.Sharable
public class CommandHandler extends ChannelInboundHandlerAdapter {
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
    private ChannelRepository channelRepository;
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		String roomNumber = "1";
		
		if(channelRepository.getChannels(roomNumber).contains(channel)){
			System.out.println("already");
		}else{
			channelRepository.putChannels(roomNumber, channel);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		String json = (String)msg;
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
			String method = (String)map.get("method");
			String message = (String)map.get("message");
			
			switch(method){
			case "send":
				redisTemplate.convertAndSend("chat", message);
				break;
			default :
			}
			
		}catch(Exception e){
			//e.printStackTrace();
			return ;
		}
		
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
        ctx.close();
	}

}
