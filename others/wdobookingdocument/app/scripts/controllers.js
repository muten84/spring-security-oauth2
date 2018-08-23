/*definizione dei controllers*/

define(["angular"], function(angular){
	var controllerPath = "controllers";
	
	return [
	        controllerPath+"/login",
	        controllerPath+"/list",
	        controllerPath+"/listDocuments",
	        controllerPath+"/main",
	        controllerPath+"/navBarController",
	        controllerPath+"/searchController",
		controllerPath+"/modalController",
		controllerPath+"/pdfController"
	]
});