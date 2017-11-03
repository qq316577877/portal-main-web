<div class="main-l fl">
<#list user_nav_list as group>
    <#if group.enable>
        <ul>
            <h2>${group.name}</h2>
            <#list group.items as item>
                <li><a href="${item.url}" <#if item.active>class="on"</#if>>${item.name}</a></li>
            </#list>
        </ul>
    <#else>
        <ul>
            <h2 class="gray">${group.name}</h2>
            <#list group.items as item>
                <li><span href="${item.url}" class="gray">${item.name}</span></li>
            </#list>
        </ul>
    </#if>

</#list>
</div>