<%@include  file="/WEB-INF/pages/header.jsp"%>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Welcome To Like Labs - Activation administrator company</td>
    </tr>
    <tr>
        <td class="body">
            <div>
                <c:choose>
                    <c:when test="${already_active eq true}">
                        <div class="field">
                            <span class="errorblock" >You already active administrator for company ${company}.</span> Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="field">
                            Congratulations! You were successfully activate administrator rights for company ${company}. Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
    </tr>
</table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>