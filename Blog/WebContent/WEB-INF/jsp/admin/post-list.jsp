<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Listado de entradas"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/fragments/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/fragments/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/fragments/menu.jsp"%>

	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<h3 class="tabs_involved">Entradas</h3>
			</header>
			<table class="tablesorter" cellspacing="0">
				<thead>
					<tr>
	   					<th></th>
	    				<th>T&iacute;tulo</th>
	    				<th>URL relativa</th>
	    				<th>Resumen</th>
	    				<th>Etiquetas</th>
	    				<th>Publicado</th>
	    				<th>Fecha publicaci&oacute;n</th>
	    				<th>Comentarios</th>
	    				<th></th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${actionBean.posts}" var="post">
					<tr>
	   					<td><input type="checkbox"></td>
	    				<td>${post.title}</td>
	    				<td>${post.titleUrl}</td>
	    				<td>${h:escape(post.excerpt)}</td>
	    				<td><c:forEach items="${post.tags}" var="tag" varStatus="tagStats"><c:if test="${tagStats.count > 1}">, </c:if>${tag.name}</c:forEach></td>
	    				<td>${post.published}</td>
	    				<td><joda:format value="${post.publicationDate}" style="SS" locale="es_ES"/></td>
	    				<td><c:choose><c:when test="${post.commentsClosed}">Cerrados</c:when><c:otherwise>Abiertos</c:otherwise></c:choose> | ${fn:length(post.comments)} comentarios</td>
	    				<td>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.EditPostAction"><stripes:param name="id" value="${post.id}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_edit.png" title="Editar entrada"></stripes:link>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.DeletePostAction"><stripes:param name="id" value="${post.id}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_trash.png" title="Borrar entrada"></stripes:link>
<c:choose>
<c:when test="${not post.published}">					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.PostPublishingAction" event="publish"><stripes:param name="id" value="${post.id}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_photo.png" title="Publicar"></stripes:link></c:when>
<c:when test="${post.published}">					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.PostPublishingAction" event="unpublish"><stripes:param name="id" value="${post.id}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_photo.png" title="Despublicar"></stripes:link></c:when>
</c:choose>
	    					<stripes:link beanclass="com.hectorlopezfernandez.action.admin.PostPublishingAction" event="changePublicationDate"><stripes:param name="id" value="${post.id}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_photo.png" title="Establecer fecha de publicación a hoy"></stripes:link>
	    				</td>
					</tr>
</c:forEach>
				</tbody>
				<tfoot>
					<tr>
	   					<td colspan="8">
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onFirstPage}">
	   								<img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_first_disabled.png" title="Primera p&aacute;gina" align="absmiddle">
	   								<img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_previous_disabled.png" title="P&aacute;gina anterior" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPostsAction"><stripes:param name="page" value="${actionBean.paginationInfo.firstPage}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_first.png" title="Primera p&aacute;gina" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPostsAction"><stripes:param name="page" value="${actionBean.paginationInfo.previousPage}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_previous.png" title="P&aacute;gina anterior" align="absmiddle"></stripes:link>
	   							</c:otherwise>
	   						</c:choose>
	   						${actionBean.paginationInfo.currentPage}
	   						<c:choose>
	   							<c:when test="${actionBean.paginationInfo.onLastPage}">
	   								<img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_next_disabled.png" title="P&aacute;gina siguiente" align="absmiddle">
	   								<img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_last_disabled.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle">
	   							</c:when>
	   							<c:otherwise>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPostsAction"><stripes:param name="page" value="${actionBean.paginationInfo.nextPage}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_next.png" title="P&aacute;gina siguiente" align="absmiddle"></stripes:link>
	   								<stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPostsAction"><stripes:param name="page" value="${actionBean.paginationInfo.lastPage}"/><img src="//d9xqzluw8al1.cloudfront.net/admin/images/icn_last.png" title="&Uacute;ltima p&aacute;gina" align="absmiddle"></stripes:link>
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