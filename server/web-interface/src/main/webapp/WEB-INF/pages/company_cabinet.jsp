<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
    <sec:authorize access="isAuthenticated()">
        <h1>Cabinet for <c:out value="${companyName}"/></h1>
    </sec:authorize>
    <sec:authorize access="not isAuthenticated()">
        <h1>About company <c:out value="${companyName}"/></h1>
    </sec:authorize>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr> 
        <sec:authorize access="isAuthenticated()">
            <td>
                <a class="btn btn-success save" style="width: 240px;" type="buttont" href="/company/${companyId}/profile">Edit profile</a>
                <br/>
                <br/>
                <a class="btn btn-info save" style="width: 240px;" type="buttont" href="/company/${companyId}/reviews">Moderate reviews</a>
            </td>
        </sec:authorize>
        <sec:authorize access="not isAuthenticated()">
            <td>
                <a class="btn btn-info save" style="width: 240px;" type="buttont" href="/public/${companyId}/reviews">See reviews</a>
            </td>
        </sec:authorize>
        <td align="right"><div class="logo"><img src="/public/${companyId}/logo " alt="logo" title="${companyName} logo"width="217"/></div></td>
    </tr>
</table>
    <div class="text-holder">
        <p>
            <b>Welcome to <c:out value="${companyName}"/></b> - the Software Development Company providing complete programming solutions for companies around the world.
        </p> 
        <p>
            Our approach, the value our expert software engineers bring to the table, and the advantages of outsourcing your IT projects makes <c:out value="${companyName}"/> a triple threat in the offshore custom software development field.
        </p>
        <p>
            Your company can only grow as fast and as large as your software and technology can handle. Aging software programs and IT systems unable to expand with your business are the number one reason companies fall behind the competition. Choose <c:out value="${companyName}"/>  to connect the disconnected, integrate fragmented systems, and give your business more dynamic software and a cohesive IT infrastructure.  <c:out value="${companyName}"/>  is your one stop IT and software development Company.
        </p>
    </div>
    <div class="clear"></div>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>
