<%@include file="./header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript" >
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
        pager_options.config.current_page = page > 0? page : 0;
        $("#pager").pagination(pager_options.items_count, pager_options.config)
    }

    var options = {
        page_number: 0
    };
    
    function pageSelectCallback(page_index, jq) {
        options.page_number = page_index;
        updateData();
        return false;
    }
    
    function updateData() {
        $.get("/company/ajax/feed/", options);
        fillTable(test_data);
    }
    
    //test data
    var i = 1;
    var test_data = [
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        },
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        },
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        },
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        },
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        },
        {
            id : i++,
            logo: "/static/images/firefox-logo.png",
            name: "Firefox",
            points: "13",
            comments: "456"
        }
    ];
    
    function fillTable(data) {
        var template = new EJS({url: "/static/templates/company_list_table.ejs"}).render({companies: data});
        $("#point_list_table").html(template);
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td class="title">Companies List</td>
                                    </tr>
                                    <tr>
                                        <td class="body">
                                            <div id="point_list_table"></div>
                                            <div id="pager" class="pagination" style="position: relative; float: right; padding-top: 20px"></div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="./footer.jsp" %>