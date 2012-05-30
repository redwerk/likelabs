<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript">
    var companyId = <c:out value="${company.id}"/>;
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
    
    var test_data = {"data":[{"id":16,"message":"message16","point":"12345 Sample1 Street","name":"Anonymous","date":1333621680000,"photo":"/company/review/16/photo"},{"id":15,"message":"message15","point":"12345 Sample1 Street","name":"Anonymous","date":1330946880000,"photo":"/company/review/15/photo"},{"id":14,"message":"message14","point":"12345 Sample2 Street","name":"Anonymous","date":1315218480000,"photo":"/company/review/14/photo"},{"id":11,"message":"message11","point":"12345 Sample2 Street","name":"Anonymous","date":1315218480000,"photo":"/company/review/11/photo"},{"id":13,"message":"message13","point":"12345 Sample1 Street","name":"Anonymous","date":1315132080000,"photo":"/company/review/13/photo"},{"id":12,"message":"message12","point":"12345 Sample2 Street","name":"Anonymous","date":1315045680000,"photo":"/company/review/12/photo"},{"id":10,"message":"message10","point":"12345 Sample1 Street","name":"Anonymous","date":1314959280000,"photo":"/company/review/10/photo"},{"id":1,"message":null,"point":"12345 Sample1 Street","name":"Anonymous","date":1314872881000,"photo":"/company/review/1/photo"}],"count":16};

    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        if (force_update)
            updateData();
        return false;
    }
    var force_update = true;
    function updateData() {
        $.get("/public/" + companyId + "/reviews", options, function(response){
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
        var template = new EJS({url: "/static/templates/photos_table.ejs"}).render({photos: data});
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
</script>
<div id="content">
    <h1>
        My Photos
    </h1>
    <div class="items-holder" >      

        <div class="filter-holder">
            <div style="margin-top: 0px; margin-left: 10px;" class="status-filter">
                <input id="status_all" type="radio" name="status_filter" value="" checked="checked"/>
                <input id="status_active" type="radio" name="status_filter" value="active"/>
                <input id="status_deleted" type="radio" name="status_filter" value="deleted"/>

                <label class="button_filter" for="status_all">All</label>
                <label class="button_filter" for="status_active">Active</label>
                <label class="button_filter" for="status_deleted">Deleted</label>
            </div>
        </div>
        <div id="feeds_table" class="items-inner"></div>
        <div id="pager" class="pager"></div>
        <div class="clear"></div>
    </div>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>