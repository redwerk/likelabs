<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript">
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
        $("#filter_date_from").datepicker({dateFormat: "dd/mm/yy"});
        $("#filter_date_to").datepicker({dateFormat: "dd/mm/yy"});
        initPager();
        $("input[name=status_filter]").change(function(){
            updateOptions();
            updateData();
        });
        $(".status-filter label").click(function(){
            $(".status-filter label").removeClass("active");
            $(this).toggleClass("active");
        })
        var filter_status = $("input[name=status_filter]:checked").val();
        filter_status = filter_status? filter_status : "all";
        $("label[for=status_" + filter_status + "]").click();
        
        $("#edit_feed_dialog").dialog({ autoOpen: false, title: "Edit feed", close: function(){document.getElementById("edit_feed_form").reset();}, modal: true, minWidth: 400, minHeight: 200});

        for (var key=0; key < companies.length; key++) {
            var company = companies[key];
            $("#company").append($("<option></option>").val(company.id).text(company.name));
        }
    });


    function initPager(page){
        updateOptions();
        pager_options.config.current_page = page > 0? page : 0;
        $("#pager").pagination(pager_options.items_count, pager_options.config)
    }

    var options = {
        feedType: null,
        point: null,
        toDate: null,
        fromDate: null,
        sortBy: null,
        page: 0,
        status: null,
        company: ""
    };

    function pageSelectCallback(page_index, jq) {
        options.page = page_index;
        if (force_update)
            updateData();
        return false;
    }
    var force_update = true;
    
    function updateData() {
        if (!options.company) options.point = "";
        $.get("/user/${userId}/feed/data", options, function(response){
            if (!response.success) {
                errorDialog("Server error", response.message);
                return;
            }
            pager_options.items_count = response.count;
            force_update = false;
            initPager(options.page);
            force_update = true;
            fillTable(response.data);
        });
    }

    function getCompany(companyId) {
        
        if (!companyId) return null;
        companyId = parseInt(companyId);
        for (var key=0; key<companies.length; key++) {
            if (companyId == parseInt(companies[key].id)) {
                return companies[key];
            }
        }
        return null;
    }

    function updatePoints(companyId){
        var select = $("<select></select>")
        .attr("onchange", "changeFilter()")
        .attr("id", "point")
        .css("width", "192px");
        select.append($("<option></option>").val("").text("All"));

        var company = getCompany(companyId);


        if (company && company.points && company.points.length > 0 ) {
            for (var key = 0; key < company.points.length; key++) {
                var point = company.points[key];
                var option = $("<option></option>").val(point.id).text(point.address);
                select.append(option);
            }
        }
        if (!$("#company").val()) {
            select.attr("disabled", "disabled");
        } else {
            select.removeAttr("disabled");
        }
        $("#point").replaceWith(select);
    }

    function changeCompany(){
        if (!$("#company").val()) {
            $("#point").val("");
            $("#point").attr("disabled");
        } else {
            $("#point").removeAttr("disabled");
        }

        options.company = $("#company").val();
        options.point = "";
        updatePoints($("#company").val());
        updateData();
        
    }

    function fillTable(data) {
        var template = new EJS({url: "/static/templates/user_review_table.ejs"}).render({feeds: data});
        $("#feeds_table").html(template);
    }
    
    function changeFilter() {
        options.fromDate = $("#filter_date_from").val();
        options.toDate = $("#filter_date_to").val();
        options.feedType = $("#feed_type").val();
        options.company = $("#company").val();
        options.point = $("#point").val() != null ? $("#point").val() : "";
        options.page = 0;
        initPager();
    }
    
    function changeSort() {
        options.sortBy = $("#sort_by").val();
        updateData();
    }
    
    function updateOptions() {
        options.fromDate = $("#filter_date_from").val();
        options.toDate = $("#filter_date_to").val();
        options.feedType = $("#feed_type").val();
        options.point = $("#point").val() != null ? $("#point").val() : "";
        options.page = 0;
        options.sortBy = $("#sort_by").val();
        options.status = $("input[name=status_filter]:checked").val();
    }
    
    function editFeedDialog (id, message) {
        $("#edit_feed_id").val(id);
        $("#edit_feed_message").val(message);
        $("#edit_feed_dialog").dialog("open");
    }
    
    function submitEditDialog(id){
        $.post("/user/${userId}/feed/"+ id +"/edit", $("#edit_feed_form").serialize(), function(response) {
            if (!response.success) {
                errorDialog("Error edit review", response.message);
                return;
            }
            updateData(); 
        });
        $("#edit_feed_dialog").dialog("close");
    }

    function removeReview(id) {
        confirmDialog("Remove review", "Are you sure?", function(){
            $.ajax({
                url: "/user/${userId}/feed/"+ id +"/remove",
                type: "DELETE",
                success: function(response){
                    if (!response.success) {
                        errorDialog("Error removing review", response.message);
                        return;
                    }
                    updateData();
                }
            });
        });
    }
    var companies = [
        <c:forEach var="company" items="${companies}" varStatus="c">
        {
            "id": "${company.id}",
            "name": "${company.name}",
            "points" : [
            <c:forEach var="point" items="${company.points}" varStatus="p" >
                    {"id" : "${point.id}", "address": "${point.addressLine1}"}<c:if test="${(p.index +1) lt company.pointsCount}">,</c:if>
            </c:forEach>
            ]
        }<c:if test="${(c.index +1) lt count}">,</c:if>
        </c:forEach>
    ]
