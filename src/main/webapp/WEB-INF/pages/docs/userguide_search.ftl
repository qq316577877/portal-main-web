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
                        如何在九创金服上搜索产品
                    </div>

                    <div class="about-details-mode" style="padding-bottom: 210px;">
                        <div class="about-details">
                            <div class="about-details-txt">
                                1. 您可以搜索品牌、型号、订货号的部分或者是全部关键词。
                            </div>
                            <div class="about-details-txt">
                                2. 使用模糊搜索提高搜索命中率，使搜索结果更多，比如：“LC1D”，模糊搜索请尽量输入型号、订货号的前半部分，使搜索结果更佳。
                            </div>
                            <div class="about-details-txt">
                                3. 采用完整的产品型号或订货号可进行产品的精确搜索，如：“LC1D09M7C”、“EA9F1X20”。
                            </div>
                            <div class="about-details-txt">
                                4. 产品的关键性能指标等关键词也可进行搜索，但是搜索范围广，搜索精度降低，如：“9A”。
                            </div>
                            <div class="about-details-txt">
                                5. 结合产品型号和性能指标关键词，降低搜索范围，提升搜索命中率，如：“LC1D 9A”。
                            </div>
                            <div class="about-details-txt">
                                6. 如想在一次搜索中同时搜索多种不同类型的产品，请使用批量搜索功能。
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
