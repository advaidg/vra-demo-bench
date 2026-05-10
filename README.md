# vra-demo-bench

A small, deliberately-vulnerable Spring/Maven project for demoing
[VRA](https://github.com/) — the Vulnerability Remediation Agent. Compiles,
runs `mvn test` cleanly, and exercises every VRA agent path:

| Vuln class | Where | What VRA should do |
|---|---|---|
| **CVE in declared dep** (5×) | `pom.xml` | Open a PR per dep with a real `<version>` bump |
| **SQL injection** (CWE-89) | `dao/UserDao.java#L29` | Replace string-concat with `PreparedStatement` |
| **Hardcoded API key** (CWE-798) | `auth/Auth.java#L8` | Replace literal with `System.getenv` |
| **Weak crypto MD5** (CWE-327) | `crypto/CryptoHelper.java#L18` | `MD5` → `SHA-256` |
| **Open redirect** (CWE-601) | `web/RedirectController.java#L12` | Add allow-list check |
| **Path traversal** (CWE-22) | `xml/PathReader.java#L19` | `Path.normalize` + base-dir containment |

## Build + test locally

```bash
mvn -B test
```

Should pass. The JUnit 5 tests pin behavior contracts (return types,
deterministic outputs, in-bounds path reads) without depending on the
specific vulnerable implementations — so once VRA fixes a class, the
tests still pass.

## CVEs in `pom.xml`

| Coordinate | Version | Notable CVE |
|---|---|---|
| `org.apache.commons:commons-text` | `1.9` | **CVE-2022-42889** — Text4Shell, RCE |
| `com.fasterxml.jackson.core:jackson-databind` | `2.13.4` | CVE-2022-42003 (DoS), CVE-2023-35116 |
| `org.yaml:snakeyaml` | `1.30` | **CVE-2022-1471** — unsafe constructor by default |
| `org.apache.logging.log4j:log4j-core` | `2.14.0` | **CVE-2021-44228** Log4Shell era |
| `org.springframework:spring-web` | `5.3.0` | CVE-2024-22243 etc. |
| `com.h2database:h2` | `1.4.200` | CVE-2022-23221 console RCE (test-scope) |

## Custom Semgrep rules

`.semgrep.yml` ships custom rules for the patterns Semgrep's stock packs
miss (Stripe-style hardcoded keys, `URI.create` open-redirect,
`Files.readString` path traversal). Combined with `p/java`, all 5 SAST
issues trigger.

## Onboard into VRA

```bash
# from the parent VRA repo, with backend running on :8847
curl -X POST http://localhost:8847/api/services/onboard \
  -H 'content-type: application/json' \
  -d '{"repo_url":"<your-fork>/vra-demo-bench","sources":["osv","trivy_cli","grype","semgrep"],"tier":2,"owner_team":"appsec"}'
```

Or via Mission Control: `+ Onboard real repo` → paste the slug.

## Push this repo to your GitHub

```bash
cd examples/vra-demo-bench
git init && git add . && git commit -m "init: deliberately vulnerable demo bench"
gh repo create vra-demo-bench --public --source=. --push
```

Then onboard `<your-username>/vra-demo-bench` in Mission Control.

## Expected VRA output

After onboarding (with `gh auth token` configured + push access on the
fork), expect:

- ~6 PRs from the SCA path — one per vulnerable coordinate (or grouped
  per the service's `batch_policy`)
- ~5 PRs from the SAST path — one per CWE class
- All PRs are real diffs against `pom.xml` or the relevant `.java` file
- `mvn test` continues to pass on each PR branch
