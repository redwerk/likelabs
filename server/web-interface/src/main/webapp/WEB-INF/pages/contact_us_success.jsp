<%@include file="header.jsp" %>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Successfully sent a letter to contact us</td>
    </tr>
    <tr>
        <td class="body">
            <c:choose>
                <c:when test="${empty error}">
                    <p>
                        Your message was successfully sent.
                    </p>
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
<%@include file="footer.jsp" %>
