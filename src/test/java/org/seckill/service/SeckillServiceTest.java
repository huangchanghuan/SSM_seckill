package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() {
		List<Seckill> list=seckillService.getSeckillList();
		logger.info("list={}",list);
	}
//
	@Test
	public void testGetById() {
		long id=1000;
		Seckill seckill=seckillService.getById(id);
//		logger.info("seckill={}",seckill);
	}
	
	/**
	 * 秒杀测试，所有结果都可以测试。
	 * @throws InterruptedException 
	 */
	@Test
	public void testSeckillLogic() throws InterruptedException {
		
		
		Thread t1=new Thread(){
			public void run() {
				//通过id获取秒杀商品的信息
				long id=1000;
				Exposer exposer=seckillService.exportSeckillUrl(id);
				if(exposer.isExposed()){//秒杀开启时候
					long userPhone=92182657254L;
					String md5=exposer.getMd5();
					try {
						SeckillExecution seckillExecution=seckillService.executeSeckill(id, userPhone, md5);
						logger.info("seckillExecution={}",seckillExecution);
						}catch(SeckillCloseException e1){
							logger.info(e1.getMessage());
						}catch(RepeatKillException e2){
							logger.info(e2.getMessage());
						} catch (SeckillException e) {
							logger.error(e.getMessage());
						}
				}else{
					//秒杀未开启时候
					logger.warn("exposer={}",exposer);
				}
			}
		};
		
		Thread t2=new Thread(){
			public void run() {
				//通过id获取秒杀商品的信息
				long id=1000;
				Exposer exposer=seckillService.exportSeckillUrl(id);
				if(exposer.isExposed()){//秒杀开启时候
					long userPhone=91186657254L;
					String md5=exposer.getMd5();
					try {
						SeckillExecution seckillExecution=seckillService.executeSeckill1(id, userPhone, md5);
						logger.info("seckillExecution={}",seckillExecution);
						}catch(SeckillCloseException e1){
							logger.info(e1.getMessage());
						}catch(RepeatKillException e2){
							logger.info(e2.getMessage());
						} catch (SeckillException e) {
							logger.error(e.getMessage());
						}
				}else{
					//秒杀未开启时候
					logger.warn("exposer={}",exposer);
				}
			}
		};
		t1.start();
		t2.start();
		Thread.sleep(100000);
		
	}

}
