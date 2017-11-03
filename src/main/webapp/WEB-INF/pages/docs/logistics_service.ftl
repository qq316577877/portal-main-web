<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>物流服务</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/indexBase.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/customsClearanceService.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/js/pages/logisticsService/logisticsService.main.js"></script>


</head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">


<!--logo-->
<div class="logoBox">

    <#include "/common/home_logo_top.ftl">

    <div class="imgBox">
        <img src="${static_file_server}/assets/img/logistics.jpg" />
    </div>

</div>

<!--main-->
<div class="main">

    <div class="main-content">
        <div class="main-top">
            <p>随着我国成为全球第二大进口国，国际贸易纠纷也随之快速增加，以至于我国进口采购方越来越趋向于在国际贸易中采用EXW或FOB条款。我司洞察需求，为有效保障我国进口采购方的利益，为客户提供全球进口门到门的“国外上门提货、进口国际货运、国内物流派送”服务。</p>
        	<p>本平台覆盖全球各地的多家海外代理将专业为客户提供门到门服务。根据客户要求，国外上门提货，办理物流装载、捆扎、加固方案、现场监装等服务。</p>
        	<h4 class="title">进口海运：</h4>
        	<p>本平台与全球多家船公司建立良好的合作关系，可以根据货物及航程需要，选择、组织最佳运输工具及运输方案，为客户提供装货港清关、订舱、运输、付费、制单等货运代理服务。</p>
        	<img src="${static_file_server}/assets/img/logistics1.png"/>
        	<h4 class="title">国内物流派送：</h4>
        	<p>为了更好完善进口物流供应链服务，货物在港口清关完毕后，本平台可为客户提供国内物流派送服务，服务项目以公路运输为主。</p>
       		<img src="${static_file_server}/assets/img/logistics2.png"/>
        </div>

    </div>

</div>

<!--底部通栏-->
<div class="f-Box">
<@bottom.footer  add_qa=1/>
</div>



</body>

</html>