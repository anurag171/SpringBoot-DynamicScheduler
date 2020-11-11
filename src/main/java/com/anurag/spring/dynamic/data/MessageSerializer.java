package com.anurag.spring.dynamic.data;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.core.serializer.Serializer;

public class MessageSerializer implements Serializer<Message> {

	@Override
	public void serialize(Message object, OutputStream outputStream) throws IOException {
		
		
	}

}
