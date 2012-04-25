<%@include file="/header.jsp" %>

<style type="text/css">
    @import "/css/fb.css";
</style>
        <div class="row">
            <h4>Sign Up for Like Labs - step 3</h4>
            
            <div>
                <div class="field">
                    Congratulations! You were successfully registered to Like Labs!
                </div>
            </div>
        </div>
        <div class="row">
            <h4>Connect Social Accounts:</h4>
            <div>Link your account to share your comments, videos and photos with your friends</div>
            <div>
                <div class="field">
                    <div class="fb-login-button"><a class="fb_button fb_button_medium" href="javascript:void(0);"><span class="fb_button_text">Connect with Facebook</span></a></div>
                </div>
                <div  class="field">
                    <div class="fb-login-button"><a class="fb_button" style="font-size: 11px;line-height: 14px;width: 178px;" href="javascript:void(0);"><span class="fb_button_text">Connect with VK</span></a></div>
                </div>
            </div>
        </div>
        <div class="row">
            <h4>Verify Email:</h4>
            <div>Verify email for receiving notifications</div>
            <div>
                <div class="field">
                    <input type="text" />
                </div>
                <div  class="field">
                    <input type="button" value="Verify Email" onclick="document.location.href = '/registration/step4.jsp'" />
                </div>
            </div>
        </div>
<%@include file="/footer.jsp" %>