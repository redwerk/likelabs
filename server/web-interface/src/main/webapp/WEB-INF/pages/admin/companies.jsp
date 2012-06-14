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
        
        $("#add_company_dialog").dialog({ autoOpen: false, title: "Add Company", close: function(){document.getElementById("add_company_form").reset();}, modal: true, minWidth: 400, minHeight: 200});
    });


    function initPager(page){
        pager_options.config.current_page = page > 0? page : 0;
        $("#pager").pagination(pager_options.items_count, pager_options.config)
    }

    var options = {
        page: 0
    };
    
    function pageSelectCallback(page_index, jq) {
        options.page = page_index;
        if (force_update)
            updateData();
        return false;
    }

    var force_update = true;
    function updateData() {
        $.get("/administrator/companies/data", options, function(response){
            if (!response.success) {
                errorDialog("Server error", response.error);
                return;
            }
            pager_options.items_count = response.count;
            force_update = false;
            initPager(options.page);
            force_update = true;
            fillTable(response.data);
        });
    }
  
    function fillTable(data) {
        var template = new EJS({url: "/static/templates/admin/company_table.ejs"}).render({companies: data});
        $("#point_list_table").html(template);
    }
    
    function addCompany(){
        $.post("/administrator/companies/add", $("#add_company_form").serialize(),function(response){
            if (!response.success) {
                errorDialog("Error adding company", response.error);
                return;
            }
            if (!response.valid) {
                errorsDialog("Validation company", response.messages);
                return;
            }
            $("#add_company_dialog").dialog("close");
            updateData();
        })
        
    }
    function deleteCompany(id) {
        confirmDialog("Delete company", "Are you sure?",function() {
            $.ajax({
                url: "/administrator/companies/" + id,
                type: "DELETE",
                success: function(response){
                    if (!response.success) {
                        errorDialog("Error deleting company", response.error);
                        return;
                    }
                    updateData();
                }
            })});
    }
    
</script>
<div id="content">
    <h1>Companies</h1>
    <div style="height: 20px;"><div class="right" style="height: 20px;"><a href="javascript:void(0)" onclick="$('#add_company_dialog').dialog('open')">add</a></div><div class="clear"></div></div>
    <div id="point_list_table"></div>
    <div id="pager" class="pager" style="position: relative; float: right; padding-top: 20px"></div>
    <div class="clear"></div>
</div>
<div id="add_company_dialog" class="hidden">
    <form action="" onsubmit="addCompany();return false;" id="add_company_form">
        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
            <tr>
                <td><label for="add_company_name" style="width: 160px;">Company Name: </label></td>
                <td><input type="text" name="name" id="add_company_name"/></td>
            </tr>
            <tr>
                <td><label for="add_company_phone" style="width: 160px;">Company Phone: </label></td>
                <td><input type="text" name="phone" id="add_company_phone"/></td>
            </tr>
            <tr>
                <td><label for="add_company_email" style="width: 160px;">Company Email: </label></td>
                <td><input type="text" name="email" id="add_company_email"/></td>
            </tr>
            <tr>
                <td><label for="add_company_admin_phone" style="width: 160px;">Administrator Phone: </label></td>
                <td><input type="text" name="admin_phone" id="add_company_admin_phone"/></td>
            </tr>
            <tr>
                <td><label for="add_company_admin_email" style="width: 160px;">Administrator Email: </label></td>
                <td><input type="text" name="admin_email" id="add_company_admin_email"/></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center; padding-top: 30px;">
                    <button class="btn btn-success save" type="submit">Submit</button>
                    <button class="btn btn-info save" type="button" onclick="$('#add_company_dialog').dialog('close')">Cancel</button>
                </td>
            </tr>
        </table>
    </form>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>