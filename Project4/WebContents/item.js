function init() {
	var apikey = "AIzaSyCul2EpN5C7Ztf5vW1yNGppaEv6k1FQpuk";
    var myOptions = { 
      zoom: 8, // default is 8  
      center: {lat: -34.397, lng: 150.644}, 
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"),
        myOptions); 

	if (document.getElementById("latitude").dataset.latitude) {
		map.setCenter(new google.maps.LatLng(document.getElementById("latitude").dataset.latitude,document.getElementById("longitude").dataset.longitude));
		console.log("we found latitude" + document.getElementById("latitude").innerHTML.length);
	}
	else {
		console.log("No latitude :(");
	  	var geocoder = new google.maps.Geocoder();
	  	var address = document.getElementById("location").dataset.location;	
	  	geocoder.geocode({'address': address}, function(results, status) {
		    if (status === google.maps.GeocoderStatus.OK) {
		      map.setCenter(results[0].geometry.location);
		      var marker = new google.maps.Marker({
		        map: map,
		        position: results[0].geometry.location
		      });
		    }
	  	});
	}


}