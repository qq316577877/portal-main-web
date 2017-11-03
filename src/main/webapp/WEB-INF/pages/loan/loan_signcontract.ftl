<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>获取额度</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/limitGenerality.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/MyLoan.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/loanGetLines.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
<#--<link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />-->
<#--<link rel="stylesheet" href="${static_file_server}/plugin/calculator/calculator.css"/>-->
<#--<link rel="stylesheet" href="${static_file_server}/plugin/jQueryPage/jquery.page.css"/>-->

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/plugin/jQueryPage/jquery.page.js"></script>
    <script src="${static_file_server}/plugin/pdf/pdfobject.min.js"></script>
    <script src="${static_file_server}/js/pages/getLoanLines/getLoanLines.main.js"></script>
<#--<script src="${static_file_server}/plugin/calculator/calculator.js"></script>-->
<#--<script src="${static_file_server}/plugin/moment/moment.min.js"></script>-->
</head>

<body>


<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">

<!--main-->
<div class="line"></div>
<div class="main w clearfix">
    <h2 id="header">获取额度</h2>
    <div class="main-head">
        <ul class="clearfix">
            <li>
                <span>1</span>
                <p>开通安心签账户</p>
            </li>
            <li class="on">
                <span class="on">2</span>
                <p class="colorOn">签订借款合同</p>
            </li>
        </ul>
    </div>

    <div id="showError" style="margin-left:200px;height:200px; display: none"><h3>您的合同信息客户经理正在快马加鞭的录入哦，请稍后尝试!</h3></div>

    <div id="pdf1" style="width:700px; height:600px; display: block">
        该浏览器不支持pdf预览，
        <a id="downloanContract" href="~/pdf/CGVET22-08-2011V2P.pdf">请点击下载预览PDF</a>
    </div>
    <input type="button" id="sub" value="确认签订电子合同">
</div>

<div id="mask">
    <div id="pop">
        <div id="info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您正在对安心签发出签名请求，委托安心签调用数字证书签署借款合同，数字证书一经调用立即生效。</div>
        <div class="userInfo">
            <div class="userName">用户信息：
            </div>
            <div id="userTel"></div>
        </div>
        <div class="userInfo">
            <div class="userName">验证码：</div>
            <input type="text" maxlength="6" id="capth">
            <input type="button" value="获取验证码" id="sendBtn" >
            <p id="errormsg" style="text-align: center;color:red;visibility:hidden;">
                请输入有效的验证码
            </p>
        </div>

        <div style="margin-left: 120px; margin-top: 16px;">
            <input type="button" class="btn1" id="grantBtn" value="授权">
            <input type="button" class="btn1" id="cancelBtn" value="取消">

        </div>
    </div>
</div>

<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>
<script>
    __DATA = ${__DATA};
</script>

</html>