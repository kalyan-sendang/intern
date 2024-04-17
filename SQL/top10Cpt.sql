SELECT bCT, negR
FROM mergedData
where bCT = "CPT" 
ORDER BY negR DESC
LIMIT 10;

SELECT distinct bCT, negR
FROM mergedData
where bCT = "CPT" 
ORDER BY negR DESC
LIMIT 10;

