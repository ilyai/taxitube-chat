$(function() {
	// Full-duplex channel between client and server
	if (window.WebSocket === undefined) {
		bootbox.alert("WebSocket technology is not supported. Please update your browser.");
		return;
	}
	
	var socket = new WebSocket($('body').data('ws-url'));
	socket.onmessage = function(event) {
		var data = JSON.parse(event.data);
		switch (data.type) {
		case 'message':
			$('<dt/>').text(data.name).appendTo('#board');
			$('<dd/>').text(data.msg).appendTo('#board');
			$('.cover').scrollTop($('.cover')[0].scrollHeight);
			break;
		default:
			console.error("Unknown type of message: " + data.type);
		}
	};
	socket.onopen = function() {
		$.notify("Connection established");
	};
	socket.onerror = function() {
		$.notify("Connection error", { type: "danger" });
	};
	socket.onclose = function() {
		$.notify(
				"Connection lost. Please <a href='javascript:location.reload()'>reload the page</a>.",
				{ type: "danger", delay: 0 });
	};
	
	// Send chat message
	$('#msgform').submit(function(e) {
		e.preventDefault();
		var data = {
			msg: $('#msgtext').val()
		};
		socket.send(JSON.stringify(data));
		$('#msgtext').val('');
	});
	
	// Changing user name
	$('.username').click(function(e) {
		e.preventDefault();
		bootbox.prompt("What's your name?", function(name) {
			if (!name) return;
			$.post("/changeName", { name: name })
				.done(function() { location.reload(); })
				.fail(function() { $.notify("Request failed. Please retry.", { type: "danger" }); });
		});
	});
	
	// Bootstrap tooltips
	$("[title]").tooltip();
});