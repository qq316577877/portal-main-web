<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>通关服务</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/indexBase.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/customsClearanceService.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/customsClearanceService/customsClearanceService.main.js"></script>


</head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">


<!--logo-->
<div class="logoBox">

    <#include "/common/home_logo_top.ftl">

    <div class="imgBox">
        <img src="${static_file_server}/assets/img/T-EIndex.jpg" />
    </div>

</div>

<!--main-->
<div class="main">

    <div class="main-content">
        <div class="main-top">
            <h3>清关介绍</h3>
            <p>清关（Customs Clearance）即结关，是指进出口货物和转运货物进入或出口一国海关关境或国境必须向海关和商检申报，办理海关和商检规定的各项手续。进口清关包括进口报检和进口报关两部分。</p>
        	<p>各口岸的进口申报流程存在一定的偏差，这些客观因素，决定了“不同性质的企业、不同产品、不同口岸、不同申报方式”的进口申报多样性。</p>
        	<p>本平台业务团队能为客户提供华东、华南、华北以及西南等地区的各类产品的专业进口报检报关服务。</p>
        </div>
        

    </div>

</div>

<!--底部通栏-->
<div class="f-Box">
<@bottom.footer  add_qa=1/>
</div>



</body>

</html>