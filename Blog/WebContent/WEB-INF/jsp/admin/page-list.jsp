<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Listado de páginas"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/fragments/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/fragments/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/fragments/menu.jsp"%>

	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<h3 class="tabs_involved">Páginas</h3>
			</header>
			<table class="tablesorter" cellspacing="0">
				<thead>
					<tr>
	   					<th></th>
	   					<th>Id</th>
	    				<th>T&iacute;tulo</th>
	    				<th>URL relativa</th>
	    				<th>Fecha publicaci&oacute;n</th>
	    				<th></th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${actionBean.pages}" var="page">
					<tr>
	   					<td><input type="checkbox"></td>
	   					<td>${page.id}</td>
	    				<td>${page.title}</td>
	    				<td>${page.titleUrl}</td>
	    				<td><joda:format value="${page.publicationDate}" style="SS" locale="es_ES"/></td>
	    				<td>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.EditPageAction"><stripes:param name="id" value="${page.id}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_edit.png" title="Editar p&aacute;gina"></stripes:link>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.DeletePageAction"><stripes:param name="id" value="${page.id}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_trash.png" title="Borrar p&aacute;gina"></stripes:link>
	    				</td>
					</tr>
</c:forEach>
				</tbody>
				<tfoot>
					<tr>
	   					<td colspan="8">
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onFirstPage}">
	   								<img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_first_disabled.png" title="Primera p&aacute;gina" align="absmiddle">
	   								<img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_previous_disabled.png" title="P&aacute;gina anterior" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPagesAction"><stripes:param name="page" value="${actionBean.paginationInfo.firstPage}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_first.png" title="Primera p&aacute;gina" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPagesAction"><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_previous.png" title="P&aacute;gina anterior" align="absmiddle"></stripes:link>
	   							</c:otherwise>
	   						</c:choose>
	   						${actionBean.paginationInfo.currentPage}
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onLastPage}">
	   								<img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_next_disabled.png" title="P&aacute;gina siguiente" align="absmiddle">
	   								<img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_last_disabled.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPagesAction"><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_next.png" title="P&aacute;gina siguiente" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPagesAction"><stripes:param name="page" value="${actionBean.paginationInfo.lastPage}"/><img src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/images/icn_last.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle"></stripes:link>
	   							</c:otherwise>
	   						</c:choose>
						</td>
	   				</tr>
	   			</tfoot> 
			</table>
		</article><!-- end of article -->
		
		<div class="spacer"></div>
	</section>

</body>

</html>