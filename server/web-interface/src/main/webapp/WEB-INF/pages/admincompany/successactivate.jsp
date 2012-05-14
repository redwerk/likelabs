<%@include  file="/WEB-INF/common/header.jsp"%>
<div class="row">
    <h4>Welkome To Like Labs - Activation administrator company</h4>
    <div>
        <c:choose>
            <c:when test="${already_active eq true}">
                <div class="field">
                    <span class="errorblock" >You already active administrator for company ${company}.</span> Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="field">
                    Congratulations! You were successfully activate administrator rights for company ${company}. Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/dashboard'">Dashboard</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>
<%@include  file="/WEB-INF/common/footer.jsp"%>