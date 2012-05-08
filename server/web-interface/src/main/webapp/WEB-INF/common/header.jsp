<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html> <%-- end html tag in footer --%>
    <head>
        <title>Like Labs</title>
        <style type="text/css">
            @import "/static/css/styles.css";
        </style>
    </head>
    <%-- end body tag in footer --%>
    <body>
        <div class="header">
            <div class="container" style="width: 900px;">
                <div class="logo">
                    <a href="javascript:void(0);" onclick="document.location.href='/'">Like Labs</a>
                </div>
                <div class="userName">
                    <sec:authorize access="isAuthenticated()">
                        Hi, <sec:authentication  property="principal.username" />
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()" >
                        Hi, guest
                    </sec:authorize>
                </div>
                <div class="fRight signin_block">
                    <sec:authorize access="isAuthenticated()">
                        <div class="field">
                            <input type="button" value="Logout" style="width: 187px;" onclick="document.location.href='/j_spring_security_logout'"/>
                            <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
                                <input type="button" value="Admin Panel" style="width: 187px;" onclick="document.location.href='/admin/panel'"/>
                            </sec:authorize>
                        </div>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()" >
                        <form id="loginForm" action="/j_spring_security_check" method="POST">
                            <table cellspacing="0">
                                <tr>
                                    <td>
                                        Phone:
                                    </td>
                                    <td>
                                        Password:
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input name="j_username" type="text" />
                                    </td>
                                    <td>
                                        <input name="j_password" type="password" />
                                    </td>
                                    <td>
                                        <input type="submit" value="Sign in"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="_spring_security_remember_me" />
                                    </td>
                                    <td>
                                        Remember me
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </sec:authorize>
                </div>
            </div>
        </div>
        <div class="menu"></div>
        <%-- end this div tag in footer --%>
        <div class="content container">