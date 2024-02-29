//package ru.lechiffre.itrumtask;
//
//import com.github.javafaker.Faker;
//import net.minidev.json.JSONObject;
//import org.apache.http.entity.ContentType;
//import org.apache.jmeter.protocol.http.util.HTTPConstants;
//import org.junit.jupiter.api.Test;
//import us.abstracta.jmeter.javadsl.core.TestPlanStats;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Random;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

//public class LoadTestApp {
//    class FakeGenerator {
//        private static Faker faker = new Faker();
//        private static String operationType = new Random().nextBoolean() ? "DEPOSIT" : "WITHDRAW";
//        public static String buildRequestBody() {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("walletId", UUID.randomUUID());
//            jsonObject.put("operationType", operationType);
//            jsonObject.put("amount", faker.random().nextDouble());
//            return jsonObject.toJSONString();
//        }
//    }
//
//    private static final Integer THREADS = 1000;
//    private static final Integer ITERATIONS = 10;
//
//    @Test
//    public void saveAndGetMeasurementsLoadTest() throws IOException {
//        TestPlanStats stats = testPlan(
//                threadGroup(THREADS, ITERATIONS,
//
//                        httpSampler("http://localhost:8080/api/v1/wallet")
//                                .method(HTTPConstants.POST)
//                                .post(s -> FakeGenerator.buildRequestBody(), ContentType.APPLICATION_JSON)
//                                .children(jsonExtractor("walletIdVariable", "walletId"))
//                                .children(jsonAssertion("dateTime")),
//
//                        httpSampler("http://localhost:8080/api/v1/wallets/{WALLET_UUID}")
//                                .method(HTTPConstants.GET)
//                                .contentType(ContentType.APPLICATION_JSON)
//                                .children(jsonAssertion("[*].walletId"))
//
//                ), influxDbListener("http://localhost:8086/write?db=jmeter")
//                        .measurement("jmeter")
//                        .application("jmeter")
//                        .token("TOKEN from influxDb")
//        ).run();
//
//        assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
//    }
//}