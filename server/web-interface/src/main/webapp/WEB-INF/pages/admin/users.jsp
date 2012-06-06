<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript" >
    var pager_options = {
        items_count: <c:out value="${count}"/>,
        config: {
            items_per_page : <c:out value="${items_per_page}"/>,
            next_text : "&gt;",
            num_display_entries : 6,
            num_edge_entries : 2,
            current_page: 0,
            prev_text : "&lt;",
            ellipse_text: "...",
            callback: pageSelectCallback
        }
    }
    
    
    $(document).ready(function(){
        $("#filter_date_from").datepicker();
        $("#filter_date_to").datepicker();
        initPager();
        
        $("#add_user_dialog").dialog({ autoOpen: false, title: "Add User", close: function(){document.getElementById("add_user_form").reset();$("#add_user_dialog").dialog({title: "Add User"})}, modal: true, minWidth: 400, minHeight: 200});
    });


    function initPager(page){
        pager_options.config.current_page = page > 0? page : 0;
        $("#pager").pagination(pager_options.items_count, pager_options.config)
    }

    var options = {
        page_number: 0
    };
    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        if (force_update)
            updateData();
        return false;
    }

    var force_update = true;
    var test_data = {"data":[{"id":"1","phone":"000000000011","email":"test1@test.com","name":"User1"},{"id":"1","phone":"000000000012","email":"test2@test.com","name":"User2"},{"id":"1","phone":"000000000013","email":"test3@test.com","name":"User3"}],"count":3};
    function updateData() {
        $.get("/admin/companies/data", options, function(response){
            response = test_data;
            if (response.error) {
                console.warn(response.error);
                return;
            }
            pager_options.items_count = response.count;
            force_update = false;
            initPager(options.page_number);
            force_update = true;
            fillTable(response.data);
        });
    }
  
    function fillTable(data) {
        var template = new EJS({url: "/static/templates/admin/user_table.ejs"}).render({users: data});
        $("#point_list_table").html(template);
    }
    
    function addUser(){
        $.get("/admin/users", $("#add_user_form").serialize(),function(){
            $("#add_user_dialog").dialog("close");
        })
    }
    
    function editUserDialog(id, phone, email, password){
        $("#add_user_dialog").dialog({title: "Edit User"})
        $("#add_user_phone").val(phone);
        $("#add_user_email").val(email);
        $("#add_user_password").val(password);
        $("#add_user_id").val(id);
        $("#add_user_dialog").dialog('open');
    }
    
</script>
<div id="content">
    <h1>Users</h1>
    <div style="height: 20px;"><div class="right" style="height: 20px;"><a href="javascript:void(0)" onclick="$('#add_user_dialog').dialog('open')">add</a></div><div class="clear"></div></div>
    <div id="point_list_table"></div>
    <div id="pager" class="pager" style="position: relative; float: right; padding-top: 20px"></div>
    <div class="clear"></div>
</div>
<div id="add_user_dialog" class="hidden">
    <form action="" onsubmit="addUser();return false;" id="add_user_form">
        <input type="hidden" name="id" id="add_user_id"/>
        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
            <tr>
                <td><label for="add_user_phone" style="width: 160px;">User Phone: </label></td>
                <td><input type="text" name="phone" id="add_user_phone"/></td>
            </tr>
            <tr>
                <td><label for="add_user_email" style="width: 160px;">User Email: </label></td>
                <td><input type="text" name="email" id="add_user_email"/></td>
            </tr>
            <tr>
                <td><label for="add_user_password" style="width: 160px;">User Password: </label></td>
                <td><input type="text" name="password" id="add_user_password"/></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center; padding-top: 30px;">
                    <button class="btn btn-success save" type="submit">Submit</button>
                    <button class="btn btn-info save" type="button" onclick="$('#add_user_dialog').dialog('close')">Cancel</button>
                </td>
            </tr>
        </table>
    </form>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>