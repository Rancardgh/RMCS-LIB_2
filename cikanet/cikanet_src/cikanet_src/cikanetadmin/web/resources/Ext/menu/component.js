
// define the namespaces
jmaki.namespace("jmaki.widgets.Ext.menu");

jmaki.widgets.Ext.menu.Widget = function(wargs) {
    
    var container = document.getElementById(wargs.uuid);
    var self = this;

    // use this topic name as this is what the dcontainer is listening to.
    var topic = "/jmaki/menu";

    this.menubar;

    if (wargs.args && wargs.args.topic) {
        topic = wargs.args.topic;
    }
     if ( wargs.publish) {
        topic = wargs.publish;
    }
    
    // pull in the arguments
    if (wargs.value) {
        menu = wargs.value.menu;
        rows = wargs.value.rows;
        init();
    } else if (wargs.service) {
            jmaki.doAjax({url: wargs.service, callback: function(req) {
        if (req.readyState == 4) {
            if (req.status == 200) {
              var data = eval('(' + req.responseText + ')');
              menu = data.menu;
              init();
          }
        }
      }});
    } else {
        menu = [ 
        {label: 'Some Topic',
            menu: [
                {label:'Some Item', action:{message: '1.jsp'}},
                {label:'Some Item 2', href:'http://jmaki.com'}
                ]},

        {label: 'Some Other Topic',

            menu: [
                {label:'Some Other Item', action:{topic: '/mytopic', message:'2.jsp'}},
                {label:'Some Other Item 2', href:'http://www.sun.com'}
                ]}
        ];
        init();
    }
    
    function showURL (largs) {
        var _topic = topic;
        if (largs.action ) {
            if (largs.action.topic) _topic = largs.action.topic;
            jmaki.publish (_topic, largs.action.message);
        }

        if ( largs.href) {
             window.location.href = largs.href;
        }
    }
    
    function init() {
        menubar = new Ext.Toolbar(wargs.uuid);
        for (var i = 0; i < menu.length; i++) {
            var items = [];
            for (var ii=0; ii < menu[i].menu.length; ii++){
               if (menu[i].menu[ii].url ) {   //support old style for now
                   var _href = menu[i].menu[ii].url;
               } else {
                   var _href = menu[i].menu[ii].href;
               }
                items.push({text : menu[i].menu[ii].label, handler : showURL, href : _href, action: menu[i].menu[ii].action});
            }
            menubar.add(new Ext.Toolbar.MenuButton({
                cls: 'x-btn-text-icon bmenu',
                        text : menu[i].label,
                        id : wargs.uuid + '_' + i,
                        menu : new Ext.menu.Menu({items : items})}));
            if (i < menu.length -1 ) menubar.addSeparator();
        }
        
    }
}
