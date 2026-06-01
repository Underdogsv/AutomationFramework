package org.example.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * Publishes bugs to Jira REST API v3. Default: dry-run logs payload only.
 */
public class JiraIssuePublisher {
    private static final Logger LOG = Logger.getLogger(JiraIssuePublisher.class.getName());
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final boolean dryRun;
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

    public JiraIssuePublisher() {
        this(Boolean.parseBoolean(System.getProperty("jira.dryRun", "true")));
    }

    public JiraIssuePublisher(boolean dryRun) {
        this.dryRun = dryRun;
        this.httpClient = new OkHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String publish(BugReportDto report, String projectKey) throws IOException {
        ObjectNode body = buildIssuePayload(report, projectKey);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);

        if (dryRun) {
            LOG.info("JIRA_DRY_RUN issue payload:\n" + json);
            return "DRY-RUN";
        }

        String baseUrl = System.getenv("JIRA_BASE_URL");
        String email = System.getenv("JIRA_EMAIL");
        String token = System.getenv("JIRA_API_TOKEN");
        if (baseUrl == null || email == null || token == null) {
            throw new IllegalStateException("Set JIRA_BASE_URL, JIRA_EMAIL, JIRA_API_TOKEN or use jira.dryRun=true");
        }

        String auth = Base64.getEncoder().encodeToString((email + ":" + token).getBytes());
        Request request = new Request.Builder()
                .url(baseUrl + "/rest/api/3/issue")
                .addHeader("Authorization", "Basic " + auth)
                .post(RequestBody.create(json, JSON))
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Jira API error: " + response.code() + " " + response.message());
            }
            return response.body() != null ? response.body().string() : "";
        }
    }

    ObjectNode buildIssuePayload(BugReportDto report, String projectKey) {
        ObjectNode root = mapper.createObjectNode();
        ObjectNode fields = root.putObject("fields");
        ObjectNode project = fields.putObject("project");
        project.put("key", projectKey);
        fields.put("summary", report.summary());
        ObjectNode issueType = fields.putObject("issuetype");
        issueType.put("name", "Bug");
        fields.put("description", report.description());
        fields.putArray("labels").add("ai-generated").add("regression");
        return root;
    }
}
