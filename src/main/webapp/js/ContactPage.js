(function () {
  "use strict";
  if (new URLSearchParams(window.location.search).get("sent") === "1") {
    document.getElementById("sentMessage").classList.add("is-visible");
  }
}());
