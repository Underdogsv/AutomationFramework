package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.logging.Logger;

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
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(buildIssuePayload(report, projectKey));
        if (dryRun) {
            LOG.info("JIRA_DRY_RUN:\n" + json);
            return "DRY-RUN";
        }
        throw new UnsupportedOperationException("Configure Jira env vars for live publish");
    }

    ObjectNode buildIssuePayload(BugReportDto report, String projectKey) {
        ObjectNode root = mapper.createObjectNode();
        ObjectNode fields = root.putObject("fields");
        fields.putObject("project").put("key", projectKey);
        fields.put("summary", report.summary());
        fields.putObject("issuetype").put("name", "Bug");
        fields.put("description", report.description());
        fields.putArray("labels").add("ai-generated");
        return root;
    }
}
