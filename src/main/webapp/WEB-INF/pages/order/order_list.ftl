<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>我的订单</title>
    
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/vipCenter/vipCenter.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/account/animate.min.css" />
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/orderQuery.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/plugin/laydate/laydate.dev.js"></script>
    <script src="${static_file_server}/plugin/jQueryPage/jquery.page.js"></script>
    <script src="${static_file_server}/plugin/moment/moment.min.js"></script>
    <script src="${static_file_server}/js/pages/orderQuery/orderQuery.main.js"></script>
	<link rel="stylesheet" href="${static_file_server}/assets/css/vipCenter/QRCodePopup.css"/>
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
        <ul class="clearfix query-head">
            <li class="">全部订单</li>
            <li>待提交</li>
            <li>待付款</li>
            <li>待发货</li>
            <li>待收货</li>
            <li>完成</li>
        </ul>
        <div class="order-search">
            <input type="text" id="orderSearch" placeholder="请输入订单号进行搜索"/>
            <button id="orderSea">订单搜索</button>
        </div>
        <div class="filtrate-day clearfix">
            <span class="fl">日期：</span>
            <ul class="fl">
                <li id="today">当天</li>
                <li id="toweek">本周</li>
                <li id="tomonth">本月</li>
                <li id="threeMonth">前三个月</li>
            </ul>
            <div class="custom">
                <span>自定义：</span>
                <input type="text" id="day_1"/> -
                <input type="text" id="day_2"/>
                <input type="button" value="自定义查询" id="day_3"/>
            </div>
        </div>

        <ul class="order-head">
            <li>货柜批次号</li>
            <li>货柜名称</li>
            <li>货柜状态</li>
            <li>贷款申请金额</li>
            <li>贷款状态</li>
            <li>订单状态</li>
            <li>操作</li>
        </ul>
        <div class="ctrl-btn">
            <input type="text" id="totalPages" style="display: none"/>
            <button id="btn-last">上一页</button>
            <button id="btn-next">下一页</button>
        </div>
        <div class="order-list clearfix" id="orderList" style="display: none">

        </div>

        <div class="pageDiv"></div>

        <div class="noData">
            <i class="iconfont icon-order order-icon1"></i>

            <p>没有相关的订单哦</p>

            <div class="noData-btn">
                <input type="button" id="allList" value="查看全部订单"/>
                <input type="button" id="create" value="去下单"/>
            </div>
        </div>


    </div>
</div>
<div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>

<!--mask-->
<div class="mask">
    <div id="logistics">
        <div class="inform-head">
            <span class="fl">物流详情</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body" id="inform-body">
            <!--<div class="inform-top">-->
            <!--<div class="inform-top1">-->
            <!--订单号：<span>101111111122333</span>-->
            <!--</div>-->
            <!--<div class="inform-top1">-->
            <!--货柜批次号：<span>101111111122333</span>-->
            <!--</div>-->
            <!--<div class="inform-top1">-->
            <!--国际物流公司：<span class="company">上海物流公司</span>-->
            <!--</div>-->
            <!--<div class="inform-top1">-->
            <!--国内物流公司：<span class="company">上海物流公司</span>-->
            <!--</div>-->
            <!--</div>-->
            <div class="inform-down">
                <div class="down-head">
                    <span>物流详情</span>
                </div>
                <!--<div class="logistics-details">-->
                <!--<span class="date">2017-06-15</span>-->
                <!--<span class="time">13:20:02</span>-->
                <!--<span>-->
                <!--您的货柜已发货，车牌号是c92944-->
                <!--</span>-->
                <!--<div class="img2">-->
                <!--<img src="" alt=""/>-->
                <!--</div>-->
                <!--</div>-->
            </div>
        </div>
    </div>

    <div id="inform-1">
        <div class="info">是否取消订单</div>
        <div class="can-btn">
            <button id="btn-1">确认</button>
            <button id="btn-2">取消</button>
        </div>
    </div>
     <!--二维码-->
    <div class="inform">
        <div class="inform-head">
            <span class="fl">查看二维码</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
        	<img class="img" src="${static_file_server}/assets/img/jiuChuang.jpg"/>
        	<p class="announcement">尊敬的客户您好，请移步到微信端下单哦！</p>
        	<button class="cancel exit" type="button">关闭</button>
        </div>
    </div>
</div>


<!--底部通栏-->
<@bottom.footer pos_fixed=0/>


</body>
<script>
    laydate({

        elem: '#day_1'

    });

    laydate({

        elem: '#day_2'

    });

</script>

</html>