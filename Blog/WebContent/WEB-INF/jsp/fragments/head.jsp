<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@page import="com.hectorlopezfernandez.utils.Constants"%><head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <title>${pageName} - ${blogTitle}</title>
    <c:if test="${not empty metaDescription}"><meta name="description" content="${metaDescription}" /></c:if>

    <meta name="HandheldFriendly" content="True" />
    <meta name="MobileOptimized" content="320" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
    <meta name="generator" content="<%=Constants.APPLICATION_TAG%>"/>

	<link rel="shortcut icon" href="http://media.hectorlopezfernandez.com/favicon.gif" type="image/gif"/>
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?subset=latin,latin-ext&family=Open+Sans+Condensed:300|Open+Sans:400,600,400italic,600italic|Merriweather:400,300,300italic,400italic|Roboto+Slab:400,300" type="text/css"/>
	<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" type="text/css"/>
    <link rel="stylesheet" href="http://media.hectorlopezfernandez.com/normalize.css" type="text/css"/>
    <link rel="stylesheet" href="http://media.hectorlopezfernandez.com/screen.css" type="text/css"/>
	<!-- Code syntax highlighter -->
	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/syntaxhighlighter/shCore.css" type="text/css"/>
	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/syntaxhighlighter/shThemeDefault.css" type="text/css"/>
    
   	<link rel="alternate" href="<stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction"/>" type="application/atom+xml" title="Feed de entradas en formato ATOM"/>
	<link rel="index" href="<stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction"/>" title="Inicio"/>
</head>