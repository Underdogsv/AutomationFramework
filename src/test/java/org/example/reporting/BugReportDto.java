package org.example.reporting;

import java.util.List;

public record BugReportDto(
        String summary,
        String description,
        List<String> stepsToReproduce,
        String expected,
        String actual
) {
}
