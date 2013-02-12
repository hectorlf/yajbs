<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@taglib prefix="h" uri="http://www.hectorlopezfernandez.com/jsp/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="pageName" value="Nueva página"/>

<!DOCTYPE html>
<html dir="ltr" lang="es-ES">

<%@include file="/WEB-INF/jsp/admin/layouts/head.jsp"%>

<body>

<%@include file="/WEB-INF/jsp/admin/layouts/header.jsp"%>

<%@include file="/WEB-INF/jsp/admin/layouts/menu.jsp"%>

	<section id="main" class="column">
		<stripes:form beanclass="com.hectorlopezfernandez.action.admin.SavePageAction">
		<article class="module width_full">
			<header>
				<h3>Nueva p&aacute;gina</h3>
			</header>
			<div class="module_content">
					<fieldset>
						<label>T&iacute;tulo <span style="font-size:0.7em">texto plano, se codificar&aacute; en html autom&aacute;ticamente</span></label>
						<stripes:text name="title" id="title"/>
					</fieldset>
					<fieldset>
						<label>URL de enlace permanente <span style="font-size:0.7em">ej. "www.ejemplo.com/pages/<em>url-permanente</em>", no se codifica y conviene que se eviten caracteres conflictivos</span></label>
						<stripes:text name="titleUrl" id="titleUrl"/>
					</fieldset>
					<fieldset>
						<label>Etiqueta META-DESCRIPTION <span style="font-size:0.7em">texto plano, se codificar&aacute; en html autom&aacute;ticamente</span></label>
						<stripes:textarea name="metaDescription" id="metaDescription"/>
					</fieldset>
					<fieldset>
						<label>Contenido <span style="font-size:0.7em">texto html, se muestra tal cual en el detalle de la p&aacute;gina</span></label>
						<stripes:textarea name="content" id="content" rows="20"/>
					</fieldset>
					<fieldset style="width:48%; float:left; margin-right: 3%;"> <!-- to make two field float next to one another, adjust values accordingly -->
						<label>Blog</label>
						<stripes:select name="hostId" style="width:92%;"><stripes:options-collection collection="${actionBean.hosts}" label="title" value="id"/></stripes:select>
					</fieldset>
					<div class="clear"></div>
			</div>
			<footer>
				<div class="submit_link">
					<stripes:submit name="publish" class="alt_btn">Publicar</stripes:submit>
				</div>
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
	$("#content").charCount(3000);
});
</script>

</body>

</html>