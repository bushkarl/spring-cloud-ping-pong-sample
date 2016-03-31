package org.bk.producer.controller;


import org.bk.producer.domain.Message;
import org.bk.producer.domain.MessageAcknowledgement;
import org.bk.producer.service.ElasticSearchSnatchService;
import org.bk.producer.service.KafkaSnatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.BaseEncoding;
import com.ysdzz.ds.commons.util.Answer;

@RestController
public class PongController {

    @Value("${reply.message}")
    private String message;

    @Autowired
    private KafkaSnatchService kafkaSnatchService;

    @Autowired
    private ElasticSearchSnatchService elasticSearchSnatchService;
    
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Resource<MessageAcknowledgement> pongMessage(@RequestBody Message input) {
        return new Resource<>(
                new MessageAcknowledgement(input.getId(), input.getPayload(), message));
    }

    @RequestMapping(value = "/snatch", method = RequestMethod.GET)
    public Resource<MessageAcknowledgement> snatchMessage(@RequestParam(defaultValue="0", value="msg") String msg) {
//    	Goods goods = new Goods();
//    	goods.setContent("lll.sfe2328909899）*）（*）*&*……￥#！@#￥%……&*（）你好");
//    	kafkaSnatchService.sendGoods(goods);
    	return new Resource<>(new MessageAcknowledgement("1","2",msg+">>>"+message));
    }

    @RequestMapping(value = "/submitGoods", method = RequestMethod.POST)
    public Resource<Answer> submitGoods(@RequestParam(required=false,value="goods",defaultValue="") String goods) {
//    	Gson gson = new GsonBuilder()  
//    			  .setDateFormat("yyyyMMddHHmmss")  
//    			  .create();
//    	Goods g = gson.fromJson(goods, Goods.class);
    	try {
			kafkaSnatchService.sendGoods(new String( BaseEncoding.base64().decode(goods)));
		} catch (Exception e) {
			e.printStackTrace();
		}
//    	try {
//    		elasticSearchSnatchService.sendGoods(new String(BaseEncoding.base64().decode(goods)));
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}
    	return new Resource<>(new Answer());
    }


}
