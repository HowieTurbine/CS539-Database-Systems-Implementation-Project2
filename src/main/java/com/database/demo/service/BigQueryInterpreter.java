package com.database.demo.service;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.UUID;
// [END bigquery_simple_app_deps]

@Service
public class BigQueryInterpreter implements DBUtil{
    public BigQueryInterpreter() {
    }

    public Map<String, Object> getData(String query) {
        HashMap<String, Object> returnValue = new HashMap<>();
        double start, end;

        // [START bigquery_simple_app_client]
        //TODO:Set up the projectId and location of your json file
        BigQuery bigquery = null;
        start = System.currentTimeMillis();
        try {
            bigquery = BigQueryOptions.newBuilder().setProjectId("ageless-venture-224721")
                    .setCredentials(
                            ServiceAccountCredentials.fromStream(new FileInputStream("D:\\My First Project-c8cab68c3f17.json"))
                    ).build().getService();
            // [END bigquery_simple_app_client]
            // [START bigquery_simple_app_query]
            QueryJobConfiguration queryConfig =
                    QueryJobConfiguration.newBuilder(query)
                            // Use standard SQL syntax for queries.
                            // See: https://cloud.google.com/bigquery/sql-reference/
                            .setUseLegacySql(false)
                            .build();

            // Create a job ID so that we can safely retry.
            JobId jobId = JobId.of(UUID.randomUUID().toString());
            Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

            // Wait for the query to complete.
            queryJob = queryJob.waitFor();
            end = System.currentTimeMillis();

            // Check for errors
            if (queryJob == null) {
                returnValue.put("ERROR","Job no longer exists");
                throw new RuntimeException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                returnValue.put("ERROR",queryJob.getStatus().getError().toString());
                throw new RuntimeException(queryJob.getStatus().getError().toString());
            }
            // [END bigquery_simple_app_query]

            // [START bigquery_simple_app_print]
            // Get the results.
            QueryResponse response = bigquery.getQueryResults(jobId);

            TableResult result = queryJob.getQueryResults();
            //Get metaData
            FieldList fieldValueList=result.getSchema().getFields();
            List<List<String>> TotalResult = new ArrayList<>();
            List<String> NameCol = new ArrayList<>();
            for(Field field:fieldValueList)
            {
                NameCol.add(field.getName());
            }
            TotalResult.add(NameCol);


            // Print all pages of the results.
            for (FieldValueList row : result.iterateAll()) {
                List<String> DataCol = new ArrayList<>();
                for(String name:NameCol)
                {
                    DataCol.add(row.get(name).getStringValue());
                }
                TotalResult.add(DataCol);
            }
            returnValue.put("status","OK");
            returnValue.put("result", TotalResult);
            returnValue.put("time", (end - start) / 1000);
            // [END bigquery_simple_app_print]
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            end = System.currentTimeMillis();
            returnValue.put("ERROR", e.toString());
            returnValue.put("time", (end - start) / 1000);
        }
        return returnValue;
    }
}

