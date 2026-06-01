# API Contract (Mock — WireMock)

Base URL: from `configs.ApiProperties` → running `WireMockServer` (dynamic port, set via Spring in `api.ApiTestConfig` / `e2e.E2eTestConfig`).

Mock profiles (fixed IDs in stubs):

| `profileId` | Usage |
|-------------|--------|
| `profile-new-created` | `POST /v1/profile` + onboarding scenario (AC-1) |
| `profile-recommendation-scenario` | AC-6 scenario: default → personalized after save |
| `profile-personalized` | Static GET prefs/recs (legacy stub) |
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
  "itemIds": [101, 102, 103, 104, 105],
  "basedOnGenreIds": [1, 2, 3]
}
```

| `source` | When (mock) |
|----------|-------------|
| `default` | Before preferences saved on `profile-recommendation-scenario` (scenario **Started**) |
| `personalized` | After valid `POST .../vod-preferences` on same profile (scenario **PreferencesSaved**) |
| `personalized` | Static stub for `profile-personalized` (legacy shortcut — not used for AC-6 proof test) |
| `default` | `profile-default` after skip, or generic fallback profile |

WireMock scenario `RecommendationsPersonalization` on `profile-recommendation-scenario`:

1. Initial GET → `source=default`, `itemIds=[1,2,3,4,5]`
2. POST with ≥3 `genreIds` and 5 `movieIds` → `willSetStateTo(PreferencesSaved)`
3. Subsequent GET → `source=personalized`, selected movie IDs, `basedOnGenreIds=[1,2,3]`

Implementation: `api.tests.RecommendationsApiTest.savePreferences_shouldChangeRecommendationsFromDefaultToPersonalized`.
