<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>LikeLabs</title>
    <link href="https://fonts.googleapis.com/css?family=Lobster" rel="stylesheet" type="text/css" />
    <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
    <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/flick/jquery-ui.css" rel="stylesheet" type="text/css" />
    
    
    <link href="/static/css/all.css" rel="stylesheet" type="text/css"/>
    
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.19/jquery-ui.min.js"></script>
    <script type="text/javascript" src="https://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    <script type="text/javascript" src="/static/scripts/ejs_production.js"></script>
    <script type="text/javascript" src="/static/scripts/jquery.loadmask.min.js"></script>
    <script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
    <sec:authorize access="not isAuthenticated()">
        <script type="text/javascript">
            function signIn() {
                var data = {
                    "j_username":$('#username').val(),
                    "j_password":$('#password').val(),
                    "_spring_security_remember_me":$('#mem').val()};
                $.ajax({
                    url: "/login",
                    async: false,
                    global: true,
                    type: "POST",
                    processData: true,
                    dataType: "text",
                    data: data,
                    success: function(data){
                        if (data == "true") {
                            window.location.reload();
                        } else {
                            $('#password').val("");
                            $('#authfailed').html('<spring:message code="message.auth.failed"/>');
                        }
                    },
                    error: function(){
                        $('#password').val("");
                        $('#authfailed').html('<spring:message code="message.auth.failed"/>');
                    }
                })
                return false;
            }
        </script>
    </sec:authorize>
</head>
<body>
    <div id="sitewrapper">
        <div id="header">
            <div class="site-logo">
                <a href="/"><img src="/static/images/logo.png" alt="logo" /></a>
            </div>
            <div class="right signin_block">
                <sec:authorize access="isAuthenticated()">
                    <div class="field">
                        <a class="btn btn-success" href="/logout">Logout</a>
                        <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
                            <input type="button" value="Admin Panel" style="width: 187px;" onclick="document.location.href='/admin/panel'"/>
                        </sec:authorize>
                    </div>
                </sec:authorize>
                <sec:authorize access="isAnonymous()" >
                    <form onsubmit="return signIn();" id="loginForm" >
                        <table cellspacing="0">
                            <tr>
                                <td>
                                    Phone:
                                </td>
                                <td>
                                    Password:
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>
                                    <input id="username" name="j_username" type="text" />
                                </td>
                                <td>
                                    <input id="password" name="j_password" type="password" />
                                </td>
                                <td>
                                    <button class="btn btn-success" type="submit">Login</button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input id="mem" type="checkbox" name="_spring_security_remember_me" />
                                    <label for="mem">Remember me</label>                                            
                                </td>
                                <td>
                                    <div>New to LikeLabs? <b><a href="/signup/start/">Join now</a></b></div>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="3">
                                    <div id="authfailed" class="errorblock"></div>
                                </td>
                            </tr>
                        </table>
                    </form>
                </sec:authorize>
            </div>
        </div>
        <div id="main-holder">
            <div id="sidebar">
                <ul class="menu">
                    <li class=' <c:if test="${page eq 'companies'}">active</c:if>'><a href="/companyadmin/companies"><span class="mark dashboard"><span></span></span> Companies</a></li>
                    <li class=' <c:if test="${page eq 'feed'}">active</c:if>'><a href="/companyadmin/feed"><span class="mark feed"><span></span></span> Feed</a></li>
                    <li class=' <c:if test="${page eq 'profile'}">active</c:if>'><a href="/companyadmin/profile"><span class="mark profile"><span></span></span> Profile</a></li>
                </ul>
            </div>
