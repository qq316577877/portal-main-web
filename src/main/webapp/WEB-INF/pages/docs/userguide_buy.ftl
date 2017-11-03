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
                        如何在九创金服上采购产品
                    </div>

                    <div class="about-details-mode">
                        <div class="about-details">
                            <div class="about-details-txt">
                                您在新版九创金服依然可以通过首页的搜索框来查找想要的产品型号，新版还支持类似百度搜索的“搜索联想”功能，当您输入了一个型号的前几个字符，系统会自动帮您联想出您可能搜索的产品型号，点击联想的型号即可立即搜索。
                                <a href="${userguide_search_helper_url}">搜索指南</a>
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-1.png-20160125173400149-ffffc86a65d8.png">
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-2.png-20160125173418904-000003fee43f.png">
                            </div>
                            <div class="about-details-txt">
                                搜索到相应型号后，在产品详情页即可查看全国范围内多家分销商的报价，选择您感兴趣的报价，添加到购物车。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-3.png-20160125173442595-00000e54a560.png">
                            </div>
                            <div class="about-details-txt">
                                将想要购买的产品以及对应报价都添加到购物车后，您就可以去购物车下单了。选择好您想要下在一个订单里的产品，点击右侧“下订单”按钮。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-4.png-20160125173513971-ffff94b3dc3c.png">
                            </div>
                            <div class="about-details-txt">在随后的确认订单环节，填写好您的收货地址、发票相关信息，点击“提交订单”按钮，就成功向卖方提交了一个订单，卖方将收到您所下订单的通知。</div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-5.png-20160125173641335-000079f9aa1c.png">
                            </div>
                            <div class="about-details-txt">成功提交订单后，您可以与卖方联系，确认订单信息，也可以直接在线支付订单费用，九创金服支持支付宝、国内30多家网银支付，也支持钱款转入九创金服账户的担保支付方式。</div>
                            <div class="about-details-txt">一旦您成功支付，九创金服将会通知卖家准备订单并发货，相关进度也会更新在九创金服的对应订单详情中。</div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-6.png-20160125173711934-ffff91a45ed1.png">
                            </div>
                            <div class="about-details-txt">之后，您就可以在九创金服订单列表中找到该订单，查看卖方发货状态、物流信息等，直至确认收货。</div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-buy-7.png-20160125173725398-0000041f52a8.png">
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
