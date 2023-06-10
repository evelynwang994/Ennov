<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Upload File</title>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
		<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
		<style><%@include file="../css/bootstrap.min.css"%></style>
		<script><%@include file="../js/bootstrap.min.js"%></script>
		<script><%@include file="../js/layer.js"%></script>
		<style><%@include file="../css/style.css"%></style>
	</head>
	<body>
		<!-- header -->
		<p class="header">Please Upload Your CSV File!</p>
		<form id="UploadForm" method="post" action="/upload" enctype="multipart/form-data">
			<!-- File Upload -->
			<div class="file-upload">
	  			<div class="file-select">
	    			<div class="file-select-button" id="fileName">Choose File</div>
	    			<div class="file-select-name" id="noFile">No file chosen...</div> 
	    			<input type="file" name="fileUpload" id="chooseFile">
	  			</div>
			</div>
			<div class="file-upload-button">
				<button type="button" id="btnSubmit" class="btn btn-success btn-rounded" onClick="doUpload()">Upload</button>
			</div>
		</form>
		<!-- The Modal -->
		<div class="modal fade" id="messageModal">
			<div class="modal-dialog">
				<div class="modal-content">
		      
		        <!-- Modal Header -->
		        <div class="modal-header">
		          <h4 class="modal-title">Alert Message</h4>
		          <button type="button" class="close" data-dismiss="modal">&times;</button>
		        </div>
		        
		        <!-- Modal body -->
		        <div id="messageBody" class="modal-body"></div>
		        
		        <!-- Modal footer -->
		        <div class="modal-footer">
		          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
		        </div>
		        
		      </div>
		    </div>
		</div>
	</body>
	<script>
		$('#chooseFile').bind('change', function () {
			var filename = $("#chooseFile").val();
			if (/^\s*$/.test(filename)) {
				$(".file-upload").removeClass('active');
			    $("#noFile").text("No file chosen..."); 
			 }else {
				$(".file-upload").addClass('active');
			    $("#noFile").text(filename.replace("C:\\fakepath\\", "")); 
			 }
		});
		
		function doUpload(){
			var fileName = $('#chooseFile').val();
			if(!fileName){
				$('#messageBody').text("Please Choose Your File!");
				$('#messageModal').modal('show');
				return;
			}else{
				var extension = fileName.replace(/^.*\./, '');
				if(extension != 'csv'){
					$('#messageBody').text("Please Upload a CSV File!");
					$('#messageModal').modal('show');
					return;
				}
				
				$('#UploadForm').submit();
			}
		}
	</script>
</html>