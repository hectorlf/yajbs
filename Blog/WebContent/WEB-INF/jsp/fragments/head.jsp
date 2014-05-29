<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@page import="com.hectorlopezfernandez.utils.Constants"%><head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="HandheldFriendly" content="True" />
    <meta name="MobileOptimized" content="320" />

    <link rel="stylesheet" href="http://cdn.hectorlopezfernandez.com/screen.min.css" type="text/css"/>
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?subset=latin,latin-ext&family=Open+Sans+Condensed:300|Open+Sans:400,600,400italic,600italic|Merriweather:400,300,300italic,400italic|Roboto+Slab:400,300" type="text/css"/>
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" type="text/css"/>
	<!-- Code syntax highlighter -->
	<link rel="stylesheet" href="http://cdn.hectorlopezfernandez.com/sh/sh.min.css" type="text/css"/>
    
   	<link rel="alternate" href="<stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction"/>" type="application/atom+xml" title="Feed de entradas en formato ATOM"/>
	<link rel="index" href="<stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction"/>" title="Inicio"/>

    <title>${pageName} - ${blogTitle}</title>
    <c:if test="${not empty metaDescription}"><meta name="description" content="${metaDescription}" /></c:if>
	<link rel="shortcut icon" href="http://media.hectorlopezfernandez.com/favicon.gif" type="image/gif"/>
    <meta name="generator" content="<%=Constants.APPLICATION_TAG%>"/>
</head>