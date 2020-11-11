package com.anurag.spring.dynamic.jms.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.anurag.spring.dynamic.data.Message;

@Component
public class MessageReceiver {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageReceiver.class);	
	
	
	@Autowired
	Environment env;	
	
	@Autowired
	ApplicationContext context;
	
	
	@JmsListener(destination = "jms.message.endpoint")
    public void receiveMessage(Message msg) 
    {
		logger.info(msg.toString());
		msg.getCountry();
    }
}
