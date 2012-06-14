<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#add_administrator_dialog").dialog({ autoOpen: false, title: "Add administrator", close: function(){document.getElementById("add_administrator_form").reset();$("#add_administrator_dialog").dialog({title: "Add administrator"})}, modal: true, minWidth: 400, minHeight: 200});
        $("#edit_administrator_dialog").dialog({ autoOpen: false, title: "Edit administrator", close: function(){document.getElementById("edit_administrator_form").reset();$("#edit_administrator_dialog").dialog({title: "Edit administrator"})}, modal: true, minWidth: 400, minHeight: 200});
        $("#add_social_page_dialog").dialog({ autoOpen: false, title: "Add social page", close: function(){document.getElementById("add_social_page_form").reset();}, modal: true, minWidth: 400, minHeight: 200});
        <c:if test="${error eq true}">
            errorDialog("Error", '<c:out value="${message}"/>');
        </c:if>
        
    })
    function addAdmin() {
        $.post("/company/${company.id}/profile/admin/add", $("#add_administrator_form").serialize(), function(response){
            if (!response.success) {
                errorDialog("Error adding administrator", response.error);
                return;
            }
            if (!response.valid) {
                errorsDialog("Validation administrator", response.messages);
                return;
            }
            $("#add_administrator_dialog").dialog("close");
            window.location.reload();
        });
    }

    function addPage() {
        $.post("/company/${company.id}/profile/page", $("#add_social_page_form").serialize(), function(response){
            if (!response.success) {
                errorDialog("Error adding social page", response.error);
                return;
            }
            if (!response.valid) {
                errorsDialog("Vlidation social page", response.messages);
                return;
            }
            $("#add_social_page_dialog").dialog("close");
            window.location.reload();
        });
    }
    function submitLogo() {
        $('#logoForm').submit();
    }
    function deleteAdmin(id) {
        confirmDialog("Delete administrator company", "Are you sure?",function(){
            $.ajax({
                url: "/company/${company.id}/profile/admin/" + id,
                type: "DELETE",
                success: function(response){
                    if (!response.success) {
                        errorDialog("Error deleting administrator", response.error);
                        return;
                    }
                    window.location.reload();
                }
            })});
    }
    function deletePoint(id) {
        confirmDialog("Delete point", "Are you sure?",function(){
            $.ajax({
                url: "/company/${company.id}/profile/point/" + id,
                type: "DELETE",
                success: function(response){
                    if (!response.success) {
                        errorDialog("Error deleting point", response.error);
                        return;
                    }
                    window.location.reload();
                }
            })});
    }
    function deletePage(id) {
        confirmDialog("Delete social page", "Are you sure?",function(){
            $.ajax({
                url: "/company/${company.id}/profile/page/" + id,
                type: "DELETE",
                success: function(response){
                    if (!response.success) {
                        errorDialog("Error deleting page", response.error);
                        return;
                    }
                    window.location.reload();
                }
            })});
    }

    <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
        function editAdmin() {
            $.post("/company/${company.id}/profile/admin/edit", $("#edit_administrator_form").serialize(), function(response){
                if (!response.success) {
                    errorDialog("Error editing administrator", response.error);
                    return;
                }
                if (!response.valid) {
                    errorsDialog("Validation administrator", response.messages);
                    return;
                }
                $("#edit_administrator_dialog").dialog("close");
                window.location.reload();
            });
        }

        function editUserDialog(id, phone, email, password){
            $("#edit_administrator_phone").val(phone);
            $("#edit_administrator_email").val(email);
            $("#edit_administrator_password").val(password);
            $("#edit_administrator_id").val(id);
            $("#edit_administrator_dialog").dialog('open');
        }
    </sec:authorize>
