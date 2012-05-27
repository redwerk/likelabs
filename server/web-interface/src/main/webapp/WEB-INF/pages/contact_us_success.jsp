<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">
    <h1>Contact Us</h1>
    <div class="text-holder">
        <c:choose>
            <c:when test="${empty error}">
                <p style="margin: 40px 0; font-weight: bold; font-style: italic; color: #455172;">
                    Your message was successfully sent.
                </p>
            </c:when>
            <c:otherwise>
                <p class="errorblock" style="margin: 40px 0; font-weight: bold; font-style: italic;">
                    There was a problem with your submission. Please try again.
                </p>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="clear"></div>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
