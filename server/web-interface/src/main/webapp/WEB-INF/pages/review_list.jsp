<%@include file="./header.jsp" %>
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
        page_number: 0
    };
    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        if (force_update)
            updateData();
        return false;
    }
    var force_update = true;
    function updateData() {
        $.get("/company/" + companyId + "/reviews/data", options, function(response){
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
        var template = new EJS({url: "/static/templates/feed_table.ejs"}).render({feeds: data});
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
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="width: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td style="height: 85px;">
                                            <div class="title" style="position: relative; float: left;">Feed for ${company.name}</div>
                                            <div style="position: relative; float: right; padding-right: 5px;">
                                                <select onchange="changeSort()" id="sort_by" style="width: 150px">
                                                    <option value="">Sort By</option>
                                                    <option value="date">Date</option>
                                                    <option value="point">Point</option>
                                                    <option value="review_type">Content type</option>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="height:100px">
                                            <div class="filter">
                                                <div class="title">
                                                    <div style="display: inline-block">Contains :</div>
                                                    <div style="display: inline-block">
                                                        <select onchange="changeFilter()" id="feed_type">
                                                            <option value="">Not selected</option>
                                                            <option value="contains_text">Text</option>
                                                            <option value="contains_photo">Photo</option>
                                                            <option value="contains_text_and_photo">Text and Photo</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="title">
                                                    <div style="display: inline-block">Point :</div>
                                                    <div style="display: inline-block">
                                                        <select onchange="changeFilter()" id="point">
                                                            <option value="">All</option>
                                                            <c:forEach varStatus="status" var="point" items="${points}">
                                                                <option value="${point.id}">${point.address.addressLine1}</option>
                                                            </c:forEach> 
                                                        </select>
                                                    </div>
                                                </div>
                                                
                                                <div class="title">
                                                    <div style="display: inline-block">Date :</div>
                                                    <div style="display: inline-block">
                                                        <input type="text" id="filter_date_from" onchange="changeFilter()" />
                                                        -
                                                        <input type="text" id="filter_date_to" onchange="changeFilter()" />
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <!--
                                    <tr>
                                        <td>
                                            <div class="filter">
                                                <div class="title" style="padding-right: 15px;">View :</div>
                                                <div class="button_active">All</div>

                                                <div class="button">Pending</div>
                                                <div class="button">Approved</div>
                                                <div class="button">Flagged</div>
                                                <div class="button">Photos</div>
                                                <div class="button">Comments</div>
                                            </div>
                                        </td>
                                    </tr>
                                    -->
                                    <tr>
                                        <td class="body" style="width: 100%;">
                                            <div id="feeds_table"></div>
                                            <div style="clear: both"></div>
                                            <div id="pager" class="pagination" style="position: relative; float: right;">asd</div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="./footer.jsp" %>