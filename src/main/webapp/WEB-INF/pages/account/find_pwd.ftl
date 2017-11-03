<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>找回密码</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/findPwd.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/findPwd/findPwd.main.js"></script>
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
			<li class="on">
				<span class="on">1</span>
				<p class="colorOn">填写手机号并验证</p>
			</li>
			<li><span>2</span><p>设置新密码</p></li>
			<li><span class="iconfont icon-gou"></span><p>完成</p></li>
		</ul>
	</div>
	

    <form action="" id="findPwdForm">
        <label>
            <div class="input-l"><span>*</span> 手机号码：</div>
            <input type="text" placeholder="请输入11位手机号" id="tel" name="tel" maxlength="11"/>
            <i class="iconfont icon-gou tel-true"></i>
            <div class="tel-false"><span class="wrong"></span>请输入11位手机号</div>
            <div class="tel-verify-w"></div>
            <div class="tel-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 图形验证码：</div>
            <input type="text" name="patterning" maxlength="4" id="patterning" placeholder="请输入图形验证码"/>
            <div class="main-img">
                <img src="#" alt=""/>
            </div>
            <a href="#" class="main-p">
                看不清？换一张
            </a>
            <i class="iconfont icon-gou img-true"></i>
            <div class="img-false"><span class="wrong"></span>验证码不能为空</div>
        </label><br/>
        <label>
            <div class="input-l"><span>*</span> 短信验证码：</div>
            <input type="text" maxlength="6" class="input-pass" placeholder="请输入短信验证码" id="telCode" name="telCode"/>
            <div class="main-passage">
                <input type="button" id="btn-passage" value="免费获取验证码"/>
            </div>
            <i class="iconfont icon-gou message-true"></i>
            <div class="message-false"><span class="wrong"></span>验证码错误</div>
        </label><br/>
        
        <input type="button" class="sub" value="下一步"/>
    </form>
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>
</body>
</html>