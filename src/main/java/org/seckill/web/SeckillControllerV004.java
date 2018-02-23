package org.seckill.web;

import org.seckill.dto.SeckillResult;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 
 * @author Peter  2016-9-3下午5:02:48
 *
 */
@Controller
@RequestMapping("/seckill")//url:/项目/模块/资源/{id}/细分  seckill/list
public class SeckillControllerV004 {
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/time/now" ,method={RequestMethod.POST,RequestMethod.DELETE,RequestMethod.GET})
	@ResponseBody
	public SeckillResult<Long> time(){
		System.out.println("版本:001");
		Date now=new Date();
		return new SeckillResult<Long>(true,now.getTime());
	}
}
