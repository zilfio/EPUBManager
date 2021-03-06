<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        $("#cssfile").fileinput({
            allowedFileExtensions: ["css"],
            elErrorContainer: "#errorcssfile"
        });
    });
</script>
<div class="row">
    <div class="col-lg-12">
	<h1 class="page-header">
            Upload CSS
	</h1>
    </div>
</div>
<form:form action="${pageContext.request.contextPath}${requestScope.action}" method="POST" enctype="multipart/form-data">
    <input type="text" id="epub" name="epub" value="${epub}" />
    <div class="well">
        <div class="form-group">
            <label for="cssfile">Seleziona file CSS</label>
            <input id="cssfile" name="cssfile" type="file" class="file-loading" data-show-upload="false" accept="text/css" required />
            <div id="errorcssfile" class="help-block"></div>
        </div>
    </div>
    <div class="form-group">
	<button type="submit" class="btn btn-primary">
            Inserisci
	</button>
    </div>
</form:form>