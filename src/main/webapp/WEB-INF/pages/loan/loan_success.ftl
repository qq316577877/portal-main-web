<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>申请贷款-成功</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/loanSucceed.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/loanSucceed/loanSucceed.main.js"></script>

</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">

<!--main-->
<div class="main w">
    <div class="main-r-show">
        <img src="${static_file_server}/assets/img/loanSucceed.png"/>
        <h3>提交成功,等待批复</h3>
        <p class="inform">您提交的贷款申请已受理，请您保持手机号为<i class="telNum">13252114152</i>的手机畅通，我们的客户经理预计在2个工作日内联系您确认贷款申请事宜。</p><br />
        <a href="${dashboard_url}" class="approve">进入会员中心</a>

    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>
</html>