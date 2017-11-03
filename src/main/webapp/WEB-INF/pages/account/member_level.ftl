<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员等级</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/memberLevel.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>

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
        <h3>会员等级</h3>
        <div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>

        <div class="LevelBox">
            <img src="${static_file_server}/assets/img/level.png"/>
            <i>您的等级</i>
        </div>
        <h3>会员等级是什么？</h3>
        <p class="mafoot">会员等级是我们根据您在九创金服的行为给出的综合评级，等级越高，意味着您可以享受九创金服提供的特权越多。该评级的确定主要参考了您的进口下单行为以及资金服务使用信用状况，同时综合了您的账户安全情况、授信额度情况，各合作方评价情况、订单付款情况等多种因素。但没有任何一个单向因素可以直接或者完全决定您的会员等级。保持您在九创金服良好的采购行为，将有助于提升您的评级。</p>
        <h3>会员权益</h3>
        <p>您通过九创金服的会员认证以后，即可使用进口下单业务以及资金申请服务。在您良好的个人征信记录及经营状况下，可以获得一笔可观的平台合作银行授信额度。获得授信额度以后，每次进口下单都可以按照货柜申请一笔贷款，等级越高，意味着您可以申请的单笔贷款金额越高。同时，每笔订单的预付款与尾款结算比例可根据会员等级进行调整。</p>
    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>

</html>