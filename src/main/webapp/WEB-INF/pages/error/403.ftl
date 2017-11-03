<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
<#include "/common/seo.ftl">
    <title>403</title>
    <link rel="stylesheet" href="${static_file_server}/assets/css/base.css"/>
    <link rel="stylesheet" href="${static_file_server}/assets/css/error/error.css"/>
</head>
<body>
<div class="main mian-403">
    <img src="${static_file_server}/assets/img/403.png"/>
    <h1>很抱歉,您访问的页面受限制了...</h1>
    <p>没关系,反正农场那么大,去别的地方逛逛吧</p>
    <div class="btn-box">
        <a href="${home_page_url}" class="home"><i></i>返回首页</a>
        <a href="#" onclick="javascript :history.back(-1);" class="go-back"><i></i>返回上一步</a>
    </div>
</div>


<@bottom.footer pos_fixed=1 add_qa=1/>

</body>
</html>
