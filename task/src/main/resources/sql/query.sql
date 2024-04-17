-- mysql query to find out top 10 CPT codes by cost(negotiated rates)
SELECT  bCT,bC as code_number , negR as negotiated_rates
FROM mergedData
where bCT = "CPT"
ORDER BY negR DESC
    LIMIT 10;
