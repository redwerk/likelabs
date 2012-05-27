<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<form id="formFilter" action="/" method="post" onsubmit="return false;">
    <div id="content">
    
    <h1>My Feed</h1> 
    <div class="order-holder">
        <select style="width: 100px;" id="sortingCriteria" name="sortingCriteria">
        <option value="DATE">Date</option>
        <option value="COMPANY_AND_POINT">Company / Point</option>
        <option value="REVIEW_TYPE">Review type</option>
        <option value="REVIEW_STATUS">Review status</option>
    </select>
        </div>
    <div class="items-holder" >       

        <div class="filter-holder">
            <div>
                <label for="contentType">Contains:</label>
                <select style="width: 120px;" id="contentType" name="contentType">
                        <option value="CONTAINS_TEXT_AND_PHOTO">Any content</option>
                        <option value="CONTAINS_PHOTO">Images</option>
                        <option value="CONTAINS_TEXT">Text</option>
                    </select>
                 <label>Date:</label> <input name="startDate" type="text" id="startDate" /> 
                <label> &mdash;</label> <input name="endDate" type="text" id="endDate" />
            </div>
            <div>
                <label for="location">Point:</label>
                <select style="width: 200px;" name="loacation" id="location">
                    <option value="">All</option>
                    <option value="1">12345 Sample1 Street</option>
                    <option value="2">12345 Sample2 Street</option>
                    <option value="3">12345 Sample3 Street</option>
                </select>
            </div>
           
            
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
    
    var companiesPoints = [
<c:forEach items="${compniesPointsMap}" var="companyPointsPair" >        { name : "${companyPointsPair.key}",
          points : [ 
<c:forEach items="${companyPointsPair.value}" var="point" >                { id : ${point.id},
                  name : "${point.address.addressLine1}" },</c:forEach>
          ] },</c:forEach>
    ];
    
    
    (function(){
        var template = new EJS({url: '/static/templates/feed_items.ejs'});
        var pagerOptions = {
            config: {
                items_per_page : ${items_per_page},
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
                error: function(jqXHR, textStatus, errorThrown){
                  
                   if(textStatus !== 'abort'){
                       alert("Server temporarily unavailable");
                   }
                   $('#content').unmask();
                    
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
                $('#content').unmask();
                return;
            } else{
                var view = template.render({reviews: data.reviews});
                $('#feedContainer').html(view);
            }
           
            $('#content').unmask();
            
            //change status
            $('#feedContainer select').change(function(){                
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper').mask();
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('review-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/review/'+id +'/data/status',
                    data: {status: $me.val().toLowerCase()},
                    success: function(data){
                       $me.replaceWith('<span class="btn disabled">Published</span>');
                       $wrapper.unmask();
                    },
                    error:function(jqXHR, textStatus){
                        $wrapper.unmask();
                    }
                });
            });
            
            //change favorite
            $('#feedContainer .promo').click(function(){
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper').mask();
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('review-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/review/'+id +'/data/promo',
                    data: {promo: $me.val()},
                    success: function(data){
                       $me.replaceWith('<span class="btn disabled">Published</span>');
                       $wrapper.unmask();
                    },
                    error:function(jqXHR, textStatus){
                        $wrapper.unmask();
                    }
                });
            });
            $('#feedContainer button.publish').click(function(){
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper').mask();
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('review-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/review/'+id +'/data/publish',                    
                    data: {publish: true},
                    success: function(data){
                       $me.replaceWith('<span class="btn disabled">Published</span>');
                       $wrapper.unmask();
                    },
                    error:function(jqXHR, textStatus){
                        $wrapper.unmask();
                    }
                });
            });
        }
        
        $(document).ready(function(){
            loadData(0);
            $('#startDate').datepicker({dateFormat: "dd/mm/yy"});
            $('#endDate').datepicker({dateFormat: "dd/mm/yy"});
            $('.status-filter input[name=status]').change(function(){
                pagerOptions.resetPager = true;
                loadData(0);
                var statusId = $(this).attr('id');
                $('.status-filter label').removeClass('active');
                $('.status-filter label[for='+ statusId+']').addClass('active');
            });
            $('#startDate').add('#endDate').add('#sortingCriteria').add('.filter-holder select').change(function(){
                loadData(0);
            });
        });
    })();
    
</script>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>