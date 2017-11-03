<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <#include "/common/seo.ftl">
    <title>九创金服-常见问题</title>
    <link href="${static_file_server}/assets/css/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/docs/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/docs/about.css" rel="stylesheet" type="text/css">
</head>

<body>
<!--toolbar-->
<#include "/common/header.ftl">
<!--toolbar end-->
<!--warp-->
<div class="warp">
    <div class="constitution">
        <!--常见问题-->
        <div class="about-box">

        <#include "/docs/about_nav.ftl">

            <!--右侧-->
            <div class="about-main">
                <div class="about-mode">
                    <div class="hd"><h6>常见问题</h6></div>
                    <div class="faq-box">
                        <h6>网站注册与认证类问题</h6>
                        <div class="faq-list">
                            <ul>
                                <li><a href="${doc_question_detail_url}#how_register" title="">1. 如何注册九创金服？</a></li>
                                <li><a href="${doc_question_detail_url}#forget_pwd" title="">2. 我忘记了登录名或密码怎么办？</a></li>
                                <li><a href="${doc_question_detail_url}#why_enterprise_auth" title="">3. 为什么注册九创金服后需要进行企业认证？</a></li>
                                <li><a href="${doc_question_detail_url}#what_if_brand_auth" title="">4. 我有进口服务需求，九创金服能为我提供什么服务？</a></li>
                            </ul>
                        </div>
                    </div>
                    <#--<div class="faq-box">-->
                        <#--<h6>采购类问题</h6>-->
                        <#--<div class="faq-list">-->
                            <#--<ul>-->
                                <#--<li><a href="${doc_question_detail_url}#how_find_product" title="">1. 如何在九创金服寻找我需要的型号？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_multi_find" title="">2. 我要一次采购很多型号的产品，有什么方便的方法吗？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#can_bargain" title="">3. 我可以和卖家讨价还价吗？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#what_order_process" title="">4. 九创金服上交易的流程是怎样的？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_pay" title="">5. 如何支付货款？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_refund" title="">6. 收到货物后不满意，想要退货怎么办？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_bill" title="">7. 我将货款支付给九创金服进行担保，但发票由卖家开具，财务上应该如何做账？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#what_points" title="">8. 什么是九创金服积分，九创金服积分有什么作用？</a></li>-->
                            <#--</ul>-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<div class="faq-box">-->
                        <#--<h6>销售类问题</h6>-->
                        <#--<div class="faq-list">-->
                            <#--<ul>-->
                                <#--<li><a href="${doc_question_detail_url}#how_sell" title="">1.  如何在九创金服上架产品并销售？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_sell_overstock" title="">2. 我的仓库里有滞销的库存产品，九创金服能帮我卖掉吗？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_lack_stock" title="">3. 如果接到订单后发现实际库存不足，我应该怎么办？</a></li>-->
                                <#--<li><a href="${doc_question_detail_url}#how_withdraw" title="">4. 如何在交易成功后提取货款？</a></li>-->
                            <#--</ul>-->
                        <#--</div>-->
                    <#--</div>-->
                </div>
            </div>
            <!--右侧 end-->
        </div>
        <!--常见问题 end-->
        <!--服务信息-->
    <#include "/common/service_info.ftl">
        <!--服务信息 end-->
    </div>
</div>
<@bottom.footer  add_qa=1/>


</body>
</html>
