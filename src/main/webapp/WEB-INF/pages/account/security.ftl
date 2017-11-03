<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>账户信息与安全</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/vipCenter.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/animate.min.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/js/pages/vipCenter/vipCenter.main.js"></script>
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
        <div class="main-r-t clearfix">
            <h2>账户信息</h2>
            <div class="numline">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </div>
            <div class="main-l-t1">
                <div class="account">
                    <span class="">账号：<i id="tel">18669938582</i></span>
                    <a href="#" class="skip" id="changeTel">更换手机号</a>
                </div>
                <div class="account attestation">
                    <span>会员：<i>您还未进行会员认证</i></span>
                    <a href="${member_audit_url}" class="skip">进行会员认证</a>
                </div>
                <div class="account">
                    <span>邮箱：<i>未绑定邮箱</i></span>
                    <a href="#" class="skip skip-email">绑定邮箱</a>
                </div>
                <div class="account">
                    <span>Q Q：<i>1234567890</i></span>
                    <a href="#" class="skip skip-qq">修改QQ号</a>
                </div>
            </div>
        </div>
        <div class="main-r-b">
            <h2>账户安全</h2>
            <div class="numline">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </div>
            <div class="main-info">
                <div class="main-info1">
                    <div class="using fl">
                        <i class="unable fl"></i>
                        <span class="fl">未启用</span>
                    </div>
                    <span class="fl">绑定邮箱</span>
                    <div class="using-info fl">
                        未绑定邮箱，绑定后可接收重要订单通知
                    </div>
                    <a href="#" class="using-bind fr" id="update_email">绑定</a>
                </div>
                <div class="main-info1">
                    <div class="using fl">
                        <i class="able fl"></i>
                        <span class="fl">已启用</span>
                    </div>
                    <span class="fl">绑定手机</span>
                    <div class="using-info fl">
                        18669938582
                    </div>
                    <a href="#" class="using-bind fr" id="update_phone">修改</a>
                </div>
                <div class="main-info1">
                    <div class="using fl">
                        <i class="able fl"></i>
                        <span class="fl">已启用</span>
                    </div>
                    <span class="fl">修改密码</span>
                    <div class="using-info fl">
                        为确保账户安全，建议定期修改密码
                    </div>
                    <a href="#" class="using-bind fr" id="revise">修改</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--mask-->
<div class="mask">
    <div class="inform">
        <div class="inform-head">
            <span class="fl">更换手机</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="tel">
                <span>手机号：</span><div class="oldTel">13252114152</div>
            </div>
            <div class="tel inputCapth">
                <span class="fl">图形验证码：</span><input maxlength="4" class="fl" id="oldImgCapth" type="text">
                <div class="main-img fl">
                    <img src="http://24isbw.natappfree.cc/fruitupload/gcaptcha/2017-10-19/53c79610372a4fe9840f65f23d2cf29d-43ab68d7b52446ebadc13160c8bbd1e2.png" alt="">
                </div>
                <a href="javascript:;" id="changeCapth">看不清？换一张</a>
            </div>
            <p id="falseImgCapth">验证码错误</p>
            <div class="tel">
                <span class="fl">验证码：</span><input  class="fl" type="text" class="img" maxlength="6" id="old-img"/><button class="gain fl" id="old-telBtn">获取短信验证码</button>
            </div>
            <span class="wrong" id="old-Null">验证码不能为空</span>
            <button class="nextStep">确定</button>
        </div>
    </div>
    <div class="inform-1">
        <div class="inform-head">
            <span class="fl">更换手机</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="tel">
                <span>新手机号：</span><input type="text" maxlength="11" class="newTel"/>
            </div>
            <span class="wrong wrong-tel">请输入正确的手机号</span>
            <div class="tel inputCapth">
                <span class="fl">图形验证码：</span><input maxlength="4" class="fl" id="NewImgCapthNew" type="text">
                <div class="main-img fl">
                    <img src="http://24isbw.natappfree.cc/fruitupload/gcaptcha/2017-10-19/53c79610372a4fe9840f65f23d2cf29d-43ab68d7b52446ebadc13160c8bbd1e2.png" alt="">
                </div>
                <a href="javascript:;" id="changeCapth">看不清？换一张</a>
            </div>
            <p id="falseImgCapthNew">验证码错误</p>
            <div class="tel">
                <span>验证码：</span><input type="text" maxlength="6" class="img img-1"/><button class="gain gain-1" id="new-telBtn">获取短信验证码</button>
            </div>
            <span class="wrong wrong-1">验证码不能为空</span>

            <button class="nextStep nextStep-1">提交</button>
        </div>
    </div>
    <div class="inform-2">
        <div class="inform-head">
            <span class="fl">更换手机</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body inform-body-2">

        </div>
        <button class="nextStep nextStep-2">确定</button>
    </div>

    <!--邮箱-->
    <div class="inform-3">
        <div class="inform-head">
            <span class="fl">绑定邮箱</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="tel">
                <span>邮箱地址：</span><input type="text" class="specialChar  newTel new_email"/>
            </div>
            <div class="wrong-email">
                <span></span>
            </div>
            <div id="next">
                <button class="nextStep nextStep-3">确定</button>
            </div>
        </div>
    </div>
    <!--邮箱匹配成功-->
    <div class="inform-4">
        <div class="inform-head">
            <span class="fl">绑定邮箱</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <p>邮箱保存成功</p>
            <button class="nextStep nextStep-4">确定</button>
        </div>
    </div>
    <!--修改密码-->
    <div class="inform-5">
        <div class="inform-head">
            <span class="fl">修改密码</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <form action="" id="changePwd">
            <div class="inform-body">
                <label>
                    <span>原密码：</span>
                    <input class="oldpwd" id="oldpwd" name="oldpwd" type="password"/>
                    <span class="oldpwd-wrong">请输入原密码</span>
                </label>
                <label>
                    <span>新密码：</span>
                    <input class="newpwd" type="password" id="newpwd" name="newpwd"  placeholder="6-18位英文或数字"/>
                </label>
                <label>
                    <span>再次输入：</span>
                    <input class="newpwd2" id="newpwd2" name="newpwd2"  type="password"/>
                    <span class="newpwd2-w">两次密码不一致</span>
                </label>
                <span class="fail-pwd">密码修改失败</span>
                <button class="nextStep nextStep-5">提交</button>
            </div>
        </form>
    </div>
    <!--修改密码-2   修改成功-->
    <div class="inform-6">
        <div class="inform-head">
            <span class="fl">修改密码</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <p>密码修改成功！</p>
            <button class="nextStep nextStep-6">确定</button>
        </div>
    </div>

    <!--qq修改-->
    <div class="inform-7">
        <div class="inform-head">
            <span class="fl">绑定QQ</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="tel">
                <span>QQ账号：</span><input type="text" class="newTel new_qq"/>
            </div>
            <div class="wrong-email">
                <span>11111111111111</span>
            </div>
            <div id="next">
                <button class="nextStep nextStep-7">确定</button>
            </div>
        </div>
    </div>
    <!--qq匹配成功-->
    <div class="inform-8">
        <div class="inform-head">
            <span class="fl">绑定QQ</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <p>修改QQ成功</p>
            <button class="nextStep nextStep-8">确定</button>
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