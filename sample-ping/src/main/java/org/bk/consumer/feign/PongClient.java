package org.bk.consumer.feign;

import org.bk.consumer.domain.Message;
import org.bk.consumer.domain.MessageAcknowledgement;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysdzz.ds.commons.util.Answer;

@FeignClient("samplepong")
public interface PongClient {

    @RequestMapping(method = RequestMethod.POST, value = "/message",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    MessageAcknowledgement sendMessage(@RequestBody Message message);
    
    @RequestMapping(method = RequestMethod.GET, value = "/snatch")
    @ResponseBody
    MessageAcknowledgement snatch(@RequestParam(defaultValue="0", value="msg") String msg);
    
    @RequestMapping(value = "/submitGoods", method = RequestMethod.POST)
    @ResponseBody
    public Answer submitGoods(@RequestParam(value="goods",defaultValue="") String goods) ;
}