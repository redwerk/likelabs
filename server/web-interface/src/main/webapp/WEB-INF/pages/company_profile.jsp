<%@include file="./header.jsp" %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#add_administrator_dialog").dialog({ autoOpen: false, title: "Add administrator", modal: true, minWidth: 400, minHeight: 200});
        $("#add_social_page_dialog").dialog({ autoOpen: false, title: "Add social page", modal: true, minWidth: 400, minHeight: 200});
    })
    
    function submitAdministrator() {
        $.post("/company/1/profile/administrator", $("#add_administrator_form").serialize());
        $("#add_administrator_dialog").dialog("close");
    }

    function submitSocialPage() {
        $.post("/company/1/profile/social_page", $("#add_social_page_form").serialize());
        $("#add_social_page_dialog").dialog("close");
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td class="title">Edit Company Profile</td>
                                    </tr>
                                    <tr>
                                        <td class="body">
                                            <div class="form">
                                                <div class="left">
                                                    <div class="label"><label for="">Name</label></div>
                                                    <div class="field"><input type="text" /></div>

                                                    <div class="label"><label for="">Phone</label></div>
                                                    <div class="field"><input type="text" /></div>

                                                    <div class="label"><label for="">Email</label></div>
                                                    <div class="field"><input type="text" /></div>

                                                    <div class="label"><label for="">Moderation</label></div>
                                                    <div class="field"><input type="checkbox" /></div>
                                                    
                                                    <div style="margin-bottom: 20px">
                                                        <button class="btn btn_success save" type="button" style="">Save</button> or <a href="/company/1/profile/" >Cancel</a>
                                                    </div>

                                                    <div class="field">
                                                        <div class="left label">Points</div>
                                                        <div class="right"><a href="/company/1/point/add">add</a></div>
                                                    </div>
                                                    <table cellpadding="0" cellspacing="1" style="width: 100%; border: solid 1px #d2d9df" summary="" class="content_table field">
                                                        <thead style="height: 30px;">
                                                            <tr style="background-color: #efefef">
                                                                <th>Address</th>
                                                                <th>Phone</th>
                                                                <th width="40">&nbsp;</th>
                                                            </tr>
                                                        </thead>

                                                        <tr style="background-color: #fdfeff">
                                                            <td>Address1</td>
                                                            <td>+38(066)123-45-67</td>
                                                            <td>
                                                                <a href="/company/1/point/add"><img src="/static/images/edit-icon.png" alt=""/></a>
                                                                <a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a>
                                                            </td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td>Address2</td>
                                                            <td>+38(066)123-45-67</td>
                                                            <td>
                                                                <a href="/company/1/point/add"><img src="/static/images/edit-icon.png" alt=""/></a>
                                                                <a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a>
                                                            </td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td>Address3</td>
                                                            <td>+38(066)123-45-67</td>
                                                            <td>
                                                                <a href="/company/1/point/add"><img src="/static/images/edit-icon.png" alt=""/></a>
                                                                <a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a>
                                                            </td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td>Address4</td>
                                                            <td>+38(066)123-45-67</td>
                                                            <td>
                                                                <a href="/company/1/point/add"><img src="/static/images/edit-icon.png" alt=""/></a>
                                                                <a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a>
                                                            </td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td>Address5</td>
                                                            <td>+38(066)123-45-67</td>
                                                            <td>
                                                                <a href="/company/1/point/add"><img src="/static/images/edit-icon.png" alt=""/></a>
                                                                <a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a>
                                                            </td>
                                                        </tr>
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

                                                        <tr style="background-color: #fdfeff">
                                                            <td>Name1</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td>Name2</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td>Name3</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td>Name4</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td>Name5</td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                    </table>
                                                    <div id="add_administrator_dialog">
                                                        <form action="" onsubmit="submitAdministrator();return false;" id="add_administrator_form">
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
                                                                        <button class="btn btn_success save" type="button" onclick="$('#add_administrator_dialog').dialog('close')">Cancel</button>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </form>
                                                    </div>
                                                </div>
                                                <div class="right">
                                                    <div class="logo"><img src="http://lifehacker.ru/wp-content/uploads/2012/03/1229898047_mozilla_firefox.jpg" alt="" width="217"/></div>
                                                    <div class="clear"></div>
                                                    <form method="POST" enctype="multipart/form-data" action="logo" id="logoForm">
                                                        <div style="position: relative;">
                                                            <button class="btn upload_logo_btn" type="button" onclick="$('#file').click()">Upload New Logo</button>
                                                            <input type="file" class="file" id="file" onchange="submitLogo();return false;"/>
                                                        </div>
                                                    </form>
                                                    <div class="clear"></div>
                                                    <div class="field" style="padding-top: 20px;">
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

                                                        <tr style="background-color: #fdfeff">
                                                            <td><a href="javascript:void(0);">http://vk.com/dron48</a></td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td><a href="javascript:void(0);">http://vk.com/dron48</a></td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td><a href="javascript:void(0);">http://vk.com/dron48</a></td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #efefef">
                                                            <td><a href="javascript:void(0);">http://vk.com/dron48</a></td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                        <tr style="background-color: #fdfeff">
                                                            <td><a href="javascript:void(0);">http://vk.com/dron48</a></td>
                                                            <td><a href="javascript:void(0);"><img src="/static/images/delete.png" alt=""/></a></td>
                                                        </tr>
                                                    </table>
                                                    <div id="add_social_page_dialog">
                                                        <form action="" onsubmit="submitSocialPage();return false;" id="add_social_page_form">
                                                            <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
                                                                <tr>
                                                                    <td><label for="add_administrato_phone">Social network: </label></td>
                                                                    <td>
                                                                        <select name="social_type">
                                                                            <option value="facebook">facebook.com</option>
                                                                            <option value="vkontakte">vk.com</option>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td><label for="add_administrato_phone">Url: </label></td>
                                                                    <td><input type="text" name="url" id="add_social_page_url"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2" style="text-align: center; padding-top: 30px;">
                                                                        <button class="btn btn_success save" type="submit">Submit</button>
                                                                        <button class="btn btn_success save" type="button" onclick="$('#add_social_page_dialog').dialog('close')">Cancel</button>
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