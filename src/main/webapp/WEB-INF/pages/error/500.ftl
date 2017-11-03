<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>500</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/error/error.css"/>
</head>
<body>
<div class="main main-500">
    <img src="${static_file_server}/assets/img/500.png"/>
    <h1>bibi~迷路啦,没关系,农场主开车来接你啦!</h1>
    <div class="btn-box">
        <a href="${home_page_url}" class="home"><i></i>返回首页</a>
        <a href="#" onclick="javascript :history.back(-1);" class="go-back"><i></i>返回上一步</a>
    </div>
</div>


<@bottom.footer pos_fixed=1 add_qa=1/>

</body>
</html>
