<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript" src="/static/scripts/jquery.pagination.js"></script>
<div id="content">
    <div>
        <h1>Review details</h1>
        <c:choose>
            <c:when test="${isAllowed eq true}">
                <div style="position: relative; float: left; width: 300px; padding-top: 10px;">
                        <table style="width: 100%" class="review_table">
                            <tr>
                                <td>Author: </td>
                                <td>${review.author.name}</td>
                            </tr>
                            <tr>
                                <td>Taken: </td>
                                <td><fmt:formatDate pattern="dd/MM/yyyy H:mm" value="${review.createdDT}"/></td>
                            </tr>
                            <tr>
                                <td style="width: 80px;">Company: </td>
                                <td>${review.company.name}</td>
                            </tr>
                            <tr>
                                <td>Point: </td>
                                <td>${review.point.address.addressLine1}</td>
                            </tr>
                        </table>
                        <div style="padding-top: 40px; padding-left: 5px;">
                            <a href="/public/${review.company.id}/reviews" style="font-style: italic">Show all reviews for ${review.company.name}</a>
                        </div>
                    </div>
                    <div class="item-wrapper photo" style="float: right; width: 320px; height: 320px; margin-right: 20px">
                        <div class="item-data">
                            <div class="photo" style="text-align: center">
                                <c:choose>
                                    <c:when test="${not empty review.photo.id}">
                                        <img src="/public/photo/${review.photo.id}" alt=""/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/static/images/no-img-available.jpg" alt=""/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
                <div style="position: relative">
                    <h2>Review message</h2>
                    <div style="text-align: justify; text-indent: 50px;">
                        ${review.message}
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: justify; text-indent: 50px;">
                    This review was not approved yet. Please try later
                </div>
            </c:otherwise>
    </c:choose>
    <div class="clear"></div>
</div>

<%@include file="/WEB-INF/pages/commons/footer.jsp" %>