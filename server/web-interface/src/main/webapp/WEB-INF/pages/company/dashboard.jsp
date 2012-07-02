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

    google.setOnLoadCallback(drawVisualization);

    function drawVisualization(data) {
        $('#chart').html("");
        var wrap = new google.visualization.ChartWrapper();
        wrap.setChartType('LineChart');
        wrap.setDataTable(data);
        wrap.setContainerId('chart');
        wrap.setOptions({
            chartArea: {left:35,top:35,width:"90%",height:"75%",backgroundColor: '#efefef'},
            titlePosition: 'out', legend : 'top' ,
            hAxis : { format: 'MMM yy', 'gridlines': {color: '#ccc', count: 5}},
            vAxis:{ 'gridlines': {color: '#ccc', count: 5}, 'minorGridlines': {color: '#333', count: 0}}
        });
        wrap.draw();
    }

    function changeIterval(value) {
        $.get("/company/${companyId}/chartData", {"interval" : value}, function(response){
            if (response.error) {
                errorDialog("Error update chart", response.error);
                return;
            }
            var data = new google.visualization.DataTable();
            data.addColumn('date', 'Start Date');
            data.addColumn('number', 'Photos Taken');
            data.addColumn('number', 'Facebook');
            data.addColumn('number', 'VKontakte');
            data.addColumn('number', 'Email');
            data.addRows(response.data.length);
            for (var i=0; i < response.data.length ; i++) {
                data.setCell(i, 0, response.data[i].date);
                data.setCell(i, 1, response.data[i].photosTaken);
                data.setCell(i, 2, response.data[i].facebook);
                data.setCell(i, 3, response.data[i].vkontakte);
                data.setCell(i, 4, response.data[i].emails);
            }
            data.addRows(30);
            for (var i=0; i < 30 ; i++) {
                data.setCell(i, 0, new Date(i*10000000000));
                data.setCell(i, 1, i+100);
                data.setCell(i, 2, i+120);
                data.setCell(i, 3, i+80);
                data.setCell(i, 4, i+150);
            }
            drawVisualization(data);
        });
    }

    $(document).ready(function(){
        changeIterval($('#interval').val());
    });

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