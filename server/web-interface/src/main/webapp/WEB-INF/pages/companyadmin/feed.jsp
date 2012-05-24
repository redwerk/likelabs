<%@include  file="/WEB-INF/pages/admin_header.jsp"%>
<form id="formFilter" action="/" method="post" onsubmit="return false;">
    <div id="content">
    
    <h1>My Feed</h1> 
    <div class="order-holder">
        <select style="width: 100px;" id="sortingCriteria" name="sortingCriteria">
        <option value="DATE">Date</option>
        <option value="POINT">Point</option>
        <option value="REVIEW_TYPE">Review type</option>
        <option value="REVIEW_STATUS">Review status</option>
    </select>
        </div>
    <div class="items-holder" >
        
       

        <div class="filter-holder">
            <label for="contentType">Contains:</label>
            <select style="width: 120px;" id="contentType" name="contentType">
                    <option value="CONTAINS_TEXT_AND_PHOTO">Any content</option>
                    <option value="CONTAINS_PHOTO">Images</option>
                    <option value="CONTAINS_TEXT">Text</option>
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
                <input type="radio"  value="" name="status" id="statusAll" checked="checked" />
                <input type="radio" value="PENDING" name="status" id="statusPending" />
                <input type="radio" value="APPROVED" name="status" id="statusApproved" />
                <input type="radio" value="ARCHIVED" name="status" id="statusArchived" />
                <input type="radio" value="FLAGGED" name="status" id="statusFlagged" />
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
            config: {
                items_per_page : 10,
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

        var ajaxObj = false;
        function pageChanged(newPage, jq) {
            loadData(newPage);
            return false;
        }
        
        function loadData(newPage) {
            
            newPage = newPage || 0;
            newPage++;
            $('#content').mask('Please wait...');
            
            ajaxObj && ajaxObj.abort();
            
            
            var formValues = $('#formFilter').serializeArray();
            var dataFilter = {};
            for(var i=0,len = formValues.length; i<len; ++i){
                var obj = formValues[i];
                dataFilter[obj.name] = obj.value;
            }
            dataFilter.page = newPage;
            dataFilter.sortingOrder = 'ASCENDING';
            ajaxObj= $.ajax({
                dataType: 'json',
                url: '/companyadmin/feed/data/' + newPage,
                data: dataFilter,
                success: function(data){
                    initPager(data.count);
                    renderData(data);
                },
                error: function(){
                   $('#content').unmask();
                   alert("Server temporarily unavailable");
                    
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
                pagerOptions.resetPager = false;
                $('.pager').pagination(itemsCount, pagerOptions.config);
            }
        }
        function renderData(data){
            if(!data.count){
                $('#feedContainer').html('No Data Found.');
                return;
            } else{
                var view = template.render({reviews: data.reviews});
                $('#feedContainer').html(view);
            }
           
            $('#content').unmask();
            $('#feedContainer select').change(function(){
                
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper');
                var id = $wrapper.mask().attr('review-id');
                
                $.ajax({
                    type: 'POST',
                    url: '/companyadmin/review/'+id +'/status/',
                    data: {status: $me.val()},
                    complete: function(jqXHR, textStatus){
                      $wrapper.unmask();
                    }
                });
            });
            
        }
        
        $(document).ready(function(){
            loadData(0);
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
<%@include  file="/WEB-INF/pages/admin_footer.jsp"%>