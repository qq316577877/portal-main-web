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
                <div class="about-details-box">
                    <!--面包屑-->
                    <div class="crumbs">
                        <a href="${doc_userguide_url}" title="" class="crumba-link">返回目录</a><em>>
                    </em>
                        如何使用批量找货功能
                    </div>

                    <div class="about-details-mode">
                        <div class="about-details">
                            <div class="about-details-txt">
                                如果您需要采购一批多型号产品，新版九创金服也支持批量找货，简单说就是一次查询多个型号的产品的在售信息，如果有合适的分销商报价，则可以直接全部下单。
                            </div>
                            <div class="about-details-txt">
                                访问九创金服，批量找货功能的入口就在页面右侧，您可以在表单中填写想要采购的型号和数量，如果数量很多，可以进入询货单页面添加更多型号。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-multlsearch-1.png-20160125172632685-ffffcf4c4d82.png">
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-multlsearch-2.png-20160125172702212-ffffbd26c4b4.png">
                            </div>
                            <div class="about-details-txt">
                                在询货单页面，您可以看到您查询的产品在九创金服的在售情况，如果能显示出产品描述及“有货”标识，说明您可以在下一步的搜索中查询到分销商的报价。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-multlsearch-3.png-20160125172733018-00001866c2d3.png">
                            </div>
                            <div class="about-details-txt">在搜索到分销商的报价后，您就可以将这批产品及报价批量添加到购物车，或者直接去下订单了。</div>
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
</div>
    <@bottom.footer  add_qa=1/>

</body>
</html>
