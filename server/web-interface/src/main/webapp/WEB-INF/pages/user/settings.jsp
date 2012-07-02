<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript">
$(document).ready(function(){
        <c:if test="${success eq false}">
            errorDialog("Error", '<c:out value="${error}"/>');
        </c:if>
});
</script>
<div id="content">
    <h1>
        My Settings
    </h1>
    <form:form commandName="user" method="post">
    <div class="items-holder" >
        <div class="checkbox-holder">
            <table cellpadding="0" cellspacing="0" summary="">
                <tr>
                    <td style="vertical-align: top; width: 30px; text-align: center"><form:checkbox path="postToSn"/></td>
                    <td>Publish the reviews to my Facebook and VK pages (if connected)</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 30px; text-align: center"><form:checkbox path="postToEmail"/></td>
                    <td>Send the text reviews to my Email (if connected)</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 30px; text-align: center"><form:checkbox path="eventReviewCreated"/></td>
                    <td>Notify me when I add a new review</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 30px; text-align: center"><form:checkbox path="eventClientReviewCreated"/></td>
                    <td>Notify me of any new reviews for the point if I am the client there</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 30px; text-align: center"><form:checkbox path="eventReviewApproved"/></td>
                    <td>Notify me when the administrator approved my review</td>
                </tr>
            </table>
        </div>
        <div class="field-holder" style="padding-top: 20px;">
            <form:button class="btn btn-success save" type="submit">Save</form:button> or &nbsp;<a href="/user/${userId}">Cancel</a>
        </div>
        <div class="clear"></div>
    </div>
    </form:form>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>