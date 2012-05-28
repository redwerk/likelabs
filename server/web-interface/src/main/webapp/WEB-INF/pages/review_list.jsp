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
    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        if (force_update)
            updateData();
        return false;
    }
    var force_update = true;
    function updateData() {
        $.get("/public/" + companyId + "/reviews/data", options, function(response){
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
        var template = new EJS({url: "/static/templates/public_review_table.ejs"}).render({feeds: data});
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
        Feed for ${company.name}
    </h1>
    <div class="order-holder">
        <select onchange="changeSort()" id="sort_by" style="width: 150px">
            <option value="">Sort By</option>
            <option value="date">Date</option>
            <option value="point">Point</option>
            <option value="review_type">Content type</option>
        </select>
    </div>
    <div class="items-holder" >      

        <div class="filter-holder">
            <label for="feed_type">Contains :</label>
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

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>