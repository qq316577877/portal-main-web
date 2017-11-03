<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>重置密码</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/findPwdReset.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/findPwdReset/findPwdReset.main.js"></script>
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
			
			<li  class="on">
				<span class="on">2</span>
				<p class="colorOn">设置新密码</p>
			</li>
			
			<li>
				<span class="iconfont icon-gou"></span>
				<p>完成</p>
			</li>
		</ul>
	</div>
	
    <form action="">
       <label>
            <div class="input-l"><span>*</span> 设置密码：</div>
            <input type="password" id="pwd" placeholder="请输入6-20位字符"/>
            <span class="hint">请输入6-20位字符，需包含大写字母、小写字母、数字、符号，至少两种</span>
            <i class="iconfont icon-gou pwd-true"></i>
            <div class="pwd-false"><span class="wrong"></span>请输入6-20位字符，需包含大写字母、小写字母、数字、符号，至少两种</div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 重复密码：</div>
            <input type="password" id="pwd1" placeholder="请再次输入密码"/>
            <i class="iconfont icon-gou pwd1-true"></i>
            <div class="pwd1-false"><span class="wrong"></span>两次密码不一致</div>
        </label><br/>
        
        <input type="button" class="sub" value="下一步"/>
    </form>
</div>
<!--mask-->
<div class="mask">
	<div class="inform" id="inform">
        <div class="inform-head">
            <span class="fl">温馨提示</span>
            <i class="iconfont icon-chacha cancel fr" id="cancel"></i>
        </div>
        <div class="inform-body">
          	<div class="view">
          		<p id="view-font"></p>
          	</div>
			<div class="btn-ca">
                <button id="btn">确定</button>
            </div>
			
        </div>

    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>
</body>
</html>