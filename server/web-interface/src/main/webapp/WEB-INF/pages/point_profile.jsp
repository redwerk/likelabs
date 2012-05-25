<%@include file="./header.jsp" %>
<script type="text/javascript">
    var companyId = <c:out value="${companyId}" default="0"/>;
    var pointId = <c:out value="${pointId}" default="0"/>;
    $(document).ready(function(){
        $("#add_tablet_dialog").dialog({ autoOpen: false, title: "Attach tablet", modal: true, width: 400, height: 250});
        $("#error_dialog").dialog({ autoOpen: false, title: "Error tablet", modal: true, width: 400, height: 150});
    })
    var tablet = {
        login : null,
        loginPassword : null,
        logoutPassword : null,
        id : 0
    };
    function addTablet() {
        $.post("/company/" + companyId + "/point/" + pointId + "/profile/tablet", $("#add_tablet_form").serialize(),function(response){
            $("#add_tablet_dialog").dialog("close");
            if (!response.success) {
                $('#error_dialog').dialog('open');
                $('#eroor_message').html(response.error);
                return;
            }
            window.location.reload();
        });
    }
    function deleteTablet(id) {
        $.ajax({
            url: "/company/" + companyId + "/point/" + pointId + "/profile/tablet/" + id,
            type: "DELETE",
            success: function(response){
                if (!response.success) {
                    $('#error_dialog').dialog('open');
                    $('#eroor_message').html(response.error);
                    return;
                }
                window.location.reload();
            }
        })
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td class="title">${title} ${companyName}</td>
                                    </tr>
                                    
                                    <tr>
                                        <td class="body">
                                            <div class="form">
                                                <div class="left">
                                                    <form:form commandName="point" method="POST">
                                                    <div class="label"><label for="">City</label></div>
                                                    <div class="field">
                                                        <form:input path="city" />
                                                        <form:errors path="city" cssClass="errorblock"/>
                                                    </div>

                                                    <div class="label"><label for="">State</label></div>
                                                    <div class="field">
                                                        <form:input path="state" />
                                                        <form:errors path="state" cssClass="errorblock"/>
                                                    </div>
                                                    
                                                    <div class="label"><label for="">Country</label></div>
                                                    <div class="field">
                                                        <form:input path="country" />
                                                        <form:errors path="country" cssClass="errorblock"/>
                                                    </div>
                                                    
                                                    <div class="label"><label for="">Postal code</label></div>
                                                    <div class="field">
                                                        <form:input path="postalCode" />
                                                        <form:errors path="postalCode" cssClass="errorblock"/>
                                                    </div>
                                                    
                                                    <div class="label"><label for="">Address first</label></div>
                                                    <div class="field">
                                                        <form:input path="addressLine1" />
                                                        <form:errors path="addressLine1" cssClass="errorblock"/>
                                                    </div>
                                                    
                                                    <div class="label"><label for="">Address two</label></div>
                                                    <div class="field">
                                                        <form:input path="addressLine2" />
                                                        <form:errors path="addressLine2" cssClass="errorblock"/>
                                                    </div>
                                                    <div class="label"><label for="">Phone</label></div>
                                                    <div class="field">
                                                        <form:input path="phone" />
                                                        <form:errors path="phone" cssClass="errorblock"/>
                                                    </div>
                                                    <div class="label"><label for="">Email</label></div>
                                                    <div class="field">
                                                        <form:input path="email" />
                                                        <form:errors path="email" cssClass="errorblock"/>
                                                    </div>
                                                        <form:hidden path="id"/>
                                                    <div style="margin-bottom: 20px">
                                                        <button class="btn btn_success save" type="submit" style="">Save</button>or&nbsp;&nbsp;<a href="/company/${companyId}/point/${pointId}/profile/cancel" >Cancel</a>
                                                    </div>
                                                    </form:form>
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
                                                        <c:forEach items="${tablets}" var="tablet">
                                                        <tr style="background-color: #fdfeff">
                                                            <td>${tablet.login}</td>
                                                            <td>${tablet.loginPassword}</td>
                                                            <td>${tablet.logoutPassword}</td>
                                                            <td>
                                                                <a href="javascript:deleteTablet(${tablet.id});"><img src="/static/images/delete.png" title="Delete" alt="Delete"/></a>
                                                            </td>
                                                        </tr>
                                                        </c:forEach>

                                                    </table>
                                                    <div id="add_tablet_dialog" style="width: 400px;">
                                                        <form action="" onsubmit="return false;" id="add_tablet_form">
                                                            <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                                                <tr>
                                                                    <td><label for="add_point_login">Login: </label></td>
                                                                    <td><input type="text" name="login" id="add_point_login"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><label for="add_point_password">Login Password: </label></td>
                                                                    <td><input type="text" name="loginPassword" id="add_point_password"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td><label for="add_point_password_logout">Logout Password: </label></td>
                                                                    <td><input type="text" name="logoutPassword" id="add_point_password_logout"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" style="text-align: center; padding-top: 30px;">
                                                                        <button class="btn btn_success save" type="submit" onclick="addTablet();">Submit</button>
                                                                        <button class="btn btn-info save" type="button" onclick="$('#add_tablet_dialog').dialog('close')">Cancel</button>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </form>
                                                    </div>
                                                    <div id="error_dialog" style="width: 400px;">
                                                        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                                            <tr>
                                                                <td style="text-align: center"><span id="eroor_message" class="errorblock"></span></td>
                                                            </tr>
                                                            <tr>
                                                                <td style="text-align: center"><button class="btn btn_success save" type="button" onclick="$('#error_dialog').dialog('close');$('#add_tablet_dialog').dialog('open')">OK</button></td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="./footer.jsp" %>