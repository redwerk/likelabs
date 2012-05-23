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
                <label for="email">Password:</label>
                <form:input id="password" name="password" type="password" autocomplete="off" path="password" />
            </div>
            <div class="field-holder">
                <button class="btn btn-success save" type="submit">Save</button> or <a href="#">Cancel</a>
            </div>
        </fieldset>
    </form:form>
    <div class="right-col">
        <div class="logo-holder">
            <div class="company-logo"><img src="/companyadmin/profileImage" width="210" height="180" alt="Company Logo"  /></div>
            <form:form method="POST" enctype="multipart/form-data" action="logo" id="logoForm" modelAttribute="imageData" >
                <div style="position: relative;">
                    <button id="uploadLogo" class="btn upload-logo" type="button">Upload New Logo</button>
                    <form:input type="file" class="file" id="file" onchange="submitLogo();return false;" path="fileData" />
                </div>
            </form:form>
            <div class="clear"></div>
        </div>
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
			venueName: {
                            required: true,
                            minlength: 2
			},
			email: {
				required: true,
				email: true
			}
		},
		messages: {
			venueName: {
                            required: "Please provide a Venue Name",
                            minlength: "The name must be at least 2 characters long"
                        },	
			email: {
                            required: "Please provide an email",
                            email: "Please enter a valid email address"
                        }
		}
            });
            
            $("#uploadLogo").click(function(){
                $("#file").click();
            });
        });
    })(jQuery);
    function submitLogo(){
        $('iframe.file_hack').remove();
        var target = 'ifr' + (new Date()).getTime();
        var frame = '<iframe id="'+target+'" name="'+target+'" class="file_hack"></iframe>';
        $(frame).appendTo(document.body).css('display', 'none').load(function(){ 
           
        });        
        $("#logoForm").attr('target', target).submit();
       // event.preventDefault();       
    }
</script>

<%@include file="/WEB-INF/pages/admin_footer.jsp" %>