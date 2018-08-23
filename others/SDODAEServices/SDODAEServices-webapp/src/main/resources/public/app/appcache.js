// Cache application function
(function () {

	function setLoaded() {
		var currentClass = document.body.className;
		document.body.className = currentClass + " loaded";
	}

	var cache = window.applicationCache;
	if (!cache) {
		//se non esiste l'oggetto cache nascondi i loader
		setLoaded();
		return;
	}

	/*inizializza gli eventi della cache per capire quando e' andato tutto ok o se e' andato qualcosa in errore
	a presincedere nascondi il loader, questo per rendere il tutto trasparente all'utente*/
	cache.addEventListener("cached", function () {
		console.log("All resources for this web app have now been downloaded. You can run this application while not connected to the internet");
		setLoaded();
	}, false);
	cache.addEventListener("checking", function () {
		console.log("Checking manifest");
	}, false);
	cache.addEventListener("downloading", function () {
		console.log("Starting download of cached files");
	}, false);
	cache.addEventListener("error", function (e) {
		console.log("There was an error in the manifest, downloading cached files or you're offline: " + e);
		setLoaded();
	}, false);
	cache.addEventListener("noupdate", function () {
		console.log("There was no update needed");
		setLoaded();
	}, false);
	cache.addEventListener("progress", function () {
		console.log("Downloading cached files");
	}, false);
	cache.addEventListener("updateready", function () {
		cache.swapCache();
		console.log("Updated cache is ready");

		window.location.reload(true);
		console.log("Window reloaded");
	}, false);

})();