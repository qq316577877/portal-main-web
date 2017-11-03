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
                        收到货品不满意如何退货
                    </div>

                    <div class="about-details-mode">
                        <div class="about-details">
                            <div class="about-details-txt">
                                如果您在九创金服上采购的产品在收到后需要退货，可以在订单列表中找到该订单，进入详情页，根据页面上给出的分销商联系电话，与卖方沟通退货事宜，然后在页面上点击“申请退货”。
                            </div>
                            <div class="about-details-txt">
                                需要注意的是，如果您希望退货，请不要点击“确认收货”按钮，如果确认收货，分销商将会收到您的货款，后续您如果再需要退货退款，就需要直接与分销商联系，九创金服将无法介入。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-refund-1.png-20160125174426942-ffffa55a1a99.png">
                            </div>
                            <div class="about-details-txt">
                                点击“申请退货”链接后，您需要在确认窗口里输入退货的理由，然后再点击确认。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-refund-2.png-20160125174440707-ffffef3f7c21.png">
                            </div>
                            <div class="about-details-txt">
                                提交退货申请后，订单进入“退款申请中”状态，此时需要分销商来进行操作，如果分销商同意退款，九创金服将第一时间将您的货款原路退回，如果不同意，九创金服将介入调解直至问题解决。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-refund-3.png-20160125174504386-000073cda232.png">
                            </div>
                            <div class="about-details-txt">
                                分销商同意退款后，九创金服将立即处理退款事项，按照之前您支付的路径，将货款退还给您，此时订单的状态会变为“等待系统退款”。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-refund-4.png-20160125174518261-ffffaee7053e.png">
                            </div>
                            <div class="about-details-txt">
                                当九创金服工作人员确认已支付退款后，订单状态将变更为“已完成退款”，您可以查收您的支付账户是否有收到相应款项，考虑到不同银行处理业务的时间，可能具体到账会略有延迟。
                            </div>
                            <div class="about-details-txt"><img
                                    src="http://picture.0ku.com/1-refund-5.png-20160125174537537-ffff93a8fc7d.png">
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
