<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
    <h1>
        Notification Settings
    </h1>
    <form:form commandName="settings" method="post">
        <table style="width: 100%; border: solid 1px #d2d9df;" class="settings_table content_table">
            <thead>
                <tr style="background-color: #efefef">
                    <th>Event</th>
                    <th>SMS Interval</th>
                    <th>Email Interval</th>
                </tr>
            </thead>
            <tr style="background-color: #fdfeff">
                <td>User's email is not specified</td>
                <td>
                    <form:select path="userNotEmail">
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
                <td> - </td>
            </tr>
            <tr style="background-color: #efefef">
                <td>Review was created by user</td>
                <td>
                    <form:select path="sms.userCreatedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
                <td>
                    <form:select path="email.userCreatedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
            </tr>
            <tr style="background-color: #fdfeff">
                <td>Review was created for point</td>
                <td>
                    <form:select path="sms.pointCreatedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
                <td>
                    <form:select path="email.pointCreatedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
            </tr>
            <tr style="background-color: #efefef">
                <td>User's review was approved</td>
                <td>
                    <form:select path="sms.userApprovedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
                <td>
                    <form:select path="email.userApprovedReview">
                        <form:option value="IMMEDIATELY">Immediately</form:option>
                        <form:option value="DAILY">Daily</form:option>
                        <form:option value="WEEKLY">Weekly</form:option>
                        <form:option value="MONTHLY">Monthly</form:option>
                    </form:select>
                </td>
            </tr>
        </table>
        <div class="field-holder" style="padding-top: 30px;">
            <form:button class="btn btn-success save" type="submit">Save</form:button> or &nbsp;<a href="/" >Cancel</a>
        </div>

    </form:form>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>