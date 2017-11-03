<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <#include "/common/seo.ftl">
    <title>申请贷款-实名认证</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/loan/loanCertificate.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->

<div class="main w">
    <h3>获取额度</h3>
    <div class="main-head">
        <ul class="clearfix">
            <li class="on">
                <span class="on">1</span>
                <p class="colorOn">开通安心签账户</p>
            </li>
            <li>
                <span>2</span>
                <p>签订借款合同</p>
            </li>
        </ul>
    </div>

    <form action="">
        <h2>
            用户须知：
        </h2>
        <ul class="certificateMsg">
            <li>1、在本平台签署电子合同需接受安心签服务并确认开通安心签账户；</li>
            <li>2、由于安心签为您开立安心签账户时需要确认您的身份，您点击下方【开通安心签账户】即视为您同意平台方将您的基础身份信息传输给安心签；</li>
            <li>3、请您知晓，开通安心签账户后，安心签将为您颁发数字证书，您的数字证书将托管在安心签云平台，并授权安心签在满足条件时签署。</li>
            <li>4、为保持电子合同的完整性，请您自行上传能够代表您身份的个人印章电子版。</li>
        </ul>
        <label class="sealImg">
            <div class="image-upload">
                <h2 class="warning">个人印章</h2>
                <div  class="imgBox">
                    <div id="filePicker3">
                        <img id="showImg3"  src="${static_file_server}/assets/img/upload.gif"/>
                    </div>
                </div>
                <p class="red judge">请上传图片</p>
            </div>
        </label>
        <p class="hint"><span>温馨提示：</span>印章仅支持尺寸200*200以内、大小3MB以下，背景透明的png格式的图片</p>
        <div >
            <input type="checkbox" id="agreementcbx" class="input-box"/>
            <div class="checkbox2">阅读并接受
                <a target="_blank" href="https://www.anxinsign.com/Web/duty/use_agreement.html">《安心签平台服务协议》</a>
                <a target="_blank"  href="https://www.anxinsign.com/Web/duty/use_agreement.html">《CFCA数字证书服务协议》</a>
                <a target="_blank"  href="https://www.anxinsign.com/Web/duty/privacy_policy.html">、《隐私声明》</a>
            </div>
        </div>
        <p class="red agreement">请先阅读并接受服务协议等</p>
        <br/>

        <div class="submitdiv"><input type="button" id="signCertificate" value="开通安心签账户"/></div>
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

<script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
<script src="${static_file_server}/js/pages/loanCertificate/loanCertificate.main.js"></script>

</body>
</html>