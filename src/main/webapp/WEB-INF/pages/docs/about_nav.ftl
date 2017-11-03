<div class="about-aside">
    <div class="about-nav-list">
        <ul>
            <#list __about_navs as nav>
                <li <#if nav.active>class="active"</#if>>
                    <a href="${nav.url}" title="" class="about-nav">${nav.name}</a>
                </li>
            </#list>
        </ul>
    </div>
</div>