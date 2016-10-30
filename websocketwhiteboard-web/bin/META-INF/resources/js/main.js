AUI().ready(
	'event-custom',
	function(A) {
		
		var wsUri = 'ws://' + window.location.hostname+ ':9000/remoteagent/whiteboardapp';
		var websocket = new WebSocket(wsUri);

		var path;

		paper.install(window);

		paper.setup('whiteboradCanvas');
		// Create a simple drawing tool:
		var tool = new Tool();

		// Define a mousedown and mousedrag handler
		tool.onMouseDown = function(event) {
			A.fire('onMouseDown', event.point);
			A.fire('websocketSend', "onMouseDown",event.point);
		}

		tool.onMouseDrag = function(event) {
			A.fire('onMouseDrag', event.point);
			A.fire('websocketSend', "onMouseDrag",event.point);
		}

		tool.onMouseUp = function(event) {
			A.fire('onMouseUp', event.point);
			A.fire('websocketSend', "onMouseUp",event.point);
		}

		function Point(x, y) {
			this.x = x;
			this.y = y;
		}

		websocket.onmessage = function(event) {
			var data = JSON.parse(event.data);
			A.fire(data.type, data);
		};
				
		A.on('onMouseDown', function (data) {
			// If we produced a path before, deselect it:
			if (path) {
				path.selected = false;
			}

			// Create a new path and set its stroke color to black:
			path = new Path({
				segments : new Point(data.x, data.y),
				strokeColor : 'black'
			});
    });
				
		A.on('onMouseDrag', function (data) {
			path.add(new Point(data.x, data.y));
    });
		
		A.on('onMouseUp', function (data) {
			path.simplify(5);
		});
		
		A.on('websocketSend', function (type,point) {
			websocket.send(JSON.stringify({
				type: type,
				x : point.x,
				y : point.y
			}));
		});
	}
);
