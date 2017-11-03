<!doctype html>
<html lang="en">

	<head>
		<meta charset="UTF-8">
		<#include "/common/seo.ftl">
		<title>公司新闻</title>
			<link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
			<link rel="stylesheet" href="${static_file_server}/assets/css/account/news.css" />
			<link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css" />
			<script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
			<script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
			
			
			
	</head>

	<body>
		<!--顶部通栏-->
		<#include "/common/top.ftl">
		<!--logo-->
		<#include "/common/header.ftl">

		<!--main-->
		<div class="main w">
			<div class="main-top">
				<p>首页><i>新闻</i>><i>公司新闻</i></p>
			</div>
			<div class="main-content">
				<h1>一带一路出国门,中越合作稳步升</h1>
				<time>2017-03-30</time>
				<p class="font">
					如今，在中国——东盟自贸区全面建成的助推下，中国与东盟各国在农业资源与流动上愈加频繁，强化市场开拓成为各国适应国际竞争新形势的首选。借助“一带一路”的机遇，我公司始终坚持走出国门，开放合作。
				</p>
				<img src="${static_file_server}/assets/img/new-1.jpg" />
				<p class="font">
					2017年3月30日，我公司董事长邱珍君在越南胡志明接受了越南农业部领导会见，并于越南时间10时00分召开会议。此次会议主要目的是促进双方国际农产品双向采购合作。
				</p>
				<img src="${static_file_server}/assets/img/new-2.jpg" />
				<p class="font">
					双方主要围绕中越俩国农产品展开讨论，就农产品生产、销售、仓储等细节深入分析，最终双方达成一致意见。
				</p>
			</div>

		</div>


		<!--底部通栏-->
		<@bottom.footer pos_fixed=0/>
	</body>

</html>