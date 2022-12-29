<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Dashboard"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/fragments/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/fragments/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/fragments/menu.jsp"%>

	<section id="main" class="column">
		<article class="module width_full">
			<header><h3>&Iacute;ndice de Lucene</h3></header>
			<div class="module_content">
				<article>
				<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SaveConfigAction">
					<stripes:submit name="reindexLucene" class="alt_btn">Reindexar</stripes:submit>
				</stripes:form>
				</article>
				<div class="clear"></div>
			</div>
		</article>

		<div class="spacer"></div>

		<article class="module width_full">
			<header><h3>Feeds de entradas</h3></header>
			<div class="module_content">
				<article>
				<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SaveConfigAction">
					<stripes:submit name="reprocessFeeds" class="alt_btn">Reconstruir</stripes:submit>
				</stripes:form>
				</article>
				<div class="clear"></div>
			</div>
		</article>

		<div class="spacer"></div>

		<article class="module width_full">
			<header><h3>Exportar contenido como XML</h3></header>
			<div class="module_content">
				<article>
				<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SaveConfigAction">
					<stripes:submit name="exportData" class="alt_btn">Exportar</stripes:submit>
				</stripes:form>
				</article>
				<div class="clear"></div>
			</div>
		</article>

		<div class="spacer"></div>
	</section>

</body>

</html>