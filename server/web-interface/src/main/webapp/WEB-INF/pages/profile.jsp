<%@include file="./admin_header.jsp" %>

<div id="content">
    <h1>Edit Your Profile</h1>
    
    <form id="companyProfile" class="cmxform" method="POST">
        <fieldset>
            <div class="field-holder">
                <label for="venueName">Venue Name:</label>
                <input id="venueName" name="venueName" type="text" autocomplete="off" minlength="2" />
            </div>
            <div class="field-holder">
                <label for="phone">Phone Number:</label>
                <input id="phone" name="phone" type="text" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="email">Email:</label>
                <input id="email" name="email" type="email" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="website">Website:</label>
                <input id="website" name="website"  type="url" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="address1">Addresse Line 1:</label>
                <input id="address1" name="address1" type="text" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="address2">Addresse Line 2:</label>
                <input id="address2" name="address2" type="text" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="city">City:</label>
                <input id="city" name="city" type="text" autocomplete="off" />
            </div>
            <div class="field-holder">
                <label for="postalCode">Postal Code:</label>
                <input id="postalCode" name="postalCode" type="text" autocomplete="off" />
            </div>
            <div class="field-holder">
                <button class="btn btn-success save" type="submit">Save</button> or <a href="#">Cancel</a>
            </div>
        </fieldset>
    </form>
    <div class="right-col">
        <div class="logo-holder">
            <div class="company-logo"><img src="/static/images/team-member.png" width="210" height="180" alt="Company Logo"  /></div>
            <form method="POST" enctype="multipart/form-data" action="logo" id="logoForm">
                <div style="position: relative;">
                    <button id="uploadLogo" class="btn upload-logo" type="button">Upload New Logo</button>
                    <input type="file" class="file" id="file" onchange="submitLogo();return false;"/>
                </div>
            </form>
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
           
           $("#companyProfile").validate({
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

<%@include file="./admin_footer.jsp" %>