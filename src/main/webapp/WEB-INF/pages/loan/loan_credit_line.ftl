<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>我的授信额度</title>

    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/limitGenerality.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/creditLimitRequest.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/calculator/calculator.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/creditLimitRequest/creditLimitRequest.main.js"></script>
	<script src="${static_file_server}/plugin/calculator/calculator.js"></script>

</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<div class="line"></div>
<div class="main w clearfix">
<#include "/common/user_nav.ftl">

    <div class="main-r fl">
        <h3>我的资金服务</h3>
        <div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>
        <div class="main-r-content">
            <div class="c-head">
                <p class="notice">可用贷款额度:<i>0</i></p>
                <p class="notify">利息按天计算，日利率低至万3.35</p>
            </div>
            <div class="btnBox">

            <#if auth_status == 1 ><!-- 已实名认证 -->
                <a href="${loan_url}" class="apply-for">申请贷款额度</a>
            <#else><!-- 未实名认证 -->
                <a href="${auth_url}" class="apply-for">申请贷款额度</a>
            </#if>


                <button id="calculate">贷款计算器</button>
            </div>
        </div>
    </div>
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>
</html>