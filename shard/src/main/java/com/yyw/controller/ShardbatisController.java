package com.yyw.controller;

import com.yyw.service.ShardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yyw
 * @date 2019/7/17
 **/
@Controller
@RequestMapping(value = "/Shardbaits")
public class ShardbatisController {
    @Autowired
    private ShardService shardService;

    @ResponseBody
    @RequestMapping(value = "/startShard")
    public String test(@RequestParam(value = "id") String id) {
        return shardService.getName(id);
    }
}
