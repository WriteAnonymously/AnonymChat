# AnonymChat

AnonymChat is a small, account-free room chat. A visitor enters under a
temporary room-specific name, talks over a WebSocket connection, and can create
either:

- a public room that appears in the searchable lobby; or
- a private room whose guests receive different, one-time email invitations.

Messages are stored in MySQL. The lobby's `people online` value is the number of
distinct guests with a live WebSocket in that room right now. It is not a
database user total, a room id, or a fabricated popularity value. Room capacity
uses that same live count.

## Stack and request flow

- Java 17, Servlets, and Jakarta-compatible Tomcat 9 (`javax.*` APIs)
- Native WebSockets at `/The_Chat`
- MySQL 8.4 with Apache DBCP connection pooling
- Gson for the HTTP/WebSocket JSON payloads
- SMTP for private invitations and the contact form
- Plain HTML, CSS, and JavaScript—no frontend build step

```text
Browser
  ├─ HTTP pages/forms ──> Tomcat servlets ──> MySQL
  ├─ WebSocket ─────────> ChatEndpoint ─────> MySQL + room broadcast
  └─ room creation ─────> SMTP ─────────────> one-time invite email
```

The room id still exists internally because URLs and database relationships need
a stable key, but the interface does not present it as a member or popularity
number.

## Run locally

Docker Desktop (or Docker Engine with Compose) is the only prerequisite.

```bash
cp .env.example .env
docker compose up --build
```

Then open:

- App: <http://localhost:8080>
- Mailpit inbox: <http://localhost:8025>
- Health check: <http://localhost:8080/health>

Mailpit captures local invitation and contact emails. Stop the stack with
`docker compose down`. MySQL data remains in the `anonymchat-db` volume.
`docker compose down -v` intentionally deletes that local data.

## Configuration

All deploy-time settings are environment variables. `.env.example` documents
the complete list.

| Variable | Purpose |
| --- | --- |
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | MySQL JDBC connection |
| `APP_BASE_URL` | Public HTTPS origin used in invitation links |
| `ALLOWED_ORIGIN` | Exact origin allowed for WebSocket handshakes |
| `SMTP_HOST`, `SMTP_PORT` | SMTP server |
| `SMTP_USERNAME`, `SMTP_PASSWORD` | SMTP credentials, if required |
| `SMTP_FROM` | From address for outgoing mail |
| `SMTP_STARTTLS` | Enable SMTP STARTTLS |
| `CONTACT_EMAIL` | Destination for contact-form mail |

Do not commit a production `.env`. The file is ignored by Git.

## Build and test

The image build creates a ROOT WAR and installs it in Tomcat:

```bash
docker compose build app
```

To run the unit suite without installing Maven locally:

```bash
docker run --rm \
  -v "$PWD":/workspace \
  -w /workspace \
  maven:3.9-eclipse-temurin-17 \
  mvn -B test
```

JavaScript syntax can be checked with:

```bash
find src/main/webapp/js -name '*.js' -print0 |
  xargs -0 -n1 node --check
```

## Production deployment

Deploy the built `app` image to a container host and provide a persistent MySQL
database plus a real SMTP service. A typical single-instance setup is:

1. Create the database from `src/main/java/DB/db.sql`.
2. Set strong database credentials and all variables listed above.
3. Set `APP_BASE_URL` and `ALLOWED_ORIGIN` to the exact public HTTPS origin,
   such as `https://chat.example.com`.
4. Put the app behind a TLS reverse proxy or load balancer and forward both
   ordinary HTTP and WebSocket upgrade headers to port `8080`.
5. Use `/health` for readiness/liveness checks.
6. Back up MySQL and retain application/Tomcat logs.

The current live-presence registry is in process memory. Run one application
instance so counts and broadcasts remain consistent. Horizontal scaling needs a
shared presence and pub/sub layer (for example Redis) before adding replicas.

The bundled Compose file is development-oriented: it exposes Mailpit, uses local
fallback passwords, and lets MySQL initialize from `db.sql`. Do not use those
defaults as production secrets.

## Data and privacy behavior

- No accounts or profile records are created.
- Temporary guest names and messages are retained in MySQL.
- Public rooms remain discoverable until their records are removed.
- Private invitation tokens are UUIDs, are different per seat, and can be
  claimed only once. A claimed link works again only in the session that claimed
  it.
- There is currently no automated message/room retention job. Define and
  implement a retention policy before presenting the service as ephemeral.
