<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>我的资金服务记录</title>

    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/limitGenerality.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/MyIou.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/jQueryPage/jquery.page.css"/>
     <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />

    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/plugin/jQueryPage/jquery.page.js"></script>
    <script src="${static_file_server}/plugin/moment/moment.min.js"></script>
    <script src="${static_file_server}/js/pages/MyIou/MyIou.main.js"></script>

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
        <div class="main-r-content">
            <div class="main-head  clearfix">
                <ul class="top-list fl">
                	
                </ul>
                <div class="searchBox fr">
                    <i class="icon-search"></i><input type="text" name="search" class="search" placeholder="请输入订单号或货柜批次号" /><button class="search-btn">搜索</button>
                </div>
            </div>
            <ul class="thList list clearfix">
                <li>序号</li>
                <li>订单号</li>
                <li>货柜批次号</li>
                <li>货柜状态</li>
                <li>借据金额</li>
                <li>借据状态</li>
                <li>借据到期日</li>
                <li>贷款状态</li>
            </ul>
            <div class="null-show">
                <img src="${static_file_server}/assets/img/MoneyNull.png" />
                <p>没有相关记录哦~</p>
            </div>
            <div class="have-show">
                <ul class="tabBox">

                </ul>
                <div class="pageDiv"></div>
            </div>
        </div>
    </div>
</div>

	
<!--详情-->
<div class="mask">

    <div class="inform" id="inform_2">
        <div class="inform-head">
            <span class="fl">借据信息</span>
            <i class="iconfont icon-chacha cancel fr" id="cancel_1"></i>
        </div>
        <div class="inform-body">
            <div class="inform-body-top clearfix">
               
            </div>

            <div class="inform-body-middle clearfix">
              
            </div>

            <div class="inform-body-foot clearfix">
               
            </div>
        </div>
    </div>
</div>


<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>

</html>