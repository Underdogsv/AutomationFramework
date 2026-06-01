# API Contract (Mock — WireMock)

Base URL: from `configs.ApiProperties` → running `WireMockServer` (dynamic port, set via Spring in `api.ApiTestConfig` / `e2e.E2eTestConfig`).

Mock profiles (fixed IDs in stubs):

| `profileId` | Usage |
|-------------|--------|
| `profile-new-created` | `POST /v1/profile` + onboarding scenario (AC-1) |
| `profile-personalized` | Saved preferences + personalized recommendations |
| `profile-default` | Skip flow → default recommendations |
| `profile-unknown` | 404 on vod-preferences |

---

## POST `/v1/profile`

Creates a new profile (AC-1).

### Response 200

```json
{
  "profileId": "profile-new-created",
  "surveyRequired": true
}
```

---

## GET `/v1/profile/{profileId}/onboarding-status`

Whether the interest survey must be shown.

| `surveyRequired` | Mock behaviour |
|------------------|----------------|
| `true` | Before preferences saved (`SurveyOnboarding` scenario **Started**) |
| `false` | After successful `POST .../vod-preferences` (scenario **SurveyCompleted**) |

Repeated GET after completion stays `false`.

### Response 200

```json
{
  "profileId": "profile-new-created",
  "surveyRequired": true
}
```

---

## POST `/v1/profile/{profileId}/vod-preferences`

Saves genre and movie preferences (AC-5).

### Request body

```json
{
  "genreIds": [1, 2, 3],
  "movieIds": [101, 102, 103, 104, 105],
  "skipped": false
}
```

| Field | Type | Rules |
|-------|------|--------|
| `genreIds` | `int[]` | Required unless `skipped=true`. Min 3 when not skipped. |
| `movieIds` | `int[]` | Optional when skipped; 5 items on happy path. |
| `skipped` | `boolean` | `true` → default recommendations, no genre minimum. |

### Responses

| Status | Condition | Body |
|--------|-----------|------|
| 200 | Valid payload (≥3 genres) | `{"profileId":"…","saved":true,"skipped":false}` |
| 200 | `skipped=true` | `{"profileId":"…","saved":true,"skipped":true}` |
| 400 | &lt;3 genres, not skipped | `{"error":"MIN_GENRES_REQUIRED","message":"At least 3 genres required"}` |
| 404 | Unknown `profileId` | `{"error":"PROFILE_NOT_FOUND"}` |

Stub priority in `api.WireMockStubs`: skip (5) → min 3 genres (10) → 400 fallback (20).

---

## GET `/v1/profile/{profileId}/vod-preferences`

Returns saved preferences (mock). Implemented for `profile-personalized` in POC.

### Response 200 (example)

```json
{
  "profileId": "profile-personalized",
  "saved": true,
  "genreIds": [1, 2, 3],
  "movieIds": [101, 102, 103, 104, 105]
}
```

Tests should verify exact IDs after POST (WireMock `verify` + this GET). See `VodPreferencesApiTest.savePreferences_withThreeGenres_returns200`.

---

## GET `/v1/profile/{profileId}/recommendations`

Recommendation list (AC-6).

### Response 200

```json
{
  "source": "personalized",
  "itemIds": [101, 102, 103, 104, 105]
}
```

| `source` | When (mock) |
|----------|-------------|
| `personalized` | `profile-personalized` or after valid save chain |
| `default` | `profile-default` after skip, or generic fallback profile |

Implementation: `api.tests.RecommendationsApiTest`.
