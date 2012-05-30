<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <script type="text/javascript">
        window.close();
        window.opener.verifyResponseConnect(<c:out value="${success}" default="false"/>, '<c:out value="${message}"/>');
    </script>
</html>
