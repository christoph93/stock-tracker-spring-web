package cc.stock.tracker.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class AlphaVantageImpl implements AlphaVantage {

	@Value("${cc.stocktrackerapi.alphavantage.urlPattern}")
	private String urlPattern;

	@Value("${cc.stocktrackerapi.alphavantage.apiKey}")
	private String apiKey;

	@Value("${cc.stocktrackerapi.alphavantage.timeSeries}")
	private String timeSeries;

	@Value("${cc.stocktrackerapi.alphavantage.timeSeriesString}")
	private String timeSeriesString;

	@Value("${cc.stocktrackerapi.alphavantage.outputSize}")
	private String outputSize;

	public LinkedHashMap<String, Double> getClosginPrices(String symbol) {
		LinkedHashMap<String, Double> closingPrices = new LinkedHashMap<String, Double>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String urlString = String.format(urlPattern, timeSeries, symbol, outputSize, apiKey);

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.getForObject(urlString, String.class);
		JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);

		JsonObject dailyPrices = jsonObject.getAsJsonObject(timeSeriesString);

		dailyPrices.keySet().forEach(key -> {
			closingPrices.put(key, dailyPrices.get(key).getAsJsonObject().get("4. close").getAsDouble());

		});

		System.out.println(closingPrices);

		return closingPrices;

	}

}
