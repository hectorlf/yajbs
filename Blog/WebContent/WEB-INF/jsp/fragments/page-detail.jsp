<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><stripes:url beanclass="com.hectorlopezfernandez.action.PagesAction" var="pageUrl"><stripes:param name="name" value="${page.titleUrl}"/></stripes:url>
			<article id="page-${page.id}" class="page">
				<header>
					<hgroup>
						<h1 class="posttitle"><a href="${pageUrl}" title="Enlace permanente a ${page.title}" rel="bookmark">${page.title}</a></h1>
					</hgroup>
				</header>
				<div class="storycontent">
					${page.content}
				</div> 
				<footer class="clearfix fl"></footer>
			</article>