</script>
<div id="content">
    <h1>${title}</h1> 
            <table cellpadding="0" cellspacing="0" summary="" class="form" style="width: 100%;">
                <tr>
                    <td>
                        <form:form method="POST" commandName="company" >
                            <div class="label"><label for="name">Name <form:errors path="name" cssClass="errorblock" cssStyle="font-weight: normal;"/></label></div>
                            <div class="field">
                                <form:input path="name" />
                            </div>

                            <div class="label"><label for="phone">Phone <form:errors path="phone" cssClass="errorblock" cssStyle="font-weight: normal;"/></label></div>
                            <div class="field">
                                <form:input path="phone" />
                            </div>

                            <div class="label"><label for="email">Email <form:errors path="email" cssClass="errorblock" cssStyle="font-weight: normal;"/></label></div>
                            <div class="field">
                                <form:input path="email" />
                            </div>

                                <div class="label"><label for="moderate1" style="vertical-align: middle;">Moderation</label> <form:checkbox path="moderate" cssStyle="margin: 0; vertical-align: middle;" /></div>
                            <div class="field">
                                
                            </div>
                            <form:hidden path="id"/>
                            <div style="margin-bottom: 20px">
                                <button class="btn btn-success save" type="submit" style="">Save</button>or&nbsp;&nbsp;<a href="/company/${company.id}/profile/cancel">Cancel</a>
                            </div>
                        </form:form><br/>

                        <div class="field">
                            <div class="left label">Points</div>
                            <div class="right"><a href="/company/${company.id}/point/0/profile">add</a></div>
                        </div>
                        <table cellpadding="0" cellspacing="1" style="width: 100%; border: solid 1px #d2d9df" summary="" class="content_table field">
                            <thead style="height: 30px;">
                                <tr style="background-color: #efefef">
                                    <th>Address</th>
                                    <th>Phone</th>
                                    <th width="40">&nbsp;</th>
                                </tr>
                            </thead>

                            <c:forEach items="${points}" var="point" >
                                <tr style="background-color: #fdfeff">
                                    <td><c:if test="${empty point.address.addressLine1}">N/A</c:if>${point.address.addressLine1}</td>
                                    <td>${point.phone}</td>
                                    <td>
                                        <a href="/company/${company.id}/point/${point.id}/profile"><img src="/static/images/edit-icon.png" title="Edit" alt="Edit"/></a>
                                        <a href="javascript:void(0);" onclick="deletePoint(${point.id})"><img src="/static/images/delete.png" title="Delete" alt="Delete"/></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>

                        <div class="field">
                            <div class="left label">Administrators</div>
                            <div class="right"><a href="javascript:void(0);" onclick="$('#add_administrator_dialog').dialog('open');">add</a></div>
                        </div>

                        <table cellpadding="0" cellspacing="1" style="width: 100%; border: solid 1px #d2d9df" summary="" class="content_table field">
                            <thead style="height: 30px;">
                                <tr style="background-color: #efefef">
                                    <th>Active</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>Email</th>
                                    <th width="40">&nbsp;</th>
                                </tr>
                            </thead>


                            <c:forEach items="${admins}" var="admin">
                                <tr style="background-color: #fdfeff">
                                    <td>
                                        <input type="checkbox" disabled="disabled" <c:if test="${admin.active eq true}">checked="checked"</c:if> />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${admin.active eq true}">${admin.name}</c:when>
                                            <c:when test="${admin.active eq false}">-</c:when>
                                        </c:choose>
                                    </td>
                                    <td>${admin.phone}</td>
                                    <td>${admin.email}</td>
                                    <td>
                                        <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
                                            <a href="javascript:void(0);" onclick="editUserDialog('${admin.id}','${admin.phone}', '${admin.email}', '${admin.password}');"><img src="/static/images/edit-icon.png" title="Edit" alt="Edit"/></a>
                                        </sec:authorize>
                                        <a href="javascript:void(0);" onclick="deleteAdmin(${admin.id})"><img src="/static/images/delete.png" title="Delete" alt="Delete"/></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <div id="add_administrator_dialog">
                            <form action="" onsubmit="addAdmin();return false;" id="add_administrator_form">
                                <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                    <tr>
                                        <td><label for="add_administrator_phone">Phone: </label></td>
                                        <td><input type="text" name="phone" id="add_administrator_phone"/></td>
                                    </tr>
                                    <tr>
                                        <td><label for="add_administrator_email">Email: </label></td>
                                        <td><input type="text" name="email" id="add_administrator_email"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" style="text-align: center; padding-top: 30px;">
                                            <button class="btn btn-success save" type="submit">Submit</button>
                                            <button class="btn btn-info save" type="button" onclick="$('#add_administrator_dialog').dialog('close')">Cancel</button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN')">
                            <div id="edit_administrator_dialog">
                                <form action="" onsubmit="editAdmin();return false;" id="edit_administrator_form">
                                    <input type="hidden" name="id" id="edit_administrator_id"/>
                                    <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                        <tr>
                                            <td><label for="edit_administrator_phone">Phone: </label></td>
                                            <td><input type="text" name="phone" id="edit_administrator_phone"/></td>
                                        </tr>
                                        <tr>
                                            <td><label for="edit_administrator_email">Email: </label></td>
                                            <td><input type="text" name="email" id="edit_administrator_email"/></td>
                                        </tr>

                                            <tr>
                                                <td><label for="edit_administrator_password">Password: </label></td>
                                                <td><input type="text" name="password" id="edit_administrator_password"/></td>
                                            </tr>
                                        <tr>
                                            <td colspan="2" style="text-align: center; padding-top: 30px;">
                                                <button class="btn btn-success save" type="submit">Submit</button>
                                                <button class="btn btn-info save" type="button" onclick="$('#edit_administrator_dialog').dialog('close')">Cancel</button>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </div>
                        </sec:authorize>
                    </td>
                    <td style="text-align: center; vertical-align: top; padding-left: 40px; width: 260px;">

                        <div class="logo" style="margin-left: 7px;"><img src="/company/${company.id}/profile/logo " alt="logo" width="217"/></div>
                        <div class="clear"></div>
                        <form method="POST" enctype="multipart/form-data" action="/company/${company.id}/profile/logo" id="logoForm">
                            <div style="position: relative;">
                                <button class="btn upload-logo" type="button" onclick="$('#file').click()">Upload New Logo</button>
                                <input name="logo" type="file" class="file" id="file" onchange="submitLogo();return false;"/>
                            </div>
                        </form>
                        <div class="clear"></div>
                        <div class="field" style="padding-top: 19px;">
                            <div class="left label">Social pages</div>
                            <div class="right"><a href="javascript:void(0);" onclick="$('#add_social_page_dialog').dialog('open');">add</a></div>
                        </div>
                        <table cellpadding="0" cellspacing="1" style="width: 100%; border: solid 1px #d2d9df" summary="" class="content_table field">
                            <thead style="height: 30px;">
                                <tr style="background-color: #efefef">
                                    <th>Url</th>
                                    <th width="30">&nbsp;</th>
                                </tr>
                            </thead>

                            <c:forEach items="${socialPages}" var="page">
                                <tr style="background-color: #fdfeff">
                                    <td><a href="${page.url}" target="_blank">${page.url}</a></td>
                                    <td><a href="javascript:void(0);" onclick="deletePage(${page.pageId})"><img src="/static/images/delete.png" alt="delete" title="Delete"/></a></td>
                                </tr>
                            </c:forEach>
                        </table>
                        <div id="add_social_page_dialog">
                            <form action="" onsubmit="addPage();return false;" id="add_social_page_form">
                                <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                    <tr>
                                        <td><label for="social_type_select">Social network: </label></td>
                                        <td>
                                            <select name="type" id="social_type_select">
                                                <option value="facebook">facebook.com</option>
                                                <option value="vkontakte">vk.com</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label for="add_social_page_url">Url: </label></td>
                                        <td><input type="text" name="url" id="add_social_page_url"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" style="text-align: center; padding-top: 30px;">
                                            <button class="btn btn-success save" type="submit">Submit</button>
                                            <button class="btn btn-info save" type="button" onclick="$('#add_social_page_dialog').dialog('close')">Cancel</button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
            </table>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>