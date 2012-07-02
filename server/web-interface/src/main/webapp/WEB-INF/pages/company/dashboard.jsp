<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<script type="text/javascript"
        src='https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1"}]}'>
</script>
<script type="text/javascript" >
    var sn = {facebook: "FACEBOOK" , vkontakte: "VKONTAKTE"};

    $(document).ready(function(){
        updateChartData($('#interval').val());
        drawTableSn(sn.facebook, $("#fb_statistic"));
        drawTableSn(sn.vkontakte, $("#vk_statistic"));
    });

    google.setOnLoadCallback(drawVisualization);

    function drawVisualization(data) {
        $('#chart').html("");
        var wrap = new google.visualization.ChartWrapper();
        wrap.setChartType('LineChart');
        wrap.setDataTable(data);
        wrap.setContainerId('chart');
        wrap.setOptions({
            chartArea: {left:50,top:35,width:"90%",height:"75%",backgroundColor: '#efefef'},
            titlePosition: 'out', legend : 'top' ,
            hAxis : { format: 'MMM yy', 'gridlines': {color: '#ccc', count: 5}},
            vAxis:{ 'gridlines': {color: '#ccc', count: 5}, 'minorGridlines': {color: '#333', count: 0}}
        });
        wrap.draw();
    }

    function updateChartData(value) {
        $.get("/company/${companyId}/chartData", {"interval" : value}, function(response){
            if (response.error) {
                errorDialog("Error update chart", response.error);
                return;
            }
            var data = new google.visualization.DataTable();
            data.addColumn('date', 'Date');
            data.addColumn('number', 'Photos Taken');
            data.addColumn('number', 'Facebook');
            data.addColumn('number', 'VKontakte');
            data.addColumn('number', 'Email');
            data.addRows(response.data.length);
            for (var i=0; i < response.data.length ; i++) {
                data.setCell(i, 0, new Date(response.data[i].date));
                data.setCell(i, 1, response.data[i].photosTaken);
                data.setCell(i, 2, response.data[i].facebook);
                data.setCell(i, 3, response.data[i].vkontakte);
                data.setCell(i, 4, response.data[i].emails);
            }
            drawVisualization(data);
        });
    }

    function drawTableSn(snType, container) {
        $.get("/company/${companyId}/tableData", {"statisticstype" : snType}, function(response){
            if (response.error) {
                errorDialog("Error creat table", response.error);
                return;
            }
            var template = new EJS({url: "/static/templates/stat_table.ejs"}).render({data: response.data});
            container.html(template);
        });
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
        <select onchange="updateChartData(this.value)" id="interval"  style="width: 100px;">
            <option value="MONTHS_12">12 Month</option>
            <option value="MONTHS_6">6 Month</option>
            <option value="DAYS_30">30 Days</option>
        </select>
    </div>
    <div id='chart' style='height: 300px; width: 700px;'></div>

    <label for="general_statistic" class="content" style="padding: 15px;">At a Glance</label>
    <div id="general_statistic" class="content" style="padding: 15px;">
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
    <br>
    <label for="fb_statistic" class="content" style="padding: 15px;">Facebook</label>
    <div id="fb_statistic"  class="content" style="padding: 15px;">
        <img src="/static/images/loading.gif" alt="">
    </div>
    <br>
    <label for="vk_statistic" class="content" style="padding: 15px;">VKontakte</label>
    <div id="vk_statistic" class="content" style="padding: 15px;">
        <img src="/static/images/loading.gif" alt="">
    </div>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>