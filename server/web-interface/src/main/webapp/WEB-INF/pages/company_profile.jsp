<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript">
    var companyId = <c:out value="${company.id}" default="0"/>;
    $(document).ready(function(){
        $("#add_administrator_dialog").dialog({ autoOpen: false, title: "Add administrator", close: function(){document.getElementById("add_administrator_form").reset();}, modal: true, minWidth: 400, minHeight: 200});
        $("#add_social_page_dialog").dialog({ autoOpen: false, title: "Add social page", close: function(){document.getElementById("add_social_page_form").reset();}, modal: true, minWidth: 400, minHeight: 200});
        $("#error_dialog_admin").dialog({ autoOpen: false, title: "Error added company administrator", modal: true, width: 400, height: 150});
        $("#error_dialog_social").dialog({ autoOpen: false, title: "Error added social page", modal: true, width: 400, height: 150});
    })
    function addAdmin() {
        $.post("/company/" + companyId + "/profile/admin", $("#add_administrator_form").serialize(), function(response){
            if (!response.success) {
                $('#error_message_admin').html("");
                for (var key = 0 ;key < response.errors.length; key++) {
                    $('#error_message_admin').html($('#error_message_admin').html() + response.errors[key] + "<br/>");
                }
                $('#error_dialog_admin').dialog('open');
                return;
            }
            window.location.reload();
        });
    }
    function addPage() {
        $.post("/company/" + companyId + "/profile/page", $("#add_social_page_form").serialize(), function(response){
            if (!response.success) {
                $('#error_message_social').html(response.error);
                $('#error_dialog_social').dialog('open');
                return;
            }
            window.location.reload();
        });
    }
    function submitLogo() {
        $('#logoForm').submit();
    }
    function deleteAdmin(id) {
        confirmDialog("Delete administrator company", "Are you sure?",function(){
            $.ajax({
            url: "/company/" + companyId + "/profile/admin/" + id,
            type: "DELETE",
            success: function(response){
                if (!response.success) {
                    $('#eroor_message_admin').html(response.error);
                    $('#error_dialog_admin').dialog('open');
                    return;
                }
                window.location.reload();
            }
        })});
    }
    function deletePoint(id) {
        confirmDialog("Delete point", "Are you sure?",function(){
            $.ajax({
            url: "/company/" + companyId + "/profile/point/" + id,
            type: "DELETE",
            success: function(response){
                if (!response.success) {
                    //TODO
                    return;
                }
                window.location.reload();
            }
        })});
    }
    function deletePage(id) {
        confirmDialog("Delete social page", "Are you sure?",function(){
            $.ajax({
            url: "/company/" + companyId + "/profile/page/" + id,
            type: "DELETE",
            success: function(response){
                if (!response.success) {
                    $('#eroor_message_social').html(response.error);
                    $('#error_dialog_social').dialog('open');
                    return;
                }
                window.location.reload();
            }
        })});
    }
</script>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">${title}</td>
    </tr>
    <tr>
        <td class="body">
            <table cellpadding="0" cellspacing="0" summary="" class="form" style="width: 100%;">
                <tr>
                    <td>
                        <form:form method="POST" commandName="company" >
                            <div class="label"><label for="">Name <form:errors path="name" cssClass="errorblock"/></label></div>
                            <div class="field">
                                <form:input path="name" />
                            </div>

                            <div class="label"><label for="">Phone <form:errors path="phone" cssClass="errorblock"/></label></div>
                            <div class="field">
                                <form:input path="phone" />
                            </div>

                            <div class="label"><label for="">Email <form:errors path="email" cssClass="errorblock"/></label></div>
                            <div class="field">
                                <form:input path="email" />
                            </div>

                            <div class="label"><label for="">Moderation</label></div>
                            <div class="field">
                                <form:checkbox path="moderate" />
                            </div>
                            <form:hidden path="id"/>
                            <div style="margin-bottom: 20px">
                                <button class="btn btn_success save" type="submit" style="">Save</button>or&nbsp;&nbsp;<a href="/company/${company.id}/profile/cancel">Cancel</a>
                            </div>
                        </form:form>

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
                                    <th>Name</th>
                                    <th width="30">&nbsp;</th>
                                </tr>
                            </thead>


                            <c:forEach items="${admins}" var="admin">
                                <tr style="background-color: #fdfeff">
                                    <td>${admin.name}</td>
                                    <td><a href="javascript:void(0);" onclick="deleteAdmin(${admin.id})"><img src="/static/images/delete.png" title="Delete" alt="Delete"/></a></td>
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
                                            <button class="btn btn_success save" type="submit">Submit</button>
                                            <button class="btn btn-info save" type="button" onclick="$('#add_administrator_dialog').dialog('close')">Cancel</button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </td>
                    <td style="text-align: center; vertical-align: top; padding-left: 40px; width: 260px;">

                        <div class="logo" style="margin-left: 7px;"><img src="/company/${company.id}/profile/logo " alt="" width="217"/></div>
                        <div class="clear"></div>
                        <form method="POST" enctype="multipart/form-data" action="/company/${company.id}/profile/logo" id="logoForm">
                            <div style="position: relative;">
                                <button class="btn upload_logo_btn" type="button" onclick="$('#file').click()">Upload New Logo</button>
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
                                    <td><a href="javascript:void(0);" onclick="deletePage(${page.pageId})"><img src="/static/images/delete.png" alt=""/></a></td>
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
                                            <button class="btn btn_success save" type="submit">Submit</button>
                                            <button class="btn btn-info save" type="button" onclick="$('#add_social_page_dialog').dialog('close')">Cancel</button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div id="error_dialog_admin" style="width: 400px;">
                            <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                <tr>
                                    <td style="text-align: center"><span id="error_message_admin" class="errorblock"/></td>
                                </tr>
                                <tr>
                                    <td style="text-align: center"><button class="btn btn_success save" type="button" onclick="$('#error_dialog_admin').dialog('close');$('#add_administrator_dialog').dialog('open');">OK</button></td>
                                </tr>
                            </table>
                        </div>
                        <div id="error_dialog_social" style="width: 400px;">
                            <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                <tr>
                                    <td style="text-align: center"><span id="error_message_social" class="errorblock"/></td>
                                </tr>
                                <tr>
                                    <td style="text-align: center"><button class="btn btn_success save" type="button" onclick="$('#error_dialog_social').dialog('close');$('#add_social_page_dialog').dialog('open');">OK</button></td>
                                </tr>
                            </table>
                        </div>
            </table>
        </td>
    </tr>
</table>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>