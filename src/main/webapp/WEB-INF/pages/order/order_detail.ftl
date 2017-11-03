<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>订单详情</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/orderDetail.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/orderDetails/orderDetails.main.js"></script>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">
<!--main-->
<div class="w">
    <div class="main">
        <div class="main-top clearfix" id="main-top">
            <ul class="clearfix">
                <li><a href="#">会员中心 ></a></li>
                <li><a href="#">订单中心 ></a></li>
                <li><a href="#">订单详情</a></li>
            </ul>
            <!--<div class="order-date fl">-->
            <!--<span>下单日期：</span>-->
            <!--<span class="order-detail1">2017-06-04</span>-->
            <!--</div>-->
            <!--<div class="order-No">-->
            <!--<span>订单号：</span>-->
            <!--<span class="order-detail1">11223333333334444</span>-->
            <!--</div>-->
            <!--<div class="order-detail">-->
            <!--<span>订单状态：</span>-->
            <!--<span class="order-detail1">待审核</span>-->
            <!--</div>-->
        </div>
        <div class="main-center clearfix">
            <div class="order-type" id="main_1">
                <span>订单类型</span>
                <input type="radio" checked/>
                <span class="main_1">海外代采</span>
            </div>
            <div class="order-type" id="suppier">
                <span>供应商</span>

                <div class="order-supplier">
                    <span>浙江创意生物科技股份有限公司</span>
                </div>
                <div class="more" id="more_2">
                    <span id="more">更多 <span class="glyphicon glyphicon-menu-down f-top-r-down down"> </span></span>
                </div>

                <!--<ul id="select_list_2">-->
                <!--<li><span>收件人：</span>邱老板</li>-->
                <!--<li><span>所在地区：</span>-->
                <!--<div class="country">中国</div>-->
                <!--<div class="province">北京市</div>-->
                <!--<div class="xian">东城区</div>-->
                <!--</li>-->
                <!--<li><span>详细地址：</span>平阳县万全镇江畔村（村委会办公楼边</li>-->
                <!--<li><span>邮政编码：</span>000000</li>-->
                <!--<li><span>手机：</span>86-13912345678</li>-->
                <!--<li><span>固定电话：</span>86-0755-928385</li>-->
                <!--</ul>-->
            </div>
            <div class="order-type purchasing" id="purchasing">
                <span>采购商品</span>

            </div>
            <div class="order-type deliveryAddress">
                <span>收货地址</span>

                <div class="goods-name" id="deliveryAddress">
                    收件人：<span>邱老板</span>
                </div>
                <div class="more-1" id="more-1">
                    <div class="">更多 <span class="glyphicon glyphicon-menu-down f-top-r-down down"> </span></div>
                </div>
                <!--<ul id="select_list">-->
                <!--<li><span>收件人：</span>邱老板</li>-->
                <!--<li><span>所在地区：</span>-->
                <!--<div class="country">中国</div>-->
                <!--<div class="province">北京市</div>-->
                <!--<div class="xian">东城区</div>-->
                <!--</li>-->
                <!--<li><span>详细地址：</span>平阳县万全镇江畔村（村委会办公楼边</li>-->
                <!--<li><span>邮政编码：</span>000000</li>-->
                <!--<li><span>手机：</span>86-13912345678</li>-->
                <!--<li><span>固定电话：</span>86-0755-928385</li>-->
                <!--</ul>-->
            </div>
            <div class="order-type">
                <span>物流服务</span>

                <div class="transport-type">
                    运输方式：
                    <input type="radio" checked/>
                    <span></span>
                </div>
                <div class="International-logistics">
                    国际物流：
                    <span id="outLogi">国际物流公司A</span>
                </div>
                <div class="International-logistics">
                    国内物流：
                    <span id="innerLogi">国内物流公司A</span>
                </div>
            </div>
            <div class="order-type">
                <span>贸易方式</span>

                <div class="transport-type trade">
                    <input checked type="radio"/>
                    <span id="trade"></span>
                </div>
            </div>
            <div class="order-type">
                <span>通关服务</span>

                <div class="clearance1">
                    <input type="checkbox" disabled checked/>
                    报关
                </div>
                <div class="clearance1">
                    <input type="checkbox" disabled checked/>
                    清关
                </div>
                <div class="clearance1-c">
                    清关公司：
                    <span id="clearanceCompany">广西进口清关公司</span>
                </div>
            </div>
            <div class="order-type">
                <span>保险服务</span>

                <div class="clearance1">
                    <input type="checkbox" disabled checked/>
                    保险
                </div>
            </div>
            <div class="order-type " id="money">
                <span>资金服务</span>

                <div class="clearance1">
                    <input type="radio" checked/>
                    <span>需要</span>
                </div>
                <table>
                    <tr>
                        <th>货柜批次号</th>
                        <th>货柜名称</th>
                        <th>申请人</th>
                        <th>可贷款金额</th>
                        <th>申请金额</th>
                        <th>确认金额</th>
                        <th>服务费</th>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170507001</td>
                        <td>越南火龙果</td>
                        <td>芦苇微微</td>
                        <td>500000元</td>
                        <td>
                            300000
                            <span>元</span>
                        </td>
                        <td>500000元</td>
                        <td>
                            <input type="text" value="0.00" disabled/>
                            <span>元</span>
                        </td>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170507001</td>
                        <td>越南火龙果</td>
                        <td>芦苇微微</td>
                        <td>500000元</td>
                        <td>
                            300000
                            <span>元</span>
                        </td>
                        <td>500000元</td>
                        <td>
                            <input type="text" value="0.00" disabled/>
                            <span>元</span>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="order-type clearfix " id="contract">
                <span class="fl" style="margin-right:120px ">采购合同</span>

                <div class="contractx fl">
                    <span>采购合同</span>

                    <div class="contract1">
                        <img id="contractUrl" src="${static_file_server}/assets/img/img_1_r2_c2.gif" alt=""/>
                    </div>
                </div>
                <div class="contractx fl">
                    <span>付款凭证</span>

                    <div class="contract1">
                        <img id="voucherUrl" src="${static_file_server}/assets/img/img_1_r2_c2.gif" alt=""/>
                    </div>
                </div>
            </div>
            <div class="order-type ">
                <span class="contract3" style="margin-right:120px ">结算方式</span>

                <div class="clearance1">
                    <input type="radio" checked/>
                    <span id="payType">预付全款</span>
                </div>
            </div>
            <div class="order-type clearfix" id="money_list">
                <span class="fl">费用清单</span>
                <table class="fl">
                    <tr>
                        <th>货柜批次号</th>
                        <th>采购货款</th>
                        <th>进口代理费</th>
                        <th>保险费</th>
                    </tr>
                    <tr>
                        <td>20170507001</td>
                        <td> 500000元</td>
                        <td>
                            <input type="text"/>
                            <span>元</span>
                        </td>
                        <td>
                            <input type="text"/>
                            <span>元</span>
                        </td>
                    </tr>
                    <tr>
                        <td>20170507001</td>
                        <td> 500000元</td>
                        <td>
                            <input type="text"/>
                            <span>元</span>
                        </td>
                        <td>
                            <input type="text"/>
                            <span>元</span>
                        </td>
                    </tr>
                </table>
                <p id="info">*说明：费用清单所列费用为我平台收取费用，进口代理费、保险费可能存在较小误差，多退少补</p>
            </div>
            <div class="order-type cost">
                <span style="margin-right: 120px">费用合计</span>
                <div>
                    采购货款：
                    <span id="cost_1">6373300元</span>
                </div>
                <div>
                    其他费用：
                    <span id="cost_2">6373300元</span>
                </div>
            </div>
            <div class="order-type settlement clearfix">
                <span class="fl" style="margin-right: 120px">结算流程</span>
                <div class="settlement_1 fl">
                    <div>
                        预付款：
                        <span id="settlement_1">8323444元</span>
                    </div>
                    <div>
                        尾&nbsp;&nbsp;&nbsp;款：
                        <span id="settlement_2">8323444元</span>
                    </div>
                </div>
            </div>
            <div class="cancel-btn">
                <button id="cancel-btn">取消订单</button>
            </div>
        </div>
    </div>
</div>

<!--mask-->
<div class="mask">
    <div id="inform-1">
        <div class="info">是否取消订单</div>
        <div class="can-btn">
            <button id="btn-1">确认</button>
            <button id="btn-2">取消</button>
        </div>
    </div>
    <div id="confirm">
        <div class="name">
            您正在对安心签发出签名授权请求，在本订单申请贷款放款之前委托安心签调用数字证书进行借款凭证签署，数字证书一经调用立即生效。若放款失败，则相应的借款凭证失效。
        </div>
        <div class="blankTel">
            用户信息：<span></span> <input type="button" value="获取验证码" id="btnTel"/>
        </div>
        <div class="capth">
            短信验证码：<input id="capth" maxlength="6" type="text"/>
            <div id="capthWrong"></div>
        </div>
        <div class="btn-1">
            <input type="button" id="comfirm" value="授权"/>
            <input type="button" id="canBtn" value="取消"/>
        </div>
        <div style="display:none;">a</div>
    </div>
</div>


<!--底部通栏-->
<@bottom.footer pos_fixed=0/>


</body>
</html>
<script>
    __DATA = ${__DATA}
</script>