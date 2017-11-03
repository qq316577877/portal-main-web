<!--首页logo top -->

<div class="logo-top">
	<div class="logo w">
		<div class="logo-l">
			<div class="logo-l1">
				<a href="${home_page_url}"></a>

			</div>
			<div class="logo-r clearfix">
				<ul class="index-nav fl">
					<li>
						<a href="${home_page_url}">首页</a>
					</li>
					<li>
						<a href="${import_service_page_url}">进口服务</a>
					</li>
					<li>
						<a href="${logistics_service_page_url}">物流服务</a>
					</li>
					<li>
						<a href="${customs_clea_service_page_url}">通关服务</a>
					</li>
					<li>
						<a href="${fund_service_page_url}">资金服务</a>
					</li>
					<li>
						<a href="${dashboard_url}">会员中心</a>
					</li>
					<#--<li>-->
						<#--<a href="">新闻中心</a>-->
							<#--</li>-->
				</ul>

				<#if _header_user_login==1>
					<#if _header_user_verify==true>
						<div class="show-h fl">
							<!--<a href="${create_order_page_url}" class="immediately">立即下单</a>-->
							<p class="immediately">立即下单</p>
							<div class="QR_code">
								<img src="${static_file_server}/assets/img/jiuChuang.jpg"></img>
								<p class="reminder">尊敬的客户您好，请移步到微信端下单哦！</p>
							</div>
						</div>
					</#if>
					<#else>
						<div class="show-h fl">
							<a href="${login_url}" class="fl login">登录</a>
							<a href="${register_url}" class="fl register">注册</a>
						</div>
				</#if>

			</div>
		</div>
	</div>
</div>