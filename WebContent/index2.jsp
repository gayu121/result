<%@page import="Test32.Pai"%>
<%@page import="Test32.Count1"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="echarts.js"></script>
</head>
<body>
<%
	
	Pai ss=	new Pai();
	ss.run();
	String[] a=new String[11];
	String[] b=new String[11];
	int i=0,j=0;
	/*for(String l:ss.Names)
		a[i++]=l;
	for(String l:ss.Values)
		b[i++]=l;*/
		for(i = 0 ; i < 10 ; i++)
		{
			a[i] = ss.Values.get(i);
			//b[i] = ss.Names.get(i);
			
		}
%>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <a href="index1.jsp">最受欢迎文章/视频</a>
    <a href="index2.jsp">按照地市最受欢迎</a>
    <a href="index3.jsp">按照流量最受欢迎</a>
    <hr>
    <div id="main" style="width: 600px;height:400px;"></div>

    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '按照地市最受欢迎'
            },
            tooltip: {},
            legend: {
                data:['统计']
            },
            xAxis: {
                data: ['ip1','ip2','ip3','ip4','ip5','ip6','ip7','ip8','ip9','ip10']
            },
            yAxis: {},
            series: [{
                name: '最受欢迎的文章',
                type: 'line',
                data: [
                <%
                for( i=0;i<10;i++)
                {
                %><%=a[i]%>,<%
                
                }
                %>

						]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>