<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员认证(空)</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/memberNull.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugi/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>



    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/memberNull/memberNull.main.js"></script>


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
        <div class="main-r-show">
            <img src="${static_file_server}/assets/img/approve.png"/>
            <p class="hint">您还没有进行会员认证哦&nbsp;!</p>
            <a href="###" class="approve">会员认证</a>

        </div>
    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>


</body>
</html>