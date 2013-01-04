if (window.attachEvent) window.attachEvent("onload", IEhover);

window.onload = function() {
	linksExternal();
 	if (document.getElementById('navt_tabs')) {
		var el = document.getElementById('navt_tabs');
		_add_show_handlers(el);
	}
 	if (document.getElementById('page_tabs')) {
		var el = document.getElementById('page_tabs');
		_add_show_handlers(el);
	}
}

function IEhover() {
		cssHover('nav','LI');
	 	if (document.getElementById('navt_tabs')) {
			cssHover('navt_tabs','DIV');
		}
	 	if (document.getElementById('page_tabs')) {
			cssHover('page_tabs','DIV');
		}
}

function cssHover(tagid,tagname) {
	var sfEls = document.getElementById(tagid).getElementsByTagName(tagname);
	for (var i=0; i<sfEls.length; i++) {
		sfEls[i].onmouseover=function() {
			this.className+=" cssHover";
		}
		sfEls[i].onmouseout=function() {
			this.className=this.className.replace(new RegExp(" cssHover\\b"), "");
		}
	}
}

function change(id, newClass, oldClass) {
	identity=document.getElementById(id);
	if (identity.className == oldClass) {
		identity.className=newClass;
	} else {
		identity.className=oldClass;
	}
}

function _add_show_handlers(navbar) {
    var tabs = navbar.getElementsByTagName('div');
    for (var i = 0; i < tabs.length; i += 1) {
	tabs[i].onmousedown = function() {
	    for (var j = 0; j < tabs.length; j += 1) {
		tabs[j].className = '';
		document.getElementById(tabs[j].id + "_c").style.display = 'none';
	    }
	    this.className = 'active';
	    document.getElementById(this.id + "_c").style.display = 'block';
	    return true;
	};
    }
    var activefound=0;
    for (var i = 0; i < tabs.length; i += 1) {
    	if (tabs[i].className=='active') activefound=i;
    }
    tabs[activefound].onmousedown();
}

function activatetab(index) {
	var el=0;
	if (document.getElementById('navt_tabs')) {
		el = document.getElementById('navt_tabs');

	} else {
 	  if (document.getElementById('page_tabs')) {
		  el = document.getElementById('page_tabs');
	  }
	}
	if (el==0) return;
	var tabs = navbar.getElementsByTagName('div');
	tabs[index].onmousedown();
}

function linksExternal()	{
	if (document.getElementsByTagName)	{
		var anchors = document.getElementsByTagName("a");
		for (var i=0; i<anchors.length; i++)	{
			var anchor = anchors[i];
			if (anchor.getAttribute("rel") == "external")	{
				anchor.target = "_blank";
			}
		}
	}
}
function checkAll(theForm) { // check all the checkboxes in the list
  for (var i=0;i<theForm.elements.length;i++) {
    var e = theForm.elements[i];
		var eName = e.name;
    	if (eName != 'allbox' &&
            (e.type.indexOf("checkbox") == 0)) {
        	e.checked = theForm.allbox.checked;
		}
	}
}

/* Function to clear a form of all it's values */
function clearForm(frmObj) {
	for (var i = 0; i < frmObj.length; i++) {
        var element = frmObj.elements[i];
		if(element.type.indexOf("text") == 0 ||
				element.type.indexOf("password") == 0) {
					element.value="";
		} else if (element.type.indexOf("radio") == 0) {
			element.checked=false;
		} else if (element.type.indexOf("checkbox") == 0) {
			element.checked = false;
		} else if (element.type.indexOf("select") == 0) {
			for(var j = 0; j < element.length ; j++) {
				element.options[j].selected=false;
			}
            element.options[0].selected=true;
		}
	}
}

/* Function to get a form's values in a string */
function getFormAsString(frmObj) {
    var query = "";
	for (var i = 0; i < frmObj.length; i++) {
        var element = frmObj.elements[i];
        if (element.type.indexOf("checkbox") == 0 ||
            element.type.indexOf("radio") == 0) {
            if (element.checked) {
                query += element.name + '=' + escape(element.value) + "&";
            }
		} else if (element.type.indexOf("select") == 0) {
			for (var j = 0; j < element.length ; j++) {
				if (element.options[j].selected) {
                    query += element.name + '=' + escape(element.value) + "&";
                }
			}
        } else {
            query += element.name + '='
                  + escape(element.value) + "&";
        }
    }
    return query;ss
}
function getValues(frmObj ,elementname) {
    var query = "";
	for (var i = 0; i < frmObj.length; i++) {
        var element = frmObj.elements[i];
    if(element.name==elementname  ){
 //       alert(elementname);
        if (element.type.indexOf("checkbox") == 0 ||
        element.type.indexOf("radio") == 0) {
        if (element.checked) {
        query += element.name + '=' + escape(element.value) + "&";
        }
        } else if (element.type.indexOf("select") == 0) {
        for (var j = 0; j < element.length ; j++) {
        if (element.options[j].selected) {
        query += element.name + '=' + escape(element.value) + "&";
        }
        }
        } else {
        query += element.name + '='
        + escape(element.value) + "&";
        }
        }
    }
    return query;
}
