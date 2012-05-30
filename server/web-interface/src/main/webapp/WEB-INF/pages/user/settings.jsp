<%@include file="/WEB-INF/pages/commons/header.jsp" %>

<div id="content">
    <h1>
        My Settings
    </h1>
    <div class="items-holder" >
        <div class="checkbox-holder">
            <table cellpadding="0" cellspacing="0" summary="">
                <tr>
                    <td style="vertical-align: top; width: 20px;"><input type="checkbox" /></td>
                    <td>Publish the reviews to my Facebook and VK pages (if connected)</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 20px;"><input type="checkbox" /></td>
                    <td>Notify me when I add a new review</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 20px;"><input type="checkbox" /></td>
                    <td>Notify me of any new reviews for the point if I am the client there</td>
                </tr>
                <tr>
                    <td style="vertical-align: top; width: 20px;"><input type="checkbox" /></td>
                    <td>Notify me when the administrator approves my review</td>
                </tr>
            </table>
        </div>
        <div class="field-holder" style="padding-top: 20px;">
            <button class="btn btn-success save" type="submit">Save</button> or &nbsp;<a href="/user">Cancel</a>
        </div>
        <div class="clear"></div>
    </div>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>