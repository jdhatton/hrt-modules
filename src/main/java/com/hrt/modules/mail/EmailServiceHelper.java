package com.hrt.modules.mail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.hrt.data.db.beans.User;

@Component
public class EmailServiceHelper {

    @Autowired
    private  JavaMailSender  javaMailService;
    
    private VelocityEngine velocityEngine;

    public void setMailSender(JavaMailSender mailSender) {
    	javaMailService = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void register(User user) {

        sendConfirmationEmail(user);
    }

    private void sendConfirmationEmail(final User user) {
//        MimeMessagePreparator preparator = new MimeMessagePreparator() {
//            public void prepare(MimeMessage mimeMessage) throws Exception {
//                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//                message.setTo(user.getEmail());
//                message.setFrom("homeroomtechnologies@gmail.com"); // could be parameterized...
//                Map model = new HashMap();
//                model.put("user", user);
//                String text = VelocityEngineUtils.mergeTemplateIntoString(
//                        velocityEngine, "inviteTemplate.vm", model);
//                message.setText(text, true);
//            }
//        };
//        javaMailService.send(preparator);
    	
    	SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Registration");
        mailMessage.setText("Hello " +user.getFirstName() +"\n Your registration is successfull");
        javaMailService.send(mailMessage);
    }

}