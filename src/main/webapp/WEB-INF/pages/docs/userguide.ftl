<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <#include "/common/seo.ftl">
    <title>九创金服-使用指南</title>
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
                    <div class="hd"><h6>使用指南</h6></div>
                    <div class="faq-box">
                        <div class="faq-list">
                            <ul>
                                <li><a href="${userguide_buy_url}" title="">1. 如何在九创金服上采购产品？</a></li>
                                <li><a href="${userguide_search_helper_url}" title="">2. 如何在九创金服上搜索想要的产品？</a></li>
                                <li><a href="${userguide_multisearch_url}" title="">3. 如何使用批量找货功能？</a></li>
                                <li><a href="${userguide_refund_url}" title="">4. 收到货品不满意如何退货？</a></li>
                                <li><a href="${userguide_sell_url}" title="">5. 我是品牌授权分销商，如何上传待售产品？</a></li>
                            </ul>
                        </div>
                    </div>
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
