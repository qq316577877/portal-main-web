<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<#include "/common/seo.ftl">
    <title>欢迎注册</title>
    <link href="${static_file_server}/assets/css/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/account/register.css" rel="stylesheet" type="text/css">

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
</head>

<body>

<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">


<!--直接登录-->
<div class="direct w">
    <div>已有账号？<a href="${login_url}">请登录</a></div>
</div>
<!--main-->
<div class="main w">
    <p>填写注册信息</p>

    <form action="" id="register">
        <label>
            <div class="input-l"><span>*</span> 账号：</div>
            <input type="text" placeholder="请输入11位手机号" id="tel" name="tel"/>
            <i class="iconfont icon-gou tel-true"></i>
            <div class="tel-false"><span class="wrong"></span>请输入11位手机号</div>
            <div class="tel-verify-w"></div>
            <div class="tel-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 图形验证码：</div>
            <input type="text" id="patterning" placeholder="请输入图形验证码" name="patterning"/>

            <div class="main-img">
                <img src="#" alt=""/>
            </div>
            <a href="#" class="main-p">
                看不清？换一张
            </a>
            <i class="iconfont icon-gou img-true"></i>
            <div class="img-false"><span class="wrong"></span>请输入正确的验证码</div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 短信验证码：</div>
            <!-- 889337-->
            <input type="text" id="msg_code" class="input-pass" name="msg_code" placeholder="请输入短信验证码"/>
            <div class="main-passage">
                <input type="button" id="btn-passage" value="免费获取验证码"/>
            </div>
            <i class="iconfont icon-gou message-true"></i>
            <div class="message-false"><span class="wrong"></span>验证码错误</div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 设置密码：</div>
            <input type="password" id="pwd" placeholder="请输入6-20位字符" name="pwd"/>
            <span class="hint">请输入6-20位字符，需包含大写字母、小写字母、数字、符号，至少两种</span>
            <i class="iconfont icon-gou pwd-true"></i>
            <div class="pwd-false"><span class="wrong"></span>请输入6-20位字符，需包含大写字母、小写字母、数字、符号，至少两种</div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 重复密码：</div>
            <input type="password" id="pwd1" placeholder="请再次输入密码" name="pwd1"/>
            <i class="iconfont icon-gou pwd1-true"></i>
            <div class="pwd1-false"><span class="wrong"></span>两次密码不一致</div>
        </label><br/>
        <label>
            <div class="input-l">邮箱：</div>
            <input class="specialChar" type="text" id="email" name="email" placeholder=""/>

            <i class="iconfont icon-gou email-true"></i>
            <div class="email-false"><span class="wrong"></span>邮箱格式错误</div>
        </label><br/>
        <label>
            <div class="input-l">QQ：</div>
            <input type="text" id="qq" name="qq" placeholder=""/>
            <i class="iconfont icon-gou qq-true"></i>
            <div class="qq-false"><span class="wrong"></span>QQ格式错误</div>
        </label><br/>
        <input type="checkbox"  class="input-box"/>

        <div class="checkbox2">阅读并同意 <a href="${user_agreement_url}">《九创金服服务条款》</a></div>
        <br/>
        <input type="button" class="sub" value="提交注册"/>
    </form>
</div>
<!--warp end-->

<!--footer-->
<@bottom.footer pos_fixed=0/>
<!--footer end-->

<script src="${static_file_server}/js/pages/register/register.main.js"></script>

</body>
</html>
