<%--
  Created by IntelliJ IDEA.
  User: yurir
  Date: 10/14/17
  Time: 12:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>BattleShips - By Amir Shavit and Yuri Ritvin - ${param.title}</title>
    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>
<div class="container">
    <jsp:include page="/pages/html/${param.content}"/>

    <div class="row">
        <div class="col-xs-12" id="js-messages">
        </div>
    </div>
</div>
<jsp:include page="/pages/footer.jsp"/>

<!-- Vendor Scripts -->
<script type="text/javascript">
    window.BASE_URL = "${pageContext.request.contextPath}";
    console.log(window.BASE_URL);
</script>
<script src="${pageContext.request.contextPath}/static/js/vendor/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/vendor/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/common.js"></script>
<script src="${pageContext.request.contextPath}/static/js/${param.jsFile}"></script>


</body>
</html>
