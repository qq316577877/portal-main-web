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
                        我是品牌授权分销商，如何上传待售产品
                    </div>

                    <div class="about-details-mode">
                        <div class="about-details">
                            <div class="about-details-txt">
                                如果您是品牌授权分销商，登录九创金服后即可在首页看到“我要卖货”按钮。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-1.png-20160125175117456-ffffe778443c.png">
                            </div>
                            <div class="about-details-txt">
                                也可以在“我的九创金服>管理待售产品”页面的右上角找到上传待售产品页面的链接。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-2.png-20160125175138358-ffffd2b7a4f6.png">
                            </div>
                            <div class="about-details-txt">
                                进入上传待售产品页面后，首先您需要确认上传的待售产品属于“新品”还是“闲置库存”，在九创金服上，这两类产品的销售渠道和展示方式都有所不同。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-3.png-20160125175151455-fffffd785a25.png">
                            </div>
                            <div class="about-details-txt">
                                选定要上传的待售产品类型后，就可以开始上传待售产品了。九创金服支持两种上传方式，一种是在页面上直接添加，适用于单次上货的型号数量较少的情况。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-4.png-20160125175215686-00002a9c8462.png">
                            </div>
                            <div class="about-details-txt">
                                如果单次上货的型号数量很多，可以使用excel文件上传的方式，按照模板的格式上传文件。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-5.png-20160125175233105-ffffe4bc1e7d.png">
                            </div>
                            <div class="about-details-txt">
                                需要特别注意的是，您只能上传您通过授权分销认证的品牌的产品。另外如果上传的型号已存在于您的待售产品列表中，新上传的报价、库存数量等信息将覆盖之前的信息。
                                上传成功后，您将可以在待售产品列表中看到相关信息。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-6.png-20160125175246889-00005fe64959.png">
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-sell-7.png-20160125175259453-0000677f0f28.png">
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
    </div>
    <@bottom.footer  add_qa=1/>

</body>
</html>
