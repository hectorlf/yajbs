<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose><c:when test="${actionBean.editing}"><c:set var="pageName" value="Editar etiqueta"/></c:when><c:when test="${not actionBean.editing}"><c:set var="pageName" value="Nueva etiqueta"/></c:when></c:choose>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/layouts/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/layouts/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/layouts/menu.jsp"%>

	<section id="main" class="column">
		<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SaveTagAction"><c:if test="${actionBean.editing}">
		<stripes:hidden name="id"/>
</c:if>		<article class="module width_full">
			<header>
				<h3>${pageName}</h3>
			</header>
			<div class="module_content">
					<fieldset>
						<label>Nombre <span style="font-size:0.7em">texto plano, se codificar&aacute; en html autom&aacute;ticamente</span></label>
						<stripes:text name="name" id="name" value="${actionBean.name}"/>
					</fieldset>
					<fieldset>
						<label>URL de enlace permanente <span style="font-size:0.7em">ej. "www.ejemplo.com/tags/<em>url-permanente</em>", no se codifica y conviene que se eviten caracteres conflictivos</span></label>
						<stripes:text name="nameUrl" id="nameUrl" value="${actionBean.nameUrl}"/>
					</fieldset>
					<div class="clear"></div>
			</div>
			<footer>
				<div class="submit_link"><c:choose><c:when test="${actionBean.editing}">
					<stripes:submit name="modify" class="alt_btn">Modificar</stripes:submit></c:when><c:when test="${not actionBean.editing}">
					<stripes:submit name="publish" class="alt_btn">Guardar</stripes:submit>
</c:when></c:choose>				</div>
			</footer>
		</article><!-- end of page form -->
		</stripes:form>
		<div class="spacer"></div>
	</section>

<script type="text/javascript">
$(document).ready(function() {
	$("#name").charCount(30);
	$("#nameUrl").charCount(20);
});
</script>

</body>

</html>