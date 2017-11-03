<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>进口下单</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/importOrder.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>
    <script src="${static_file_server}/js/pages/importOrder/importOrder.main.js"></script>
    <!--<script src="js/imOrder.js"></script>-->
    <!--<script src="js/importOrder.js"></script>-->

</head>
<body>
<!--顶部通栏1-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">

<!--main-->
<div class="w">
    <div class="main">
        <div class="order-type">
            <h2>订单类型</h2>
            <label>
                <div class="type-1">
                    <input type="radio" id="agency" name="type"/>
                    <span>海外代采</span>
                    <span>（平台提供进口采购、物流通关、保险、资金一条龙服务）</span>
                </div>
            </label>
            <label>
                <div class="type-1 type-2">
                    <input type="radio" id="direct" name="type"/>
                    <span>通关物流</span>
                    <span>（您直接联系供应商进行采购，平台可提供物流通关、保险、资金服务）</span>
                </div>
            </label>
        </div>
        <div class="supplier">
            <h2>供应商</h2>
            <div class="supplier-2">
                <span>供应商</span>
                <select name="" id="" class="selsct">
                    <option selected value="default">请选择供应商</option>
                </select>
                <button class="add-supplier">新增供应商</button>
            </div>
            <ul>
                <li><span>供应商名称：</span>越南正心火龙果工厂</li>
                <li><span>所在地区：</span>越南</li>
                <li><span>详细地址：</span>越南同塔省沙沥市黎利路3坊3组284/3A号</li>
                <li><span>邮政编码：</span>000000</li>
                <li><span>联系人：</span>胡志敏</li>
                <li><span>手机号：</span>86-1391234567</li>
            </ul>
        </div>
        <div class="supplier">
            <h2>采购商品</h2>
            <div class="container-add">
                <p>商品信息不可为空，请先添加货柜商品</p>
                <button class="container-btn" id="container-add">新增一个货柜</button>
            </div>
            <div class="pro-list clearfix" id="pro_list">
                <p>*商品成交价波动较大，以客服回访商议为准，如您下单时需要了解商品价格，
                    可致电400-826-5128或联系企业QQ2357266196询价</p>
                <div class="pro-list-1" id="p-l-1">
                    <div class="pro-list-head">
                        <h3>货柜批次号：</h3>
                        <h3 class="container-name">货柜名称：</h3>
                        <h3 class="container-norms">货柜规格：0-1400箱</h3>
                        <div class="x">
                            <i class="iconfont icon-chacha cancel cancel-1 fr"></i>
                        </div>
                    </div>
                    <div class="pro-list-body clearfix" id="pro-list-body">
                        <div class="hide_inform">
                            <input type="text"/>
                            <input type="text"/>
                            <input type="text"/>
                            <input type="text"/>
                        </div>
                        <table>
                            <tr class="list-head">
                                <th class="first-th">商品名称</th>
                                <th class="two-th"></th>
                                <th class="three-th">大小</th>
                                <th class="four-th">品种</th>
                                <th>成交价/箱</th>
                                <th style="margin-left: 5px">数量</th>
                                <th class="total">合计</th>
                            </tr>
                            <tr class="clone-1">
                                <td class="in-1">
                                    <input disabled type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" disabled placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input placeholder="0.00" type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="in-1">
                                    <input disabled type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" disabled placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input placeholder="0.00" type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="in-1">
                                    <input disabled type="text" class="first-td"/>
                                </td>
                                <td class="in-2">
                                    <select>
                                        <option>一级</option>
                                    </select>
                                </td>
                                <td class="in-3">
                                    <select>
                                        <option>大</option>
                                    </select>
                                </td>
                                <td class="in-4">
                                    <select>
                                        <option>红心</option>
                                    </select>
                                </td>
                                <td class="price">
                                    <input type="number" disabled placeholder="0.00"/>
                                    <span>元</span>
                                </td>
                                <td class="amount">
                                    <input placeholder="0.00" type="number"/>
                                    <span>箱</span>
                                </td>
                                <td class="price-all">
                                    <input type="text" value="0.00"/>
                                    <span>元</span>
                                </td>
                            </tr>

                        </table>
                        <div class="add-h add-h-1">
                            <span>+</span>
                            <span>添加一行</span>
                        </div>
                        <div class="sum clearfix">
                            <span>总计：</span>
                            <input type="text" value="0.00" disabled/>
                            <span>元</span>
                        </div>
                    </div>
                </div>

                <button class="ad" id="ad_1">新增一个货柜</button>

            </div>
        </div>

        <button class="order-next" id="next_1">下一步</button>
        <button class="order-next" id="next_2">下一步</button>
    </div>
