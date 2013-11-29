// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
win.open();

// TODO: write your module tests here
var tidialogs = require('yy.tidialogs');

win.addEventListener('doubletap', function() {
  var picker = tidialogs.createDatePicker({day:28, month:7, year: 1980});
  picker.addEventListener('click',function(e){
    alert(JSON.stringify(e))
  });
  picker.show();
});
win.addEventListener('swipe', function(e) {
  var picker;
  if (e.direction === "left") {
    picker = tidialogs.createMultiPicker({title:"Hello World", options:["A","B","C"], selected: ["B","C"]});
  } else {
    picker = tidialogs.createTimePicker({hour: 10, minute: 30});
  }
  picker.addEventListener('click',function(e){
    alert(JSON.stringify(e))
  });
  picker.show();
});

