<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html lang="kor">
<head>
<!-- Theme Made By www.w3schools.com - No Copyright -->
<title><tiles:insertAttribute name="title" /></title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
<link href="static/css/kendo.common.min.css" rel="stylesheet">
<link href="static/css/kendo.default.min.css" rel="stylesheet">
<link href="static/css/examples-offline.css" rel="stylesheet">

<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script	src="static/js/kendo.all.min.js"></script>

<style>
.fakeimg {
	height: 200px;
	background: #aaa;
}
.margin {
	margin-bottom: 45px;
}
.navbar {
	padding-top: 15px;
	padding-bottom: 15px;
	border: 0;
	border-radius: 0;
	margin-bottom: 0;
	font-size: 12px;
	letter-spacing: 5px;
}
.navbar-nav  li a:hover {
	color: #1abc9c !important;
}
#treeview-left, #treeview-right {
    overflow: visible;
}
.folder-image {
	background-image: url("img/folder.png");    
}
.file-image {
	background-image: url("img/file.jpg");
}

.btn-file{
    position: relative;
    overflow: hidden;
}
.btn-file input[type=file] {
    position: absolute;
    top: 0;
    right: 0;
    min-width: 100%;
    min-height: 100%;
    font-size: 100px;
    text-align: right;
    filter: alpha(opacity=0);
    opacity: 0;
    outline: none;
    background: white;
    cursor: inherit;
    display: block;
}
</style>
</head>

<body>
	<tiles:insertAttribute name="header" />

	<tiles:insertAttribute name="menu" />

	<tiles:insertAttribute name="body" />

	<tiles:insertAttribute name="footer" />	
</body>
</html>