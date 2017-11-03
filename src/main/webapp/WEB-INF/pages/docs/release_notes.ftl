<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <#include "/common/seo.ftl">
    <title>九创金服-网站公告</title>
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
                    <div class="hd"><h6>网站公告</h6></div>
                    <div class="faq-box">
                        <div class="faq-list">
                            <ul>
                                <#list items as item>
                                    <li>${item_index + 1}. ${item.title} (${item.time?string('MM月dd日')})</a></li>
                                </#list>
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
