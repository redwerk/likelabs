<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>LikeLabs</title>
    <link href="http://fonts.googleapis.com/css?family=Lobster|PICO%20Alphabet" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        @import "/static/css/styles.css";
        @import "/static/css/smoothness/jquery-ui-1.8.20.custom.css";
        @import "/static/css/pagination.css";
    </style>
    <script type="text/javascript" src="/static/scripts/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="/static/scripts/jquery-ui-1.8.20.custom.min.js"></script>
    
    <script type="text/javascript" src="/static/scripts/json2.min.js"></script>
    <script type="text/javascript" src="/static/scripts/ejs_production.js"></script>
    
<!--    <script type="text/javascript" src="/static/scripts/scripts.js"></script>-->
    
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
                            window.location.href = "/company/list";
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
    <div class="container">
        <table cellpadding="0" cellspacing="0" style="height: 100%; width: 100%" summary="">
            <tr>
                <td class="left_shadow" rowspan="2"><img src="/static/images/spacer.png" width="10" height="100%" alt=""/></td>
                <td class="header">
                    <div class="header_logo left"><img src="/static/images/logo.png" width="162" height="40" alt="LikeLabs"/></div>
                    <div class="right signin_block">
                        <sec:authorize access="isAuthenticated()">
                            <div class="field">
                                <button class="btn btn_success" type="button" onclick="document.location.href='/logout'">Logout</button>
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
                                    </tr>
                                    <tr>
                                        <td>
                                            <input id="username" name="j_username" type="text" />
                                        </td>
                                        <td>
                                            <input id="password" name="j_password" type="password" />
                                        </td>
                                        <td>
                                            <button class="btn btn_success" type="submit">Login</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <input id="mem" type="checkbox" name="_spring_security_remember_me" />
                                            Remember me
                                        </td>
                                        <td>
                                            <div>New to LikeLabs? <a href="/signup/start/">Join now</a></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <div id="authfailed" class="errorblock"></div>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </sec:authorize>
                    </div>
                </td>
                <td class="right_shadow" rowspan="2"><img src="/static/images/spacer.png" width="10" height="100%" alt=""/></td>
            </tr>
            <tr>
                <td class="content_container" style="vertical-align: top; height: 400px;">
                    <table cellpadding="0" cellspacing="0" style="height: 100%; background: url('/static/images/shadow_tb.png') repeat-x transparent;" summary="">
                        <tr>
                            <td class="left_menu" style="vertical-align: top;">
                                <div class="menu_item <c:if test="${page eq 'company'}">active</c:if>" ><a href="/company/list">COMPANIES</a></div>
                                <div class="menu_separator"><img src="/static/images/spacer.png" width="100%" height="1" alt=""/></div>
                                <div  class="menu_item <c:if test="${page eq 'about'}">active</c:if>"><a href="/about">ABOUT US</a></div>
                                <div class="menu_separator"><img src="/static/images/spacer.png" width="100%" height="1" alt=""/></div>
                                <div class="menu_item <c:if test="${page eq 'tos'}">active</c:if>"><a href="/tos">TERMS OF SERVICE</a></div>
                                <div class="menu_separator"><img src="/static/images/spacer.png" width="100%" height="1" alt=""/></div>
                                <div class="menu_item <c:if test="${page eq 'faq'}">active</c:if>"><a href="/faq">FAQ</a></div>
                                <div class="menu_separator"><img src="/static/images/spacer.png" width="100%" height="1" alt=""/></div>
                                <div class="menu_item <c:if test="${page eq 'contact'}">active</c:if>"><a href="/contact">CONTACT US</a></div>
                            </td>
                            <td class="content" style="vertical-align: top;">