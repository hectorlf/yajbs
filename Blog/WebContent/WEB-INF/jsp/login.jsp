<%@page session="false"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
<stripes:form beanclass="com.hectorlopezfernandez.action.LoginAction" method="POST">
<c:if test="${not empty shiroLoginFailure}"><span style="color:red;">Algo está mal ahí, ¿eh?</span><br/></c:if>
User: <stripes:text name="username" /><br>
Password: <stripes:password name="password" /><br>
<stripes:submit name="send" value="Enviar" />
</stripes:form>
</body>
</html>