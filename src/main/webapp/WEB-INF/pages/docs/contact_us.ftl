<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<#include "/common/seo.ftl">
    <title>九创金服【客服电话】</title>
    <link href="${static_file_server}/assets/css/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/docs/base.css" rel="stylesheet" type="text/css">
    <link href="${static_file_server}/assets/css/docs/about.css" rel="stylesheet" type="text/css">
</head>

<body>
<!--toolbar-->
<#include "/common/header.ftl">
<!--toolbar end-->
<!--warp-->
<div class="warp">
    <div class="constitution">
        <!--常见问题-->
        <div class="about-box">

        <#include "/docs/about_nav.ftl">

            <!--右侧-->
            <div class="about-main">
                <div class="about-mode">
                    <div class="hd"><h6>联系我们</h6></div>
                </div>
                <div class="about-details-box">

                    <!--面包屑 end-->
                    <div class="about-details-mode">
                        <div class="about-details">
                            <div class="about-details-txt" style="font-size: 14px;">
                                浙江创意生物科技股份有限公司
                            </div>
                            <div class="about-details-txt">
                                【客服电话】：400-826-5128
                            </div>
                            <div class="about-details-txt">
                                【地址】：浙江省温州市平阳县万全镇江畔村
                            </div>
                            <div class="about-details-txt">
                                	【电子邮箱】：chengxinnc@aliyun.com
                            </div>
                            <div style="width:697px;height:500px;border:#ccc solid 1px;" id="dituContent"></div>
                        </div>
                    </div>
                    <!--右侧 end-->
                </div>
                <!--常见问题 end-->
                <!--服务信息-->
                <!--服务信息 end-->
            </div>
        <#include "/common/service_info.ftl">
        </div>
    </div>
    <style type="text/css">
        .iw_poi_title {color:#CC5522;font-size:14px;font-weight:bold;overflow:hidden;padding-right:13px;white-space:nowrap}
        .iw_poi_content {font:12px arial,sans-serif;overflow:visible;padding-top:4px;white-space:-moz-pre-wrap;word-wrap:break-word}
    </style>
    <#--<script type="text/javascript" src="https://api.map.baidu.com/api?key=&v=1.1&services=true&s=1"></script>-->
    <script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=yBcf7XHDdFkKKlikH09FQTfchtrIn6O3&s=1"></script>
    <script type="text/javascript">
        //创建和初始化地图函数：
        function initMap(){
            createMap();//创建地图 啊啊
            setMapEvent();//设置地图事件
            addMapControl();//向地图添加控件
            addMarker();//向地图中添加marker
        }

        //创建地图函数：
        function createMap(){
            var map = new BMap.Map("dituContent");//在百度地图容器中创建一个地图
            var point = new BMap.Point(120.556678,27.736438);//定义一个中心点坐标
            map.centerAndZoom(point,18);//设定地图的中心点和坐标并将地图显示在地图容器中
            window.map = map;//将map变量存储在全局
        }

        //地图事件设置函数：
        function setMapEvent(){
            map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
            map.enableScrollWheelZoom();//启用地图滚轮放大缩小
            map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
            map.enableKeyboard();//启用键盘上下左右键移动地图
        }

        //地图控件添加函数：
        function addMapControl(){
            //向地图中添加缩放控件
            var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE});
            map.addControl(ctrl_nav);
            //向地图中添加缩略图控件
            var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1});
            map.addControl(ctrl_ove);
            //向地图中添加比例尺控件
            var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT});
            map.addControl(ctrl_sca);
        }

        //标注点数组
        var markerArr = [{title:"浙江创意生物科技股份有限公司",content:"电话：400-826-5128&nbsp;<br/>地址：浙江省温州市平阳县万全镇江畔村",point:"120.556678|27.736438",isOpen:0,icon:{w:21,h:21,l:0,t:0,x:6,lb:5}}
        ];
        //创建marker
        function addMarker(){
            for(var i=0;i<markerArr.length;i++){
                var json = markerArr[i];
                var p0 = json.point.split("|")[0];
                var p1 = json.point.split("|")[1];
                var point = new BMap.Point(p0,p1);
                var iconImg = createIcon(json.icon);
                var marker = new BMap.Marker(point,{icon:iconImg});
                var iw = createInfoWindow(i);
                var label = new BMap.Label(json.title,{"offset":new BMap.Size(json.icon.lb-json.icon.x+10,-20)});
                marker.setLabel(label);
                map.addOverlay(marker);
                label.setStyle({
                    borderColor:"#808080",
                    color:"#333",
                    cursor:"pointer"
                });

                (function(){
                    var index = i;
                    var _iw = createInfoWindow(i);
                    var _marker = marker;
                    _marker.addEventListener("click",function(){
                        this.openInfoWindow(_iw);
                    });
                    _iw.addEventListener("open",function(){
                        _marker.getLabel().hide();
                    })
                    _iw.addEventListener("close",function(){
                        _marker.getLabel().show();
                    })
                    label.addEventListener("click",function(){
                        _marker.openInfoWindow(_iw);
                    })
                    if(!!json.isOpen){
                        label.hide();
                        _marker.openInfoWindow(_iw);
                    }
                })()
            }
        }
        //创建InfoWindow
        function createInfoWindow(i){
            var json = markerArr[i];
            var iw = new BMap.InfoWindow("<b class='iw_poi_title' title='" + json.title + "'>" + json.title + "</b><div class='iw_poi_content'>"+json.content+"</div>");
            return iw;
        }
        //创建一个Icon
        function createIcon(json){

//          https://app.baidu.com/map/images/us_mk_icon.png
            var icon = new BMap.Icon("https://api.map.baidu.com/img/markers.png", new BMap.Size(json.w,json.h),{imageOffset: new BMap.Size(-json.l,-json.t),infoWindowOffset:new BMap.Size(json.lb+5,1),offset:new BMap.Size(json.x,json.h)})
            return icon;
        }

        initMap();//创建和初始化地图
    </script>
</div>
        <@bottom.footer  add_qa=1/>

</body>
</html>
