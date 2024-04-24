//package org.practise.task.config;
//
//import co.elastic.clients.elasticsearch._types.aggregations.ExtendedStatsAggregate;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ExtendedStatsSerializer {
//
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public ExtendedStatsSerializer(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    public String serializeToJson(ExtendedStatsAggregate extendedStats) {
//        try {
//            return objectMapper.writeValueAsString(extendedStats);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // Handle serialization error
//            return null;
//        }
//    }
//}
