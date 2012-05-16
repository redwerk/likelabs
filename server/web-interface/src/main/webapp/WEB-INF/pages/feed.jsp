<%@include file="./header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript">
    
    var pager_options = {
        items_count: 122,
        config: {
            items_per_page : 5,
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
        updateData();
        return false;
    }
    
    function updateData() {
        $.get("/company/ajax/feed/", options);
        fillTable();
    }
    
    var test_data = [
        {
            message: "test test test1",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test2",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test3",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test4",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test5",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test6",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.random() * 1000,
            point: "Bla bla bla"
        },
        {
            message: "test test test5",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.ceil(Math.random() * 10000000000),
            point: "Bla bla bla"
        },
        {
            message: "test test test6",
            photo: "/static/images/feed_photo.png",
            name: "Vasya " + Math.ceil(Math.random() * 1000),
            date: (new Date()).getTime() + Math.random() * 1000,
            point: "Bla bla bla"
        }
    ];
    
    function fillTable(data) {
        var template = new EJS({url: "/static/templates/feed_table.ejs"}).render({feeds: test_data});
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
function show_hint(target) {
    $(target).prev().removeClass('hidden');
    console.warn("show");
}
function hide_hint(target) {
    console.warn("hide");
    $(target).addClass('hidden');
}
</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%; width: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td style="height: 85px;">
                                            <div class="title" style="position: relative; float: left;">Feeds for {CompanyName}</div>
                                            <div style="position: relative; float: right; padding-right: 5px;">
                                                <select onchange="changeSort()" id="sort_by" style="width: 150px">
                                                    <option value="">Sort By</option>
                                                    <option value="date">Date</option>
                                                    <option value="point">Point</option>
                                                    <option value="feed_type">Feed type</option>
                                                </select>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="filter">
                                                <div class="title">
                                                    <div style="display: inline-block">Feed type :</div>
                                                    <div style="display: inline-block">
                                                        <select onchange="changeFilter()" id="feed_type">
                                                            <option value="">All</option>
                                                            <option value="text">Text</option>
                                                            <option value="photo">Photo</option>
                                                            <option value="text_photo">Text with Photo</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="title">
                                                    <div style="display: inline-block">Point :</div>
                                                    <div style="display: inline-block">
                                                        <select onchange="changeFilter()" id="point">
                                                            <option value="">All</option>
                                                            <option value="1">Point 1</option>
                                                            <option value="2">Point 2</option>
                                                            <option value="3">Point 3</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                
                                                <div class="title">
                                                    <div style="display: inline-block">Date :</div>
                                                    <div style="display: inline-block">
                                                        <input type="text" id="filter_date_from" onchange="changeFilter()" />
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
                                            <div id="pager" class="pagination" style="position: relative; float: right;"></div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="./footer.jsp" %>