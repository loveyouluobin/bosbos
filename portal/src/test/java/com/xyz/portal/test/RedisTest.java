package com.xyz.portal.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyz.bos.utils.MailUtils;

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration("classpath:applicationContext.xml")
	public class RedisTest {
		@Autowired//注入redisTemplate
		private RedisTemplate<String, String> redisTemplate;
		
		
				@Test
		public void test1() {
			 redisTemplate.opsForValue().set("name", "小小");// 存储数据
			 String string = redisTemplate.opsForValue().get("name");//取数据
			 System.out.println(string);
			// 存储数据并设置有效期
			// 第三个参数 : 时间值
			// 第四个参数 : 时间单位
			// 1000 * 60 * 60
			// redisTemplate.opsForValue().set("age", "11", 10, TimeUnit.SECONDS);
			// 删除数据
			//redisTemplate.delete("name");
		}
				@Test
				public void test2() {
					MailUtils.sendMail("bb@store.com", "标题", "内容");
				}
				
				
	}


