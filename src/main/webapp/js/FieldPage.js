function addMembers() {
	var numMembers = document.getElementById("numMembers").value;
	var list = document.getElementById("memberList");
	while(list.hasChildNodes()) {
		list.removeChild(list.lastChild);
    }
	
	list.appendChild(document.createElement("br"));
	for (k=0; k<numMembers; k++) {		
		var memberDiv = document.createElement("div");
		memberDiv.classList.add("inputField");
		var newMember = document.createElement("input");
		newMember.classList.add("inputType");
		newMember.classList.add("validateInput");
		newMember.type = "text";
		var tmp = k + 1;
		newMember.id = "member"+tmp;
		// newMember.name = "member" + tmp + "@gmail.com";
		// newMember.placeholder = newMember.name;
		memberDiv.appendChild(newMember);
		list.appendChild(memberDiv);
     }
}
