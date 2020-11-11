package com.anurag.spring.dynamic.data;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.serializer.Deserializer;

public class MessageDeserializer implements Deserializer<Message> {

	@Override
	public Message deserialize(InputStream inputStream) throws IOException {
		
		return null;
	}

}
