<%@page import="com.hectorlopezfernandez.utils.Constants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8"/>
	<meta name="generator" content="<%=Constants.APPLICATION_TAG%>">
	<title>${pageName}</title>
	<!--[if lt IE 9]>
	<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<link rel="stylesheet" href="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/css/layout.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/css/jquery-charCount.css" type="text/css" media="screen" />
	<script src="https://code.jquery.com/jquery-1.12.0.min.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js" type="text/javascript"></script>
	<script src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/js/hideshow.js" type="text/javascript"></script>
	<script src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/js/jquery-charCount.min.js" type="text/javascript"></script>
	<!-- markitup editor -->
	<link href="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/markitup/skins/markitup/style.css" rel="stylesheet" type="text/css"/>
	<link href="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/markitup/sets/html/style.css" rel="stylesheet" type="text/css"/>
	<script src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/markitup/jquery.markitup.js" type="text/javascript"></script>
	<script src="https://storage.googleapis.com/resources.hectorlopezfernandez.com/admin/markitup/sets/html/set.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//When page loads...
			$(".tab_content").hide(); //Hide all content
			$("ul.tabs li:first").addClass("active").show(); //Activate first tab
			$(".tab_content:first").show(); //Show first tab content
			//On Click Event
			$("ul.tabs li").click(function() {
				$("ul.tabs li").removeClass("active"); //Remove any "active" class
				$(this).addClass("active"); //Add "active" class to selected tab
				$(".tab_content").hide(); //Hide all tab content
				var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
				$(activeTab).fadeIn(); //Fade in the active ID content
				return false;
			});
		});
	</script>
</head>