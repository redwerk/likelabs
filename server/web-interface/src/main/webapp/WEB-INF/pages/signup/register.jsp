<%@include  file="/WEB-INF/common/header.jsp"%>
<div class="row">
    <h4>Sign Up for Like Labs - step two</h4>
    <form action="/signup/end" method="POST">
        <div>
            <div class="field">
                Activation code was sent to your phone. Please enter received code here:
            </div>
            <c:if test="${not empty error}">
                <div class="field errorblock">
                    ${error}
                </div>
            </c:if>
            <div class="field">
                <input name="password" type="text" style="width: 180px;"/>
            </div>
            <div class="field">
                <input type="submit" value="Register" style="width: 187px;"/>
            </div>
        </div>
    </form>
</div>
<%@include  file="/WEB-INF/common/footer.jsp"%>