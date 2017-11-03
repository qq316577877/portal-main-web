<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <#include "/common/seo.ftl">
    <title>九创金服-常见问题</title>
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
                        <a href="${doc_question_url}" title="" class="crumba-link">返回目录</a><em>>
                    </em>
                        常见问题
                    </div>

                    <!-- 网站注册与认证类问题-->
                    <div class="about-details-mode">
                        <div style="font-size: 14px; font-weight: 600">网站注册与认证类问题</div>
                        <br/>
                        <a name="how_register"></a>
                        <div class="about-details">
                            <h6>1. 如何注册九创金服？</h6>
                            <div class="about-details-txt">
                               九创金服是一个为农产品经销商全方位提供货物物流质押融资服务的平台，需要注册账号并通过审核方可使用，您可以在首页右上角找到注册的入口，点击进入注册流程后，按照网站提示填写相关信息即可完成注册。 在九创金服，账号注册分为两步——基本信息注册和会员认证。基本信息注册环节，您需要设置登录账号（手机号）、密码，邮箱和QQ号。在企业认证环节，您需要提交一些公司或个人真实资料，提交后等待平台工作人员的审核，审核通过后即完成注册，可以使用九创金服的全部功能。
                            </div>
                        </div>
                        <div class="about-details">
                            <a name="forget_pwd"></a>
                            <h6>2. 我忘记了登录名或密码怎么办？</h6>
                            <div class="about-details-txt">
                                如果忘记了密码，可以在登录页选择“忘记密码”，进行密码重置，在密码重置过程中，也会需要用到注册时验证的手机号码。
                            </div>
                        </div>
                        <div class="about-details">
                            <a name="why_enterprise_auth"></a>
                            <h6>3. 为什么注册九创金服后需要进行企业认证？</h6>
                            <div class="about-details-txt">
                              九创金服是一个为农产品经销商服务的企业级平台，所以平台会对注册用户的身份进行审核，只有有相关业务经营资质的企业或个人才能在九创金服进行进口下单及申请资金服务。在会员认证环节，您需要提交一些公司资料或个人真实资料，提交后等待平台工作人员的审核，审核通过后即完成注册，可以使用九创金服的全部功能。
                            </div>
                            <div class="about-details-txt">
                               您可以在注册九创金服的账户时就提交会员认证，也可以稍晚些在会员中心提交会员认证。请注意，在九创金服提交会员认证前，请您务必确保已获得企业负责人的授权或您本人就是是企业的法人代表。
                            </div>
                        </div>
                        <div class="about-details">
                            <a name="what_if_brand_auth"></a>
                            <h6>4. 我有进口服务需求，九创金服能为我提供什么服务？</h6>
                            <div class="about-details-txt">如果您有进口服务需求，通过九创金服的会员认证后，就可以在九创金服进行进口下单。九创金服与优质的物流公司，清关公司、保险公司进行深度合作，保障全程货物安全。同时，平台联合银行，为客户提供货物物流质押融资服务，解决客户短期资金困难，提升自身运营能力，提高市场竞争力。
                            </div>
                        </div>
                    </div>

                    <#--<div class="about-details-mode">-->
                        <#--<div style="font-size: 14px; font-weight: 600">采购问题</div>-->
                        <#--<br/>-->
                        <#--<a name="how_find_product"></a>-->
                        <#--<div class="about-details">-->
                            <#--<h6>1. 如何在九创金服寻找我需要的型号？</h6>-->
                            <#--<div class="about-details-txt">九创金服上有超过100万的工业电气产品信息，您可以按型号进行搜索，访问九创金服<a href="http://www.fruit.com">首页</a>，在上方的搜索框内输入您想要查询的型号，即可快速检索。<a href="${userguide_search_helper_url}">搜索指南</a></div>-->
                            <#--<div class="about-details-txt">如果不确定要采购的具体型号，您也可以在首页按照九创金服推荐的分类进行查询。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_multi_find"></a>-->
                            <#--<h6>2. 我要一次采购很多型号的产品，有什么方便的方法吗？</h6>-->
                            <#--<div class="about-details-txt">九创金服支持批量找货的功能，您可以在九创金服首页右侧找到一个批量填写型号和采购数量的窗口，在窗口内或者打开完整填写询货单<a href="/search/ids">页面</a>，填写您想要查询的型号和希望采购数量，提交询货单即可批量搜索授权分销商的报价。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="can_bargain"></a>-->
                            <#--<h6>3. 我可以和卖家讨价还价吗？</h6>-->
                            <#--<div class="about-details-txt">九创金服是一个连接交易双方的平台，由于工业电气产品的特殊性，我们鼓励采购方在提交订单后与分销商确认订单的价格和货期，然后再进行交易。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="what_order_process"></a>-->
                            <#--<h6>4. 九创金服上交易的流程是怎样的？</h6>-->
                            <#--<div class="about-details-txt">如果您是采购方，在九创金服提交订单后，如果要选择网上交易，可以选择支付宝、网上银行或九创金服担保的方式进行线上支付，支付成功后，分销商将收到通知并对订单进行后续处理。当您收到货后，如果对货品满意，请回到订单详情页确认收货，并对订单进行评价，至此交易就全部完成了。</div>-->
                            <#--<div class="about-details-txt">如果您是供货方，在收到的九创金服订单已经由采购方付款后，请尽快确认订单是否可接受，接受订单后请尽快发货，等采购方确认收货后，您就可以立即申请对担保在九创金服的货款进行提现了。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_pay"></a>-->
                            <#--<h6>5. 如何支付货款？</h6>-->
                            <#--<div class="about-details-txt">如果订单走网上交易流程，您可以选择支付宝、网上银行（含企业网银）等网上支付渠道进行担保支付，也可以选择“九创金服担保”方式，将订单货款转账给九创金服进行担保，九创金服工作人员核对无误后即可确认订单已支付，分销商就可以为您发货了。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_refund"></a>-->
                            <#--<h6>6. 收到货物后不满意，想要退货怎么办？</h6>-->
                            <#--<div class="about-details-txt">如果收到货物后不满意，请不要点击“确认收货”，直接选择“申请退货”，并和卖家直接沟通，如果卖家同意退货并在网站上确认，您就可以很快收到全部订单款项的退款。如果卖家不同意退货，九创金服工作人员将介入调解，直至给您一个满意的答复。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_bill"></a>-->
                            <#--<h6>7. 我将货款支付给九创金服进行担保，但发票由卖家开具，财务上应该如何做账？</h6>-->
                            <#--<div class="about-details-txt">在九创金服的网站服务条款中，有相应的介绍：-->
                            <#--</div>-->
                            <#--<div class="about-details-txt">2、关于交易的规则</div>-->
                            <#--<div class="about-details-txt">2.1添加产品描述条目。产品描述是由您提供的在九创金服站上展示的文字描述、图画和/或照片，可以是(a)对您拥有而您希望出售的产品的描述；或(b)对您正寻找的产品的描述。您须将该等产品描述归入正确的类目内。九创金服不对产品描述的准确性或内容负责。</div>-->
                            <#--<div class="about-details-txt">2.2用户在九创金服进行交易的相关支付将由九创金服作为按照下述方式提供保障：需方将产品全款打入九创金服的支付宝或快钱账号后，九创金服将自动通知供方发货。供方收到发货通知后安排发货并将物流信息（货运公司、运单号等必备信息）提交到九创金服并确认已发货。待需方收到货物并点击确认收货后，九创金服将在第一时间将需方全额货款打给供方（包括根据供方随时在其管理账户点击的提现申请后，打至供方指定银行账户中）。</div>-->
                            <#--<div class="about-details-txt">2.3就交易进行协商。交易供方通过在九创金服站上对拟交易商品进行明确描述，供需双方可通过在线QQ或线下进行相互协商。所有各方接纳报价及议价所涉及的九创金服用户有义务完成交易。除非在特殊情况下，诸如用户在您提出报价后实质性地更改对物品的描述或澄清任何文字输入错误，或您未能证实交易所涉及的用户的身份等，报价和承诺均不得撤回。</div>-->
                            <#--<div class="about-details-txt">2.4 运输方式及到达站港和费用依据供方网页发布产品为准</div>-->
                            <#--<div class="about-details-txt">2.5 发票：合同结算完成之后，供方将向需方开具国家规定的发票。</div>-->
                            <#--<br/>-->
                            <#--<div class="about-details-txt">同时您在平台自动生成的合同里也可以看到相关条款，上面也有买卖双方的公司名字，请把条款和合同交给财务，以备查账之用就可以了，做账完全没有问题。这是所有电商平台的统一操作方式，九创金服所有客户也都如此做账，请您放心。</div>-->
                            <#--<br/>-->
                            <#--<div class="about-details-txt">关联问题：交易时通过平台支付，如何开具发票？</div>-->
                                <#--<div class="about-details-txt">答：一、根据《中华人民共和国发票管理办法》（中华人民共和国国务院令第587号）第十九条规定：“销售商品、提供服务以及从事其他经营活动的单位和个人，对外发生经营业务收取款项，收款方应当向付款方开具发票；特殊情况下，由付款方向收款方开具发票。”-->
                                <#--</div><div class="about-details-txt">二、根据《中华人民共和国发票管理办法实施细则》（国家税务总局令第37号）第二十六条规定：“单位和个人必须在发生经营业务确认营业收入时开具增值税的发票。”-->
                        <#--</div><div class="about-details-txt">因此，支付平台（如翼支付、微信支付、支付宝等）只是资金转移的平台，顾客通过其为商家提供的货物、劳务或服务进行支付，增值税的发票由提供的货物、劳务或服务的商家向顾客开具。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="what_points"></a>-->
                            <#--<h6>8. 什么是九创金服积分，九创金服积分有什么作用？</h6>-->
                            <#--<div class="about-details-txt">什么是九创金服积分：</div>-->
                            <#--<div class="about-details-txt">九创金服积分是九创金服回馈用户的虚拟资产，用户在九创金服采购产品或参与平台不定期组织的活动即可获得积分，和信用卡积分类似，随着用户在九创金服上采购量的增加，积分也会逐渐增加，用户可以将累积到一定量的积分兑换成礼品。</div>-->
                            <#--<div class="about-details-txt">积分计算规则：</div>-->
                            <#--<div class="about-details-txt">常规情况下，用户成功完成了一单交易后（须确认收货），即可立即获得本单交易额乘以10%的积分数量，比如成功订单金额为2300.00元，则可获取的积分数额为230分。</div>-->
                            <#--<div class="about-details-txt">另外，九创金服会不定期举行一些活动，向积极参与活动的用户送出积分，具体规则以活动公告为准。</div>-->
                            <#--<div class="about-details-txt">积分如何兑换：</div>-->
                            <#--<div class="about-details-txt">您可以在“我的九创金服”首页查询到自己当前已获得的积分，超过2000积分的用户可以联系九创金服企业QQ（九创金服助手）申请兑换购物卡等礼品，成功兑换的积分将在礼品寄出后扣除。</div>-->
                        <#--</div>-->
                    <#--</div>-->

                    <#--<div class="about-details-mode">-->
                        <#--<div style="font-size: 14px; font-weight: 600">销售类问题</div>-->
                        <#--<br/>-->
                        <#--<a name="how_sell"></a>-->
                        <#--<div class="about-details">-->
                            <#--<h6>1. 如何在九创金服上架产品并销售？</h6>-->
                            <#--<div class="about-details-txt">九创金服仅允许品牌授权分销商在九创金服销售产品。</div>-->
                            <#--<div class="about-details-txt">如果您是品牌授权分销商，请先在<a href="http://www.fruit.com">九创金服</a>注册账号并通过企业认证，之后在我的九创金服<a href="/member/dashboard/home">页面</a>点击“我要卖货”，申请授权分销认证，提交您的授权品牌、证书编号、终止日期和证书图片，九创金服工作人员将会在第一时间联系您，对您的授权分销身份进行审核。</div>-->
                            <#--<div class="about-details-txt">通过授权分销认证审核后，您就可以在九创金服上架产品对外销售了。在我的九创金服点击“管理待售产品”菜单项目，然后再点击右上角链接“上传待售产品”，即可通过网页添加或上传excel表格的方式来上传待售产品。</div>-->
                            <#--<div class="about-details-txt">需要注意的事，成功上传的待售产品分为“尚未上架”和“上架待售”两种状态，“尚未上架”代表产品此时正处于下架维护状态，买方无法采购，“上架待售”产品可以被买方搜索到并下单采购，您可以随时更改产品的状态。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_sell_overstock"></a>-->
                            <#--<h6>2. 我的仓库里有滞销的库存产品，九创金服能帮我卖掉吗？</h6>-->
                            <#--<div class="about-details-txt">九创金服专门为品牌授权分销商的滞销库存产品设置了一个品类——“闲置库存”，如果您有全新未使用的滞销库存，可以将其以闲置库存身份上传到九创金服对外销售，针对价格有竞争力的闲置库存，九创金服会优先向广大有需求的分销商推荐，借助互联网快速传播的力量，帮助您快速清库存。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_lack_stock"></a>-->
                            <#--<h6>3. 如果接到订单后发现实际库存不足，我应该怎么办？</h6>-->
                            <#--<div class="about-details-txt">九创金服主要支持现货交易，针对买方已在线支付的订单，卖方必须在3天内确认订单，5天内发出货品并在线确认，如果您能在短时间内补足订单内商品，请先与买方沟通，并在九创金服限制的逾期时间内完成相关操作。如果您无法及时补货，请与买方沟通解释后再取消订单。</div>-->
                        <#--</div>-->
                        <#--<div class="about-details">-->
                            <#--<a name="how_withdraw"></a>-->
                            <#--<h6>4. 如何在交易成功后提取货款？</h6>-->
                            <#--<div class="about-details-txt">买方确认收货后即交易成功，您可以在我的九创金服点击“提现管理”菜单项目，设置好公司提现账户，即可对交易成功订单的金额进行提现，您也可以点击“全部申请提现”按钮，对当前所有可提现的订单进行合并提现。</div>-->
                        <#--</div>-->
                    <#--</div>-->
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
