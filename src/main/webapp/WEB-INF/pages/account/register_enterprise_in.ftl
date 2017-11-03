<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员中心</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/PersonageAndcompany2.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>

    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />
    <link rel="stylesheet" type="text/css" href="${static_file_server}/plugin/alert/alert.css"/>

<!--    <script type="text/javascript" src="${static_file_server}/plugin/webuploader/upload.js"></script>   -->
<!--     <script type="text/javascript" src="${static_file_server}/plugin/webuploader/webuploader.min.js"></script>     -->
<!--    <script type="text/javascript" src="${static_file_server}/plugin/webuploader/upflie.js" charset="utf-8"></script>   -->
    <script src="${static_file_server}/js/pages/vipCompany/vipCompany.main.js"></script>
    
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
        <h3>会员认证</h3>
        <div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>
        <form id="listForm1" name="listForm1" method="post" enctype="multipart/form-data">
            <label>
                <div class="input-l"><span>*</span> 会员类型：</div>
                <div class="memEntBOX">
                    <input type="radio" name="mORe" value="2"  checked/>企业
                </div>
                <div class="memEntBOX">
                    <input type="radio" name="mORe" value="1"/>个人
                </div>
            </label><br/>
            <label>
                <div class="input-l"><span>*</span> 企业名称：</div>
                <input type="text" id="enterpriseName" name="enterpriseName"/>
                <p class="red judge" id="enterpriseNameJudge"></p>
            </label><br/>
            <label>
                <div class="input-l"><span>*</span> 证照号：</div>
                <input type="text" id="credential" name="credential" maxlength="18" placeholder="营业执照号或社会信用代码证号"/>
                <p class="red judge"></p>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 所在地区 ：</div>
                <div class="selectList">
                    <select id="country" name="country"><option value="0">选择国家</option></select>
                    <select class="province" id="province" name="province"><option value="0">选择省</option></select>
                    <select class="city" id="city" name="city"><option value="0">选择市</option></select>
                    <select class="district mr0" id="district" name="district"><option value="0">选择区</option></select>
                    <p class="red judge"></p>
                </div>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 详细地址：</div>
                <input id="address" type="text" name="address"/>
                <p class="red judge"></p>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 联系电话：</div>
                <input id="phoneNum" type="text" name="phoneNum" maxlength="20" />
                <p class="red judge"></p>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 法人姓名：</div>
                <input id="legalPerson" name="legalPerson" type="text"/>
                <p class="red judge"></p>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 法人身份证号：</div>
                <input id="identity" type="text" name="identity" maxlength="18" placeholder="法人身份证号码"/>
                <p class="red judge"></p>
            </label><br/>

            <label>
                <div class="input-l"><span>*</span> 上传照片：</div>

                <div class="uploadingBox">
                    <div class="image-upload mb">
                        <p class="warning">营业执照/社会信用代码证</p>
                        <div class="imgBox">
                            <div  id="filePicker1">
                                <img id="showImg1"  src="${static_file_server}/assets/img/upload.gif" />
                            </div>
                        </div>
                        <p class="red judge"></p>
                    </div>
                </div>
            </label><br/>
            <label>
                <div class="input-l"></div>
                <div class="image-upload mr">
                    <p class="warning">法人身份证正面</p>
                    <div  class="imgBox">
                        <div id="filePicker22">
                            <img id="showImg22"  src="${static_file_server}/assets/img/upload.gif"/>
                        </div>
                    </div>
                    <p class="red judge"></p>
                </div>
                <div class="image-upload">
                    <p class="warning">法人身份证反面</p>
                    <div  class="imgBox">
                        <div id="filePicker3">
                            <img id="showImg3"  src="${static_file_server}/assets/img/upload.gif"/>
                        </div>
                    </div>
                    <p class="red judge"></p>
                </div>
                <p class="hint"><span>温馨提示：</span>证照仅支持3MB以下的jpg、gif、png、pdf、jpeg格式的图片</p>
            </label><br/>
            <a href="###" class="sub">提交认证</a>
        </form>
    </div>
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