package com.xyz.bos_sms.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

@Component
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message msg) {
		try {
			MapMessage mapMessage=(MapMessage) msg;//获得消息列队
			String tel = mapMessage.getString("tel");//拿到列队中手机号
			String code = mapMessage.getString("code");//验证码
			System.out.println(tel+code);
			//SmsUtils.sendSms(tel,code); 发送短信工具类
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
