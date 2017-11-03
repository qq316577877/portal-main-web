<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员中心</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/PersonageAndcompany2.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>

    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${static_file_server}/plugin/alert/alert.css"/>


<!--    <script type="text/javascript" src="${static_file_server}/plugin/webuploader/upload.js"></script>   -->
<!--    <script type="text/javascript" src="${static_file_server}/plugin/webuploader/webuploader.min.js"></script>  -->
<!--    <script type="text/javascript" src="${static_file_server}/plugin/webuploader/upflie.js" charset="utf-8"></script>   -->
    <script src="${static_file_server}/js/pages/vipPersonage/vipPersonage.main.js"></script>
   
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<!--main-->
<div class="line"></div>
<div class="main w clearfix">
<#include "/common/user_nav.ftl">
    <div class="main-r fl">
        <h3>会员认证</h3>
		<div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>
        <form action="" id="personal">
            <label>
                <div class="input-l"><span>*</span> 会员类型：</div>
                <div class="memEntBOX">
                    <input type="radio" name="mORe" value="2"  />企业
                </div>
                <div class="memEntBOX">
                    <input type="radio" name="mORe" value="1" checked/>个人
                </div>
            </label>

            <div class="vcBox ">

                    <div class="vORc personage">
                        <label>
                            <div class="input-l"><span>*</span> 姓名：</div>
                            <input type="text" id="per-name" name="per_name"/>
                            <p class="red judge"></p>
                        </label><br/>

                        <label>
                            <div class="input-l"><span>*</span> 身份证号：</div>
                            <input type="text" id="per-identity" placeholder="个人居民身份证号" name="per_identity" maxlength="18"/>
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
                            <input type="text"  id="per-address" name="per_address" maxlength="120"/>
                            <p class="red judge"></p>
                        </label><br/>
                        <label>
                            <div class="input-l"><span>*</span> 联系电话：</div>
                            <input type="text" id="per-phoneNum" name="per_phoneNum" name="" maxlength="20"/>
                            <p class="red judge"></p>
                        </label><br/>

                        <label>
                            <div class="input-l"><span>*</span> 上传证件照片：</div>

                            <div class="uploadingBox">
                                <div class="image-upload mb mr">
                                    <p class="warning red">上传业务合同或凭证</p>
                                    <div class="imgBox">
                                        <div id="filePicker1">
                                            <img id="showImg1"  src="${static_file_server}/assets/img/upload.gif" />
                                        </div>

                                    </div>
                                    <p class="red judge"></p>
                                </div>
                                <div class="image-upload">
                                    <div class="imgBox mt">
                                        <div id="filePicker4">
                                            <img id="showImg4"  src="${static_file_server}/assets/img/upload.gif" />
                                        </div>
                                    </div>
                                    <p class="red judge"></p>
                                </div>
                            </div>
                        </label><br/>
                        <label>
                            <div class="input-l"></div>
                            <div class="image-upload mr">
                                <p class="warning">个人身份证正面</p>
                                <div class="imgBox">
                                    <div id="filePicker22">
                                        <img id="showImg22"  src="${static_file_server}/assets/img/upload.gif" />
                                    </div>
                                </div>
                                <p class="red judge"></p>
                            </div>
                            <div class="image-upload">
                                <p class="warning">个人身份证反面</p>
                                <div class="imgBox">
                                    <div id="filePicker3">
                                        <img id="showImg3"  src="${static_file_server}/assets/img/upload.gif" />
                                    </div>
                                </div>
                                <p class="red judge"></p>
                            </div>
                    </div>
            </div>
            <p class="hint"><span>温馨提示：</span>证照仅支持3MB以下的jpg、gif、png、pdf、jpeg格式的图片</p>
            </label><br/>
            <!-- <input type="submit" class="sub" value="提交认证"/>-->
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