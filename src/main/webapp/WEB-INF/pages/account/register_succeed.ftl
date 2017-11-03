<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员注册</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/registerSucceed.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/js/pages/registerSucceed/registerSucceed.main.js"></script>
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->

<!--main-->
<div class="main w">

    <div class="main-head">
        <span class="smiling-face"></span><p class="congratulation">恭喜您,注册成功</p>
    </div>



    <div class="main-content">
        <span class="portrait"></span>
        <p class="account">您的账号：<span class="Tel-color">${mobile}</span></p>
        <div class="main-content-btn">
           <!-- <a href="${register_enterprise_url}" class="btn-w green">会员认证</a>-->
            <a href="/member/register/enterpriseIn" class="btn-w green">会员认证</a>
            <a href="${home_page_url}" class="btn-w white">暂不认证</a>
        </div>
        <p class="hint"><mark>!</mark>您需要先完成会员认证，才可以正常使用下单，资金服务等相关功能。</p>
        <span class=" line"></span><p class="why">为什么进行会员认证</p><span class=" line"></span>
    </div>

    <div class="main-foot">
        <ul class="clearfix pictureBox">
            <li>
                <span class="cause picture1"></span>
                <p class="state">核实真实信息</p>
            </li>
            <li>
                <span class="cause picture2"></span>
                <p class="state">保障账户安全</p>
            </li>
            <li>
                <span class="cause picture3"></span>
                <p class="state">获取合作方信赖</p>
            </li>
            <li>
                <span class="cause picture4"></span>
                <p class="state">申请资金服务</p>
            </li>
        </ul>
    </div>



</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>


</body>
</html>