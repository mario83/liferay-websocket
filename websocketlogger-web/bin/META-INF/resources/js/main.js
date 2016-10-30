(function () {
	
	var wsUri = 'ws://' +window.location.hostname+ ':9000/remoteagent/logapp';
	var websocket = new WebSocket(wsUri);
	
	websocket.onmessage = function(event) {
	  $('#log-container').append('<div>' + event.data + '</div>');
	  $("#log-container").scrollTop($("#log-container").prop('scrollHeight'));
	};
})(); 

