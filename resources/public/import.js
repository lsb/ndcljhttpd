watchfulEye = false;

$(document.body).insert(new Element("form", {'id': 'make-new-passage', 'action': '/superpowers/make-new-passage', 'style': 'position: fixed; bottom: 0'}))
$w('book_id title position subpassage_one subpassage_two subpassage_three good_as_is').each(
  function(name) { $('make-new-passage').insert(new Element((name.match(/^subpassage/) ? "textarea" : "input"), {"id": 'f-'+name, "name": name, "type": (name == "good_as_is" ? "checkbox" : "text")})) }
);
$('f-good_as_is').checked = true;
$('f-position').value = '0';
var prose = confirm("Prose?");
$('f-title').value = prose ? '0' : '0-0';

function eachk(a,fk,k) { a.length > 0 ? fk(a[0], function(){ eachk(a.slice(1),fk,k) }) : k()}
function wipe(s) { var r=window.getSelection().getRangeAt(0); r.deleteContents(); r.insertNode(document.createTextNode(s));}
function unDot(s) { return s.replace(/(Ap|A|D|C|Cn|K|L|Mam|M\'|M|N|O|P|Q|Qu|Sept|S|Ser|Sex|Sp|Tib?|T)[.]/g,"$1 "); }
function unNBSP(s) { return s.replace(/([^\xA0\n]+)\xA0+/g,"$1"); }
function unNumber(s) { return s.replace(/\s*\[?\d+\]?[.]?/g,""); }
function spaceEms(s) { return s.replace(/(\u2014)/g, " $1 "); }
function addspaces(s) { return s.replace(/([-!.,;?])(?=[a-zA-Z])/g,"$1 "); }

function sanitizeChildrenAndRecurse(e, f) {
  if (e.nodeName == '#text') { e.nodeValue = f(e.nodeValue); return; }
  $A(e.childNodes).each(function(c) { sanitizeChildrenAndRecurse(c,f) });
}

churn = sanitizeChildrenAndRecurse.curry(document.body);

[unDot, spaceEms, addspaces].each(churn);

if(!prose) {  [unNumber,unNBSP].each(churn);  }

function clickG() {
    oldContents = (unNumber(oldContents) != oldContents) ? prompt("Without the number in it, it would be: "+unNumber(oldContents),oldContents) : oldContents;
    if(null == oldContents) { nomoar; }
    if($F('f-subpassage_one').length == 0) { makeSubpassage('one'); }
    if($F('f-book_id') * 1 == 0) { $('f-book_id').value = prompt("Book id?") }
    if ($F('f-title') == $F('f-position')) { // then we are in prose, with each title being the numerical position
        $('f-title').value = $('f-title').value * 1 + 1;
    } else if ($F('f-title').include('-')) { // then we are in hyphenated poetry, with one subpassage, and each title being the line numbers
        var startingNumber = $F('f-title').match(/[^-]+$/)[0]*1 + 1;
	var numberOfLinesMinusOne = $F('f-subpassage_one').match(/\n+/g).length;
	$('f-title').value = startingNumber + '-' + (startingNumber + numberOfLinesMinusOne);
    } else { // then it's a special text
      $('f-title').value = prompt("New passage title:", $('f-title').value);
    }
    $('f-position').value = $('f-position').value * 1 + 1;
}
function clickH() {
    $('make-new-passage').request();
    $w('one two three').each(function(n) { $('f-subpassage_' + n).value = '' });
}

document.observe('keypress', function(e) {
  var s = window.getSelection();
  oldContents = s.toString();
  makeSubpassage = function(n) { wipe(''); $('f-subpassage_' + n).value = oldContents; }

  if(e.charCode == "#".charCodeAt(0)) { wipe(unNumber(oldContents)); }
  if(e.charCode == "x".charCodeAt(0)) { wipe(''); }
  if(e.charCode == "m".charCodeAt(0)) { wipe(prompt("replace with ", oldContents)); }
  if(e.charCode == "1".charCodeAt(0)) { makeSubpassage('one'); }
  if(e.charCode == "2".charCodeAt(0)) { makeSubpassage('two'); }
  if(e.charCode == "3".charCodeAt(0)) { makeSubpassage('three');}
  if(e.charCode == "u".charCodeAt(0)) { wipe(oldContents.replace(/ /g,'').toUpperCase()) }
  if(e.charCode == "g".charCodeAt(0)) { clickG() }
  if(e.charCode == "h".charCodeAt(0)) { clickH() }
});

function submitSlowly(pairs) {
    eachk(pairs, function(str,k) {$('f-subpassage_one').value=str;clickG();clickH();setTimeout(k,20000)},function(){});
}

document.title = 'x = delete. m = modify. 1,2,3. # = take out numbers. g = get ready. h = heave ho!'