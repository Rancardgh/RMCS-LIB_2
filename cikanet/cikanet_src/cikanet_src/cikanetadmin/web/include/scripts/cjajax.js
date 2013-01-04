
function cjAjaxEngine(uri,handlerFunction,errorFunction)
{
  if (handlerFunction==null)  handlerFunction = function() {};
  if (!errorFunction==null)   errorFunction = function () {};

  var r = (window.ActiveXObject)?new ActiveXObject("Microsoft.XMLHTTP"):new XMLHttpRequest();
  if (r)
  {
    r.onreadystatechange = function()
    {
	if (r.readyState == 4)
         if (r.status == 200)
  	 {
	   xmlDoc = r.responseXML;				
           txt = r.responseText;
           handlerFunction(txt, xmlDoc);
	 }
         else
          errorFunction();
    }
    r.open("GET", uri);
    if (window.XMLHttpRequest) r.send(null);
    else                       r.send();
    return true;
  }
  else
  {
    errorFunction();
    return false;
  }
}

function loadScript(uri)
{
  var scr = document.createElement('script'); 
  scr.setAttribute('type', 'text/javascript');
  scr.setAttribute('language','JavaScript');
  scr.setAttribute('src',uri);
  document.body.appendChild(scr); 
}

function createScript(s)
{
  var scr = document.createElement('script'); 
  scr.setAttribute('type', 'text/javascript');
  scr.setAttribute('language','JavaScript');
  scr.text=s;
  document.body.appendChild(scr); 
}
