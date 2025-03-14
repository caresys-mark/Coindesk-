package demo.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	private ObjectMapper objectMapper;

    // 1.測試呼叫查詢幣別對應表資料API，並顯示其內容。
    @Test
    public void testGetCurrencyList() throws Exception {
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // 2.測試呼叫新增幣別對應表資料API。
    @Test
    public void testAddCurrency() throws Exception {
        Map<String, Object> currencyData = new HashMap<>();
        currencyData.put("code", "USD");
        currencyData.put("chartName", "美元");
        currencyData.put("rate", 1234.56);  

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyData)))
                .andExpect(status().isOk());
    }

    // 3.測試呼叫更新幣別對應表資料API，並顯示其內容。
    @Test
    public void testUpdateCurrency() throws Exception {
        String currencyCode = "USD"; 
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("chartName", "美金");
        updateData.put("rate", 999.99); 

        mockMvc.perform(put("/api/currencies/{code}", currencyCode) 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
               .andExpect(status().isOk());
    }

    // 4.測試呼叫刪除幣別對應表資料API。
    @Test
    public void testDeleteCurrency() throws Exception {
        String currencyCode = "USD";  

        mockMvc.perform(delete("/api/currencies/{code}", currencyCode)) 
               .andExpect(status().isOk());
    }

    // 5.測試呼叫coindesk API，並顯示其內容。
    @Test
    public void testCallCoindeskApi() throws Exception {
        mockMvc.perform(get("/api/currencies/raw"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // 6.測試呼叫資料轉換的API，並顯示其內容。 
    @Test
    public void testCallConvertApi() throws Exception {
        mockMvc.perform(get("/api/currencies/coindeskFetch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // 7.測試幣別資料不存在時的錯誤處理（例如更新不存在的幣別）
    @Test
    public void testUpdateCurrencyNotFound() throws Exception {
        String currencyCode = "INVALID";  
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("chartName", "不存在的幣別");
        updateData.put("rate", 0.00);

        mockMvc.perform(put("/api/currencies/{code}", currencyCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());  
    }

    // 8.測試刪除不存在的幣別資料
    @Test
    public void testDeleteCurrencyNotFound() throws Exception {
        String currencyCode = "INVALID";  // 用不存在的幣別代碼

        mockMvc.perform(delete("/api/currencies/{code}", currencyCode))
                .andExpect(status().isNotFound());  
    }
    
}
