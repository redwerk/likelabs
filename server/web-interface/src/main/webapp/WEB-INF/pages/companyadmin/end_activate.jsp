<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<script type="text/javascript">
    var id_VKontakte = <spring:message code="app.vkontakte.clientid"/>;
    var id_Facebook = <spring:message code="app.facebook.clientid"/>;
    $(document).ready(function(){
        <c:if test="${success eq false}">
            errorDialog("Error", '<c:out value="${message}"/>');
        </c:if>
    });
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
            Now you can go to <a href="javascript:void(0)" onclick="document.location.href='/'">Dashboard</a>
        </div>
    </div>
    <h4>Connect Social Accounts: </h4>
    <div>Link your account to share your comments, videos and photos with your friends</div>

    <div>
        <div style="width: 250px;">
            <ul class="social-buttons">
                <li></li>
                <li class="fb-btn linked" id="fb_Btn_Linked" style="display: none;">
                    <i class="icon" ></i>
                    <p>Facebook is linked to Like Labs.
                        <a href="javascript:void(0);" onclick="unlink('facebook')" class="unlink">Unlink</a>
                    </p>
                </li>
                <li class="fb-btn" id="fb_Btn" ><i class="icon"></i><a id="connectToFB" href="javascript:void(0);" onclick="linkFacebook()" >Connect with Facebook</a></li>
                <li class="vk-btn linked" id="vk_Btn_Linked" style="display: none;">
                    <i class="icon" ></i>
                    <p>VKontakte is linked to Like Labs.
                        <a href="javascript:void(0);" onclick="unlink('vkontakte')" class="unlink">Unlink</a>
                    </p>
                </li>
                <li class="vk-btn" id="vk_Btn"><i class="icon"></i> <a id="connectToVK"href="javascript:void(0);" onclick="linkVKontacte()">Connect with VK</a></li>
            </ul>
        </div>
    </div>
    <form action="/companyadmin/activate/finish" method="post">
        <div class="field">
            <button type="submit" class="btn btn-success save">Finish</button>
        </div>
    </form>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>