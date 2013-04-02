<%@page import="com.hectorlopezfernandez.utils.Constants"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><c:if test="${not empty metaDescription}">
	<meta name="description" content="${metaDescription}">
	</c:if><meta charset="UTF-8">
	<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="generator" content="<%=Constants.APP_NAME%> <%=Constants.APP_VERSION%>">
	<link rel="shortcut icon" type="image/gif" href="http://media.hectorlopezfernandez.com/favicon.gif">
	<!--[if IE]><![endif]-->
	<!--[if lt IE 9]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
	<link rel="profile" href="http://gmpg.org/xfn/11"><%--
	<link rel="stylesheet" href="<c:url value="/css/style.css"/>" type="text/css" media="screen">
	<link rel="stylesheet" href="<c:url value="/css/custom.css"/>" type="text/css" media="screen">
--%>	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/style.css" type="text/css" media="screen">
	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/custom.css" type="text/css" media="screen">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Simonetta" type="text/css">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,400" type="text/css">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Quattrocento" type="text/css">
	<link rel="alternate" href="<stripes:url beanclass="com.hectorlopezfernandez.action.FeedAction"/>" type="application/atom+xml" title="Feed de entradas en formato ATOM">
<%--	<link rel="pingback" href="/pingback.php">--%>
	<link rel="index" title="Home" href="<stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction"/>">
	<title>${pageName} - ${blogTitle}</title>
</head>