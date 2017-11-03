<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>供应商</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/supplier.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/js/pages/supplier/supplier.main.js"></script>
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
        <div class="main-r-show">
      		<img src="${static_file_server}/assets/img/supplier.png"/>
      		<p class="hint">您还没有相关供应商哦&nbsp;!</p>
      		<button class="add-ship-site pop-up">新增供应商</button>
        </div>
        
        
        <div class="main-r-hide">
        	<div class="addBox">
        		<button class="addSite pop-up">新增供应商</button>
        		<p class="tell">您已拥有<i class="gree"></i>个供应商</p>
        	</div>
        </div>
        
        <ul id="lists">
        	
         </ul>
        
       
    </div>
</div>
<!--mask-->
<div class="mask">
     <div class="inform">
        <div class="inform-head">
            <span class="fl">新增供应商</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <form action="" id="addSuppier">
            <div class="inform-body">
             <div class="lab">
                <div class="input-l"><span class="red">*</span> 所在地区 ：</div>
                 <div class="selectList">
                    <select id="country" name="country"><option value="0">选择国家</option></select>
                    <select id="province" name="province" class="province" ><option value="0">选择省</option></select>
                    <select id="city" name="city" class="city"><option value="0">选择市</option></select>
                    <select id="district" name="district" class="district"><option value="0">选择区</option></select>
               </div>
                <p class="red judge"></p>
            </div>

            <div class="lab">
                <div class="input-l"><span class="red">*</span> 详细地址：</div>
                <#--<input  placeholder="请输入详细地址"  maxlength="120" id="address" class="txt" type="text" />-->
                <#--<input  onkeyup="ValidateValue(this)"  placeholder="请输入详细地址"  maxlength="120" id="address" class="txt" type="text" />-->
                <input placeholder="请输入详细地址"  maxlength="120" id="address" name="address" class="txt" type="text" />
                <p class="red judge"></p>
            </div>

            <div class="lab">
                <div class="input-l"><span class="red">*</span> 邮政编码：</div>
                <input placeholder="如您不清楚邮政编码，请填写000000" maxlength="10" id="zipCode" name="zipCode" class="txt" type="text" />
                 <p class="red judge"></p>
            </div>

             <div class="lab">
                <div class="input-l fuml"><span class="red">*</span> 供应商名称：</div>

                <#--<input placeholder="长度不超过120个字" maxlength="120" id="supplierName" class="txt" type="text" />-->

                <input    placeholder="长度不超过120个字" maxlength="120" id="supplierName" name="supplierName" class="txt" type="text" />

                 <p class="red judge"></p>
            </div>

             <div class="lab">
                <div class="input-l fuml"><span class="red">*</span>联系人姓名：</div>


                <#--<input placeholder="长度不超过25个字" maxlength="25" id="supplierContact" class="ml-0 txt" type="text"/>-->

                <input    placeholder="长度不超过25个字" maxlength="25" id="supplierContact" name="supplierContact" class="ml-0 txt" type="text"/>

                <p class="red judge"></p>
            </div>

             <div class="lab lab-tel">
                <div class="input-l">手机号：</div>
                <select id="cellPhone-c" name="cellPhone-c" class="country  ml-18">
                    <option value="0">选择国家</option>
                </select>
                <input id="cellPhone" name="cellPhone" maxlength="20" placeholder="手机号/电话号码必填一项" type="text" />
                <p class="red judge"></p>
            </div>

             <div class="lab lab-phone">
                <div class="input-l">电话号码：</div>
                <select id="phoneNum-c" name="phoneNum-c" class="country">
                    <option value="0">选择国家</option></select>
                <input id="area" name="area" maxlength="10" placeholder="区号" type="text" />
                <input id="phoneNum" name="phoneNum" placeholder="电话号码"  maxlength="10"  type="text" />
                <p class="red judge"></p>
            </div>
            <p class="red reminder">手机号、电话号码选填一项，其余为必填</p>
            <button id="addSuppierBtn"  class="save">保存</button>
            <input type="text" class="hide-input"/>
            </div>
        </form>
    </div>
    
   <!--删除-->
    <div class="inform-1">
        <div class="inform-head">
            <span class="fl">删除供应商</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
         	<div><i class="warning-sign">!</i>确定删除供应商</div>
         	<button class="mr35 shutOut-true">确定</button> <button class="out">取消</button>
         <input type="text" class="hide-input"/>
        </div>
    </div>
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

<script>
    __DATA = ${__DATA};

</script>
</body>
</html>