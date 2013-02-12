<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Dashboard"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/layouts/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/layouts/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/layouts/menu.jsp"%>

	<section id="main" class="column">
		<article class="module width_full">
			<header><h3>Estad&iacute;sticas</h3></header>
			<div class="module_content">
				<article>
					<iframe width="100%" height="350" src="http://piwik.hectorlopezfernandez.com/index.php?module=Widgetize&action=iframe&columns[]=nb_visits&moduleToWidgetize=VisitsSummary&actionToWidgetize=getEvolutionGraph&idSite=1&period=day&date=today&disableLink=1&widget=1&token_auth=f2496363f0c02e2b85130a19c7fd3caf" scrolling="no" frameborder="0" marginheight="0" marginwidth="0"></iframe>
				</article>
				<div class="clear"></div>
			</div>
		</article><!-- end of stats article -->
		
		<div class="spacer"></div>
	</section>

</body>

</html>