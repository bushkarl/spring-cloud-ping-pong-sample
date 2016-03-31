package org.bk.consumer.controller;

import org.bk.consumer.domain.Message;
import org.bk.consumer.domain.MessageAcknowledgement;
import org.bk.consumer.feign.PongClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysdzz.ds.commons.util.Answer;

import scala.util.Random;

@RestController
public class PingController {

    @Autowired
    @Qualifier("hystrixPongClient")
    private PongClient pongClient;

    @RequestMapping("/dispatch")
    public MessageAcknowledgement sendMessage(@RequestBody Message message) {
        return this.pongClient.sendMessage(message);
    }
    

    @RequestMapping("/snatch")
    public MessageAcknowledgement snatch() {
    	Message message = new Message();
    	message.setPayload("33");
    	return this.pongClient.snatch("wewew!!!");
    }
    

    @RequestMapping(value = "/test" , method = RequestMethod.GET)
    public Message test() {
    	Message message = new Message();
    	message.setPayload("33-"+new Random(100).nextInt());
    	return message;
    }
    
    @RequestMapping(value = "/submitGoods", method = RequestMethod.POST)
    public Answer submitGoods(@RequestParam(value="goods",defaultValue="") String goods) {
//    	Gson gson = new Gson();
//    	Goods g = gson.fromJson(goods, Goods.class);
    	return this.pongClient.submitGoods(goods);
    }
}
