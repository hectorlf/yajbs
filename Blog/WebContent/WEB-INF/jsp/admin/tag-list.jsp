<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Listado de etiquetas"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/fragments/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/fragments/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/fragments/menu.jsp"%>

	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<h3 class="tabs_involved">Etiquetas</h3>
			</header>
			<table class="tablesorter" cellspacing="0">
				<thead>
					<tr>
	   					<th></th>
	    				<th>Nombre</th>
	    				<th>URL relativa</th>
	    				<th>N&uacute;mero de entradas asociadas</th>
	    				<th></th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${actionBean.tags}" var="tag">
					<tr>
	   					<td><input type="checkbox"></td>
	    				<td>${tag.name}</td>
	    				<td>${tag.nameUrl}</td>
	    				<td>${tag.count}</td>
	    				<td>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.EditTagAction"><stripes:param name="id" value="${tag.id}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_edit.png" title="Editar etiqueta"></stripes:link>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.DeleteTagAction"><stripes:param name="id" value="${tag.id}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_trash.png" title="Borrar etiqueta"></stripes:link>
	    				</td>
					</tr>
</c:forEach>
				</tbody>
				<tfoot>
					<tr>
	   					<td colspan="8">
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onFirstPage}">
	   								<img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_first_disabled.png" title="Primera p&aacute;gina" align="absmiddle">
	   								<img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_previous_disabled.png" title="P&aacute;gina anterior" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListTagsAction"><stripes:param name="page" value="${actionBean.paginationInfo.firstPage}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_first.png" title="Primera p&aacute;gina" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListTagsAction"><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_previous.png" title="P&aacute;gina anterior" align="absmiddle"></stripes:link>
	   							</c:otherwise>
	   						</c:choose>
	   						${actionBean.paginationInfo.currentPage}
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onLastPage}">
	   								<img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_next_disabled.png" title="P&aacute;gina siguiente" align="absmiddle">
	   								<img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_last_disabled.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListTagsAction"><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_next.png" title="P&aacute;gina siguiente" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListTagsAction"><stripes:param name="page" value="${actionBean.paginationInfo.lastPage}"/><img src="https://d9xqzluw8al1.cloudfront.net/admin/images/icn_last.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle"></stripes:link>
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