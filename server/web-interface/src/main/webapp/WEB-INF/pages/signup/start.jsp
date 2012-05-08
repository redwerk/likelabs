<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/common/header.jsp" />
<div class="row">
    <h4>Sign Up for Like Labs step one</h4>
    <c:if test="${not empty error}">
        <div class="field errorblock">
            ${error}
        </div>
    </c:if>
    <form action="/signup/register" method="POST" onsubmit="return register();"  >
        <div style="width: 350px;" class="center">
            <div class="field">
                Enter your phone:
            </div>
            <div class="field">
                <select id="countryCodeSelect" style="width: 140px" onchange="document.getElementById('phone').focus();" name="countryCode"></select>
                <input type="text" style="width: 165px;font-family: Arial" id="phone" name="phone"/>
                <script type="text/javascript" src="/static/scripts/phones.js"></script>
                <script type="text/javascript">
                    var select = document.getElementById("countryCodeSelect");
                    var option;
                    for (var i=0; i < codes.length;i++) {
                        option = document.createElement("option");
                        option.innerHTML = codes[i][1];
                        option.value = codes[i][3];
                        select.appendChild(option);
                    }
                    function register() {
                        var chek = document.getElementById("chekTOS");
                        if (chek.checked) {
                            return true;
                        } else {
                            window.alert("You must agree to the Terms of Service")
                            return false;
                        }
                    }

                </script>
            </div>
            <div class="field">
                <input  type="submit" value="Sign Up" style="width: 187px;"/>
            </div>
        </div>
    </form>
</div>
<div class="row">
    <h4>Terms of Service</h4>
    <div class="text">
        <textarea readonly="true" style="width: 900px; height: 500px;"> <spring:message code="message.tos"/></textarea>
    </div>
    <div class="field" style="font-size: 10px;margin: 0; padding: 0">
        <span><input id="chekTOS" type="checkbox"/></span>
        <span>I agree to the Terms of Service</span>
    </div>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp" />