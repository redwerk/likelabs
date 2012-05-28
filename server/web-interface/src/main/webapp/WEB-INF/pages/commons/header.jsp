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
    <title>LikeLabs
        <c:if test="${page eq 'faq'}"> - Frequently Asked Questions</c:if>
        <c:if test="${page eq 'news'}"> - Our news</c:if>
        <c:if test="${page eq 'about'}"> - About us</c:if>
        <c:if test="${page eq 'tos'}"> - Terms of service</c:if>
        <c:if test="${page eq 'contact'}"> - Contact us</c:if>
    </title>
    <link href="http://fonts.googleapis.com/css?family=Lobster" rel="stylesheet" type="text/css"/>
    <link href="/static/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="/static/css/all.css" rel="stylesheet" type="text/css"/>
    
    <link href="/static/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet" type="text/css" />
    <link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/flick/jquery-ui.css" rel="stylesheet" type="text/css" />
    
    <script type="text/javascript" src="/static/scripts/jquery-1.7.2.min.js"></script>    
    <script type="text/javascript" src="/static/scripts/jquery-ui-1.8.20.custom.min.js"></script>    
    <script type="text/javascript" src="/static/scripts/ejs_production.js" ></script>
    <script type="text/javascript" src="/static/scripts/jquery.loadmask.min.js" ></script>
    <script type="text/javascript" src="/static/scripts/jquery.pagination.js" ></script>
    <script type="text/javascript" src="/static/scripts/jquery.validate.min.js"></script>
    <script type="text/javascript" src="/static/scripts/json2.min.js"></script>
    <sec:authorize access="not isAuthenticated()">
        <script type="text/javascript">
            function signIn() {
                if ($('#username').val()[0] != "+") {
                    $('#authfailed').html('<spring:message code="message.auth.failed"/>');
                    return false;
                }
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
                            window.location.href = "/public";
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

<script type="text/javascript">
    function confirmDialog (title, description, callback) {
        $("#confirm_dialog").dialog({modal: true, autoOpen: false, title: title }); 
        $("#confirm_dialog #description").html(description);
        $("#confirm_dialog").dialog("open");
        $("#confirm_dialog_ok").click(function(){callback.call();$("#confirm_dialog").dialog("close")});
        $("#confirm_dialog_cancel").click(function(){$("#confirm_dialog").dialog("close")});
    }
    function errorsDialog(title, errors) {
        $("#error_dialog").dialog({modal: true, autoOpen: false, title: title, minHeight: 50 });
        $('#error_message').html("");
        for (var key = 0 ;key < errors.length; key++) {
            $('#error_message').html($('#error_message').html() + errors[key] + "<br/>");
        }
        $('#error_dialog').dialog('open');
    }
    function errorDialog(title, error) {
        $("#error_dialog").dialog({modal: true, autoOpen: false, title: title, minHeight: 50 });
        $('#error_message').html(error);
        $('#error_dialog').dialog('open');
    }
</script>
</head>
<body>
    <div id="confirm_dialog" style="display: none;">
        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
            <tr>
                <td style="text-align: center"><div id="description" ></div></td>
            </tr>
            <tr>
                <td style="text-align: center; padding-top: 30px;">
                    <button id='confirm_dialog_ok' class="btn btn-success save" type="button">OK</button>
                    <button id="confirm_dialog_cancel" class="btn btn-info save" type="button">Cancel</button>
                </td>
            </tr>
        </table>
    </div>
    <div id="error_dialog" style="display: none;">
        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
            <tr>
                <td style="text-align: center"><div id="error_message" class="errorblock"></div></td>
            </tr>
            <tr>
                <td style="text-align: center;"><button class="btn btn-info save" type="button" onclick="$('#error_dialog').dialog('close');">OK</button></td>
            </tr>
        </table>
    </div>
    <div id="sitewrapper">
         <div id="header">
            <div class="site-logo">
                <a href="/"><img src="/static/images/logo.png" alt="Likelabs" width="162" height="40" /></a>
            </div>
            <div class="right signin_block">
                <sec:authorize access="isAuthenticated()">
                    <div style="width: 100px; float: right;">
                        <div><a class="btn btn-success" href="/logout">Logout</a></div> 
                        <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
                            <div><a href="/admin/panel">Admin Panel</a></div>
                        </sec:authorize>
                    </div>
                    
                </sec:authorize>
                <sec:authorize access="isAnonymous()" >
                    <form onsubmit="return signIn();" id="loginForm" >
                        <table cellspacing="0">
                            <tr>
                                <td>Phone:</td>
                                <td>Password:</td>
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
                    <li class=' <c:if test="${page eq 'companies'}">active</c:if>'><a href="/companyadmin/companies"><span class="mark company"><span></span></span>Companies</a></li>
                    <li class=' <c:if test="${page eq 'feed'}">active</c:if>'><a href="/companyadmin/feed"><span class="mark dashboard"><span></span></span>Feed</a></li>
                    <li class=' <c:if test="${page eq 'profile'}">active</c:if>'><a href="/companyadmin/profile"><span class="mark profile"><span></span></span> Profile</a></li>
                    
                    <li class=' <c:if test="${page eq 'company'}">active</c:if>'><a href="/public"><span class="mark company"><span></span></span>Company list</a></li>
                    <li class=' <c:if test="${page eq 'reviews'}">active</c:if>'><a href="/public/1/reviews"><span class="mark feed"><span></span></span>Company feed</a></li>
                    
                </ul>
            </div>
       