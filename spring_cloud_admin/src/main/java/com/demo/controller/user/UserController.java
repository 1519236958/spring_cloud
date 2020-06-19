package com.demo.controller.user;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心控制层
 * @Author: sutao
 * @Date: 2020/6/19
 * @Description:
 */
@RestController
public class UserController {

	@PostMapping("user/login")
	public JSONObject login(){
		JSONObject json = new JSONObject();
		json.put("code","0");
		json.put("msg","成功");
		return json;
	}
}