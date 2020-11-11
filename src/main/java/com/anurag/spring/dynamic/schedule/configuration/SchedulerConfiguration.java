package com.anurag.spring.dynamic.schedule.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.anurag.spring.dynamic.data.Message;
import com.anurag.spring.dynamic.schedule.db.CountryProductCutOff;
import com.anurag.spring.dynamic.schedule.repository.SchedulerRepo;
import com.anurag.spring.dynamic.schedule.runnable.Job;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


@Configuration
@EnableScheduling
@EnableKafka
public class SchedulerConfiguration implements SchedulingConfigurer {
	
	@Autowired
	private SchedulerRepo schedulerRepo;
	
	@Value(value = "classpath:/preload.json")
	String preloadFilePath;

	@Autowired
	ResourceLoader resourceLoader;	

	@Autowired
	ApplicationContext context;
	
	ScheduledTaskRegistrar scheduledTaskRegistrar;    

		
	@PostConstruct
	void init() {

		System.out.println("init");
		StringBuilder contentBuilder = new StringBuilder();
		Map<String,CountryProductCutOff> dataMap = context.getBean("cutOffBean",Map.class);
		List<CountryProductCutOff>	 entities = schedulerRepo.findAll();
		File resource=null;
		if(entities.isEmpty()) {
			try {
				resource = resourceLoader.getResource(preloadFilePath).getFile();
			} catch (IOException e1) {			
				e1.printStackTrace();
			}			    
			try(Stream<String> stream = Files.lines(Paths.get(resource.getPath()),StandardCharsets.UTF_8)){
				stream.forEach(s -> contentBuilder.append(s).append("\n"));
				Gson gson = new Gson();
				entities =  gson.fromJson(contentBuilder.toString(), new TypeToken<List<CountryProductCutOff>>(){}.getType());
				schedulerRepo.saveAll(entities);						
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}		
		for(CountryProductCutOff data :entities) {
			dataMap.put(data.getCountry(), data);
		}	
	}
	
	@Bean
	public Map<String,CountryProductCutOff> cutOffBean() {
		Map<String,CountryProductCutOff> cutOffBeanData = new HashMap<>();
		return cutOffBeanData;
	}	
   
    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		if (scheduledTaskRegistrar == null) {
        scheduledTaskRegistrar = taskRegistrar;
	}
    if (taskRegistrar.getScheduler() == null) {
        taskRegistrar.setScheduler(poolScheduler());
    }
    @SuppressWarnings("unchecked")
	Map<String,CountryProductCutOff> cutOffBean = (Map<String, CountryProductCutOff>) context.getBean("cutOffBean");
    
    cutOffBean.values().forEach(bean->{
    	CronTrigger cronTrigger = new CronTrigger(bean.getSchedulerExpression(),TimeZone.getTimeZone(bean.getTimeZone()));
    	Job job = new Job(bean);
    	CronTask task = new CronTask(job, cronTrigger);
    	scheduledTaskRegistrar.addCronTask(task);
    });   


    
    
	}
	
	
	@Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Message> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(Message.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    
    @Bean
	public JmsListenerContainerFactory<?> myFactory(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) 
	{
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		//factory.setConcurrency("1-1");
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}


}
