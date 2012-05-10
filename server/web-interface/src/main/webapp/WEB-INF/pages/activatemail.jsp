<%@include  file="/WEB-INF/common/header.jsp"%>
<div class="row">
    <h4>Welkome To Like Labs - activation e-mail</h4>
    <div>
        <c:choose>
            <c:when test="${empty error}">
                <div class="field">
                    Email was successfully verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="field">
                    <span class="errorblock" >Wrong parameters.</span> Email cannot be verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<%@include  file="/WEB-INF/common/footer.jsp"%>