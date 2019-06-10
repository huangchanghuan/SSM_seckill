package org.seckill.web;

import org.seckill.dto.SeckillResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Peter  2016-9-3下午5:02:48
 *
 */
@Controller
@RequestMapping("/{version}")//url:/项目/模块/资源/{id}/细分  seckill/list
public class SeckillController {


	@RequestMapping("hello")
	@ResponseBody
	public SeckillResult hello1(){
		return new SeckillResult<StringBuffer>(true,"版本1的hello");
	}

	@RequestMapping("hello2")
	@ResponseBody
	public SeckillResult hello2(){
		return new SeckillResult<StringBuffer>(true,"版本4的hello");
	}
}
