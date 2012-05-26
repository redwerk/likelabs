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

        $(".button_filter").click(function(){
            $(".button_filter").removeClass("active");
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
        feedType: null,
        point: null,
        dateTo: null,
        dateFrom: null,
        sortBy: null,
        page: 0,
        status: null
    };

    function pageSelectCallback(page_index, jq) {
        options.page = page_index;
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
            initPager(options.page);
            force_update = true;
            fillTable(response.data);
        });
    }

    function fillTable(data) {
        var template = new EJS({url: "/static/templates/company_review_table.ejs"}).render({feeds: data});
        $("#feeds_table").html(template);
    }

    function changeFilter() {
        options.dateFrom = $("#filter_date_from").val();
        options.dateTo = $("#filter_date_to").val();
        options.feedType = $("#feed_type").val();
        options.point = $("#point").val();
        options.page = 0;
        initPager();
    }

    function changeSort() {
        options.sortBy = $("#sort_by").val();
        updateData();
    }

    function updateOptions() {
        options.dateFrom = $("#filter_date_from").val();
        options.dateTo = $("#filter_date_to").val();
        options.feedType = $("#feed_type").val();
        options.point = $("#point").val();
        options.page = 0;
        options.sortBy = $("#sort_by").val();
        options.status = $("input[name=status_filter]:checked").val();
    }

    function updateFeed(reviewId, name, value) {
        $.post("/company/" + companyId + "/reviews/" + reviewId + "/data/" + name, name+'='+value, function(response){
            if (response.error) {
                errorDialog("Error update review", response.error)
                return;
            }
            updateData();
        });
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="width: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td style="height: 85px;">
                                            <div class="title" style="position: relative; float: left;">
                                                <a href="/company/${companyId}">
                                                    Reviews for ${company.name}
                                                </a></div>
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
                                        <td>
                                            <div class="filter admin">
                                                <div>
                                                    <div class="sub_title">
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
                                                    <div class="sub_title">
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
                                                    <div class="sub_title">
                                                        <div style="display: inline-block">Date :</div>
                                                        <div style="display: inline-block">
                                                            <input type="text" id="filter_date_from" onchange="changeFilter()" />
                                                            -
                                                            <input type="text" id="filter_date_to" onchange="changeFilter()" />
                                                        </div>
                                                    </div>
                                                </div>
                                                    <div style="margin-top: 15px; margin-left: 10px;">
                                                        <input id="status_all" type="radio" name="status_filter" value="" checked="checked"/>
                                                        <input id="status_pending" type="radio" name="status_filter" value="pending"/>
                                                        <input id="status_approved" type="radio" name="status_filter" value="approved"/>
                                                        <input id="status_archived" type="radio" name="status_filter" value="archived"/>
                                                        <input id="status_flagged" type="radio" name="status_filter" value="flagged"/>
                                                        <input id="status_published" type="radio" name="status_filter" value="published"/>
                                                        <input id="status_promo" type="radio" name="status_filter" value="promo"/>


                                                        <label class="button_filter" for="status_all">All</label>
                                                        <label class="button_filter" for="status_pending">Pending</label>
                                                        <label class="button_filter" for="status_approved">Approved</label>
                                                        <label class="button_filter" for="status_archived">Archived</label>
                                                        <label class="button_filter" for="status_flagged">Flagged</label>
                                                        <label class="button_filter" for="status_published">Published</label>
                                                        <label class="button_filter" for="status_promo">Favourites</label>
                                                    </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="body" style="width: 100%;">
                                            <div id="feeds_table"></div>
                                            <div style="clear: both"></div>
                                            <div id="pager" class="pagination" style="position: relative; float: right;">asd</div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>