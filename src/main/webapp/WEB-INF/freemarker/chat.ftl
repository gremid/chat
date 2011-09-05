<#ftl>
<#assign cp = springMacroRequestContext.getContextPath()>

<#-- Contains the generic template for each page -->
<#macro page title header="">
<!DOCTYPE html>
<html lang="de">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="${cp}/static/yui-3.3.0/cssfonts/fonts-min.css">
    <link rel="stylesheet" type="text/css" href="${cp}/static/yui-3.3.0/cssreset/reset-min.css">
    <link rel="stylesheet" type="text/css" href="${cp}/static/yui-3.3.0/cssgrids/grids-min.css">
    <link rel="stylesheet" type="text/css" href="${cp}/static/yui-3.3.0/cssbase/base-min.css">
    <link rel="stylesheet" type="text/css" href="${cp}/static/chat.css">

    <script type="text/javascript" src="${cp}/static/yui-3.3.0/yui/yui-min.js"></script>
    <script type="text/javascript" src="${cp}/static/d3.min.js"></script>
    <script type="text/javascript" src="${cp}/static/d3.layout.min.js"></script>
    <script type="text/javascript" src="${cp}/static/ace-0.2.0/src/ace.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cp}/static/ace-0.2.0/src/theme-solarized_light.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cp}/static/ace-0.2.0/src/mode-javascript.js" charset="utf-8"></script>


    <script type="text/javascript">var cp = "${cp?js_string}";</script>
    <script type="text/javascript" src="${cp}/static/chat.js" charset="utf-8"></script>

    <title>${title} :: Chat</title>
    <#if header?has_content>${header}</#if>
</head>
<body>
<div id="main-menu">
    <ul>
        <li><a href="${cp}/" title="Neuer Chat" >Start</a></li>
        <li><#--<a href="${cp}/doc/" title="Dokumentation">-->Dokumentation<#--</a>--></li>
    </ul>
</div>
<div id="content">
    <#nested>
</div>
</body>
</html>
</#macro>