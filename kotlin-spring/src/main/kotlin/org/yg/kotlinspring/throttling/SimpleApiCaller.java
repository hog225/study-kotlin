package org.yg.kotlinspring.throttling;

import io.github.bucket4j.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SimpleApiCaller {
    private final Bucket bucket;

    public SimpleApiCaller() {
        // API 호출 제한을 정의합니다. 예제에서는 초당 10개의 호출로 제한합니다.
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofSeconds(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    public CompletableFuture<ApiResponse> callApi() {
        CompletableFuture<ApiResponse> future = new CompletableFuture<>();
        // API 호출 전에 bucket에서 토큰을 소비하고, 제한을 초과한 경우 대기합니다.
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            // 제한 내에서 토큰이 소비되었으므로 API 호출을 수행합니다.
            return performApiCall();
        } else {
            // 제한을 초과하여 대기해야 합니다. 대기 시간을 계산하고, 대기 후에 API 호출을 수행합니다.
            long waitTimeMillis = probe.getNanosToWaitForRefill() / 1_000_000;
            CompletableFuture.delayedExecutor(waitTimeMillis, TimeUnit.MILLISECONDS)
                    .execute(() -> {
                        performApiCall().thenAccept(future::complete);
                    });
        }
        return future;
    }

    private CompletableFuture<ApiResponse> performApiCall() {
        CompletableFuture<ApiResponse> future = new CompletableFuture<>();

        // 실제 API 호출을 수행하는 비동기 작업을 여기에 구현합니다.
        // 예를 들어, 외부 API와의 통신 등을 수행하고 결과를 future에 담아 완료합니다.
        // 아래는 임의로 1초 대기 후에 완료되는 예시입니다.
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS)
                .execute(() -> {
                    ApiResponse response = new ApiResponse("API 호출 결과");
                    future.complete(response);
                });

        return future;
    }

    public static void main(String[] args) {
        SimpleApiCaller apiCaller = new SimpleApiCaller();

        // 15개의 API 호출을 예약합니다.
        for (int i = 1; i <= 15; i++) {
            int requestNumber = i;
            apiCaller.callApi()
                    .thenAccept(response -> System.out.println("Request #" + requestNumber + ": " + response.getResult()));
        }

        // 프로그램을 종료하지 않고 대기합니다.
        try {
            Thread.sleep(5000); // 대기 시간을 5초로 설정합니다. 필요한 만큼 조절하십시오.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class ApiResponse {
        private final String result;

        public ApiResponse(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }
}
