<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<#include "/common/seo.ftl">
    <title>登录</title>
    <link href="${static_file_server}/assets/css/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/account/login.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" type="text/css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>

</head>

<body>

<#include "/common/top.ftl">

<!--logo-->
<#include "/common/header.ftl">

<div class="main">
    <div class="w">
        <div class="status fr">
            <h2>会员登录</h2>

            <form action="" id="loginForm" enctype="multipart/form-data">
                <div class="usename">
                    <label class="formlabel">
                        <div class="usename-icon">
                            <i class="iconfont icon-yonghu yonghu"></i>
                        </div>
                        <input type="text" placeholder="注册使用的手机号" class="usename1" id="usename" name="usename"/>
                        <!-- <div class="tel-false"><span class="wrong"></span>请输入11位手机号</div> -->
                    </label>
                    <label class="formlabel">
                        <div class="usename-icon">
                            <i class="iconfont icon-mima yonghu"></i>
                        </div>
                        <input type="password" placeholder="登录密码" class="usename1" id="pwd" name="pwd"/>
                        <!-- <div class="pwd-false"><span class="wrong"></span>请输入密码</div> -->
                        <div class="pwd-false-1"><span class="wrong"></span>账号和密码不匹配</div>
                    </label>
                </div>
            </form>
            <div class="remember">
                <div class="remember-l fl">
                    <input type="checkbox" id="checkbox" class="checkbox"/>
                    <span>自动登录</span>
                </div>
                <a href="${forget_pwd_url}" class="remember-r fr">
                    忘记密码
                </a>
            </div>
            <input type="button" value="登  录" class="login"/>
            <div class="register-in">
                <i class="iconfont icon-icon22 register-in1"></i>
                <a  href="${register_url}" class="register-in2">免费注册</a>
            </div>
        </div>
    </div>
</div>

<!--warp end-->

<!--footer-->
<@bottom.footer pos_fixed=1 />
<!--footer end-->
<!--快捷导航 -->
<#--<#include "/common/nav.ftl">-->
<!--快捷导航 end-->

            <script src="${static_file_server}/js/pages/login/login.main.js"></script>
</body>

</html>
