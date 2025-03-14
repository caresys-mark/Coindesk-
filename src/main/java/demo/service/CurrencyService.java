package demo.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import demo.dto.CurrencyDTO;
import demo.entity.Currency;
import demo.repository.CurrencyRepository;

@Service
public class CurrencyService {

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${coindesk.api.url}")
	private String COINDESK_API_URL;

	// 取得所有貨幣
	public List<Currency> getAllCurrencies() {
		return currencyRepository.findAll();
	}

	// 創建貨幣
	public Currency saveOrUpdateCurrency(Currency currency) {
		Currency existingCurrency = currencyRepository.findByCode(currency.getCode());
		if (existingCurrency != null) {
			currencyRepository.delete(existingCurrency);
		}
		return currencyRepository.save(currency);
	}

	// 更新貨幣資料
	public Currency updateCurrencyByCode(String code, Currency currency) {
		Currency existingCurrency = currencyRepository.findByCode(code);
		if (existingCurrency == null) {
			return null;
		}

		existingCurrency.setUpdated(currency.getUpdated());
		existingCurrency.setUpdatedIso(currency.getUpdatedIso());
		existingCurrency.setUpdatedUk(currency.getUpdatedUk());
		existingCurrency.setDisclaimer(currency.getDisclaimer());
		existingCurrency.setChartName(currency.getChartName());
		existingCurrency.setSymbol(currency.getSymbol());
		existingCurrency.setRate(currency.getRate());
		existingCurrency.setRateFloat(currency.getRateFloat());
		existingCurrency.setDescription(currency.getDescription());

		return currencyRepository.save(existingCurrency);
	}

	// 刪除貨幣
	public boolean deleteCurrencyByCode(String code) {
		Currency existingCurrency = currencyRepository.findByCode(code);
		if (existingCurrency != null) {
			currencyRepository.delete(existingCurrency);
			return true;
		}
		return false;
	}
	
	//Coindesk API 資料抓取
	public Map<String, Object> fetchCoindeskRawData() {
		ResponseEntity<Map> response = restTemplate.exchange(COINDESK_API_URL, HttpMethod.GET, null, Map.class);
		return response.getBody();
	}

	
	// 用於從 Coindesk API 抓取資料並保存，組成新 API。
	public List<Map<String, Object>> fetchAndSaveCurrencies() throws ParseException {
		ResponseEntity<Map> response = restTemplate.exchange(COINDESK_API_URL, HttpMethod.GET, null, Map.class);
		Map<String, Object> coindeskData = response.getBody();
	    if (coindeskData == null) {
	        return Collections.emptyList();
	    }

	    String chartName = (String) coindeskData.get("chartName");
	    String disclaimer = (String) coindeskData.get("disclaimer");
	    Map<String, Object> time = (Map<String, Object>) coindeskData.get("time");
	    String updated = (String) time.get("updated");
	    String updatedIso = (String) time.get("updatedISO");
	    String updatedUk = (String) time.get("updateduk");

	    Map<String, Object> bpi = (Map<String, Object>) coindeskData.get("bpi");

	    return processBpiData(bpi, chartName, disclaimer, updated, updatedIso, updatedUk);
	}

	private List<Map<String, Object>> processBpiData(Map<String, Object> bpi, String chartName, String disclaimer, String updated, String updatedIso, String updatedUk) throws ParseException {
	    return bpi.values().stream().map(obj -> {
	        Map<String, Object> currencyData = (Map<String, Object>) obj;
	        try {
	            CurrencyDTO currencyDTO = CurrencyDTO.fromCurrencyData(currencyData, chartName, disclaimer, updated, updatedIso, updatedUk);
	            return createCurrencyInfo(currencyDTO);
	        } catch (ParseException | java.text.ParseException e) {
	            e.printStackTrace();
	            return null;
	        } 
	    }).collect(Collectors.toList());
	}

	private Map<String, Object> createCurrencyInfo(CurrencyDTO currencyDTO) {
	    Map<String, Object> info = new HashMap<>();
	    info.put("code", currencyDTO.getCode());
	    info.put("chartName", currencyDTO.getChartName());
	    info.put("rate", currencyDTO.getRate());
	    info.put("updatedIso", currencyDTO.getUpdatedIso());
	    info.put("updatedUk", currencyDTO.getUpdatedUk());
	    info.put("currentTime", currencyDTO.getUpdated());
	    return info;
	}


}
