<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        $(".table").DataTable();
    });
</script>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
            Lista dei file XHTML
        </h1>
    </div>
</div>
<a class="btn btn-primary btn-small"
	href="${pageContext.request.contextPath}/packager/orderresources/insertxhtml?epub=${epub}">
    Inserisci XHTML</a>
<br /><br />
<c:if test="${not empty sortingXhtmlFiles}">
<table class="table table-hover">
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Path</th>
            <th>Ordine</th>
            <th>Type</th>
            <th>Azioni</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${sortingXhtmlFiles}" var="sortingXhtmlFile">
        <tr>
            <th scope="row">${sortingXhtmlFile.id}</th>
            <td>${sortingXhtmlFile.name}</td>
            <td>${sortingXhtmlFile.path}</td>
            <td>${sortingXhtmlFile.index}</td>
            <td>${sortingXhtmlFile.type}</td>
            <td><a href="${pageContext.request.contextPath}/packager/orderresources/updatexhtml?id=${sortingXhtmlFile.id}">Modifica</a> - <a href="${pageContext.request.contextPath}/packager/orderresources/deletexhtml?id=${sortingXhtmlFile.id}">Elimina</a></td>
        </tr>
        </c:forEach>
    </tbody>
</table>
</c:if>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
            Lista dei file CSS
        </h1>
    </div>
</div>
<a class="btn btn-primary btn-small"
	href="${pageContext.request.contextPath}/packager/orderresources/insertcss?epub=${epub}">
    Inserisci CSS</a>
<br /><br />
<c:if test="${not empty sortingCssFiles}">
<table class="table table-hover">
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Path</th>
            <th>Azioni</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${sortingCssFiles}" var="sortingCssFile">
        <tr>
            <th scope="row">${sortingCssFile.id}</th>
            <td>${sortingCssFile.name}</td>
            <td>${sortingCssFile.path}</td>
            <td><a href="${pageContext.request.contextPath}/packager/orderresources/updatecss?id=${sortingCssFile.id}">Modifica</a> - <a href="${pageContext.request.contextPath}/packager/orderresources/deletecss?id=${sortingCssFile.id}">Elimina</a></td>
        </tr>
        </c:forEach>
    </tbody>
</table>
</c:if>
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">
            Lista delle immagini
        </h1>
    </div>
</div>
<a class="btn btn-primary btn-small"
        href="${pageContext.request.contextPath}/packager/orderresources/insertimage?epub=${epub}">
    Inserisci immagine</a>
<br /><br />
<c:if test="${not empty sortingImageFiles}">
<table class="table table-hover">
    <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Path</th>
            <th>Azioni</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${sortingImageFiles}" var="sortingImageFile">
        <tr>
            <th scope="row">${sortingImageFile.id}</th>
            <td>${sortingImageFile.name}</td>
            <td>${sortingImageFile.path}</td>
            <td><a href="${pageContext.request.contextPath}/packager/orderresources/updateimage?id=${sortingImageFile.id}">Modifica</a> - <a href="${pageContext.request.contextPath}/packager/orderresources/deleteimage?id=${sortingImageFile.id}">Elimina</a></td>
        </tr>
        </c:forEach>
    </tbody>
</table>
</c:if>
<a href="${pageContext.request.contextPath}/packager/orderok?epub=${epub}" class="btn btn-success" role="button">Crea EPUB</a>