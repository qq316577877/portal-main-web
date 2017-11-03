<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员认证</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/vipsucced.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <#--<link rel="stylesheet" href="${static_file_server}/plugin/iconfont-2/iconfont.css"/>-->


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">

<!--main-->
<div class="w">
    <div class="main">
        <i class="iconfont icon-tijiaochenggong suc"></i>
        <h2>提交成功，等待审核中</h2>
        <p>已转入人工审核，预计3个工作日内完成。</p>
        <p>认证结果，我们将短信通知您，请您关注您的手机短信，</p>
        <p>或者您可以直接登录我们的平台查看您的会员认证结果，</p>
        <p>感谢您的支持！</p>
        <div class="back">
            <a href="${home_page_url}">返回首页</a>
            <a href="${dashboard_url}">进入会员中心</a>
        </div>
    </div>
</div>


<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>
</html>