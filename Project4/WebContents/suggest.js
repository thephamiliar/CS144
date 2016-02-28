function AutoSuggestControl(oTextbox, oProvider) {
	this.layer = null;
    this.provider = oProvider;
    this.textbox = oTextbox;
    this.init();
}

AutoSuggestControl.prototype.hideSuggestions = function () {
    this.layer.style.display = "none";
};

AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {

    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};	

AutoSuggestControl.prototype.createDropDown = function () {

    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    document.body.appendChild(this.layer);

    var oThis = this;

    this.layer.onmousedown = this.layer.onmouseup = 
    this.layer.onmouseover = function (oEvent) {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") {
            oThis.textbox.value = oTarget.firstChild.nodeValue;
            oThis.hideSuggestions();
        } else if (oEvent.type == "mouseover") {
            oThis.highlightSuggestion(oTarget);
        } else {
            oThis.textbox.focus();
        }
    };

};

AutoSuggestControl.prototype.getLeft = function () {

    var oNode = this.textbox;
    var iLeft = 0;

    console.log(oNode.tagName);

    while(oNode != null && oNode.tagName != "BODY") {
        iLeft += oNode.offsetLeft;
        oNode = oNode.offsetParent; 
    }

    return iLeft;
};

AutoSuggestControl.prototype.getTop = function () {

    var oNode = this.textbox;
    var iTop = 0;

    while(oNode != null && oNode.tagName != "BODY") {
        iTop += oNode.offsetTop;
        oNode = oNode.offsetParent; 
    }

    return iTop;
};

AutoSuggestControl.prototype.showSuggestions = function (aSuggestions) {

    var oDiv = null;
    this.layer.innerHTML = "";

    for (var i=0; i < aSuggestions.length; i++) {
        oDiv = document.createElement("div");
        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
        this.layer.appendChild(oDiv);
    }
    var search = document.getElementById("search-box");
    this.layer.style.visibility="visible";
    this.layer.style.display="block";

    search.appendChild(this.layer);
};	

AutoSuggestControl.prototype.autosuggest = function (aSuggestions) {

    if (aSuggestions.length > 0) {
        this.showSuggestions(aSuggestions);
    } else {
        this.hideSuggestions();
    }
};

AutoSuggestControl.prototype.handleKeyDown = function (oEvent) {
    switch(oEvent.keyCode) {
        case 38: //up arrow
            this.previousSuggestion();
            break;
        case 40: //down arrow 
            this.nextSuggestion();
            break;
        case 13: //enter
            this.hideSuggestions();
            break;
    }
};

AutoSuggestControl.prototype.nextSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

AutoSuggestControl.prototype.previousSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur > 0) {
        var oNode = cSuggestionNodes[--this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

AutoSuggestControl.prototype.init = function () {

    var oThis = this;

    this.textbox.onkeyup = function (oEvent) {
        if (!oEvent) {
            oEvent = window.event;
        } 

        oThis.handleKeyUp(oEvent);
    };

    this.textbox.onkeydown = function (oEvent) {

        if (!oEvent) {
            oEvent = window.event;
        } 

        oThis.handleKeyDown(oEvent);
    };

    this.textbox.onblur = function () {
        oThis.hideSuggestions();
    };

    this.createDropDown();
};

AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {

    var iKeyCode = oEvent.keyCode;

    if (iKeyCode == 8 || iKeyCode == 46) {
        this.provider.requestSuggestions(this);

    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
        //ignore
    } else {
        this.provider.requestSuggestions(this);
    }
};




function StateSuggestions() {
     this.states = [
        "Alabama", "Alaska", "Arizona", "Arkansas",
        "California", "Colorado", "Connecticut",
        "Delaware", "Florida", "Georgia", "Hawaii",
        "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana",
        "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", 
        "Mississippi", "Missouri", "Montana",
        "Nebraska", "Nevada", "New Hampshire", "New Mexico", "New York",
        "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", 
        "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", 
        "Washington", "West Virginia", "Wisconsin", "Wyoming" 
    ];
}

function SuggestionProvider() {
    //any initializations needed go here
}

StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl) {
    var aSuggestions = [];
    var text = oAutoSuggestControl.textbox.value;
    //update here
    var request = new XMLHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4 && request.status == 200) {
            //put parsing in here
            var xmlMsg = request.responseXML;
            var suggestions = xmlMsg.getElementsByTagName("suggestion");
            for (i = 0; i < suggestions.length; i++){
                aSuggestions.push(suggestions[i].getAttribute("data"));
            }

            oAutoSuggestControl.autosuggest(aSuggestions);
        }
        else{
            oAutoSuggestControl.hideSuggestions();
        }
    };
    var url = "/eBay/suggest?q=" + text;
    request.open("GET", url, true);
    request.send();
};
