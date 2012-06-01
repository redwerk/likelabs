<%@include file="/WEB-INF/pages/commons/header.jsp" %>

<div id="content">
    <h1>
        Notification Settings
    </h1>
    <form action="" method="post" onsubmit="return false;" id="settings_form">
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
                    <select>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
                <td> - </td>
            </tr>
            <tr style="background-color: #efefef">
                <td>Review was created by user</td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
            </tr>
            <tr style="background-color: #fdfeff">
                <td>Review was created for point</td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
            </tr>
            <tr style="background-color: #efefef">
                <td>User's review was approved</td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
                <td>
                    <select>
                        <option>Immediatly</option>
                        <option>Daily</option>
                        <option>Weekly</option>
                        <option>Monthly</option>
                    </select>
                </td>
            </tr>
        </table>
        <div class="field-holder" style="padding-top: 30px;">
            <button class="btn btn-success save" type="submit">Save</button> or &nbsp;<a href="javascript:void(0)" onclick="document.getElementById('settings_form').reset()">Cancel</a>
        </div>

    </form>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>