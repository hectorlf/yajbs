<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%><%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>			<aside id="search-3" class="side-widget widget_search">
<stripes:url beanclass="com.hectorlopezfernandez.action.SearchAction" var="searchUrl"/>				<form role="search" method="get" id="searchform" action="${searchUrl}">
					<label class="assistive-text" for="s">Buscar</label>
					<input type="search" id="s" name="q" placeholder="Texto..." value="${h:escape(queryText)}" maxlength="50">
				</form>
			</aside>