package com.fruit.portal.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

public class MailUtil
{

    /**
     * 发件人邮箱服务器
     */
    private String emailHost;

    /**
     * 发件人邮箱
     */
    private String emailFrom;

    /**
     * 发件人用户名
     */
    private String emailUserName;

    /**
     * 发件人密码
     */
    private String emailPassword;

    private static final MailUtil INSTANCE = new MailUtil("notifications@0ku.com", "notifications@0ku.com", "notifications@0ku.com", "0ku.com666."); //

    private MailUtil(String emailHost, String emailFrom, String emailUserName, String emailPassword)
    {
        Validate.isTrue(StringUtils.isNotBlank(emailHost), "发件人信息不完全，请确认发件人信息！");
        Validate.isTrue(StringUtils.isNotBlank(emailFrom), "发件人信息不完全，请确认发件人信息！");
        Validate.isTrue(StringUtils.isNotBlank(emailUserName), "发件人信息不完全，请确认发件人信息！");
        Validate.isTrue(StringUtils.isNotBlank(emailPassword), "发件人信息不完全，请确认发件人信息！");
        this.emailHost = emailHost;
        this.emailFrom = emailFrom;
        this.emailUserName = emailUserName;
        this.emailPassword = emailPassword;
    }

    public static MailUtil getInstance()
    {
        return INSTANCE;
    }

    /**
     * @param toEmails
     * @param subject
     * @param htmlContent
     * @param pictures
     * @param attachments
     */
    public void sendEmail(String toEmails, String subject, String htmlContent, Map<String, String> pictures, Map<String, String> attachments)
    {
        try
        {
            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            // 设定mail server
            senderImpl.setHost(emailHost);
            // 建立邮件消息
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper messageHelper = null;
            messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            // 设置发件人邮箱
            messageHelper.setFrom(emailFrom);
            // 设置收件人邮箱
            setReceivers(messageHelper, toEmails);
            // 邮件主题
            if (StringUtils.isNotBlank(subject))
            {
                messageHelper.setSubject(subject);
            }
            // true 表示启动HTML格式的邮件
            messageHelper.setText(htmlContent, true);
            // 添加图片
            addPictures(messageHelper, pictures);
            // 添加附件
            addAttachments(messageHelper, attachments);
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.timeout", "25000");
            // 添加验证
            MyAuthenticator auth = new MyAuthenticator(emailUserName, emailPassword);
            Session session = Session.getDefaultInstance(prop, auth);
            senderImpl.setSession(session);
            // 发送邮件
            senderImpl.send(mailMessage);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("邮件发送失败:" + e.getMessage());
        }
    }

    private void setReceivers(MimeMessageHelper messageHelper, String toEmails) throws MessagingException
    {
        String[] toEmailArray = toEmails.split(";");
        toEmailArray = (String[]) ArrayUtils.removeElement(toEmailArray, "");
        Validate.isTrue(ArrayUtils.isNotEmpty(toEmailArray), "收件人邮箱不得为空！");
        List<String> toEmailList = new ArrayList<String>();
        for (String s : toEmailArray)
        {
            if (StringUtils.isNotBlank(s))
            {
                toEmailList.add(s);
            }
        }
        Validate.isTrue(CollectionUtils.isNotEmpty(toEmailList), "收件人邮箱不得为空！");
        messageHelper.setTo(toEmailList.toArray(new String[toEmailList.size()]));
    }

    private void addPictures(MimeMessageHelper messageHelper, Map<String, String> pictures) throws MessagingException
    {
        if (MapUtils.isNotEmpty(pictures))
        {
            for (Iterator<Map.Entry<String, String>> it = pictures.entrySet()
                    .iterator(); it.hasNext(); )
            {
                Map.Entry<String, String> entry = it.next();
                String cid = entry.getKey();
                String filePath = entry.getValue();
                Validate.isTrue(StringUtils.isNotBlank(cid) && StringUtils.isNotBlank(filePath), "请确认每张图片的ID和图片地址是否齐全！");
                File file = new File(filePath);
                Validate.isTrue(file.exists(), "图片 " + filePath + " 不存在！");
                FileSystemResource img = new FileSystemResource(file);
                messageHelper.addInline(cid, img);
            }
        }
    }

    private void addAttachments(MimeMessageHelper messageHelper, Map<String, String> attachments) throws MessagingException
    {
        if (MapUtils.isNotEmpty(attachments))
        {
            for (Iterator<Map.Entry<String, String>> it = attachments
                    .entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<String, String> entry = it.next();
                String cid = entry.getKey();
                String filePath = entry.getValue();
                Validate.isTrue(StringUtils.isNotBlank(cid) && StringUtils.isNotBlank(filePath), "请确认每个附件的ID和地址是否齐全！");
                File file = new File(filePath);
                Validate.isTrue(file.exists(), "附件 " + filePath + " 不存在！");
                FileSystemResource fileResource = new FileSystemResource(file);
                messageHelper.addAttachment(cid, fileResource);
            }
        }
    }

    public class MyAuthenticator extends Authenticator
    {
        private String username;

        private String password;

        /**
         * @param username
         * @param password
         * @author geloin
         * @date 2012-5-8 下午2:48:53
         */
        public MyAuthenticator(String username, String password)
        {
            super();
            this.username = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }

}
