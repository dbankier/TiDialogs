// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

// TODO: write your module tests here
var tidialogs = require('yy.tidialogs');

var datePicker = tidialogs.createDatePicker();
datePicker.addEventListener('click',function(e){
	alert(e)
});

setTimeout(datePicker.show, 1000);

