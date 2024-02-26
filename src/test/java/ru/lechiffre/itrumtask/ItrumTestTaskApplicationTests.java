//package ru.lechiffre.itrumtask;
//
//import groovy.json.JsonBuilder;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ItrumTestTaskApplicationTests {
//
//	@Autowired
//	MockMvc mockMvc;
//
//	@Test
//	void depositOneDollar() throws Exception {
//		UUID id = UUID.randomUUID();
//
//		JSONObject depositOneDollarRequest = new JSONObject();
//		depositOneDollarRequest.put("walletId", id);
//		depositOneDollarRequest.put("operationType", "DEPOSIT");
//		depositOneDollarRequest.put("amount", 1);
//
//
//		var requestBuilder = MockMvcRequestBuilders.post("/api/v1/wallet")
//				.content(depositOneDollarRequest.toString());
//
//		this.mockMvc.perform(requestBuilder)
//				.andExpect(content().string("The account with specified requisites has been successfully created and replenished."));
//	}
//
//}
