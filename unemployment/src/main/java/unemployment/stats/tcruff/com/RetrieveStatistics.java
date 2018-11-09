package unemployment.stats.tcruff.com;

import java.io.IOException;


import org.springframework.http.HttpStatus;

import data.unemployment.stats.tcruff.com.FetchUnemploymentData;

public class RetrieveStatistics {
	private String apikey = "", year = "", period = "";
	private long value = 0;
	
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public RetrieveStatistics(RetrieveStatistics retrieveStatistics, HttpStatus ok) {

		value = FetchUnemploymentData.getUnemploymentData(apikey, year, period);

	}
}
