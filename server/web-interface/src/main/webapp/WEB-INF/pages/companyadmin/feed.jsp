<%@include  file="/WEB-INF/pages/commons/header.jsp"%>

<div id="content">    
<form id="formFilter" action="/" method="post" onsubmit="return false;">
    <style type="text/css">
        .filter-holder label{
            width: 90px;
        }
        .filter-holder select{
            width: 200px;
        }
        .status-filter label {
            width: 91px;
        }
    </style>
    <h1>My Feed</h1> 
    <div class="order-holder">
        <select style="width: 150px;" id="sortingCriteria" name="sortingCriteria">
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
                <select id="contentType" name="contentType">
                     <option value="">Not selected</option>
                     <option value="CONTAINS_TEXT">Text</option>
                     <option value="CONTAINS_PHOTO">Photo</option>                        
                     <option value="CONTAINS_TEXT_AND_PHOTO">Text and Photo</option>
                </select>
                <label for="startDate">Date:</label> <input name="startDate" type="text" id="startDate" /> 
                    - <input name="endDate" type="text" id="endDate" />
            </div>
            <div>
                <label for="location" >Companies:</label>
                <select name="companyes" id="companyes">
                    <option value="">All</option>
                </select>
                <label for="point">Point:</label>
                <select name="point" id="point" disabled="disabled">
                    <option value="">All</option>
                </select>
                
            </div>
           
            
            <div class="status-filter">
                <input type="radio"  value="" name="status" id="statusAll" checked="checked" />
                <input type="radio" value="PENDING" name="status" id="statusPending" />
                <input type="radio" value="APPROVED" name="status" id="statusApproved" />
                <input type="radio" value="ARCHIVED" name="status" id="statusArchived" />
                <input type="radio" value="FLAGGED" name="status" id="statusFlagged" />
                <input type="radio" value="published" name="status" id="statusPublished" />
                <input type="radio" value="promo" name="status" id="statusPromo" />


                <label for="statusAll" class="active">All</label>
                <label for="statusPending" >Pending</label>
                <label for="statusApproved" >Approved</label>
                <label for="statusArchived" >Archived</label>
                <label for="statusFlagged" >Flagged</label>
                <label for="statusPublished" >Published</label>
                <label for="statusPromo">Favourites</label>
            </div>
        </div>
        <div id="feedContainer" class="items-inner">
            
        </div>
        <div class="pager"></div>
        <div class="clear"></div>

        
    </div>
   
</form>  
</div> 
<script type="text/javascript">
    
    var companiesPoints = [
        <c:forEach items="${compniesPointsMap}" var="companyPointsPair"  varStatus="c">
                { name : "${companyPointsPair.key}",
                  points : [
                          <c:forEach items="${companyPointsPair.value}" var="point" varStatus="p">
                              { id : ${point.id},name : "${point.address.addressLine1}" },
                          </c:forEach>
                ]},
        </c:forEach>
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
            pagerOptions.current_page=newPage;
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
                url: '/companyadmin/${adminId}/feed/data/' + newPage,
                data: dataFilter,
                success: function(data){
                    initPager(data.count);
                    renderData(data);
                },
                error: function(jqXHR, textStatus, errorThrown){
                  
                   if(textStatus !== 'abort'){
                        errorDialog("Request error", textStatus);
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
                var $wrapper = $me.parents('.item-wrapper').mask('Please wait...');
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('company-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/reviews/'+id +'/data/status',
                    data: {status: $me.val().toLowerCase()},
                    success: function(data){
                       if(data.error){
                           errorDialog("Request error", data.error);
                       } else {
                            //pagerOptions.resetPager = true;
                            loadData(pagerOptions.current_page);
                       }
                    },
                    error:function(jqXHR, textStatus){                        
                        errorDialog("Request error", textStatus);
                    },
                    complete: function(){
                        $wrapper.unmask();
                    }
                    
                });
            });
            
            //change favorite
            $('#feedContainer .star').click(function(){
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper').mask('Please wait...');
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('company-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/reviews/'+id +'/data/promo',
                    data: {promo: !$me.hasClass('active')},
                    success: function(data){
                        if (data.error) {
                            errorDialog("Error update review", data.error);
                        } else{
                            if($me.hasClass('active')){
                                $me.removeClass('active');
                                $me.attr('title', 'Add to favourites');
                            } else{
                                $me.addClass('active');
                                $me.attr('title', 'Remove from favourites');
                            }
                            //pagerOptions.resetPager = true;
                            loadData(pagerOptions.current_page);
                        }
                    },
                    error:function(jqXHR, textStatus){
                        errorDialog("Request error", textStatus);
                    },
                    complete: function(){
                        $wrapper.unmask();
                    }
                });
            });
            $('#feedContainer button.publish').click(function(){
                var $me = $(this);
                var $wrapper = $me.parents('.item-wrapper').mask('Please wait...');
                var id = $wrapper.attr('review-id');
                var companyId = $wrapper.attr('company-id');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/company/'+companyId +'/reviews/'+id +'/data/publish',                    
                    data: {publish: true},
                    success: function(data){
                        if (data.error) {
                            errorDialog("Error update review", response.error);
                        } else{
                            $me.replaceWith('<span class="btn disabled">Published<'+'/span>');
                            //pagerOptions.resetPager = true;
                            loadData(pagerOptions.current_page);
                        }
                    },
                    error:function(jqXHR, textStatus){
                        errorDialog("Request error", textStatus);
                    },
                    complete: function(){
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
            $('#startDate').add('#endDate').add('#sortingCriteria').add('#contentType').add('#point').change(function(){
                pagerOptions.resetPager = true;
                loadData(0);
            });
            
            var i, len, companiesOption = "";
            for(i = 0, len = companiesPoints.length; i< len; ++i){
                companiesOption += '<option value="'+ i +'">' + companiesPoints[i].name + '<'+'/option>';
            }
            $('#companyes').append(companiesOption).change(function(){
               var value = $(this).val();
               if(value === ''){
                    $('#point').attr('disabled','disabled').html('<option value="" >All</option>').val('');
                } else{
                    value = parseInt(value, 10);
                    var points = companiesPoints[ value].points;
                    var i, len, pointsOption = '', pointsValues = [];
                    for(i = 0, len = points.length; i< len; ++i){
                        pointsOption += '<option value="'+ points[i].id +'" >' + points[i].name + '</option>';
                        pointsValues.push(points[i].id);
                    }
                    pointsOption = '<option value="'+pointsValues.join()+'" >All</option>' + pointsOption;
                    $('#point').html(pointsOption);
                    $('#point').removeAttr('disabled');
                }
                pagerOptions.resetPager = true;
                loadData(0);
            });
            $('#point').attr('disabled','disabled');
        });
    })();
    
</script>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%>