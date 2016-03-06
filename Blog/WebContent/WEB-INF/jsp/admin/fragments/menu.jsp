<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
	<aside id="sidebar" class="column">
		<h3>Entradas</h3>
		<ul class="toggle">
			<li class="icn_categories"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPostsAction">Listado de entradas</stripes:link></li>
			<li class="icn_new_article"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.NewPostAction">Nueva entrada</stripes:link></li>
		</ul>
		<h3>Etiquetas</h3>
		<ul class="toggle">
			<li class="icn_categories"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListTagsAction">Listado de etiquetas</stripes:link></li>
			<li class="icn_tags"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.NewTagAction">Nueva etiqueta</stripes:link></li>
		</ul>
		<h3>P&aacute;ginas</h3>
		<ul class="toggle">
			<li class="icn_categories"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.ListPagesAction">Listado de p&aacute;ginas</stripes:link></li>
			<li class="icn_new_article"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.NewPageAction">Nueva p&aacute;gina</stripes:link></li>
		</ul>
		<h3>Users</h3>
		<ul class="toggle">
			<li class="icn_add_user"><a href="#">Add New User</a></li>
			<li class="icn_view_users"><a href="#">View Users</a></li>
			<li class="icn_profile"><a href="#">Your Profile</a></li>
		</ul>
		<h3>Media</h3>
		<ul class="toggle">
			<li class="icn_folder"><a href="#">File Manager</a></li>
			<li class="icn_photo"><a href="#">Gallery</a></li>
			<li class="icn_audio"><a href="#">Audio</a></li>
			<li class="icn_video"><a href="#">Video</a></li>
		</ul>
		<h3>Administraci&oacute;n</h3>
		<ul class="toggle">
			<li class="icn_settings"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.PreferencesAction">Preferencias</stripes:link></li>
			<li class="icn_settings"><stripes:link beanclass="com.hectorlopezfernandez.action.admin.ConfigAction">Configuraci&oacute;n y cach&eacute;</stripes:link></li>
			<li class="icn_security"><a href="#">Security</a></li>
			<li class="icn_jump_back"><stripes:link beanclass="com.hectorlopezfernandez.action.LogoutAction">Logout</stripes:link></li>
		</ul>
<%@include file="/WEB-INF/jsp/admin/fragments/footer.jsp"%>
	</aside><!-- end of sidebar -->
