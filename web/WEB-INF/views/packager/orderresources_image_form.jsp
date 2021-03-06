<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${requestScope.update}">
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
            $("#path").attr('readonly', true);
            $("#contentType").attr('readonly', true);
            $("#epub").attr('readonly', true);
        });
    </script>
</c:if>
<c:if test="${requestScope.delete}">
    <script type="text/javascript" charset="utf-8">
        $(document).ready(function() {
            $(":input[type='text'],textarea").each(function() {
                $(this).attr('readonly', true);
            });
        });
    </script>
</c:if>
<div class="row">
    <div class="col-lg-12">
        <c:choose>
            <c:when test="${requestScope.update}">
                <h1 class="page-header">
                    Modifica immagine
                </h1>
            </c:when>
            <c:when test="${requestScope.delete}">
                <h1 class="page-header">
                    Elimina immagine
                </h1>
            </c:when>
        </c:choose>
    </div>
</div>
<form:form modelAttribute="epubImage"
           action="${pageContext.request.contextPath}${requestScope.action}"
           cssClass="form-horizontal" method="POST">
    <form:hidden path="id" />
    <div class="form-group">
        <label for="name">Name</label>
        <span class="glyphicon glyphicon-ok-circle"></span>
        <form:input path="name" cssClass="form-control" autofocus="true" />
        <form:errors path="name" cssClass="alert-danger" />
    </div>
    <div class="form-group">
        <label for="path">Path</label>
        <form:input id="path" path="path" cssClass="form-control" />
        <form:errors path="path" cssClass="alert-danger" />
    </div>
    <div class="form-group">
        <label for="contentType">Content Type</label>
        <form:input id="contentType" path="contentType" cssClass="form-control" />
        <form:errors path="contentType" cssClass="alert-danger" />
    </div>
    <div class="form-group">
        <label for="epub">EPUB</label>
        <form:input id="epub" path="epub" cssClass="form-control" />
        <form:errors path="epub" cssClass="alert-danger" />
    </div>
    <div class="form-group">
        <c:choose>
            <c:when test="${!requestScope.delete}">
                <button type="submit" class="btn btn-primary">
                    Salva
                </button>
            </c:when>
            <c:otherwise>
                <button type="submit" class="btn btn-danger">
                    Elimina
                </button>
            </c:otherwise>
        </c:choose>
    </div>
</form:form>
