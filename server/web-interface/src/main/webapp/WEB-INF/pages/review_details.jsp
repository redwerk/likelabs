<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<div id="content">
    <div>
        <h1>Review details</h1>
        <div style="position: relative; float: left; width: 300px; padding-top: 10px;">
            <table style="width: 100%" class="review_table">
                <tr>
                    <td>Author: </td>
                    <td>Vasya Pupkin</td>
                </tr>
                <tr>
                    <td>Taken: </td>
                    <td>01/05/2012 19:55</td>
                </tr>
                <tr>
                    <td style="width: 80px;">Company: </td>
                    <td>Coca Cola</td>
                </tr>
                <tr>
                    <td>Point: </td>
                    <td>McDonalds, Metallurgov 1</td>
                </tr>
            </table>
            <div style="padding-top: 40px; padding-left: 5px;">
                <a href="/public/1/reviews" style="font-style: italic">Show all reviews for Coca Cola</a>
            </div>
        </div>
        <div class="item-wrapper photo" style="float: right; width: 320px; height: 320px; margin-right: 20px">
            <div class="item-data"><div class="photo" style="text-align: center"><img src="/static/images/no-img-available.jpg" alt=""/></div></div>
        </div>
        <div class="clear"></div>
    </div>
    
    
    <div style="position: relative">
        <h2>Review message</h2>
        <div style="text-align: justify; text-indent: 50px;">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pharetra, metus nec ultricies porta, nulla nisl placerat leo, ut molestie metus purus sit amet velit. Nulla sollicitudin posuere viverra. Nulla lacinia vehicula nibh, id pretium diam malesuada nec. Sed varius leo vitae erat bibendum aliquam. Etiam enim diam, bibendum vitae ornare non, tristique et justo. Nam magna magna, malesuada lobortis euismod sit amet, egestas ac elit. Fusce eget sem mollis leo egestas luctus. Vestibulum imperdiet porta tincidunt. Sed lorem nisi, interdum quis fermentum ut, porttitor vel magna. Morbi sagittis elit eget elit venenatis eleifend. Sed rhoncus, dui sit amet tincidunt vestibulum, tortor libero gravida sem, vitae iaculis urna urna et dui. Aenean consectetur enim nulla, id ullamcorper velit. 
        </div>
    </div>
    <div class="clear"></div>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>