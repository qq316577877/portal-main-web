<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>申请贷款-提交申请</title>

    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/loanDatum.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/loanDatum/loanDatum.main.js"></script>

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
            <li class="oldOn">
                <span class="oldOn">1</span>
                <p class="oldcolorOn">实名认证</p>
            </li>
            <li class="on">
                <span class="on">2</span>
                <p class="colorOn">提交贷款申请</p>
            </li>
        </ul>
    </div>

    <form action="">
        <label>
            <div class="input-l">真实姓名:</div>
            <p class="datum" id="username"></p>
        </label><br/>
        <label>
            <div class="input-l">身份证号:</div>
            <p class="datum" id="identity">000000000000000000</p>
        </label><br/>
        <label>
            <div class="input-l">手机号:</div>
            <p class="datum" id="mobile">00000000000</p>
        </label><br/>
        <label>
            <div class="input-l"> 婚姻状况：</div>
            <div class="radeioBox">

            </div>
        </label><br/>

        <div class="showHide">
            <label>
                <div class="input-l">配偶姓名：</div>
                <input type="text" id="partnerName" maxlength="20" placeholder="请输入配偶姓名"/>
                <i class="iconfont icon-gou true"></i>
                <div class="false"><span class="wrong"></span>请输入配偶姓名</div>
                <div class="verify-w"></div>
                <div class="verify-r"></div>
            </label><br/>
            <label>
                <div class="input-l"> 配偶身份证号：</div>
                <input type="text" placeholder="请输入配偶身份证号" maxlength="20" id="partnerIdentity"/>
                <i class="iconfont icon-gou true"></i>
                <div class="false"><span class="wrong"></span>请输入配偶身份证号</div>
                <div class="verify-w"></div>
                <div class="verify-r"></div>
            </label>
        </div>

        <label>
            <div class="input-l">所在地区 ：</div>
            <div class="selectList">
                <select class="country" id="country"><option value="0">选国家</option></select>
                <select class="province"><option>选择省</option value="0"></select>
                <select class="city"><option>选择市</option value="0"></select>
                <select class="district mr0"><option value="0">选择区</option></select>
                <p class="red judge">请选择国家/省/市/区</p>
            </div>

        </label><br/>
        <label>
            <div class="input-l"> 详细地址：</div>
            <input type="text" placeholder="请输入详细地址" maxlength="40" id="address"/>
            <i class="iconfont icon-gou true"></i>
            <div class="false"><span class="wrong"></span>请输入详细地址</div>
            <div class="verify-w"></div>
            <div class="verify-r"></div>
        </label><br/>
        <p id="warn">请务必填写真实准确的地址</p>
        <div id="read"><input type="checkbox"  id="checkbox" />同意
            <a href="${letter_url}" target=_blank>九江银行查询人民银行征信记录授权协议</a>
            <p class="red judge">请阅读/勾选同意</p>
        </div>
        <input type="button" class="sub" value="提交申请" />
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