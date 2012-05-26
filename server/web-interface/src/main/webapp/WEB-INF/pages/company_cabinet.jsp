<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <sec:authorize access="isAuthenticated()">
        <tr>
            <td class="title">Cabinet for <c:out value="${companyName}"/></td>
        </tr>
    </sec:authorize>
    <sec:authorize access="not isAuthenticated()">
        <tr>
            <td class="title">About company <c:out value="${companyName}"/></td>
        </tr>
    </sec:authorize>
    <tr> 
        <sec:authorize access="isAuthenticated()">
            <td>
                <button class="btn btn_success save" style="width: 240px;" type="buttont" onclick="document.location.href='/company/${companyId}/profile'">Edit profile</button>
                <br>
                <br>
                <button class="btn btn-info save" style="width: 240px;" type="buttont" onclick="document.location.href='/company/${companyId}/reviews'">Moderate reviews</button>
            </td>
        </sec:authorize>
        <sec:authorize access="not isAuthenticated()">
            <td>
                <button class="btn btn-info save" style="width: 240px;" type="buttont" onclick="document.location.href='/public/${companyId}/reviews'">See reviews</button>
            </td>
        </sec:authorize>
        <td align="right"><div class="logo"><img src="/public/${companyId}/logo " alt="logo" title="Logo Company"width="217"/></div></td>
    </tr>

    <tr>
        <td colspan="2" class="body">
            <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. D onec sit amet nisi dui. Nullam tristique risus quis arcu iaculis consequat. Praesent aliquam consequat risus, sit amet facilisis quam porta eu. Integer sit amet velit vel lorem pretium feugiat. Ut condimentum est porttitor lectus dignissim facilisis. Cras augue libero, fermentum eu ultrices tristique, volutpat non nisl. Vestibulum rhoncus bibendum tempor. Donec sed velit vitae risus faucibus vulputate id id justo. Pellentesque a nisi turpis, nec egestas tortor. Integer ac augue neque.
            </p>
            <p>
                Aenean augue arcu, sagittis ut mattis sed, sodales nec purus. Curabitur aliquet, metus quis porttitor congue, ipsum dui mattis justo, et feugiat dui magna nec odio. Maecenas sit amet enim id elit tincidunt accumsan in quis dolor. Mauris non diam sed felis pretium viverra a quis mi. Nunc sit amet malesuada lacus. Cras ut sem urna, ac tempor enim. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus aliquet gravida augue eu auctor. Donec vel ligula justo, eu sodales neque. Nullam nec ipsum rutrum leo scelerisque placerat. Curabitur ut lectus justo.
            </p>
        </td>
    </tr>
</table>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>
