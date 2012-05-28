<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
        <h1>Dashboard <c:out value="${companyName}"/></h1>
<table style="height: 100%;" summary="" class="content_block">
    <tr>
        <td><img src="/static/images/graph.png " alt="graph" title="${companyName} graph"width="500"/></td>
    </tr>
    <tr>
        <td><img src="/static/images/tableall.png " alt="graph" title="At a Glance ${companyName}"width="500"/></td>
    </tr>
</table>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>