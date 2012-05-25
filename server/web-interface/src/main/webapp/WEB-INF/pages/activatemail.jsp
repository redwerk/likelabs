<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Welcome To Like Labs - activation e-mail</td>
    </tr>
    <tr>
        <td class="body">
            <c:choose>
                <c:when test="${empty error}">
                    <div class="field">
                        Email was successfully verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="field">
                        <span class="errorblock" >Wrong parameters.</span> Email cannot be verified! Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
