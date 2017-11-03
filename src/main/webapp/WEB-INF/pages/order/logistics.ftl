<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>进口下单</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/importStep2.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="${static_file_server}/plugin/iconfont/iconfont.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/order/upload.css"/>
    <script src="${static_file_server}/plugin/jquery/jquery.min.js"></script>
    <#--<script src="${static_file_server}/plugin/bootstrap/js/bootstrap.js"></script>-->
    <#--<script src="${static_file_server}/plugin/webuploader/webuploader.min.js"></script>-->
	<#--<script src="${static_file_server}/plugin/webuploader/upload.js"></script>-->
    <#--<script src="${static_file_server}/plugin/webuploader/uploadPath.js"></script>-->
    <script src="${static_file_server}/js/pages/importStep2/importStep2.main.js"></script>
    <script src="${static_file_server}/plugin/calculator/calculator.js"></script>
	<link rel="stylesheet" href="${static_file_server}/plugin/calculator/calculator.css"/>

</head>
<body>
<!--顶部通栏-->
<#include "/common/top.ftl">
<!--logo-->
<#include "/common/header.ftl">

<!--main-->
<div class="w">
    <div class="main">
        <div class="adress">
            <div class="adress-head">
                <h2>收货地址</h2>
                <a target="_blank" href="/member/delivery_address">管理我的收货地址</a>
            </div>
            <div class="adress-body">
                <div class="adress-1">
                    <h4>收货人</h4>
                    <select id="select">
                        <option value="">请选择</option>
                    </select>
                    <a id="add_1" href="#"></a>
                </div>
                <ul id="select_list">
                    <li>
                        <span>收件人：</span>
                        邱老板
                    </li>
                    <li>
                        <span>所在地区：</span>

                        <div class="country"></div>
                        <div class="province"></div>
                        <div class="xian"></div>
                    </li>
                    <li>
                        <span>详细地址：</span>
                        邱老板
                    </li>
                    <li>
                        <span>邮政编码：</span>
                        邱老板
                    </li>
                    <li>
                        <span>手机：</span>
                        邱老板
                    </li>
                    <li>
                        <span>固定电话：</span>
                        邱老板
                    </li>
                </ul>
            </div>
        </div>
        <div class="adress logistics">
            <div class="adress-head">
                <h2>物流服务</h2>
            </div>
            <div class="adress-body">
                <div class="logistics_1">
                    <span>运输方式</span>

                    <div class="logistics_2">
                        <input type="radio" name="logistics" checked/>
                        海运
                    </div>
                    <div class="logistics_2">
                        <input type="radio" name="logistics"/>
                        陆运
                    </div>
                </div>
                <div class="logistics_1">
                    <span>国际物流</span>
                    <span class="international-logistic">国际物流公司A</span>
                </div>
                <div class="logistics_1 logistics_3">
                    <span>国内物流</span>
                    <span class="international-logistic">宁波长运物流公司B</span>
                </div>
            </div>
        </div>
        <div class="adress trade">
            <div class="adress-head">
                <h2>贸易方式</h2>
            </div>
            <div class="adress-body">
                <div class="trade_1">
                    <input type="radio" name="trade" checked/>
                    <span>FOB</span>
                    <span>(离岸价，成本)</span>
                </div>
                <div class="trade_1">
                    <input type="radio" name="trade"/>
                    <span>CIF</span>
                    <span>(到岸价，成本+运费+保险费)</span>
                </div>
            </div>
        </div>
        <div class="adress customsClearance">
            <div class="adress-head">
                <h2>通关服务</h2>
            </div>
            <div class="adress-body">
                <div class="customsClearance_1">
                    <input type="checkbox" checked disabled name="customs"/>
                    <span>报关</span>
                </div>
                <div class="customsClearance_1">
                    <input type="checkbox" checked disabled name="customs"/>
                    <span>清关</span>
                </div>
                <div class="customsClearance_2">
                    <span>清关公司</span>
                    <span>广西进口清关有限责任公司B</span>
                </div>
            </div>
        </div>
        <div class="adress insurance">
            <div class="adress-head">
                <h2>保险服务</h2>
            </div>
            <div class="adress-body">
                <div class="insurance_1">
                    <input type="checkbox" checked disabled name="insurance"/>
                    <span>保险</span>
                    <span>（保险为保障您的货物安全，为第三方保险承保）</span>
                </div>

            </div>
        </div>
        <div class="adress money">
            <div class="adress-head">
                <h2>资金服务</h2>
            </div>
            <div class="adress-body">
                <div class="loan">
                    <div class="loan-limit">
                        可用贷款额度：0
                    </div>
                    <a href="/loan/auth/authentication" target="_blank" class="loan-apply">
                        申请贷款额度
                    </a>
                    
                    <a href="javascript:;" class="loan-cal" id="calculate">
                        贷款计算器
                    </a>
                </div>
                <p class="loan-info">利息按天计算，日利率低至万3.35</p>

                <div class="need-loan">
                    <label for="">
                        <input type="radio" name="need-loan"/>
                        <span>需要</span>
                    </label>
                    <label for="">
                        <input type="radio" name="need-loan" checked/>
                        暂不需要
                    </label>
                </div>
                <table class="loan-table">
                    <tr class="t-head">
                        <th>货柜批次号</th>
                        <th>货柜名称</th>
                        <th>可贷款金额</th>
                        <th>申请金额</th>
                        <th>服务费</th>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170517001</td>
                        <td>越南火龙果</td>
                        <td>50,000.00元</td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                    </tr>
                    <tr class="tab-tr">
                        <td>20170517001</td>
                        <td>越南香蕉</td>
                        <td>50,000.00元</td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                        <td>
                            <input type="text" class="fl"/>
                            <span class="fl">元</span>
                        </td>
                    </tr>
                </table>

                <div class="attention">
                    *说明：申请金额必须为1000的整数倍,贷款期限以实际放款日与实际还款日为准 <br/>
                    放款时间：验货发车后24小时以内<br/>
                    还款时间：预计到货时间前一天还款<br/>
                </div>
            </div>
        </div>
        <div class="adress contract">
            <div class="adress-head">
                <h2>采购合同</h2>
            </div>
           <div class="adress-body clearfix">
                <div class="contract_1 clearfix">
                    <h5><span style="color: #ff4848">*</span>上传采购合同</h5>
                    <div class="big-photo">
                        <div id="preview">
                            <div id="imgPicker"><img id="showImg" src="${static_file_server}/assets/img/img_1_r2_c2.gif"/></div>
                        </div>

                    </div>
                </div>
                <div class="contract_1 clearfix">
                    <h5>付款凭证</h5>
                    <div class="big-photo">
                        <div id="preview_2">
                            <div id="imgPicker2"><img id="showImg2" src="${static_file_server}/assets/img/img_1_r2_c2.gif"/></div>
                        </div>

                    </div>
                </div>
                <p class="clearfix add_info"><span>温馨提示：</span>证照只支持3M以下的jpg、png、pdf、jpeg格式的图片</p>
            </div>
        </div>
        <div class="adress settlement">
            <div class="adress-head">
                <h2>结算方式</h2>
            </div>
            <div class="adress-body">
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement"/>
                        <span>预付全款</span>
                        <span>(预付订单所有款项)</span>
                    </div>
                </label>
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement" checked/>
                        <span>预付货款</span>
                        <span>(预付订单所有货款)</span>
                    </div>
                </label>
                <label for="">
                    <div class="trade_1">
                        <input type="radio" name="settlement"/>
                        <span>预付定金</span>
                        <span>(预付40%-60%定金，货到结算尾款)</span>
                    </div>
                </label>
            </div>
        </div>
        <div class="submit">
            <input type="button" value="上一步" id="last_step"/>
            <input type="button" value="提交审核" id="next_step"/>
        </div>
    </div>
