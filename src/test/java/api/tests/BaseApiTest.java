package api.tests;

import api.ApiTestConfig;
import common.Constants;
import helpers.TestFailureReportingExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Epic("LOCAL-SURVEY")
@Feature("api")
@ExtendWith({SpringExtension.class, TestFailureReportingExtension.class})
@ContextConfiguration(classes = ApiTestConfig.class)
@Tag(Constants.Tags.API)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseApiTest {
}
