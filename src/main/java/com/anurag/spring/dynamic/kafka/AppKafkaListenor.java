package com.anurag.spring.dynamic.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import com.anurag.spring.dynamic.data.Message;
import com.anurag.spring.dynamic.schedule.db.CountryProductCutOff;

public class AppKafkaListenor {
	
	private static final Logger LOG = LoggerFactory.getLogger(AppKafkaListenor.class);
	
	@Autowired
	JmsTemplate _jmsTemplate;
	
	@Autowired
	ApplicationContext context;
	
	@KafkaListener(topics = "${app.topic.name}",groupId = "${app.groupid}"  )
	public void receive(@Payload Message data) {
		
		Map<String,CountryProductCutOff> dataMap = context.getBean("cutOffBean",Map.class);		
		CountryProductCutOff obj = dataMap.get(data.getCountry());		
		_jmsTemplate.convertAndSend(obj.getDestinationMq(),data);	
		
	}

}
