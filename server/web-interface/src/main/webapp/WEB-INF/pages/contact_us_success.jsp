<%@include  file="/WEB-INF/pages/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="body">
            <c:choose>
                <c:when test="${empty error}">
                    <div style="margin: 40px 0; font-weight: bold; font-style: italic; color: #455172;">
                        Your message was successfully sent.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="errorblock" style="margin: 40px 0; font-weight: bold; font-style: italic;">
                        There was a problem with your submission. Please try again.
                    </div>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>
