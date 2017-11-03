<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>认证审核状态</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/auditStatus.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/auditStatus/auditStatus.main.js"></script>
</head>

<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<div class="line"></div>
<div class="main w clearfix">
<#include "/common/user_nav.ftl">

    <div class="main-r fl">
        <h3>会员认证状态:<span id="typeText">已通过</span></h3>
        <h3 class="cause"><span style="float: left">未通过理由：</span><span id="rejectNote">身份证正面不清晰，请修改重新提交</span></h3>
        <div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>
        <div class="mainBox two">
            <label>
                <div class="input-l"> 会员类型：</div>
                <p id="stateType" class="dis-left"></p>
            </label><br/>
            <div id="enterprise" class="showHide">
                <label>
                    <div class="input-l"> 企业名称：</div>
                    <p id="enterpriseName" class="dis-left">浙江创意科技农产品进口服务有限公司</p>
                </label><br/>
                <label>
                    <div class="input-l"> 证照号：</div>
                    <p id="credential" class="dis-left">9133032656698482XY</p>
                </label><br />
                <label>
                    <div class="input-l"> 所在地区 ：</div>
                    <p id="area" class="dis-left">中国大陆浙江省温州市平阳县</p>
                </label><br/>
                <label>
                    <div class="input-l"> 详细地址：</div>
                    <p id="address" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <div class="input-l"> 联系电话：</div>
                    <p id="phoneNum" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <div class="input-l"> 法人姓名：</div>
                    <p id="name" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <div class="input-l"> 法人身份证号：</div>
                    <p id="identity" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <ul class="imgList clearfix">
                        <li>
                            <p>营业执照/社会信用代码证</p>
                            <img id="licence" src="${static_file_server}/assets/img/shangchuanttubiao.png"/>
                        </li>
                        <li>
                            <p>法人身份证正面</p>
                            <img id="identityFront" src="${static_file_server}/assets/img/shangchuanttubiao.png" />
                        </li>
                        <li>
                            <p>法人身份证反面</p>
                            <img id="identityBack" src="${static_file_server}/assets/img/shangchuanttubiao.png"/>
                        </li>
                    </ul>
                </label>
            </div>

            <div id="pre" class="showHide">
                <label>
                    <div class="input-l">姓名：</div>
                    <p id="pre-name" class="dis-left">浙江创意科技农产品进口服务有限公司</p>
                </label><br/>
                <label>
                    <div class="input-l"> 身份证号：</div>
                    <p id="pre-identity" class="dis-left">9133032656698482XY</p>
                </label><br />
                <label>
                    <div class="input-l"> 所在地区 ：</div>
                    <p id="pre-area" class="dis-left">中国大陆浙江省温州市平阳县</p>

                </label><br/>
                <label>
                    <div class="input-l"> 详细地址：</div>
                    <p id="pre-address" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <div class="input-l"> 联系电话：</div>
                    <p id="pre-phoneNum" class="dis-left">平阳县万全镇江畔村（村委会办公楼边）</p>
                </label><br/>
                <label>
                    <ul class="imgList clearfix">
                        <li>
                            <p>营业执照/社会信用代码证</p>
                            <img id="pre-attachmentOne" src="${static_file_server}/assets/img/shangchuanttubiao.png"/>
                        </li>
                        <li>
                            <p>&nbsp;</p>
                            <img id="pre-attachmentTwo" src="${static_file_server}/assets/img/shangchuanttubiao.png" />
                        </li>
                        <li>
                            <p>法人身份证正面</p>
                            <img id="pre-identityFront" src="${static_file_server}/assets/img/shangchuanttubiao.png"/>
                        </li>
                        <li>
                            <p>法人身份证反面</p>
                            <img id="pre-identityBack" src="${static_file_server}/assets/img/shangchuanttubiao.png"/>
                        </li>
                    </ul>
                </label><br/>
            </div>
            <a href="###" class="sub">修改</a>
        </div>
    </div>
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>


</body>

</html>
