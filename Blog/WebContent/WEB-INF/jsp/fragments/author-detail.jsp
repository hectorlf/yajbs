<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<stripes:url beanclass="com.hectorlopezfernandez.action.AuthorsAction" var="authorUrl"><stripes:param name="name" value="${author.username}"/></stripes:url><c:url value="${author.relatedUrl}" var="authorExternalUrl"/>			<div id="authorbox" class="clearfix fl">
				<img src="http://media.hectorlopezfernandez.com/avatar-${author.username}.jpg" class="avatar" height="80" width="80"/>
				<div class="authortext">
					<header>
						<h4>Sobre <a href="${authorUrl}" title="Entradas de ${author.displayName}" rel="author">${author.displayName}</a></h4>
					</header>
					<p>${author.about}</p>
					<p><a href="${authorExternalUrl}">${authorExternalUrl}</a></p>
				</div>
			</div>