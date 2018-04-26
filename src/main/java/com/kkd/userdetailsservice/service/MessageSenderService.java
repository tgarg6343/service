package com.kkd.userdetailsservice.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kkd.userdetailsservice.UserDetailsServiceApplication;

@Service
public class MessageSenderService {

	//autowired a template of rabbitmq to send a message
	@Autowired
	private AmqpTemplate amqpTemplate;
			
	public void produceMsg(String msg){
		//using the template defining the needed parameters- exchange name,key and message
		amqpTemplate.convertAndSend(UserDetailsServiceApplication.EXCHANGE_NAME, UserDetailsServiceApplication.ROUTING_KEY, msg);
		System.out.println("Send msg = " + msg);
	}
}