(function () {
  "use strict";

  var roomsElement = document.getElementById("roomsDiv");
  var searchForm = document.getElementById("roomSearch");
  var searchInput = document.getElementById("searchLine");
  var resultLabel = document.getElementById("roomResultLabel");
  var notice = document.getElementById("lobbyNotice");

  function showNoticeFromUrl() {
    var error = new URLSearchParams(window.location.search).get("error");
    var messages = {
      full: "That room is at its live capacity. Try another room or come back shortly.",
      invalid: "That room or invitation is no longer available."
    };
    if (messages[error]) {
      notice.textContent = messages[error];
      notice.classList.add("is-visible");
    }
  }

  function fetchRooms(url) {
    roomsElement.innerHTML = '<div class="loading-state">Listening for open rooms…</div>';
    return fetch(url, {headers: {"Accept": "application/json"}})
      .then(function (response) {
        if (!response.ok) {
          throw new Error("Rooms unavailable");
        }
        return response.json();
      })
      .then(renderRooms)
      .catch(function () {
        roomsElement.innerHTML =
          '<div class="empty-state">The lobby cannot reach the room service right now. Try again shortly.</div>';
      });
  }

  function renderRooms(rooms) {
    roomsElement.innerHTML = "";
    if (!Array.isArray(rooms) || rooms.length === 0) {
      roomsElement.innerHTML =
        '<div class="empty-state">No rooms match that phrase. Open one and set the tone.</div>';
      return;
    }
    rooms.forEach(function (room) {
      roomsElement.appendChild(createRoomCard(room));
    });
  }

  function createRoomCard(room) {
    var article = document.createElement("article");
    article.className = "room-card";

    var top = document.createElement("div");
    top.className = "room-card-top";
    var live = document.createElement("span");
    live.className = "live-count";
    var count = Number(room.activeUsers) || 0;
    live.textContent = count + (count === 1 ? " person online" : " people online");
    top.appendChild(live);

    var heading = document.createElement("h3");
    heading.textContent = room.name || "Untitled room";
    var description = document.createElement("p");
    description.textContent = room.description || "No description. Walk in curious.";

    var tags = document.createElement("div");
    tags.className = "tag-list";
    (room.tags || []).slice(0, 3).forEach(function (value) {
      var tag = document.createElement("span");
      tag.className = "tag";
      tag.textContent = value;
      tags.appendChild(tag);
    });

    var enter = document.createElement("button");
    enter.type = "button";
    enter.className = "room-card-button";
    enter.textContent = "Enter " + heading.textContent;
    enter.addEventListener("click", function () {
      var url = new URL("/ChatRoom", window.location.origin);
      url.searchParams.set("chatId", room.id);
      window.location.assign(url.toString());
    });

    article.appendChild(top);
    article.appendChild(heading);
    article.appendChild(description);
    article.appendChild(tags);
    article.appendChild(enter);
    return article;
  }

  searchForm.addEventListener("submit", function (event) {
    event.preventDefault();
    var query = searchInput.value.trim();
    if (!query) {
      resultLabel.textContent = "Recently active public conversations.";
      fetchRooms("/topRooms");
      return;
    }
    resultLabel.textContent = 'Rooms matching “' + query + '”.';
    fetchRooms("/SearchServlet?line=" + encodeURIComponent(query));
  });

  showNoticeFromUrl();
  fetchRooms("/topRooms");
}());
