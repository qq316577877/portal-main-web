<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>资金服务</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/indexBase.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/fundService.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/calculator/calculator.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/fundService/fundService.main.js"></script>
    <script src="${static_file_server}/plugin/calculator/calculator.js"></script>
</head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">

<!--logo-->
<div class="logoBox">

<#include "/common/home_logo_top.ftl">

    <div class="imgBox">
        <img src="${static_file_server}/assets/img/fundService.jpg "/>
    </div>

</div>

<!--main-->
<div class="main">
    <div class="main-content">
        <div class="main-top">
            <h3>资金服务</h3>

            <p>
                平台引进雄厚的融资渠道，致力于打造全国一流的农产品供应链金融服务；秉承提升贸易合作伙伴综合竞争力的理念，全方位为客户提供货物物流质押融资服务。让采购途中的货物直接转变为流动资金，降低供应双方的资金压力，以小资金做出大生意。解决客户资金短板，提升其运营能力和市场竞争力。</p>
            <h4 class="title">一、合作对象</h4>
            <p>具备良好的个人征信；从事火龙果、大米、海产品等贸易的客户。</p>
            <h4 class="title">二、客户的准入流程</h4>
            <p>
                客户网上提出申请，我司通过合作银行查询客户个人征信无不良记录，安排工作人员上门与客户签署相关协议为客户提供50----1000万授信额度。需提供查询人民银行征信记录授权协议。</p>
            <h4 class="title">三、合作模式：</h4>
            <p>
                与我司有资金合作的客户，在发货装车时我司派指定验货员上门验货并确定货物价值，走车后以货物价值的百分之50--70的额度当天放款给客户。货物到市场前24小时我司通知客户还款。若客户违约我司有权对客户商品进行独立处置。垫资授信额度循环使用，业务增长额度不够可申请调额。</p>
            <h4 class="title">四、收费标准：</h4>
            <p>
                贷款本息将在货物到达目的地的前一天统一收取。平台服务费将在放款时直接扣划。所有贷款均按照您实际使用天数计息，日利率低至万3.35。平台服务费按本金0.5%收取（先实行优惠费率，仅收取0.1%的平台服务费）。</p>
        </div>

        <div class="btnBOx">
            <a href="${my_aplication_page_url}" id="apply-for" class="btn">立即申请贷款</a>
            <button id="calculate" class="btn">贷款计算器</button>
        </div>

    </div>

</div>

<!--底部通栏-->
<div class="f-Box">
<@bottom.footer add_qa=1/>
</div>

</body>

</html>