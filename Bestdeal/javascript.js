function init() {
	console.log("JS log in init()");
	completeField = document.getElementById("searchId");
	completeTable = document.getElementById("complete-table");
	autoRow = document.getElementById("auto-row");
}

function doCompletion() {
	completeField = document.getElementById("searchId");
	url = "AutoComplete?action=complete&searchId=" + escape(completeField.value);
	console.log("JS log inside doComplete" + url);
	req = initRequest();
	req.onreadystatechange = callback;
	req.open("GET", url, true);
	//req.onreadystatechange = callback;
	req.send();
}

function initRequest() {
    isIE = false;
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') !== -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function appendProduct(name, productId) {   //check params //productId
	var row;
	var cell;
	var linkElement;
	console.log("Inside JS | appendProduct | ");
	if (isIE) {
	completeTable.style.display = 'block';
	row = completeTable.insertRow(completeTable.rows.length);
	cell = row.insertCell(0);
	} else {
	completeTable.style.display = 'table';
	row = document.createElement("tr");
	cell = document.createElement("td");
	row.appendChild(cell);
	completeTable.appendChild(row);
}
	cell.className = "popupCell";
	linkElement = document.createElement("a");
	linkElement.className = "popupItem";
	linkElement.setAttribute("href", "AutoComplete?action=lookup&searchId=" + productId); //check this should be productName //productId
	console.log("Inside JS | appendProduct | url :  AutoComplete?action=lookup&searchId=" + productId);
	linkElement.appendChild(document.createTextNode(name));
	cell.appendChild(linkElement);
}


function parseMessages(resXML) {
// no matches returned
	console.log("Inside JS | ParseMessage | resXML : ");
	if (resXML === null) {
		console.log("Inside JS | ParseMessage | resXML === null : " + (resXML === null));
	return false;
	} else {
	var product = resXML.getElementsByTagName("product");
	console.log(product.length);
	if (product.length > 0) {
	completeTable.setAttribute("bordercolor", "black");
	completeTable.setAttribute("border", "1");
	for (loop = 0; loop < product.length; loop++) {
	//var product = products.childNodes[loop];
	var productId = product[loop].getElementsByTagName("productId")[0].childNodes[0].nodeValue;     //changed from id
	var productName = product[loop].getElementsByTagName("name")[0].childNodes[0].nodeValue; //changed from productName
	appendProduct(productName,productId);
	}
   }
   else{
            console.log("Error in parseMessages");
        }
  }
}
function callback() {
clearTable();
console.log("Inside JS | callback | : req.state and req.status : " + req.readyState, req.status);
if (req.readyState === 4 && req.status === 200) {
	console.log("response text: " + req.responseText);
    console.log("response xml: " + req.responseXML);
            parseMessages(req.responseXML); 
    }
}

function clearTable() {
if (completeTable.getElementsByTagName("tr").length > 0) {
completeTable.style.display = 'none';
for (loop = completeTable.childNodes.length -1; loop >= 0 ; loop--) {
completeTable.removeChild(completeTable.childNodes[loop]);
}
}
}




