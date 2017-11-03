<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>九创金服</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/indexBase.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/home/index.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/plugin/jquery.roundabout/jquery.roundabout-shapes.js" type="text/javascript" charset="utf-8"></script>
    <script src="${static_file_server}/plugin/jquery.roundabout/jquery.roundabout.js" type="text/javascript" charset="utf-8"></script>
    </head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->

<div class="logoBox">

    <#include "/common/home_logo_top.ftl">

    <div id="myCarousel" class="carousel slide clearfix" style="width:100%;">
        <ol class="carousel-indicators">
            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
            <li data-target="#myCarousel" data-slide-to="1"></li>
            <li data-target="#myCarousel" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="item active">
                <a href=""><img src="${static_file_server}/assets/img/banner1.jpg" alt="第一张"></a>
            </div>
            <div class="item">
                <a href=""><img src="${static_file_server}/assets/img/banner2.jpg" alt="第二张"></a>
            </div>
            <div class="item">
                <a href=""><img src="${static_file_server}/assets/img/banner3.jpg" alt="第三张"></a>
            </div>
        </div>
        <a href="#myCarousel" data-slide="prev" class="carousel-control left">
            <!--<span class="glyphicon glyphicon-chevron-left"></span>-->
        </a>
        <a href="#myCarousel" data-slide="next" class="carousel-control right">
           <!-- <span class="glyphicon glyphicon-chevron-right"></span>-->
        </a>
    </div>

</div>

<!--main-->
<div class="main mt60">

    <div class="our-advantages">
        <h1 class="tit">我们的优势</h1>
        <p class="state">创意科技农业供应链平台，整合农产品产地资源、物流资源及金融资源， 为客户提供更优的价格，更好的服务，让商贸变得更便捷。
        </p>
        <ul class="pictureList clearfix">
            <li>
                <img src="${static_file_server}/assets/img/advantage1.jpg" alt="专业采购团队" title="专业采购团队" />
                <h2>专业采购团队</h2>
                <p class="describe">亲赴产地挑选最优的农产品，与当地农商达成长期合作 关系，为客户提供优质农产品
                </p>
            </li>
            <li>
                <img src="${static_file_server}/assets/img/advantage2.jpg" alt="正规物流合作" title="正规物流合作" />
                <h2>正规物流合作</h2>
                <p class="describe">拥有长期合作的正规物流公司，可为客户提供 定制的物流方案，灵活方便
                </p>
            </li>
            <li>
                <img src="${static_file_server}/assets/img/advantage3.jpg" alt="提供金融服务" title="提供金融服务" />
                <h2>提供金融服务</h2>
                <p class="describe">可为客户提供相应的资金服务，减少客户 购买压力，增加周转资金
                </p>
            </li>
        </ul>
    </div>

    <div class="our-products mt60">
        <h1 class="tit">我们的产品</h1>
        <ul class="roundabout" id="myroundabout">
            <li>
                <a href="###"><img src="${static_file_server}/assets/img/product1.jpg"></a>
                <p>越南火龙果一级大果</p>
            </li>
           
            <li>
                <a href="###"><img src="${static_file_server}/assets/img/product2.jpg"></a>
                <p>越南红心火龙果一级大果</p>
            </li>
            <li>
                <a href="###"><img src="${static_file_server}/assets/img/product4.jpg"></a>
                <p>南美大虾</p>
            </li>
            <li>
                <a href="###"><img src="${static_file_server}/assets/img/product5.jpg"></a>
                <p>越南香米</p>
            </li>
        </ul>
    </div>

			<div class="service-content mt90">
				<h1 class="tit">服务内容</h1>
				<ul class="serviceList clearfix">
					<li class="tu1">
						<div class="soludiv">
							<div class="margin"></div>
							<span class="icon1"></span>
							<h3>进口服务</h3>
							<p>专业进口各种农产品</p>
							<p class="mb38">足够配额保证</p>
							<a href="${import_service_page_url}" class="but">点击了解更多</a>
						</div>

					</li>
					<li class="tu2">
						<div class="soludiv">
							<div class="margin"></div>
							<span class="icon2"></span>
							<h3>物流服务</h3>
							<p>优质正规货代</p>
							<p class="mb38">按需制定方案</p>
							<a href="${logistics_service_page_url}" class="but">点击了解更多</a>
						</div>

					</li>
					<li class="tu3">
						<div class="soludiv">
							<div class="margin"></div>
							<span class="icon3"></span>
							<h3>通关服务</h3>
							<p>全国各大口岸便捷通关</p>
							<p class="mb38">专业合规处理通关事宜</p>
							<a href="${customs_clea_service_page_url}" class="but">点击了解更多</a>
						</div>

					</li>
					<li class="tu4">
						<div class="soludiv">
							<div class="margin"></div>
							<span class="icon4"></span>
							<h3>保险服务</h3>
							<p>第三方保险公司承担</p>
							<p class="mb38">全程保障,赔付90%</p>
							<#--<a href="###" class="but">点击了解更多</a>-->
						</div>

					</li>
					<li class="tu5">
						<div class="soludiv">
							<div class="margin"></div>
							<span class="icon5"></span>
							<h3>资金服务</h3>
							<p>质押贷款</p>
							<p class="mb38">24小时内放款</p>
							<a href="${fund_service_page_url}" class="but">点击了解更多</a>
						</div>

					</li>
				</ul>
			</div>

			<div class="service-process mt90">
				<h1 class="tit">服务流程</h1>
				<div class="processBox">
					<div class="process">
						<ul class="clearfix">
							<em class="dot5"></em>
							<li>
								<i class="liuc1"></i>
								<p>注册</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc2"></i>
								<p>会员认证</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc3"></i>
								<p>在线下单</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc4"></i>
								<p>资金服务</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc5"></i>
								<p>发货</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc6"></i>
								<p>结算</p>
							</li>
							<em class="dot4"></em>
							<li>
								<i class="liuc7"></i>
								<p>收货</p>
							</li>
							<em class="dot5"></em>
						</ul>
						<#--<a href="###" class="btn">点击咨询详情</a>-->
					</div>
				</div>

				<div class="press-release mt60 clearfix">
					<h1 class="tit">新闻公告</h1>
					<div class="press-box">
						<div class="press-l">
							<img src=""/>
						</div>
						<div class="press-r">
							<div class="btnBox">
								<button class="press-btn mr48 on1 tab">公司新闻</button>
								<button class="press-btn mr48 tab">行业资讯</button>
								<#--<button class="press-btn skip">更多</button>-->
							</div>

                    <ul class="list">
                    </ul>

                </div>
            </div>
        </div>

        <div class="partner mt60">
            <h1 class="tit">合作伙伴</h1>
            <div class="img-box">
                <img src="${static_file_server}/assets/img/partner1.jpg" class="line" />
                <img src="${static_file_server}/assets/img/partner2.jpg" class="line" />
                <img src="${static_file_server}/assets/img/partner3.jpg" class="line" />
                <img src="${static_file_server}/assets/img/partner4.jpg" />
             </div>
        </div>
    </div>
</div>
<!--底部通栏-->
<div class="f-Box">
<@bottom.footer  add_qa=1/>
</div>

<script>
    __DATA = ${__DATA}
</script>


<script src="${static_file_server}/js/pages/index/index.main.js"></script>
</body>
</html>