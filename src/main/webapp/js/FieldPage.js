function addMembers() {
	var numMembers = document.getElementById("numMembers").value;
	var list = document.getElementById("memberList");
	while(list.hasChildNodes()) {
		list.removeChild(list.lastChild);
    }
	
	list.appendChild(document.createElement("br"));
	for (k=0; k<numMembers - 1; k++) {
		var memberDiv = document.createElement("div");
		memberDiv.classList.add("inputField");
		var newMember = document.createElement("input");
		newMember.classList.add("inputType");
		newMember.classList.add("validateInput");
		newMember.type = "text";
		var tmp = k + 1;
		newMember.id = "member"+tmp;
		newMember.name = "member" + tmp;
		newMember.placeholder = "member" + tmp + "@anonymchat.kma";
		memberDiv.appendChild(newMember);
		list.appendChild(memberDiv);
     }
}
