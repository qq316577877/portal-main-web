<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>申请贷款-实名认证</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/loanAuthentication.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/loanAuthentication/loanAuthentication.main.js"></script>
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<div class="main w">
    <h3>申请贷款</h3>

    <div class="main-head">
        <ul class="clearfix">
            <li class="on">
                <span class="on">1</span>
                <p class="colorOn">实名认证</p>
            </li>
            <li><span>2</span><p>提交贷款申请</p></li>
        </ul>
    </div>

    <form action="">
        <label>
            <div class="input-l">真实姓名:</div>
            <p class="datum" id="name"></p>
        </label><br/>
        <label>
            <div class="input-l">身份证号:</div>
            <p class="datum" id="identity"></p>
        </label><br/>
        <label>
            <div class="input-l"> 银行：</div>
            <select name="bank" id="bankId">
                <option value="0">请选择银行卡</option>
            </select>
            <i class="iconfont icon-gou bankId-true"></i>
            <div class="bankId-false"><span class="wrong"></span>请选择银行</div>
            <div class="bankId-verify-w"></div>
            <div class="bankId-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l">银行卡号：</div>
            <input type="text" placeholder="请输入相应的银行卡号" maxlength="21" id="bankCard"/>
            <i class="iconfont icon-gou bankCard-true"></i>
            <div class="bankCard-false"><span class="wrong"></span>请输入银行卡号(整数)</div>
            <div class="bankCard-verify-w"></div>
            <div class="bankCard-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l"> 手机号码：</div>
            <input type="text" placeholder="请输入银行预留手机号" maxlength="13" id="mobile"/>
            <i class="iconfont icon-gou tel-true"></i>
            <div class="tel-false"><span class="wrong"></span>请输入11位手机号</div>
            <div class="tel-verify-w"></div>
            <div class="tel-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l"> 图形验证码：</div>
            <input maxlength="4" class="fl" id="oldImgCapth" type="text">
            <div class="main-img fl">
                <img src="http://24isbw.natappfree.cc/fruitupload/gcaptcha/2017-10-19/53c79610372a4fe9840f65f23d2cf29d-43ab68d7b52446ebadc13160c8bbd1e2.png" alt="">
            </div>
            <a href="javascript:;" id="changeCapth">看不清？换一张</a>
            <div class="tel-false imgCapth-false"><span class="wrong"></span>请输入图形验证码</div>
            <div class="tel-verify-w"></div>
            <div class="tel-verify-r"></div>
        </label><br/>
        <label>
            <div class="input-l"> 短信验证码：</div>
            <!-- 889337-->
            <input type="text" class="input-pass" maxlength="6" placeholder="请输入短信验证码"/>
            <div class="main-passage">
                <input type="button"id="btn-passage" value="免费获取验证码">
            </div>
            <i class="iconfont icon-gou message-true"></i>
            <div class="message-false"><span class="wrong"></span>验证码错误</div>
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

<script>
    __DATA = ${__DATA}
</script>
</body>
</html>