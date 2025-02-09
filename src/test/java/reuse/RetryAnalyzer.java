package reuse;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 3; // Set the maximum number of retries

    @Override
    public boolean retry(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            if (retryCount < maxRetryCount) {
                retryCount++;
                return true; // Retry the test
            }
            return false; // Do not retry if the max retry count is reached
        }
        return false;
    }
}
