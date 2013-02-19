<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose><c:when test="${actionBean.editing}"><c:set var="pageName" value="Editar entrada"/></c:when><c:when test="${not actionBean.editing}"><c:set var="pageName" value="Nueva entrada"/></c:when></c:choose>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/layouts/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/layouts/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/layouts/menu.jsp"%>

	<section id="main" class="column">
		<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SavePostAction"><c:if test="${actionBean.editing}">
		<stripes:hidden name="id"/>
</c:if>		<article class="module width_full">
			<header>
				<h3>Nueva entrada</h3>
			</header>
			<div class="module_content">
					<fieldset>
						<label>T&iacute;tulo <span style="font-size:0.7em">texto plano, se codificar&aacute; en html autom&aacute;ticamente</span></label>
						<stripes:text name="title" id="title" value="${actionBean.title}"/>
					</fieldset>
					<fieldset>
						<label>URL de enlace permanente <span style="font-size:0.7em">ej. "www.ejemplo.com/archive/1992/12/01/<em>url-permanente</em>", no se codifica y conviene que se eviten caracteres conflictivos</span></label>
						<stripes:text name="titleUrl" id="titleUrl" value="${actionBean.titleUrl}"/>
					</fieldset>
					<fieldset>
						<label>Etiqueta META-DESCRIPTION <span style="font-size:0.7em">texto plano, se codificar&aacute; en html autom&aacute;ticamente</span></label>
						<stripes:textarea name="metaDescription" id="metaDescription" value="${actionBean.metaDescription}"/>
					</fieldset>
					<fieldset>
						<label>Resumen <span style="font-size:0.7em">texto html, se muestra en todas las p&aacute;ginas que no son el detalle de la entrada</span></label>
						<stripes:textarea name="excerpt" id="excerpt" rows="4" value="${actionBean.excerpt}"/>
					</fieldset>
					<fieldset>
						<label>Contenido <span style="font-size:0.7em">texto html, se muestra en la p&aacute;gina de detalle de la entrada</span></label>
						<stripes:textarea name="content" id="content" rows="10" value="${actionBean.content}"/>
					</fieldset>
					<fieldset>
						<label>URL de la imagen de cabecera <span style="font-size:0.7em">esta imagen se muestra en el detalle de la entrada y en el &iacute;ndice cuando la entrada est&aacute; en la primera posici&oacute;n</span></label>
						<stripes:text name="headerImageUrl" id="headerImageUrl" value="${actionBean.headerImageUrl}"/>
					</fieldset>
					<fieldset>
						<label>URL de la imagen lateral <span style="font-size:0.7em">esta imagen se muestra en el &iacute;ndice cuando la entrada est&aacute; en la segunda posici&oacute;n</span></label>
						<stripes:text name="sideImageUrl" id="sideImageUrl" value="${actionBean.sideImageUrl}"/>
					</fieldset>
					<fieldset style="width:48%; float:left; margin-right: 3%;"> <!-- to make two field float next to one another, adjust values accordingly -->
						<label>Autor</label>
						<stripes:select name="authorId" style="width:92%;" value="${actionBean.authorId}"><stripes:options-collection collection="${actionBean.authors}" label="displayName" value="id"/></stripes:select>
					</fieldset>
					<fieldset style="width:48%; float:left;"> <!-- to make two field float next to one another, adjust values accordingly -->
						<label>Blog</label>
						<stripes:select name="hostId" style="width:92%;" value="${actionBean.hostId}"><stripes:options-collection collection="${actionBean.hosts}" label="title" value="id"/></stripes:select>
					</fieldset>
					<fieldset style="width:48%; float:left; margin-right: 3%;"> <!-- to make two field float next to one another, adjust values accordingly -->
						<label>Etiquetas</label>
						<stripes:select name="tagIds" multiple="multiple" size="5" style="width:92%;height:92px;" value="${actionBean.selectedTagsIds}"><stripes:options-collection collection="${actionBean.tags}" label="name" value="id"/></stripes:select>
					</fieldset>
					<fieldset style="width:48%; float:left;"> <!-- to make two field float next to one another, adjust values accordingly -->
						<label>Comentarios</label><div class="clear"></div>
						<div style="margin-left:6px;"><stripes:checkbox name="commentsAllowed" checked="${actionBean.commentsAllowed}"/>Comentarios abiertos</div>
					</fieldset>
					<div class="clear"></div>
			</div>
			<footer>
				<div class="submit_link"><c:choose><c:when test="${actionBean.editing}">
					<stripes:submit name="modify" class="alt_btn">Modificar</stripes:submit></c:when><c:when test="${not actionBean.editing}">
					<stripes:submit name="publish" class="alt_btn">Publicar</stripes:submit>
</c:when></c:choose>				</div>
			</footer>
		</article><!-- end of post new article -->
		</stripes:form>
		<div class="spacer"></div>
	</section>

<script type="text/javascript">
$(document).ready(function() {
	$("#title").charCount(60);
	$("#titleUrl").charCount(50);
	$("#metaDescription").charCount(160);
	$("#excerpt").charCount(500);
	$("#content").charCount(3000);
	$("#headerImageUrl").charCount(100);
	$("#sideImageUrl").charCount(100);
});
</script>

</body>

</html>