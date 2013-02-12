<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><!DOCTYPE html>
<html dir="ltr" lang="es-ES">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<link rel="shortcut icon" type="image/gif" href="http://media.hectorlopezfernandez.com/favicon.gif">
	<!--[if IE]><![endif]-->
	<!--[if lt IE 9]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
	<link rel="profile" href="http://gmpg.org/xfn/11">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Simonetta" type="text/css">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,400" type="text/css">
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Quattrocento" type="text/css">
	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/style.css" type="text/css" media="screen">
	<link rel="stylesheet" href="http://media.hectorlopezfernandez.com/custom.css" type="text/css" media="screen">
	<title>Uh oh...</title>
</head>

<!--[if lt IE 7 ]><body class="home blog ie6"><![endif]-->
<!--[if IE 7 ]><body class="home blog ie7"><![endif]-->
<!--[if IE 8 ]><body class="home blog ie8"><![endif]-->
<!--[if IE 9 ]><body class="home blog ie9"><![endif<]-->
<!--[if (gt IE 9)|!(IE)]><!--><body class="home blog"><!--<![endif]-->

<div id="body-wrapper" class="clearfix">
	<header id="header-main" role="banner"> <!-- begin header -->
		<ul id="headerbanner" class="clearfix"><stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" var="indexUrl"/>
			<li id="header_logo" class="mainl">
				<div id="site-logo" class="siteheader"><h1>KABOOM!!</h1> <span style="font-style: italic;">500 Internal Server Error</span></div>
			</li>
			<li id="nav" class="mainl">
				<nav class="clear fl" role="navigation">
					<h3 class="assistive-text">Men&uacute;</h3>
					<ul id="main-menu" class="menu">
						<li id="inicio"><a href="${indexUrl}">Volver al inicio</a></li>
					</ul>
				</nav>
			</li>
		</ul> <!-- end headerbanner -->
	</header> <!-- end header -->
	<ul id="main-wrapper">
		<li id="maincontent" role="main"> <!-- begin maincontent -->
			<p>Algo ha ido desastrosamente mal... Has encontrado un error en el sistema de los que hacen pupa. Si estabas introduciendo datos, lo m&aacute;s probable es que se hayan perdido.</p>
			<p>Por suerte (risas enlatadas), el error ha sido registrado y, milagro mediante, alg&uacute;n d&iacute;a ser&aacute; corregido. As&iacute; que gracias por tu ayuda, supongo.<p>
			<p>Puedes volver a intentar lo que estabas haciendo, pero seguramente obtendr&aacute;s el mismo resultado.<p>
		</li> <!-- end #maincontent -->
	</ul> <!-- end #main-wrapper ul -->
</div> <!-- end #body-wrapper -->

</body>

</html>