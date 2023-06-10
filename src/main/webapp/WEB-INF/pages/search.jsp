<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Search Page</title>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
		<style><%@include file="../css/bootstrap.min.css"%></style>
		<style><%@include file="../css/bootstrap-grid.css"%></style>
		<style><%@include file="../css/bootstrap-grid.min.css"%></style>
		<style><%@include file="../css/bootstrap-reboot.css"%></style>
		<style><%@include file="../css/bootstrap-reboot.min.css"%></style>
		<script><%@include file="../js/bootstrap.min.js"%></script>
		<script><%@include file="../js/layer.js"%></script>
		<style><%@include file="../css/style.css"%></style>
	</head>
	<body>
		<p class="header">Current Sum of All Product Prices is ${sum}</p>
		<div class="container">
			<form class="form-inline" id="search" method="post" action="/search">
				<input type="hidden" name="sum" value="${sum}">
				<input type="hidden" id="func" name="func">
				<label class="mr-3" for="productLine">Product Line</label>
				<select class="form-control mr-3" name="productLine" id="productLine">
				  	<option value="" selected>Select One</option>
				  	<c:forEach var="item" items="${ltPL}">
						<option value="${item}" ${productLine.equals(item)?'selected':''}>${item}</option>
					</c:forEach>
				</select>
				<label class="mr-3" for="productBrand">Product Brand</label>
				<select class="form-control mr-3" name="productBrand" id="productBrand">
					<option value="" selected>Select One</option>
				  	<c:forEach var="item" items="${ltPB}">
						<option value="${item}" ${productBrand.equals(item)?'selected':''}>${item}</option>
					</c:forEach>
				</select>
			</form>
			<div class="text-center mr-3 pt-5">
				<button type="button" class="btn btn-success btn-rounded mr-3" onClick="doSearch()">Search</button>
				<button type="button" class="btn btn-success btn-rounded mr-3" onClick="doClear()">Clear</button>
				<a href="http://localhost:8081/download" class="btn btn-info">Download File Showing the Differences</a>
			</div>
			<c:if test="${result != null}">
				<p id="filteredMessage" class="text-center pt-3">The sum of the filtered product prices is ${filteredSum}</p>
				<table id="result" class="table table-striped w-auto">
					<thead>
						<tr>
							<th scope="col">Product ID</th>
							<th scope="col">Product Line</th>
							<th scope="col">Product Brand</th>
							<th scope="col">Product Generic</th>
							<th scope="col">Product Price</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${result.size()==0}">
							<tr>
								<td colspan="5">No Matched Data!</td>
							</tr>
						</c:if>
						<c:forEach var="row" items="${result}">
							<tr>
								<th scope="row">${row.productId}</th>
								<td>${row.productLine}</td>
								<td>${row.productBrand}</td>
								<td>${row.productGeneric}</td>
								<td>${row.productPrice}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>
	</body>
	<script>
		function doSearch() {
			$('#func').val("search");
			$('#search').submit();
		}
		
		function doClear() {
			$('#productLine').val('');
			$('#productBrand').val('');
			
			$('#filteredMessage').hide();
			$('#result').hide();
		}
	</script>
</html>