</div>
<!--mask-->
<div class="mask">
    <div class="inform" id="address_inform" style="display: block;">
        <div class="inform-head">
            <span class="fl">新增收货地址</span>
            <i class="iconfont icon-chacha cancel fr"></i>
        </div>
        <form id="addAdress" action="">
            <div class="inform-body">
                <div class="lab">
                    <div class="input-l"><span class="red">*</span> 所在地区 ：</div>

                    <div class="selectList">
                        <select id="country" name="country"><option value="0">选择国家</option><option value="1" data-tel="086">中国</option><option value="2" data-tel="084">越南</option></select>
                        <select class="province" id="province" name="province"><option value="0">选择省</option></select>
                        <select class="city" id="city" name="city"><option value="0">选择市</option></select>
                        <select class="district" value="0" id="district" name="district"><option>选择区</option></select>
                    </div>
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span> 详细地址：</div>
                    <input    placeholder="请输入详细地址"  id="address" name="address" class="txt" type="text">
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l"><span class="red">*</span> 邮政编码：</div>
                    <input placeholder="如您不清楚邮政编码，请填写000000" maxlength="10" id="zipCode" name="zipCode" class="txt" type="text">
                    <p class="red judge"></p>
                </div>

                <div class="lab">
                    <div class="input-l fuml"><span class="red">*</span>收件人姓名：</div>
                    <input  placeholder="长度不超过25个字" id="receiver" class="ml-0 txt" name="receiver" type="text">
                    <p class="red judge"></p>
                </div>

                <div class="lab lab-tel">
                    <div class="input-l">手机号：</div>
                    <select class="country" id="cellPhone-c">
                        <option value="0">选择国家</option><option value="1" data-tel="086">中国086</option><option value="2" data-tel="084">越南084</option></select>
                    <input id="cellPhone" maxlength="20" placeholder="手机号/电话号码必填一项" type="text">
                    <p class="red judge"></p>
                </div>

                <div class="lab lab-phone">
                    <div class="input-l">电话号码：</div>
                    <select class="country" id="phoneNum-c">
                        <option value="0">选择国家</option><option value="1" data-tel="086">中国086</option><option value="2" data-tel="084">越南084</option></select>
                    <input id="area" maxlength="10" placeholder="区号" type="text">
                    <input id="phoneNum" placeholder="电话号码" maxlength="8" type="text">
                    <p class="red judge"></p>
                </div>

                <p class="red reminder">手机号、电话号码选填一项，其余为必填</p>
            <#--<div class="reminder fs16">-->
            <#--<input type="checkbox" class="moren" name="moren" checked="checked">设为默认-->
            <#--</div>-->
                <input type="text" class="hide-input">
                <button id="saveAdress" class="save">保 &nbsp;&nbsp; 存</button>

            </div>
        </form>
    </div>
   <#--<div class="inform" id="inform_2">-->
                             <#--<div class="inform-head">-->
                                 <#--<span class="fl">贷款计算器</span>-->
                                 <#--<i class="iconfont icon-chacha cancel fr" id="cancel"></i>-->
                             <#--</div>-->
                             <#--<div class="inform-body">-->
                                 <#--<label>-->
                                     <#--<span>借多少</span>-->
                                     <#--<input type="text" maxlength="10" id="money_loan"/>-->
                                     <#--<span>元</span>-->
                                     <#--<span id="empty">请输入正整数</span>-->
                                 <#--</label>-->

                                 <#--<label>-->
                                     <#--<span>借多久</span>-->
                                     <#--<input type="text" maxlength="10" id="money-day"/>-->
                                     <#--<span>天</span>-->
                                      <#--<span id="wrong-day">钱数不能为空</span>-->
                                 <#--</label>-->

                                 <#--<label>-->
                                     <#--<span>怎么还</span>-->
                                     <#--<select name="" id="">-->
                                         <#--<option value="">-->
                                             <#--到期还本付息-->
                                         <#--</option>-->
                                     <#--</select>-->
                                 <#--</label>-->
                                 <#--<p class="error red">请填写正确格式</p>-->
                                 <#--<p>月利率&nbsp;<span id="monthRet">1%</span> &nbsp;&nbsp; 服务费<i class="text">0.5%</i> &nbsp;&nbsp0.1%</p>-->

                                 <#--<div class="btn-ca">-->
                                     <#--<button id="btn_1">计算</button>-->
                                     <#--<button id="btn_2">重置</button>-->
                                 <#--</div>-->
                     			<#--<div class="loan-details">-->
                     				<#--<h4>还款详情</h4>-->
                                  	<#--<p id="principal"></p>-->
                                     <#--<p id="interest"></p>-->
                                     <#--<p id="coverCharge"></p>-->
                                 <#--</div>-->
                             <#--</div>-->

    <#--</div>-->
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


<!--底部通栏-->
<@bottom.footer pos_fixed=0/>

</body>
</html>
<script>
    __DATA = ${__DATA};

</script>