# API Contract (Mock — WireMock)

Base URL: `${api.baseUrl}` (set by `WireMockExtension` at runtime)

## POST `/v1/profile/{profileId}/vod-preferences`

Saves genre and movie preferences for a profile.

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
| `genreIds` | `int[]` | Required unless `skipped=true`. Min 3 items when not skipped. |
| `movieIds` | `int[]` | Optional when skipped; expected 5 items on happy path. |
| `skipped` | `boolean` | `true` → default recommendations, no genre minimum. |

### Responses

| Status | Condition | Body |
|--------|-----------|------|
| 200 | Valid payload | `{"profileId":"…","saved":true,"skipped":false}` |
| 400 | `< 3` genres and not skipped | `{"error":"MIN_GENRES_REQUIRED","message":"At least 3 genres required"}` |
| 404 | Unknown `profileId` | `{"error":"PROFILE_NOT_FOUND"}` |

## GET `/v1/profile/{profileId}/recommendations`

Returns recommendation list for profile.

### Response 200

```json
{
  "source": "personalized",
  "itemIds": [101, 102, 103, 104, 105]
}
```

| `source` | When |
|----------|------|
| `personalized` | After successful preferences save (not skipped) |
| `default` | After skip or new profile without preferences |
