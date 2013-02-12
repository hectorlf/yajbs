<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<stripes:url beanclass="com.hectorlopezfernandez.action.IndexAction" var="blogIndexUrl"/><stripes:url beanclass="com.hectorlopezfernandez.action.admin.IndexAction" var="indexUrl"/>

	<header id="header">
		<hgroup>
		<h1 class="site_title"><a href="${indexUrl}" title="Administraci&oacute;n del blog">Administraci&oacute;n</a></h1>
		<h2 class="section_title">${pageName}</h2><div class="btn_view_site"><a href="${blogIndexUrl}" target="_blank">Ir al blog</a></div>
		</hgroup>
	</header> <!-- end of header bar -->
	
	<section id="secondary_bar">
		<div class="user">
			<p>${actionBean.context.loggedUser.username}</p>
		</div>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs"><a href="${indexUrl}" title="Administraci&oacute;n del blog">Administraci&oacute;n</a> <div class="breadcrumb_divider"></div> <a class="current">Dashboard</a></article>
		</div>
	</section><!-- end of secondary bar -->