<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@page import="com.hectorlopezfernandez.utils.Constants"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="HandheldFriendly" content="True" />
    <meta name="MobileOptimized" content="320" />

	<style>html{font-family:sans-serif;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}article,footer,header,main,nav{display:block}a{background:transparent}a:active,a:hover{outline:0}h1{font-size:2em;margin:.67em 0}body{background-color:#fff;color:#7f8c8d;font-size:15px;font-weight:100;line-height:1.6em;margin:0;padding:0;-webkit-font-smoothing:subpixel-antialiased}body *{max-height:1000000em}h1,h2,li{font-family:Arial,sans-serif}h1{text-align:center}a,a:visited{color:#3498db;text-decoration:none;transition:color ease .7s;-webkit-transition:color ease .7s}a:focus,a:hover{color:#02090e}li{font-size:18px;font-weight:100;line-height:30px}li{padding-left:.8em}h1{color:#151515;font-family:Arial,sans-serif;font-size:36px;font-weight:400;line-height:42px;margin:0;padding:0}#site-head{margin:20px auto;max-width:600px;width:90%}#site-head img{max-width:100%}#blog-logo{display:block;max-width:100px;margin:50px auto 0;text-align:center}#blog-logo div.bloglogo{width:100px;height:100px;border-radius:50%;-webkit-background-size:cover !important;background-size:cover !important;background-position:center center !important;background-color:#fff;background:url(//d9xqzluw8al1.cloudfront.net/hector.png)}h1.blog-title,h2.blog-description{text-align:center}h1.blog-title{font-size:48px;line-height:52px;margin-top:10px;margin-bottom:30px;padding:0}h2.blog-description{border-bottom:1px solid #ecf0f1;border-top:1px solid #ecf0f1;margin-bottom:2em;padding:.5em 1em;line-height:1.2em}h1.blog-title a{color:#151515;transition:color ease .7s;-webkit-transition:color ease .7s}h1.blog-title a:focus,h1.blog-title a:hover{color:#3498db;text-decoration:none}h2{font-family:Arial,sans-serif;margin-top:2em;text-transform:uppercase;font-weight:300}nav{border-bottom:1px solid #ecf0f1;border-top:1px solid #ecf0f1;margin-bottom:2em;text-align:center;margin:20px auto 2em auto;max-width:600px}nav ul{list-style:none;margin:0 auto;padding:0;width:100%;overflow:hidden;text-align:center}nav ul li{display:inline-block;font-family:Arial,sans-serif;font-size:18px;font-weight:300;margin:0;padding:0 .5em;text-align:center;text-transform:uppercase}nav ul li a{display:block;padding:.5em 0}nav.menu{margin-bottom:2.5em}nav.menu li{margin:0 .5em;padding:0}nav.menu li:last-child{margin:0}@media only screen and (min-width:320px) and (max-width:662px){nav.menu li{display:block}}</style>

    <link rel="preconnect" href="https://d9xqzluw8al1.cloudfront.net"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://netdna.bootstrapcdn.com"/>

   	<link rel="alternate" href="<stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction"/>" type="application/atom+xml" title="Feed de entradas en formato ATOM"/>
	<link rel="index" href="<stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction"/>" title="Inicio"/>

    <title>${pageName} - ${blogTitle}</title>
    <c:if test="${not empty metaDescription}"><meta name="description" content="${metaDescription}" /></c:if>
	<link rel="shortcut icon" href="https://d9xqzluw8al1.cloudfront.net/favicon.gif" type="image/gif"/>
    <meta name="generator" content="<%=Constants.APPLICATION_TAG%>"/>
</head>
<%-- flush head as soon as possible --%><h:flush/>