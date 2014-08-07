/*the websocket variable*/
var wsocket;
function toggleWSConnect(wsURL) {
	if (window.WebSocket) {
		/*if websockets are supported*/
		if (wsocket === undefined) {
			/*if websocket is not open*/
			wsocket = new WebSocket(wsURL);
			wsocket.onmessage = wsOnMessage;
			document.getElementById('commentsheaderform:continuosreadenabledicon').style.display = 'inline';
			document.getElementById('commentsheaderform:continuosreaddisabledicon').style.display = 'none';
		} else {
			/*if websocket is open*/
			wsocket.close();
			wsocket = undefined;
			document.getElementById('commentsheaderform:continuosreadenabledicon').style.display = 'none';
			document.getElementById('commentsheaderform:continuosreaddisabledicon').style.display = 'inline';
		}
	} else {
		/*if websockets are not supported*/
		alert('Websocket not available');
	}

}

function wsOnMessage(evt) {
	// alert (evt.data);
	document.getElementById("commentsheaderform:refreshComments").click();
}
