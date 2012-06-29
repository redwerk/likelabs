<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript"
        src='https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1"}]}'>
</script>
<script type="text/javascript" >
    var dataTest = [
        ['Mounth', 'Photos Taken', 'Facebook','VKontakte','Email'],
        ['', 225, 58,45,32],
        ['', , 28,145,232],
        ['', 125, 28,45,132],
        ['', 425, 258,451,282],
        ['Dec', 12, 28,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['Feb', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 28,45,32],
        ['', 125, 258,45,32],
        ['Apr', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,92],
        ['', 125, 258,45,32],
        ['Jun', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['Aug', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['', 125, 154,45,32],
        ['', 125, 258,45,32],
        ['', 125, 258,45,32],
        ['Oct', 125, 258,45,32]

    ];


    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Employee Name');
    data.addColumn('date', 'Start Date');
    data.addRows(3);
    data.setCell(0, 0, 'Mike');
    data.setCell(0, 1, new Date(2008, 1, 28));
    data.setCell(1, 0, 'Bob');
    data.setCell(1, 1, new Date(2007, 5, 1));
    data.setCell(2, 0, 'Alice');
    data.setCell(2, 1, new Date(2006, 7, 16));
    
    google.setOnLoadCallback(drawVisualization);


    function drawVisualization() {
        // Define the chart using setters:
        var wrap = new google.visualization.ChartWrapper();
        wrap.setChartType('LineChart');
        //wrap.setDataSourceUrl('http://spreadsheets.google.com/tq?key=pCQbetd-CptGXxxQIG7VFIQ&pub=1');
        wrap.setDataTable(dataTest);
        wrap.setContainerId('chart');
        //wrap.setQuery('SELECT A,D WHERE D > 100 ORDER BY D');
        
        wrap.setOptions({chartArea: {left:35,top:35,width:"80%",height:"75%",backgroundColor: '#efefef'},titlePosition: 'out', title:'Chart', legend :{left:50,top:35} ,hAxis : { format: 'MMM yy', 'gridlines': {color: '#ccc', count: 5}}, 'vAxis':{ 'gridlines': {color: '#ccc', count: 5}, 'minorGridlines': {color: '#333', count: 0}}});
        wrap.draw();
    }

   function changeIterval(value) {
       console.warn(value);
   }
</script>
<style type="text/css">
    .content_table_stat{
        border-collapse: inherit;
        border-spacing: 0;
        border: solid 1px #d2d9df;
        -moz-border-radius: 5px;
        -webkit-border-radius: 5px;
        -khtml-border-radius: 5px;
        border-radius: 5px;
    }

    .content_table_stat tr {
        background-color: #efefef;
    }
    .content_table_stat tr:last-child td:first-child {
        -moz-border-radius-bottomleft:5px;
        -webkit-border-bottom-left-radius:5px;
        -khtml-border-bottom-left-radius: 5px;
        border-bottom-left-radius:5px;
    }

    .content_table_stat td:first-of-type{
        background-color: #bfbfbf;
        font-weight: bold;

    }

    .content_table_stat th:first-of-type{
        background-color: #bfbfbf;
        -moz-border-radius-topmleft:5px;
        -webkit-border-top-left-radius:5px;
        -khtml-border-top-left-radius: 5px;
        border-top-left-radius:5px;
    }

    .content_table_stat th{
        padding: 3px;
        text-align: left;
        height: 30px;
        width: 120px;
        font-size: 12px;
        padding: 5px;
        font-weight: bold;
    }

    .content_table_stat td{
        padding: 3px;
        text-align: left;
        height: 30px;
        width: 120px;
        font-size: 12px;
        padding: 5px;
}
</style>
<div id="content">
    <h1>Dashboard <c:out value="${companyName}"/></h1>

    <div align="right">
        <label for="interval">Interval:</label>
        <select onchange="changeIterval(this.value)" id="interval"  style="width: 100px;">
            <option value="MONTH_12">12 Month</option>
            <option value="MONTH_6">6 Month</option>
            <option value="DAYS_30">30 Days</option>
        </select>
    </div>
    <div id='chart' style='height: 300px; width: 700px;'></div>

    
    <div id="general_statistic" class="content" style="padding: 25px;">
        <label for="general_statistic">At a Glance</label>
        <table cellpadding="0" summary="" class="content_table_stat">
            <thead>
                <tr>
                    <th></th>
                    <c:forEach items="${GENERAL.items}" var="item">
                        <th>${item.description}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">All time</td>
                    <c:forEach items="${GENERAL.items}" var="item">
                        <td>${item.all}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last 24</td>
                    <c:forEach items="${GENERAL.items}" var="item">
                        <td>${item.last24Hours}</td>
                    </c:forEach>
                </tr>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">Last Week</td>
                    <c:forEach items="${GENERAL.items}" var="item">
                        <td>${item.lastWeek}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last Month</td>
                    <c:forEach items="${GENERAL.items}" var="item">
                        <td>${item.lastMonth}</td>
                    </c:forEach>
                </tr>
            </tbody>
        </table>
    </div>
    <div id="fb_statistic"  class="content" style="padding: 25px;">
        <label for="fb_statistic">Facebook</label>
        <table cellpadding="0" summary="" class="content_table_stat">
            <thead>
                <tr>
                    <th></th>
                    <c:forEach items="${FACEBOOK.items}" var="item">
                        <th>${item.description}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">All time</td>
                    <c:forEach items="${FACEBOOK.items}" var="item">
                        <td>${item.all}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last 24</td>
                    <c:forEach items="${FACEBOOK.items}" var="item">
                        <td>${item.last24Hours}</td>
                    </c:forEach>
                </tr>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">Last Week</td>
                    <c:forEach items="${FACEBOOK.items}" var="item">
                        <td>${item.lastWeek}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last Month</td>
                    <c:forEach items="${FACEBOOK.items}" var="item">
                        <td>${item.lastMonth}</td>
                    </c:forEach>
                </tr>
            </tbody>
        </table>
    </div>
    <div id="vk_statistic" class="content" style="padding: 25px;">
        <label for="vk_statistic">VKontakte</label>
        <table cellpadding="0" summary="" class="content_table_stat">
            <thead>
                <tr>
                    <th></th>
                    <c:forEach items="${VKONTAKTE.items}" var="item">
                        <th>${item.description}</th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">All time</td>
                    <c:forEach items="${VKONTAKTE.items}" var="item">
                        <td>${item.all}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last 24</td>
                    <c:forEach items="${VKONTAKTE.items}" var="item">
                        <td>${item.last24Hours}</td>
                    </c:forEach>
                </tr>
                <tr style="background-color: #fdfeff">
                    <td style="background-color: #dfdfdf;">Last Week</td>
                    <c:forEach items="${VKONTAKTE.items}" var="item">
                        <td>${item.lastWeek}</td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Last Month</td>
                    <c:forEach items="${VKONTAKTE.items}" var="item">
                        <td>${item.lastMonth}</td>
                    </c:forEach>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>