var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(8080, function(){
	console.log("Server is now running...");
});
// app.get('/', (req, res) => {
//     res.send('<h1>Hello world</h1>');
//   });
  

io.on('connection', function(socket){
	console.log("User Connected!");
	// socket.emit('socketID', { id: socket.id });
	// socket.emit('getPlayers', players);
    // socket.broadcast.emit('newPlayer', { id: socket.id });
    socket.on('sendMessage', (data) => {
        console.log(data.name + ": " + data.message);
        socket.broadcast.emit('newMessage',data);
    });


	socket.on('disconnect', function(){
        console.log("User Disconnected!");
	});
});