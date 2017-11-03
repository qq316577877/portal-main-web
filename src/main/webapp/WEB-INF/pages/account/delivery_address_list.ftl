<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>收货地址</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/vipShippingAddress.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/js/pages/vipShippingAddress/vipShippingAddress.main.js"></script>
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
      		<img src="${static_file_server}/assets/img/shipping-address.png"/>
      		<p class="hint">您还没有收货地址哦&nbsp;!</p>
      		<button class="add-ship-site pop-up">新建收货地址</button>
        </div>
        
        
        <div class="main-r-hide">
        	<div class="addBox">
        		<button class="addSite pop-up">新增收货地址</button>
        		<p class="tell">您已创建<i class="gree"></i>个收货地址</p>
        	</div>
        </div>
        
        <ul id="lists">
        	
        	
         </ul>
        
       
    </div>
</div>
<!--mask-->
<!--新增-->
<div class="mask">
    <div class="inform">
        <div class="inform-head">
            <span class="fl">新增收货地址</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <form action="" id="addAddress">
            <div class="inform-body">
             <div class="lab">
                <div class="input-l"><span class="red">*</span> 所在地区 ：</div>

                 <div class="selectList">
                    <select id="country" name="country"><option value="0">选择国家</option></select>
                    <select id="province" class="province"><option value="0">选择省</option></select>
                    <select id="city" class="city"><option value="0">选择市</option></select>
                    <select id="district" class="district" value="0"><option>选择区</option></select>
               </div>
                <p class="red judge"></p>
            </div>

            <div class="lab">
                <div class="input-l"><span class="red">*</span> 详细地址：</div>
                <#--<input onkeyup="ValidateValue(this)"  placeholder="请输入详细地址" maxlength="120" id="address" class="txt" type="text" />-->
                <input  placeholder="请输入详细地址" maxlength="120" id="address" name="address" class="txt" type="text" />
                 <p class="red judge"></p>
            </div>

            <div class="lab">
                <div class="input-l"><span class="red">*</span> 邮政编码：</div>
                <input placeholder="如您不清楚邮政编码，请填写000000" maxlength="10" id="zipCode" name="zipCode" class="txt" type="text" />
                 <p class="red judge"></p>
            </div>

             <div class="lab">
                <div class="input-l fuml"><span class="red">*</span>收件人姓名：</div>


                <#--<input onkeyup="ValidateValue(this)" placeholder="长度不超过25个字" maxlength="25" id="receiver" class="ml-0 txt" type="text"/>-->

                <input placeholder="长度不超过25个字" maxlength="25" id="receiver" name="receiver" class="ml-0 txt" type="text"/>

                <p class="red judge"></p>
            </div>

             <div class="lab lab-tel">
                <div class="input-l">手机号：</div>
                <select class="country  ml-18" id="cellPhone-c">
                    <option value="0">选择国家</option>
                </select>
                <input id="cellPhone" maxlength="20" placeholder="手机号/电话号码必填一项" type="text" />
                <p class="red judge"></p>
            </div>

             <div class="lab lab-phone">
                <div class="input-l">电话号码：</div>
                <select class="country" id="phoneNum-c" name="phoneNum-c">
                    <option value="0">选择国家</option>
                </select>
                <input id="area" name="area" maxlength="10" placeholder="区号" type="text" />
                <input id="phoneNum" name="phoneNum" placeholder="电话号码"  maxlength="10"  type="text" />
                <p class="red judge"></p>
            </div>

            <p class="red reminder">手机号、电话号码选填一项，其余为必填</p>
            <div class="reminder fs16">
                <input type="checkbox" class="moren" name="moren" checked="checked"/>设为默认
            </div>
             <input type="text" class="hide-input"/>
             <button id="saveAddress" class="save">保存</button>
            </div>
        </form>
    </div>
    <!--删除地址-->
    <div class="inform-1">
        <div class="inform-head">
            <span class="fl">删除收货地址</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
         	<div><i class="warning-sign">!</i>确定删除收货地址</div>
         	<button class="mr35 shutOut-true">确定</button> <button class="out">取消</button>
         <input type="text" class="hide-input"/>
        </div>
    </div>
    
    
<!--设为默认-->   
<div class="inform-2">
        <div class="inform-head">
            <span class="fl">设置为默认收货地址</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
         	<div><i class="warning-sign">!</i>确认更改默认收货地址</div>
         	<button class="mr35 SetDefault-true">确定</button> <button class="out">取消</button>
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