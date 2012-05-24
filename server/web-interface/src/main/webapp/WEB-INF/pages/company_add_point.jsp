<%@include file="./header.jsp" %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#add_tablet_dialog").dialog({ autoOpen: false, title: "Attach tablet", modal: true, width: 400, height: 240});
    })
    
    function submitTablet() {
        $.post("/company/1/point/tablet", $("#add_tablet_form").serialize());
        $("#add_tablet_dialog").dialog("close");
    }

</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td class="title">Edit Point for {CompanyName}</td>
                                    </tr>
                                    <tr>
                                        <td class="body">
                                            <div class="form">
                                                <div class="left">
                                                    <div class="label"><label for="">Address</label></div>
                                                    <div class="field"><input type="text" /></div>

                                                    <div class="label"><label for="">Phone</label></div>
                                                    <div class="field"><input type="text" /></div>

                                                    <div class="label"><label for="">Email</label></div>
                                                    <div class="field"><input type="text" /></div>
                                                    
                                                    <div style="margin-bottom: 20px">
                                                        <button class="btn btn_success save" type="button" style="">Save</button>or&nbsp;&nbsp;<a href="/company/1/profile/" >Cancel</a>
                                                    </div>

                                                    <div class="field">
                                                        <div class="left label">Attached Tablets</div>
                                                        <div class="right"><a href="javascript:void(0);" onclick="$('#add_tablet_dialog').dialog('open');">add</a></div>
                                                    </div>
                                                    <table cellpadding="0" cellspacing="1" style="width: 100%; border: solid 1px #d2d9df" summary="" class="content_table field">
                                                        <thead style="height: 30px;">
                                                            <tr style="background-color: #efefef">
                                                                <th>Login</th>
                                                                <th>Login Password</th>
                                                                <th>Logout Password</th>
                                                                <th width="30">&nbsp;</th>
                                                            </tr>
                                                        </thead>

                                                        <tr style="background-color: #fdfeff">
                                                            <td>login1</td>
                                                            <td>123456asd</td>
                                                            <td>123456qwe</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt="Delete" title="Delete"/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td>login1</td>
                                                            <td>123456asd</td>
                                                            <td>123456qwe</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt="Delete" title="Delete"/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td>login1</td>
                                                            <td>123456asd</td>
                                                            <td>123456qwe</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt="Delete" title="Delete"/></a></td>
                                                        </tr>
                                                    </table>
                                                    <div id="add_tablet_dialog" style="width: 400px;">
                                                        <form action="" onsubmit="submitTablet();return false;" id="add_tablet_form">
                                                            <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                                                <tr>
                                                                    <td><label for="add_point_login">Login: </label></td>
                                                                    <td><input type="text" name="phone" id="add_point_login"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><label for="add_point_password">Login Password: </label></td>
                                                                    <td><input type="text" name="phone" id="add_point_password"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><label for="add_point_password_logout">Logout Password: </label></td>
                                                                    <td><input type="text" name="phone" id="add_point_password_logout"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" style="text-align: center; padding-top: 30px;">
                                                                        <button class="btn btn_success save" type="submit">Submit</button>
                                                                        <button class="btn btn-info save" type="button" onclick="$('#add_tablet_dialog').dialog('close')">Cancel</button>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="./footer.jsp" %>