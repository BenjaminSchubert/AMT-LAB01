<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base>
    <jsp:attribute name="head">
        <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet">
    </jsp:attribute>
</t:base>
<t:container>
    <jsp:attribute name="inner">
        <h1>Login</h1>
        <form method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input id="username" class="form-control" type="text" placeholder="Username" name="username">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input id="password" class="form-control" type="password" placeholder="Password" name="password">
            </div>

            <button type="submit" class="btn btn-primary">Submit</button>
        </form>

        <div id="register">
            <a href="${pageContext.request.contextPath}/register">No account yet ? Register.</a>
        </div>
    </jsp:attribute>
</t:container>