</script>
<style type="text/css">
    .filter-holder label{
        width: 90px;
    }
    .filter-holder select{
        width: 200px;
    }
</style>
<div id="content">
    <h1>
        My Feed
    </h1>
    <div class="order-holder">
        <select onchange="changeSort()" id="sort_by" style="width: 150px">
            <option value="">Sort By</option>
            <option value="date">Date</option>
            <option value="company_and_point">Point</option>
            <option value="review_type">Content type</option>
        </select>
    </div>
    <div class="items-holder" >      

        <div class="filter-holder">
            <div>
                <label for="feed_type">Contains:</label>
                <select id="feed_type" onchange="changeFilter()">
                    <option value="">Not selected</option>
                    <option value="contains_text">Text</option>
                    <option value="contains_photo">Photo</option>
                    <option value="contains_text_and_photo">Text and Photo</option>
                </select>
                <label for="filter_date_from">Date :</label>
                <input type="text" id="filter_date_from" onchange="changeFilter()" />
                -
                <input type="text" id="filter_date_to" onchange="changeFilter()" />
            </div>
            <div>
                <label for="location" >Companies:</label>
                <select id="company" onchange="changeCompany()">
                    <option value="">All</option>
                </select>
                <label for="point">Point:</label>
                <select onchange="changeFilter()" id="point" style="width: 190px;" disabled="">
                    <option value="">All</option>
                </select>
            </div>
        </div>
        <div id="feeds_table" class="items-inner"></div>
        <div id="pager" class="pager"></div>
        <div class="clear"></div>
    </div>
</div>
<div id="edit_feed_dialog" class="hidden">
    <form action="" onsubmit="submitEditDialog($('#edit_feed_id').val());return false;" id="edit_feed_form">
        <input type="hidden" name="id" id="edit_feed_id"/>
        <table cellpadding="0" cellspacing="0" summary="" class="dialog_form">
            <tr>
                <td><label for="edit_feed_message">Message: </label></td>
                <td><textarea name="message" id="edit_feed_message" style="width: 200px; height: 80px;"></textarea></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center; padding-top: 30px;">
                    <button class="btn btn-success save" type="submit">Submit</button>
                    <button class="btn btn-info save" type="button" onclick="$('#edit_feed_dialog').dialog('close')">Cancel</button>
                </td>
            </tr>
        </table>
    </form>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>