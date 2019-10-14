var express = require('express'); 
var app = express(); 

app.listen(3000, function() { 
	console.log('server running on port 3000'); 
} ) 

// Predict image
const multer = require('multer')
var storage = multer.diskStorage({
  destination: "images",
  filename: function (req, file, cb) {
    cb(null, Date.now() + '.jpg') //Appending .jpg
  }
})

var upload = multer({ storage: storage });

function predictImage(req, res) { 
	// console.log(req.file)
	imagepath = 'images/' + req.file.filename
	var spawn = require("child_process").spawn; 
	var process = spawn('python', ["./predict.py", imagepath]); 
 
	process.stdout.on('data', function(data) { 
		res.send(data.toString()); 
	}) 
} 

app.post('/predict', upload.single('upload'), predictImage); 
