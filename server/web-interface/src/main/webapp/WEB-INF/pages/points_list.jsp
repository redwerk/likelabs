<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<script type="text/javascript" >
    var companyid = <c:out value="${company.id}"/>;
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
    
    function updateData() {
        $.get("/public/" + companyid + "/points/data", options, function(response){
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
        var template = new EJS({url: "/static/templates/point_table.ejs"}).render({points: data});
        $("#point_list_table").html(template);
    }
</script>
                                <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
                                    <tr>
                                        <td class="title">List of Points for ${company.name}</td>
                                    </tr>
                                    <tr>
                                        <td class="body">
                                            <div id="point_list_table"></div>
                                            <div id="pager" class="pagination" style="position: relative; float: right; padding-top: 20px"></div>
                                        </td>
                                    </tr>
                                </table>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>