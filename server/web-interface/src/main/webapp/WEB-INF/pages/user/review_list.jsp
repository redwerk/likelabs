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
        
    });


    function initPager(page){
        updateOptions();
        pager_options.config.current_page = page > 0? page : 0;
        $("#pager").pagination(pager_options.items_count, pager_options.config)
    }

    var options = {
        feed_type: null,
        point: null,
        date_to: null,
        date_from: null,
        sort_by: null,
        page_number: 0,
        status: null
    };
    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        if (force_update)
            updateData();
        return false;
    }
    var force_update = true;
    
    var test_data = {"data":[{"id":16,"message":"message16","point":"12345 Sample1 Street","name":"Anonymous","date":1333621680000},{"id":15,"message":"message15","point":"12345 Sample1 Street","name":"Anonymous","date":1330946880000},{"id":14,"message":"message14","point":"12345 Sample2 Street","name":"Anonymous","date":1315218480000},{"id":11,"message":"message11","point":"12345 Sample2 Street","name":"Anonymous","date":1315218480000},{"id":13,"message":"message13","point":"12345 Sample1 Street","name":"Anonymous","date":1315132080000},{"id":12,"message":"message12","point":"12345 Sample2 Street","name":"Anonymous","date":1315045680000},{"id":10,"message":"message10","point":"12345 Sample1 Street","name":"Anonymous","date":1314959280000},{"id":1,"message":null,"point":"12345 Sample1 Street","name":"Anonymous","date":1314872881000}],"count":16};
    function updateData() {
        $.get("/user/reviews/", options, function(response){
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
        var template = new EJS({url: "/static/templates/user_review_table.ejs"}).render({feeds: data});
        $("#feeds_table").html(template);
    }
    
    function changeFilter() {
        options.date_from = $("#filter_date_from").val();
        options.date_to = $("#filter_date_to").val();
        options.feed_type = $("#feed_type").val();
        options.point = $("#point").val();
        options.page_number = 0;
        initPager();
    }
    
    function changeSort() {
        options.sort_by = $("#sort_by").val();
        updateData();
    }
    
    function updateOptions() {
        options.date_from = $("#filter_date_from").val();
        options.date_to = $("#filter_date_to").val();
        options.feed_type = $("#feed_type").val();
        options.point = $("#point").val();
        options.page_number = 0;
        options.sort_by = $("#sort_by").val();
        options.status = $("input[name=status_filter]:checked").val();
    }
    
    function editFeedDialog (id, message) {
        $("#edit_feed_id").val(id);
        $("#edit_feed_message").val(message);
        $("#edit_feed_dialog").dialog("open");
    }
    
    function submitEditDialog(){
        $.get("/user/reviews", $("#edit_feed_form").serialize());
        $("#edit_feed_dialog").dialog("close");
    }
</script>
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
            <label for="feed_type" style="width: 90px">Contains :</label>
            <select onchange="changeFilter()" id="feed_type">
                <option value="">Not selected</option>
                <option value="contains_text">Text</option>
                <option value="contains_photo">Photo</option>
                <option value="contains_text_and_photo">Text and Photo</option>
            </select>
            
            <label for="point">Point :</label>
            <select onchange="changeFilter()" id="point">
                <option value="">All</option>
                <c:forEach varStatus="status" var="point" items="${points}">
                    <option value="${point.id}">${point.address.addressLine1}</option>
                </c:forEach>
            </select>
            
            <label for="filter_date_from">Date :</label>
            <input type="text" id="filter_date_from" onchange="changeFilter()" />
            -
            <input type="text" id="filter_date_to" onchange="changeFilter()" />
        </div>
        <div id="feeds_table" class="items-inner"></div>
        <div id="pager" class="pager"></div>
        <div class="clear"></div>
    </div>
</div>
<div id="edit_feed_dialog" class="hidden">
    <form action="" onsubmit="submitEditDialog();return false;" id="edit_feed_form">
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