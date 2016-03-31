package org.bk.consumer.service;

import org.bk.consumer.domain.Message;
import org.bk.consumer.domain.MessageAcknowledgement;
import org.bk.consumer.feign.PongClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ysdzz.ds.commons.util.Answer;

@Service("hystrixPongClient")
public class HystrixWrappedPongClient implements PongClient {

    @Autowired
    @Qualifier("pongClient")
    private PongClient feignPongClient;

    @Override
    @HystrixCommand(groupKey = "pongGroup", fallbackMethod = "fallBackCall")
    public MessageAcknowledgement sendMessage(Message message) {
        return this.feignPongClient.sendMessage(message);
    }

    public MessageAcknowledgement fallBackCall(Message message) {
        MessageAcknowledgement fallback = new MessageAcknowledgement(message.getId(), message.getPayload(), "FAILED SERVICE CALL! - FALLING BACK");
        return fallback;
    }
    
    public Answer submitGoodsFallBackCall( String goods) {
    	Answer fallback = new Answer("1000",goods+ "FAILED SERVICE CALL! - FALLING BACK");
    	return fallback;
    }
    
    public MessageAcknowledgement snatchFallBackCall(String message) {
    	MessageAcknowledgement fallback = new MessageAcknowledgement("1", "2", "FAILED SERVICE CALL! - FALLING BACK");
    	return fallback;
    }

	@Override
	@HystrixCommand(groupKey = "pongGroup", fallbackMethod = "snatchFallBackCall")
	public MessageAcknowledgement snatch(String msg) {
		return this.feignPongClient.snatch(msg);
	}

	@Override
	@HystrixCommand(groupKey = "pongGroup", fallbackMethod = "submitGoodsFallBackCall")
	public Answer submitGoods(String goods) {
    	return this.feignPongClient.submitGoods(goods);
    }
}
