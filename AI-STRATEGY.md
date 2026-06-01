# AI-STRATEGY.md — Scaling to start-screen ads regression

## Core idea

The **profile survey** in this repo is a **reference implementation** of an AI-Driven Test Factory:

```
Jira AC → test-cases.json → WireMock + Java tests → (on fail) AI → Jira
```

The same pipeline applies to **internal ads popups** on site open.

## Survey → Ads analogy

| Survey (reference) | Start-screen ads |
|--------------------|------------------|
| One-time onboarding | Frequency cap / segment |
| Min 3 genres → Next | CTA eligibility rules |
| POST vod-preferences | POST impression / click / dismiss |
| Recommendations change | Offer / deeplink applied |
| Skip → default | Close → snooze |

## Creative taxonomy

| creativeType | API focus | UI smoke (logic only) |
|--------------|-----------|------------------------|
| movie_promo | targeting, deeplink | CTA enabled after load |
| series_promo | season metadata | correct contentId |
| partner_integration | URL whitelist, UTM | external link allowed |
| subscription_discount | promo eligibility | CTA disabled without auth |

Shared cases: frequency cap, campaign priority, dismiss, expired campaign.

## Context pack (per generation)

- `product-rules.md` — caps, segments
- `api-catalog.md` — eligible, impression, click, dismiss
- `tms-schema.json` — TestRail fields
- `anti-patterns.md` — no layout/color tests
- `reference/` — this repo’s survey as few-shot

## Generation pipeline (80% AI / 20% review)

1. Creative Spec (1 page) per ad type
2. MCP: read Jira epic/story → extract AC
3. Prompt → `test-cases.json` with `jiraKey`, `acRef`
4. Prompt → WireMock mappings + JUnit (API `@Tag("ads-movie")`)
5. One mock UI smoke per type (POM + MockView)
6. Human review: legal URLs, segment rules, flaky async

## Stability rules

- Human owns: API clients, WireMock extension, Page Objects (`data-testid`)
- AI owns: bulk cases and test method bodies
- Assert business fields, not DOM structure
- Version prompts under `prompts/v1/`

## Bidirectional Jira

| Direction | Tool |
|-----------|------|
| In | Atlassian Rovo MCP → cases |
| Out | Java dry-run / MCP create Bug with `testCaseId` |

## Coverage matrix (target)

Rows: creative types. Columns: show, click, dismiss, frequency, conflict, negative API.

~8–12 API cases × 4 types; generate **per-type prompts**, not one monolith.

## Limits

- AI does not replace legal/partner review
- Mock contract = source of truth without staging
- Visual A/B banners out of scope
