package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.entity.UserEntity;
import com.jk.service.UserServiceFeign;
import com.jk.utils.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceFeign userService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    @RequestMapping("/saveOrder")
    @ResponseBody
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object saveOrder(Integer userId, Integer productId, HttpServletRequest request) {
        return userService.saveOrder(userId, productId);
    }

    //注意，方法签名一定要要和api方法一致 自定义降级方法
    public Object saveOrderFail(Integer userId, Integer productId, HttpServletRequest request) {
        HttpSession session=request.getSession();
        System.out.println("controller 保存订单降级方法");

        String sendValue  = (String)redisUtil.get(Constant.SAVE_ORDER_WARNING_KEY);

        String ipAddr = request.getRemoteAddr();

        //新启动一个线程进行业务逻辑处理
        // 开启一个独立线程，进行发送警报，给开发人员，处理问题
        new Thread( ()->{
            if(!StringUtil.isNotEmpty(sendValue)) {
                System.out.println("紧急短信，用户下单失败，请离开查找原因,ip地址是="+ipAddr);

                Map map = null;
                try {
                    map = sendSms("13592070535", session);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //发送一个http请求，调用短信服务 TODO
                // 写发送短信代码，带有参数发送 userId  productId

                redisUtil.set(Constant.SAVE_ORDER_WARNING_KEY, "用户保存订单失败", 60);
            }else{
                System.out.println("已经发送过短信，1分钟内不重复发送");
            }
        }).start();

        // 反馈给用户看的
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", -1);
        map.put("message", "抢购排队人数过多，请您稍后重试。");

        return map;
    }

    public Map sendSms(String phoneNumber, HttpSession session) throws Exception {
        // TODO Auto-generated method stub
        HashMap<Object, Object> map=new HashMap<>();
        if(redisTemplate.hasKey(Constant.SMS_LOCK+phoneNumber)){
            map.put("code", 3);
            map.put("msg", "不能在一分钟内重复获取");
            return map;
        }
        HashMap<String, Object> headers=new HashMap<>();
        headers.put("AppKey", Constant.APP_KEY);
        String nonce = UUID.randomUUID().toString().replace("-", "");
        headers.put("Nonce", nonce);
        String curTime = System.currentTimeMillis()+"";
        headers.put("CurTime", curTime);
        headers.put("CheckSum", CheckSumBuilder.getCheckSum(Constant.APP_SECRET, nonce, curTime));

        HashMap<String, Object> params=new HashMap<>();
        //手机号
        params.put("mobile", phoneNumber);
        params.put("templateid", Constant.TEMPLATEID);
        //随机生成验证码
        int authCode=((int) Math.ceil(Math.random())*899999+100000);
        System.out.println(authCode);
        params.put("authCode", authCode);

        String post = HttpClientUtil.post(Constant.SMS_URL, params, headers);

        JSONObject parseObject = JSON.parseObject(post);
        int code = parseObject.getIntValue("code");
        //判断是否成功
        if(code!=200){
            map.put("code", 1);
            map.put("authCode", authCode);
            map.put("msg", "验证码发送失败");
            return map;
        }
        //将验证码缓存到redis中，有效期5分钟
        redisTemplate.opsForValue().set(Constant.SMS_CODE+phoneNumber, authCode,5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(Constant.SMS_LOCK+phoneNumber, "lock",60,TimeUnit.SECONDS);


        map.put("code", 0);
        map.put("authCode", authCode);
        map.put("msg", "发送成功");
        return map;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(String name) {
        return userService.hello(name);
    }

    @RequestMapping("/selectUserList")
    @ResponseBody
    public List<UserEntity> selectUserList() {

        List<UserEntity> userList = (List<UserEntity>) redisUtil.get(Constant.SELECT_USER_LIST);

        // 1. 有值   2. 没有值
        if(userList == null || userList.size() <= 0 || userList.isEmpty()) {
            // 从数据库查询，存redis
            userList = userService.findUserList();
            redisUtil.set(Constant.SELECT_USER_LIST, userList, 30);
        }

        return userList;

    }


}
