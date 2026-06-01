package org.example.api;

import java.util.List;

public record VodPreferencesPayload(List<Integer> genreIds, List<Integer> movieIds, boolean skipped) {
    public static VodPreferencesPayload withGenresAndMovies(List<Integer> genreIds, List<Integer> movieIds) {
        return new VodPreferencesPayload(genreIds, movieIds, false);
    }

    public static VodPreferencesPayload skip() {
        return new VodPreferencesPayload(List.of(), List.of(), true);
    }
}
