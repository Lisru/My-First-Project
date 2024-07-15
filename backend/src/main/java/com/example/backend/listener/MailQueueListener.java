package com.example.backend.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String,Object> data){
        String email = (String) data.get("email");
        Integer code = (Integer) data.get("code");
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type){
            case "register" ->
                createMessage("欢迎注册我们网站",
                        "您的验证码为："+code+",有效时间3分钟",
                        email);
            case "reset" -> createMessage("您的密码重制邮件",
                    "您正在重置密码，验证码："+code+"，有效时间3分钟",email);
            default -> null;
        };
        if(message == null) return;
        sender.send(message);

    }

    private SimpleMailMessage createMessage(String title,String content,String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);  //标题
        message.setText(content);  //内容
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
