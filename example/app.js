// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white',
  layout: 'vertical',
});
win.open();

// TODO: write your module tests here
var tidialogs = require('yy.tidialogs');

var buttons = [ {
    title: 'legacy date',
    dialog: function () {
      return tidialogs.createDatePicker({day:28, month:7, year: 1980});
    }
},  {
    title: 'new date, custom buttons',
    dialog: function () {
      var date = new Date();
      date.setMonth(2);
      return tidialogs.createDatePicker({value: date, okButtonTitle: "Yep", cancelButtonTitle:"Nah"});
    }
},{
    title: 'legacy time',
    dialog: function () {
      return tidialogs.createTimePicker({hour:10, minute:30});
    }
},  {
    title: 'new time, custom buttons',
    dialog: function () {
      var date = new Date();
      date.setHours(10);
      return tidialogs.createTimePicker({value: date, okButtonTitle: "Yep", cancelButtonTitle:"Nah"});
    }
},  {
    title: 'multi picker',
    dialog: function () {
      var date = new Date();
      date.setHours(10);
      return tidialogs.createMultiPicker({title:"Hello World", options:["A","B","C"], selected: ["B","C"]});
    }
},  {
    title: 'multi, custom buttons',
    dialog: function () {
      var date = new Date();
      date.setHours(10);
      return tidialogs.createMultiPicker({title:"Hello World", options:["A","B","C"], selected: ["B","C"], okButtonTitle: "Yep", cancelButtonTitle:"Nah"});
    }
}]


buttons.forEach(function(o) {
  var button = Ti.UI.createButton({title: o.title});
  button.addEventListener("click", function() {
    var dialog = o.dialog();
    dialog.addEventListener("click", function(o){
      Ti.API.debug("click");
      Ti.API.debug(JSON.stringify(o));
    });
    dialog.addEventListener("cancel", function(o){
      Ti.API.debug("cancel");
    });
    dialog.show();
  });
  win.add(button);
});
