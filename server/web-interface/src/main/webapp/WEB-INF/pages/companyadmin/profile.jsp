<%@include file="/WEB-INF/pages/admin_header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="content">
    <h1>Edit Your Profile</h1>
    
    <form:form modelAttribute="profile" id="profile" class="cmxform" method="POST">
        <fieldset>
            <input type="hidden" id="oldUserId" value="${profile.phone}" />
            <div class="field-holder">
                <label for="phone">Phone Number:</label>
                <form:input id="phone" name="phone" type="text" autocomplete="off" path="phone" />
            </div>
            <div class="field-holder">
                <label for="email">Email:</label>
                <form:input id="email" name="email" type="email" autocomplete="off" path="email" />
            </div>
            <div class="field-holder">
                <label for="password">Password:</label>
                <form:input id="password" name="password" type="password" autocomplete="off" path="password" />
            </div>
            <div class="field-holder">
                <label for="confirm_password">Confirm Password:</label>
                <input id="confirm_password" name="confirm_password" type="password" autocomplete="off" />
            </div>
            <div class="field-holder">
                <button class="btn btn-success save" type="submit">Save</button> or <a href="#">Cancel</a>
            </div>
        </fieldset>
    </form:form>
    <div class="right-col">
        <ul class="social-buttons">
            <li><h3>Connect Social Accounts:</h3></li>
            <li class="fb-btn linked"><i class="icon"></i><p>Facebook is linked to Like Labs. <a href="#" class="unlink">Unlink</a></p></li>
            <li class="fb-btn"><i class="icon"></i><a href="#" >Unlink</a></li>
            <li class="vk-btn"><i class="icon"></i> <a href="#">Connect with VK</a></li>
            <li class="twi-btn"><i class="icon"></i><a href="#">Connect with Twitter</a></li>
        </ul>
    </div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
    (function($){
        $(document).ready(function(){
           
           $("#profile").validate({
		rules: {
			phone: {
                            required: true,
                            minlength: 3
			},
			email: {
				required: true,
				email: true
			},
                        password: {
				minlength: 5
			},
                        confirm_password: {
				minlength: 5,
				equalTo: "#password"
			}
		},
		messages: {
			phone: {
                            required: "Please provide a phone number",
                            minlength: "The field must be at least 2 characters long"
                        },	
			email: {
                            required: "Please provide an email",
                            email: "Please enter a valid email address"
                        },
                        password: {
				minlength: "Your password must be at least 5 characters long"
			},
                        confirm_password: {
				minlength: "Your password must be at least 5 characters long",
				equalTo: "Please enter the same password as above"
			}
		}
            });
        });
    })(jQuery);
</script>

<%@include file="/WEB-INF/pages/admin_footer.jsp" %>