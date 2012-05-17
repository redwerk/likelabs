<%@include file="./admin_header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<div id="content">
    <h1>My Companies</h1>
    <ul id="company-list" class="company-list">
        <li><a href="#"> <img width="100" height="50" src="/static/images/logo_100x50.png" /> Company 1 </a></li>
        <li><a href="#"> <img width="100" height="50" src="/static/images/logo_100x50.png"/> Company 2 </a></li>
        <li><a href="#"> <img width="100" height="50" src="/static/images/logo_100x50.png"/> Company 3 </a></li>
        <li><a href="#"> <img width="100" height="50" src="/static/images/logo_100x50.png"/> Company 4 </a></li>       
    </ul>
    <div class="pager"></div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
    (function(){
        var template = new EJS({url: "/static/templates/companies.ejs"});
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
                callback: pageChanged
            }
        }
         var options = {
            page_number: 0
        };
        function pageChanged(page_index, jq) {
            options.page_number = page_index;
            updateData(page_index);
            return false;
        }
        function updateData(page_index) {
            //$.get("/company/ajax/feed/", options);
            var data = [];
            var start = page_index * 7;
            for(var i = start; i< start+7; ++i){
                data[data.length] = {
                    id : i,
                    logo: "/static/images/logo_100x50.png",
                    name: "Company " + i,
                    logoHeight: "50",
                    logoWidth: "100"
                }
            }
            renderData(data);
        }
        function initPager(page){
            pager_options.config.current_page = page > 0? page : 0;
            $(".pager").pagination(pager_options.items_count, pager_options.config)
        }
       
        
        function renderData(data){
            var view = template.render({companies: data});
            $("#company-list").html(view);
        }
        $(document).ready(function(){
            initPager(0);
        });
    })();
    
</script>
<%@include file="./admin_footer.jsp" %>