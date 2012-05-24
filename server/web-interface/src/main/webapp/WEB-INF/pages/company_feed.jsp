<%@include file="./admin_header.jsp" %>
<form id="formFilter" action="/" method="post" onsubmit="return false;">
    <div id="content">
    
    <h1>My Feed</h1> 
    <div class="order-holder">
        <select style="width: 100px;" id="sortingCriteria" name="sortingCriteria">
        <option>Date</option>
        <option>Location</option>
        <option>Message type</option>
    </select>
        </div>
    <div class="items-holder" >
        
       

        <div class="filter-holder">
            <label for="contentType">Contains:</label>
            <select style="width: 120px;" id="contentType" name="contentType">
                    <option value="any">Any content</option>
                    <option value="images">Images</option>
                    <option value="text">Text</option>
                </select>
            <label for="location">Point:</label>
            <select style="width: 200px;" name="loacation" id="location">
                <option value="">All</option>
                <option value="1">12345 Sample1 Street</option>
                <option value="2">12345 Sample2 Street</option>
                <option value="3">12345 Sample3 Street</option>
            </select>
            
            <label>Date:</label> <input name="startDate" type="text" id="startDate" /> 
            <label> &mdash;</label> <input name="endDate" id="endDate" from_datetype="text" />
            
            <div class="status-filter">
                <input type="radio"  value="all" name="status" id="statusAll" checked="checked" />
                <input type="radio" value="pending" name="status" id="statusPending" />
                <input type="radio" value="approved" name="status" id="statusApproved" />
                <input type="radio" value="archived" name="status" id="statusArchived" />
                <input type="radio" value="flagged" name="status" id="statusFlagged" />
                <input type="radio" value="published" name="status" id="statusPublished" />
                <input type="radio" value="promo" name="status" id="statusPromo">


                <label for="statusAll" class="active">All</label>
                <label for="statusPending" >Pending</label>
                <label for="statusApproved" >Approved</label>
                <label for="statusArchived" >Archived</label>
                <label for="statusFlagged" >Flagged</label>
                <label for="statusPublished" >Published</label>
                <label for="statusPromo">Promo</label>
            </div>
        </div>
        <div id="feedContainer" class="items-inner">
            
        </div>
        <div class="pager"></div>
        <div class="clear"></div>

        
    </div>
   
    </div> 
</form>
<script type="text/javascript">
    (function(){
        var template = new EJS({url: '/static/templates/feed_items.ejs'});
        var pagerOptions = {
            itemsCount: 0,
            config: {
                items_per_page : 5,
                next_text : '&gt;',
                num_display_entries : 6,
                num_edge_entries : 2,
                current_page: 0,
                prev_text : "&lt;",
                ellipse_text: "...",
                callback: pageChanged
            },
            resetPager: true
        };

        var currentStatus = 'all';
        var ajaxObj = false;
        function pageChanged(newPage, jq) {
            loadData(newPage);
            return false;
        }
        
        function loadData(newPage) {
            
            newPage = newPage || 0;
            $('#content').mask('Please wait...');
            
            ajaxObj && ajaxObj.abort();
            
            
            var formValues = $('#formFilter').serializeArray();
            var dataFilter = {};
            for(var i=0,len = formValues.length; i<len; ++i){
                var obj = formValues[i];
                dataFilter[obj.name] = obj.value;
            }
            dataFilter.page = newPage;
            
            ajaxObj= $.ajax({
                dataType: 'json',
                url: '/company/1/feed/data',
                data: dataFilter,
                success: function(data){
                    initPager(data.count);
                    renderData(data);
                },
                error: function(){
                    $('#content').unmask();
                   
                    var data = [];
                    var start = newPage * 6;
                    for(var i = start; i< start+6; ++i){
                        data[data.length] = {
                            id : i,
                            logo: '/static/images/logo_100x50.png',
                            name: 'Company ' + i,
                            logoHeight: '50',
                            logoWidth: '100'
                        }
                    }
                     setTimeout(function(){
                        initPager(data.length * 5);
                    renderData({
                        count: data.length * 5,
                        data: data
                    });
                    }, 1000);
                    
                }
            });
           
        }
        function initPager(itemsCount){
            if(!itemsCount){
                $('.pager').hide();
                return;
            }
            $('.pager').show();
            if(pagerOptions.resetPager){
                pagerOptions.itemsCount = itemsCount;
                pagerOptions.resetPager = false;
                $('.pager').pagination(itemsCount, pagerOptions.config);
            }
        }
        function renderData(data){
            if(!data.count){
                $('#feedContainer').html('No Data Found.');
                return;
            } else{
                var view = template.render({companies: data.data});
                $('#feedContainer').html(view);
            }
           
            $('#content').unmask();
            $('#feedContainer select').selectmenu();
        }
        
        $(document).ready(function(){
            loadData(0);
            $('select').selectmenu();
            $('#startDate').datepicker();
            $('#endDate').datepicker();
            $('.status-filter input[name=status]').change(function(){
                pagerOptions.resetPager = true;
                loadData(0);
                var statusId = $(this).attr('id');
                $('.status-filter label').removeClass('active');
                $('.status-filter label[for='+ statusId+']').addClass('active');
            });
        });
    })();
    
</script>
<%@include file="./admin_footer.jsp" %>