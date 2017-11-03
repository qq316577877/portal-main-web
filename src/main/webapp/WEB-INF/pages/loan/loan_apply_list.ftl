<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>我的申请记录</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/limitGenerality.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/MyLoan.css" />
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/calculator/calculator.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/jQueryPage/jquery.page.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/jQueryPage/jquery.page.js"></script>
    <script src="${static_file_server}/js/pages/MyLoan/MyLoan.main.js"></script>
	<script src="${static_file_server}/plugin/calculator/calculator.js"></script>
    <script src="${static_file_server}/plugin/moment/moment.min.js"></script>
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
        <h3>我的资金服务</h3>
        <div class="clearfix"><i class="wire1 fl"></i><i class="wire2 fl"></i></div>
        <div class="ShowLines">
            <p class="Myloan">我的贷款额度: <i class="mark" id="creditLine"></i><a href="../lib/javastudy.pdf" target="view_window" class="examine">借款合同</a></p> <button id="calculate">贷款计算器</button>
            <p class="Myloan">可用贷款额度: <i class="mark" id="balance"></i><a href="/loan/auth/quota/apply" class="getLimit">获取额度</a></p>
            <span class="rate">利息按天计算，日利率低至万3.35</span>
        </div>
        <div class="main-r-content">
            <div class="main-head  clearfix">
                <ul class="top-list fl">

                </ul>
                <div class="searchBox fr">
                    <i class="icon-search"></i><input type="text" name="search" class="search" placeholder="请输入货柜批次号查询" /><button class="search-btn">搜索</button>
                </div>
            </div>
            <ul class="thList list clearfix">
                <li>序号</li>
                <li>订单号</li>
                <li>货柜批次号</li>
                <li>货柜状态</li>
                <li>申请时间</li>
                <li>贷款金额</li>
                <li>服务费</li>
                <li>贷款状态</li>
            </ul>
            <div class="null-show none">
                <img src="${static_file_server}/assets/img/MoneyNull.png" />
                <p>没有相关记录哦~</p>
            </div>
            <div class="have-show none">
                <ul class="tabBox">

                </ul>
                <div class="pageDiv"></div>
            </div>
        </div>
    </div>
</div>
<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>

</html>