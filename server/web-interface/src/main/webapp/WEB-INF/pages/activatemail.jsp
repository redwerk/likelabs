<%@include  file="/WEB-INF/common/header.jsp"%>
        <div class="row">
            <h4>Welkome To Like Labs - activation e-mail</h4>
            
            <div>
                <div class="field errorblock">
                    ${error}
                </div>
                <c:if test="${empty error}">
                <div class="field">
                    Email was successfully verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
                </c:if>

            </div>
        </div>
<%@include  file="/WEB-INF/common/footer.jsp"%>