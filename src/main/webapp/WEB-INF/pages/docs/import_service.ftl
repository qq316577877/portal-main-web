<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>进口下单</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/indexBase.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/importService.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
   <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/importService/importService.main.js"></script>
</head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">

<!--logo-->
<div class="logoBox">

<#include "/common/home_logo_top.ftl">

    <div class="imgBox">
        <img src="${static_file_server}/assets/img/importService.jpg"/>
    </div>

</div>

<!--main-->
<div class="main">
    <div class="main-content">
        <div class="main-top">
            <h3>一般贸易代理：</h3>
            <p>一般贸易是进出口的最常用的贸易方式，中国境内有进出口经营权的企业单边进口或单边出口的贸易，按一般贸易交易方式进出口的货物即为一般贸易货物。</p>
            <p>本平台可为没有进出口权的企业或个人，有进出口权的企业但没有相关经营资质的企业提供一般贸易代理服务。</p>
            <h3> 代签外贸合同：</h3>
            <p>本平台业务团队凭借经验丰富的外贸人才和多年累积的实操案例，为客户提供专业的外贸合同代签服务，能为企业最大限度地减少在国际贸易中摩擦与风险。</p>
            <p>（一） 合同文本的起草</p>
            <p>（二） 明确双方当事人的签约资格</p>
            <p>（三） 明确规定双方义务和责任</p>
            <p>（四） 明确合同中的内容及条款</p>
            <p>（五） 签约</p>
        </div>
        <!--<div class="main-foot">
            <div class="mian-foot-box">
                <div class="main-foot-c">
                    <h4>下单须知</h4>
                    <p>1、目前一个订单仅支持一个收货地址，如有多个收货地址，需分别下单</p>
                    <p>2、您下单提交审核后，平台客服会进行电话回访，确定成交价格、服务类型及订单结算方式，最后由您确认订单无误后提交</p>
                    <p>3、物流服务运输方式为海运时，采购合同约定款项需经平台对公账户结算</p>
                    <p>4、如果您自主采购，仅需平台物流、通关、资金服务时，请确保您已与平台准入物流公司、清关公司签署合作协议</p>
                    <a href="${create_order_page_url}" class="pop-up">我知道了</a>
                </div>
            </div>

            <button id="show">立即下单</button>
        </div>-->

    </div>

</div>

<!--底部通栏-->
<div class="f-Box">
<@bottom.footer  add_qa=1/>
</div>

</body>

</html>