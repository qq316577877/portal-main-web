<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>进口下单</title>

    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/vipCenter/vipCenter.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/vipCenter/vipCreate.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/vipCenter/QRCodePopup.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/QRCodePopup/QRCodePopup.main.js"></script>

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
        <div class="main-r-t clearfix">
            <h2 style="width: 155px;"><a style="color: #333" href="order/center/list">我的订单</a> >
                立即下单
            </h2>
            <div class="numline">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </div>
            <div class="main-l-t1 main-info6">
                <h3>下单须知</h3>
                <div class="create-info">
                    <p>   1、目前一个订单仅支持一个收货地址，如有多个收货地址，需分别下单</p>
                    <p>   2、您下单提交审核后，平台客服会进行电话回访，确定成交价格、服务 <br/>
                        类型及订单结算方式，最后由您确认订单无误后提交</p>
                    <p>   3、物流服务运输方式为海运时，采购合同约定款项需经平台对公账户结算</p>
                    <p>   4、如果您自主采购货款，仅需平台物流、通关、资金服务时，请确保您 <br/>
                        已与平台准入物流公司、清关公司签署合作协议</p>
                </div>
                <a id="order_create" href="javascript:void(0)">立即下单</a>
            </div>
        </div>

    </div>
</div>
<!--mask-->
<div class="mask">
   <!--二维码-->
    <div class="inform">
        <div class="inform-head">
            <span class="fl">查看二维码</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
        	<img class="img" src="${static_file_server}/assets/img/jiuChuang.jpg"/>
        	<p class="announcement">尊敬的客户您好，请移步到微信端下单哦！</p>
        	<button class="cancel exit" type="button">关闭</button>
        </div>
    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>

</html>