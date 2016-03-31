package org.bk.producer.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

@Service
public class KafkaSnatchService {

	@Value("${zk.connect}")
    private String zk;
	
	@Value("${zk.broker}")
	private String broker;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendGoods(String goods){
		Producer producer = createProducer(); 
//		Gson gson = new GsonBuilder()  
//	  			  .setDateFormat("yyyyMMddHHmmss")
//	  			  .create();
//		String str = gson.toJson(goods);
		System.out.println("save:"+goods);
		producer.send(new KeyedMessage<String, String>("snatch-jd-ds", goods));
	}
	
	@SuppressWarnings("rawtypes")
	private Producer createProducer() {
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", zk);//声明zk  
        properties.put("serializer.class", StringEncoder.class.getName());  
        properties.put("metadata.broker.list", broker);// 声明kafka broker  
//        properties.put("zookeeper.connect", "localhost:2181");//声明zk  
//        properties.put("serializer.class", StringEncoder.class.getName());  
//        properties.put("metadata.broker.list", "localhost:9092");// 声明kafka broker  
        return new Producer<Integer, String>(new ProducerConfig(properties));  
     }
	
}
