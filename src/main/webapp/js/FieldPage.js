(function () {
  "use strict";

  var countInput = document.getElementById("numMembers");
  var memberList = document.getElementById("memberList");

  function renderInvitationFields() {
    var count = Math.max(2, Math.min(12, Number(countInput.value) || 2));
    countInput.value = count;
    memberList.innerHTML = "";

    for (var index = 1; index < count; index += 1) {
      var field = document.createElement("div");
      field.className = "field";

      var label = document.createElement("label");
      label.htmlFor = "member" + index;
      label.textContent = "Invitation " + index;

      var input = document.createElement("input");
      input.id = "member" + index;
      input.name = "member" + index;
      input.type = "email";
      input.maxLength = 254;
      input.required = true;
      input.autocomplete = "off";
      input.placeholder = "person@example.com";

      field.appendChild(label);
      field.appendChild(input);
      memberList.appendChild(field);
    }
  }

  countInput.addEventListener("change", renderInvitationFields);
  countInput.addEventListener("input", renderInvitationFields);
  renderInvitationFields();
}());
