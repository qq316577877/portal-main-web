<!--顶部通栏-->
<div class="f-top">
    <div class="w">
        <div class="f-top-l fl">
            <a href="/home">
                <div class="f-top-f"></div>
                <a href="${home_page_url}" class="f-top-i">返回首页</a>
                <span class="index-f-top">欢迎来到九创金服</span>
            </a>
        </div>
        <div class="f-top-r fr">
            <ul>
            <#if _header_user_login == 1 >
                <li><a href="${dashboard_url}">${_header_username}</a></li>
                <li class="spacer"></li>
                <li><a href="${logout_url}">退出</a></li>
                <li class="spacer"></li>
            <#else>
                <li><a href="${login_url}">登录</a></li>
                <li class="spacer"></li>
            </#if>
            <#if _header_user_verify == true >
                <li><a href="${order_center_url}">我的订单</a> <#--<span class="glyphicon glyphicon-menu-down f-top-r-down"></span>--></li>
            </#if>
                <li class="spacer"></li>
                <li><a href="${dashboard_url}">会员中心</a> <#--<span class="glyphicon glyphicon-menu-down f-top-r-down"></span>--></li>
                <li class="spacer"></li>
                <li><a href="${user_guide_url}">常见问题</a> <#--<span class="glyphicon glyphicon-menu-down f-top-r-down"></span>--></li>
                <li class="spacer"></li>
                <#--<li class="f-top-extend1"><a href="#"></a></li>-->
                <#--<li class="f-top-extend2"><a href="#"></a></li>-->
            </ul>
        </div>
    </div>
</div>
