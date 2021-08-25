<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="./common/head.jsp"%>
<div class="right">
    <img class="wColck" src="${pageContext.request.contextPath}/static/images/clock.jpg" alt=""/>
    <div class="wFont">
        <h2>${userSession.userName}</h2>
        <p>欢迎来到超市订单管理系统!</p>
    </div>
</div>
</section>
<%@include file="./common/foot.jsp" %>
