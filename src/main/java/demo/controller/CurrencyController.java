package demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.Currency;
import demo.service.CurrencyService;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;

	// 取得所有貨幣
	@GetMapping
	public List<Currency> getAllCurrencies() {
		return currencyService.getAllCurrencies();
	}

	// 創建貨幣（若幣別代碼已存在，先刪除再新增）
	//我先設定貨幣為唯一值來做下面的API，實際需求需要討論
	@PostMapping
	public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
	    Currency savedCurrency = currencyService.saveOrUpdateCurrency(currency);
	    return ResponseEntity.ok(savedCurrency);
	}

	// 更新貨幣
	@PutMapping("/{code}")
	public ResponseEntity<Currency> updateCurrencyByCode(@PathVariable String code, @RequestBody Currency currency) {
	    Currency updatedCurrency = currencyService.updateCurrencyByCode(code, currency);
	    if (updatedCurrency == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(updatedCurrency);
	}

	// 刪除貨幣
	@DeleteMapping("/{code}")
	public ResponseEntity<Void> deleteCurrencyByCode(@PathVariable String code) {
	    boolean deleted = currencyService.deleteCurrencyByCode(code);
	    if (deleted) {
	        return ResponseEntity.noContent().build();
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	// 呼叫 Coindesk API 並回傳原始資料
	@GetMapping("/raw")
	public ResponseEntity<Map<String, Object>> getCoindeskRawData() {
		Map<String, Object> data = currencyService.fetchCoindeskRawData();
		return ResponseEntity.ok(data);
	}

	// 用於從 Coindesk API 抓取資料並保存，組成新 API。
	@GetMapping("/coindeskFetch")
	public ResponseEntity<List<Map<String, Object>>> fetchAndSaveCurrencies() {
		List<Map<String, Object>> res = currencyService.fetchAndSaveCurrencies();
		return ResponseEntity.ok(res);
	}

}