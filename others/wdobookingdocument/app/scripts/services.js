/*definizione dei controllers*/
define(["angular"], function(angular){
	var servicesPath = "services";
	return [
	        servicesPath+"/api",
	        servicesPath+"/logger",
		servicesPath+"/apiInterceptor"
	]
});