<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>重置密码成功</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/findPwdOk.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <#--<script src="${static_file_server}/js/pages/findPwdOk/findPwdOk.main.js"></script>-->
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<div class="main w">
    <h3>通过手机号找回密码</h3>
	
	<div class="main-head">
		<ul class="clearfix">
			<li class="oldOn" >
				<span class="oldOn">1</span>
				<p class="oldcolorOn">填写手机号并验证</p>
			</li>
			
			<li  class="oldOn">
				<span class="oldOn">2</span>
				<p class="oldcolorOn">设置新密码</p>
			</li>
			
			<li class="on">
				<span class="iconfont icon-gou on"></span>
				<p class="colorOn">完成</p>
			</li>
		</ul>
	</div>
	
	<div class="main-content">
			<i class="iconfont icon-gou oldcolorOn"></i>
			<div class="contentBox">
				<p class="oldcolorOn fs">新密码设置成功！</p>
				<p class="small">请牢记您新设置的密码</p>
			</div>
	</div>
  
  	<div class="main-foot">
  		<ul>
  			<li class="mf"><a class="mf-btn" href="${dashboard_url}">进入会员中心</a></li>
  			<li class="mf"><a class="mf-btn" href="${create_order_page_url}">进口下单</a></li>
  			<li class="mf"><a class="mf-btn" href="${fund_service_page_url}">资金服务</a></li>
  		</ul>
  	</div>
  	
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>
</body>
</html>