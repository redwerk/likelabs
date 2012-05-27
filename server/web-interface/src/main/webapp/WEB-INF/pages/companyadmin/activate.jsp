<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
 <div id="content">
    
    <h1>Activation Administrator Company</h1> 

        <c:choose>
            <c:when test="${empty errorcode}">
            <form action="/companyadmin/activate" method="POST">
                <div>
                    <div class="field">
                        Activation code was sent to your phone. Please enter received code here:
                    </div>
                    <div class="field errorblock">
                        ${error}
                    </div>
                    <div class="field">
                        <input name="password" type="text" style="width: 180px;"/>
                    </div>
                    <input type="hidden" name="userId" value="<c:out value='${id}'/>" />
                    <input type="hidden" name="code" value="<c:out value='${activatecode}'/>" />
                    <div class="field">
                        <button class="btn btn_success save" type="submit" style="">Activate</button>
                    </div>
                </div>
            </form>
            </c:when>
            <c:otherwise>
                <div class="field errorblock">
                    <spring:message code="message.registration.invalid.email.activate.code"/>
                </div>
            </c:otherwise>
        </c:choose>
 </div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
