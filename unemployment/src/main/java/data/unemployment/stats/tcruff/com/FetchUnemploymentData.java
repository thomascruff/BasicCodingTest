package data.unemployment.stats.tcruff.com;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryResponse;
import com.google.cloud.bigquery.TableResult;
import com.opencsv.CSVWriter;
import utils.tcruff.com.SanitizeInput;
import utils.tcruff.com.ValidateAPIKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class FetchUnemploymentData {
	private static final String tablename = "bigquery-public-data.bls.unemployment_cps";
	private static final String seriesID = "LNS14000000";
	private static final String outputFile = "UnemploymentStats.csv";
	private static final String[] columnNames = {"series_id", "year", "period", "value", "date", "series_title"};
	
	/* I understand that this method does leave the possibility of leaking resources when an exception 
	 * happens either connecting to BigQuery or writing the file.  This has been ignored for the sake of time.
	 * It also relies on the garbage collector to clean up the BigQuery resources - again for the sake of time */
	
	public static long getUnemploymentData(String apikey, String year, String period)
	{
		if (!ValidateAPIKey.ValidateKey(apikey))
		{
			return -1;
		}
		BigQuery bigquery = null;
		int numOfRows = 0;
		
		try {
			bigquery = BigQueryOptions.newBuilder().setProjectId("iron-inkwell-221903")
					.setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("credentials.json")))
					.build().getService();
			String myQuery = "SELECT  * from `" + FetchUnemploymentData.tablename + "` WHERE series_id=\"" + FetchUnemploymentData.seriesID + "\"";
			if (year != null && !year.isEmpty())
				myQuery = myQuery + " AND year= " + SanitizeInput.sanitizeForSQL(year) ; 
			if (period != null && !period.isEmpty())
				myQuery = myQuery + " AND period= " + SanitizeInput.sanitizeForSQL(period) ; 
			myQuery = myQuery + " ORDER BY date";
			System.out.println(myQuery);
			QueryJobConfiguration queryConfig =
					QueryJobConfiguration.newBuilder(myQuery).setUseLegacySql(false).build();
			// Create a job ID so that we can safely retry.
			JobId jobId = JobId.of(UUID.randomUUID().toString());
			Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

			// Wait for the query to complete.
			queryJob = queryJob.waitFor();
			// Check for errors
			if (queryJob == null) {
				throw new RuntimeException("Job no longer exists");
			} else if (queryJob.getStatus().getError() != null) {
				// You can also look at queryJob.getStatus().getExecutionErrors() for all
				// errors, not just the latest one.
				throw new RuntimeException(queryJob.getStatus().getError().toString());
			}
			// Get the results.
			QueryResponse response = bigquery.getQueryResults(jobId);

			TableResult result = queryJob.getQueryResults();

			File outputFile = new File(FetchUnemploymentData.outputFile);
			FileWriter outputfile = new FileWriter(outputFile);
			CSVWriter writer = new CSVWriter(outputfile);
			// Write out CSV header line
			writer.writeNext(FetchUnemploymentData.columnNames);
			System.out.println(result.getTotalRows());

			for (FieldValueList row : result.iterateAll()) {
				String data[] = new String[columnNames.length];
				String value = null;
				for (int i=0; i < columnNames.length; i++)
				{
					// System.out.println(FetchUnemploymentData.columnNames[i]);
					value = row.get(FetchUnemploymentData.columnNames[i]).getStringValue();
					if (value==null)
						value="";
					data[i] = value;
				}
					
				writer.writeNext(data);	 
				numOfRows++;
			}
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return numOfRows;
	}
}
