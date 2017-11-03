<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>会员中心</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/bankingManagement.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>


    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
<#--<script src="${static_file_server}/js/pages/bacnkingManagement/index.js"></script>-->
<#--<script src="${static_file_server}/js/pages/bacnkingManagement/country.js"></script>-->
    <script src="${static_file_server}/js/pages/bacnkingManagement/bacnkingManagement.main.js"></script>
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
            <img src="${static_file_server}/assets/img/bankingManagement.png"/>
            <p class="hint">您还没有添加银行账号哦&nbsp;!</p>
            <button class="add-ship-site pop-up">新增银行账号</button>
        </div>


        <div class="main-r-hide">
            <div class="addBox">
                <button class="addSite pop-up">新增银行卡账号</button>
                <p class="tell">您已创建<i class="gree"></i>个银行账号</p>
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
            <span class="fl">新增银行账号</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <form action="" id="addBank">
            <div class="inform-body">
                <div class="lab">
                    <div class="input-l"><span class="red">*</span>开户名称：</div>
                    <input placeholder="开户名称" maxlength="128" id="accountName" name="accountName" class="txt" type="text"/>
                    <p class="red judge"></p>
                </div>
                <div class="lab">
                    <div class="input-l"><span class="red">*</span>开户银行：</div>
                    <select class="bankType" id="bankTypeId" name="bankTypeId">
                        <option value="0" >请选择</option>
                    </select>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l minusml15"><span class="red">*</span>开户所在地：</div>
                    <div class="selectList">
                        <select class="province" id="province" name="province">
                            <option value="0">请选择省</option>
                        </select>
                        <select class="city" id="city" name="city">
                            <option value="0">请选择市</option>
                        </select>
                        <select class="district" id="district" name="district">
                            <option value="0">请选择区</option>
                        </select>
                    </div>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span>开户支行：</div>
                    <input p="111" maxlength="128" id="bankName" name="bankName" class="txt" type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span>银行账号：</div>
                    <input maxlength="21" id="bankCard" name="bankCard" class="txt" type="text"/>
                    <p class="red judge"></p>
                </div>
                <button class="save">保存</button>
                <input type="text" class="hide-input"/>
            </div>
        </form>
    </div>
    <!--删除-->
    <div class="inform-1">
        <div class="inform-head">
            <span class="fl">删除银行卡</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div><i class="warning-sign">!</i>确定删除银行卡</div>
            <button class="mr35 shutOut-true">确定</button>
            <button class="out">取消</button>
            <input type="text" class="hide-input"/>
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