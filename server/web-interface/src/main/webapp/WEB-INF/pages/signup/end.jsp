<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<script type="text/javascript">
    var id_VKontakte = <spring:message code="app.vkontakte.clientid"/>;
    var id_Facebook = <spring:message code="app.facebook.clientid"/>;
</script>
<script type="text/javascript" src="/static/scripts/ConnectorSocialNetwork.js"></script>
<div id="content">

    <h1>Sign Up for Like Labs</h1> 
    <div>
        <div class="field">
            Congratulations! You were successfully registered to Like Labs!
        </div>
    </div>
    <div>
        <div class="field">
            Now you can go to <a href="/" >Dashboard</a>
        </div>
    </div>
    <div>Link your account to share your comments, videos and photos with your friends</div>
    <div style="width: 250px;">
        <%@include file="/WEB-INF/pages/commons/socialButtons.jspf" %>
    </div>
    <h4>Link Email:</h4>
    <div>Link email for receiving notifications</div>
    <div>In the message you receive, please link provided there</div>
    <div>
        <div class="field">
            <input id="email" name="email" type="text" />
            <button class="btn btn-success" type="button" onclick="linkEmail();">Link  Email</button>
        </div>
    </div>
    <div class="field">
        <a class="btn btn-success save" href="/">Finish</a>
    </div>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>