</div>
<!--弹出框-->
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
                    <select class="country" id="country" name="country"></select>
                <div class="selectList">
                    <select id="province" name="province" class="province">
                        <option>请选择省</option>
                    </select>
                    <select id="city" name="city" class="city">
                        <option>请选择市</option>
                    </select>
                    <select id="district" name="district" class="district">
                        <option>请选择区</option>
                    </select>
                </div>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span> 详细地址：</div>
                    <input id="address" name="address" special="1" placeholder="请输入详细地址" p="111"  class="txt"
                           type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span> 邮政编码：</div>
                    <input id="zipCode" name="zipCode" placeholder="如您不清楚邮政编码，请填写000000" maxlength="10" class="txt"
                           type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l fuml"><span class="red">*</span>供应商名称：</div>
                    <input id="supplierName" name="supplierName" placeholder="长度不超过25个字"  class="ml-0 txt"
                           type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l fuml"><span class="red">*</span>联系人姓名：</div>
                    <input id="receiver" name="receiver" placeholder="长度不超过25个字" class="ml-0 txt" type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab lab-tel">
                    <div class="input-l">手机号：</div>
                    <select id="country_phone" class="country"></select>
                    <input id="cellPhone" name="cellPhone" maxlength="20" placeholder="手机号/电话号码必填一项" type="text"/>
                    <p class="red judge"></p>
                </div>

                <div class="lab lab-phone">
                    <div class="input-l">电话号码：</div>
                    <select id="country_phoneNum" class="country"></select>
                    <input id="area" name="area" maxlength="10" placeholder="区号" type="text"/>
                    <input id="phoneNum" name="phoneNum" placeholder="电话号码" maxlength="10" type="text"/>
                    <p class="red judge"></p>
                </div>

                <p class="red reminder">手机号、电话号码选填一项，其余为必填</p>

                <input type="button" value="保存" id="addSuppierBtn" class="save">
            </div>
        </form>
    </div>
    <div class="inform inform-1">
        <div class="inform-head">
            <span class="fl">新增货柜</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">

            <div class="lab">
                <label>
                    <div class="input-l"><span class="red">*</span> 水果类型：</div>
                    <select class="type_product" id="type_product">
                        <option>请选择</option>
                    </select>
                    <p class="red judge"></p>
                </label>
            </div>

            <div class="lab">
                <label>
                    <div class="input-l input-2"><span class="red">*</span> 最高容量：</div>
                    <input maxlength="10" class="txt num-fruit" type="text"/>
                    <p class="red judge"></p>
                </label>
            </div>

            <button class="save save-1" id="save_product">确&nbsp;定</button>
        </div>
    </div>
    <div class="inform inform-2" id="inform2">
        <div class="inform-head">
            <span class="fl">新增货柜</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">

            <div class="lab">
                <label>
                    <div class="input-l"><span class="red">*</span> 水果类型：</div>
                    <select class="type_product" id="type_pro">
                        <option>请选择</option>

                    </select>
                    <p class="red judge"></p>
                </label>
            </div>

            <div class="lab">
                <label>
                    <div class="input-l input-2"><span class="red">*</span> 最高容量：</div>
                    <input maxlength="10" id="num-fruit" class="txt num-fruit" type="text"/>
                    <p class="red judge"></p>
                </label>
            </div>

            <button class="save save-1" id="save_2">确&nbsp;定</button>
        </div>
    </div>

    <div class="inform inform-3" id="inform3">
        <div class="inform-head">
            <span class="fl">提示消息</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="alertInfo">
                商品总箱数超出货柜规格
            </div>
            <button class="save save-1" id="save_3">确&nbsp;定</button>
        </div>
    </div>

    <div class="inform inform-4" id="inform4">
        <div class="inform-head">
            <span class="fl">提示消息</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <div class="inform-body">
            <div class="alertInfo">
                请选择订单类型
            </div>
            <button class="save save-1" id="save_4">确&nbsp;定</button>
        </div>
    </div>

</div>


<script>
    __DATA = ${__DATA};
</script>
</body>
</html>
