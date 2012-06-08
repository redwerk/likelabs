<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
    <h1>My Companies</h1>
    <div id="company-list">
        
    </div>
    <div class="pager"></div>
    <div class="clear"></div>
</div>
<script type="text/javascript">
    (function(){
        var template = new EJS({url: "/static/templates/companies.ejs"});
        var pagerOptions = {
            items_per_page : ${items_per_page},
            next_text : "&gt;",
            num_display_entries : 6,
            num_edge_entries : 2,
            current_page: 0,
            prev_text : "&lt;",
            ellipse_text: "...",
            callback: pageChanged,
            resetPager: true
        };     
        
        var ajaxObj = false;
        function pageChanged(newPage, jq) {
            loadData(newPage);
            return false;
        }
        
        function loadData(newPage) {
            
            newPage = newPage || 0;
            $('#content').mask('Please wait...');
            
            ajaxObj && ajaxObj.abort();
            
            ajaxObj= $.ajax({
                dataType: 'json',
                url: '/companyadmin/${userId}/companies/data/' + newPage,
                success: function(data){
                    initPager(data.count);
                    renderData(data);
                },
                error: function(){
                    $('#content').unmask();
                    //alert("Server temporarily unavailable");
                }
            });
           
        }
        function initPager(itemsCount){
            if(!pagerOptions.resetPager){
                return;
            }
            pagerOptions.resetPager = false;
            if(!itemsCount){
                $('.pager').hide();
                return;
            }
            $('.pager').show();
            $('.pager').pagination(itemsCount, pagerOptions);
        }
        function renderData(data){
            if(!data.count || !data.companies.length){
                $('#company-list').html('No Data Found.');
                $('#content').unmask();
                return;
            } else{
                var view = template.render({companies: data.companies});
                $('#company-list').html(view);
            }
           
            $('#content').unmask();
        }
        
        $(document).ready(function(){
            loadData(0);
        });
    })();
    
</script>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>
