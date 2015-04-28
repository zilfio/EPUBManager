<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        $("#epub").fileinput({
            allowedFileExtensions: ["epub", "zip"],
            elErrorContainer: "#errorepub"
        });
    });
</script>
<div class="row">
    <div class="col-lg-12">
	<h1 class="page-header">
            <spring:message code="uploadresources.heading.create" />
	</h1>
    </div>
</div>
<form:form action="${pageContext.request.contextPath}${requestScope.action}" method="POST" enctype="multipart/form-data">
    <div class="well">
        <div class="form-group">
            <label for="xhtmlfiles"><spring:message code="uploadresources.epub" /></label>
            <input id="epub" name="epub" type="file" class="file-loading" data-show-upload="false" accept="application/epub+zip" required />
            <div id="errorepub" class="help-block"></div>
        </div>
    </div>
    <div class="form-group">
	<button type="submit" class="btn btn-primary">
            <spring:message code="common.next" />
	</button>
    </div>
</form:form>