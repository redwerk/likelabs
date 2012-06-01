<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="">
    <tr>
        <td><h1>Welcome To Like Labs - Activation completed</h1></td>
    </tr>
    <tr>
        <td class="body">
            <div>
                <c:choose>
                    <c:when test="${success eq false}">
                        <div class="field">
                            <span class="errorblock" >You have already activated your administrator account for ${companyName}.</span> <br>
                            Now you can go to <a href="/" >Dashboard.</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="field">
                            Congratulations!<br>
                            You have successfully registered your administrator account for ${companyName}.<br>
                            Now you can go to <a href="/">Dashboard.</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
    </tr>
</table>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>