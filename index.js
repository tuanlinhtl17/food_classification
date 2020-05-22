var express = require('express'); 
var app = express(); 

app.listen(80, function() { 
	console.log('server running on port 80'); 
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
	var process = spawn('python3', ["./predict.py", imagepath], { detached: true }); 
 
	process.stdout.on('data', function(data) {
		var final_data = data.toString().replace(/\n/g, "");
		var result = { name: final_data }; 
		res.send(result); 
	}) 
} 

app.post('/predict', upload.single('upload'), predictImage); 
app.get('/', function(req, res){
	res.send('Hello');
})
