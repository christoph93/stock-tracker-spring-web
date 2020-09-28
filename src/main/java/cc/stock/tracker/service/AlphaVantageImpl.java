package cc.stock.tracker.service;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
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

	public TreeMap<String, Double> getClosginPrices(String symbol) throws Exception  {
		TreeMap<String, Double> closingPrices = new TreeMap<String, Double>();

		String urlString = String.format(urlPattern, timeSeries, symbol + ".SAO", outputSize, apiKey);
//		System.out.println(urlString);

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.getForObject(urlString, String.class);
		JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
		
		if(!jsonObject.has("Meta Data")){			
			throw new Exception("Response did not contain Meta Data. Returning");
        }

//		System.out.println(jsonObject.toString());

		JsonObject dailyPrices = jsonObject.getAsJsonObject(timeSeriesString);

		dailyPrices.keySet().forEach(key -> {
			closingPrices.put(key, dailyPrices.get(key).getAsJsonObject().get("4. close").getAsDouble());

		});

		return closingPrices;

	}

}
