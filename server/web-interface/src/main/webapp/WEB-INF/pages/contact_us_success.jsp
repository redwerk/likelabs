<%@include  file="/WEB-INF/pages/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Successfully sent a letter to contact us</td>
    </tr>
    <tr>
        <td class="body">
            <c:choose>
                <c:when test="${empty error}">
                    <div class="title">
                        Your message was successfully sent.
                    </div>
                </c:when>
                <c:otherwise>
                    <p>
                        There was a problem with your submission. Please try again.
                    </p>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>
