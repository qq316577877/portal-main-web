<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>进口下单</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/importStep3.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

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
        <div class="info-1">
            您提交的采购订单已转入快捷人工审核，
            请您保持手机号为<span id='user_phone'></span>的手机畅通， <br/>
            我们的客服人员预计在30分钟内联系您确认订单信息。
        </div>
        <div class="info-2">
            <span>温馨提示</span>：人工审核受理时间为工作日9：00—17：00，当天17:00之后提交的采购订单，
            将在第二个工作日 <br/>
            10：00之前审核完毕。
            若您的采购订单非常紧急，请直接致电0577-87050258。
        </div>
        <div class="main-btn">
            <a href="/order/create">继续下单</a>
            <a href="/order/center/list">返回我的订单</a>
            <a href="/home">返回首页</a>
        </div>
    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

<script>
    $(function () {
        $('#user_phone').html(__DATA.mobile);
    });
</script>

</body>
</html>
<script>
    __DATA = ${__DATA}
</script>