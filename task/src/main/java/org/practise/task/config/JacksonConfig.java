//package org.practise.task.config;
//
//import co.elastic.clients.elasticsearch._types.aggregations.ExtendedStatsAggregate;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JacksonConfig {
//
////    @Bean
////    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
////        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.configure(SerializationFeature.CLOSE_CLOSEABLE.FAIL_ON_EMPTY_BEANS, false);
////        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
////        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
////        converter.setObjectMapper(objectMapper);
////        return converter;
////    }
//
//    @Bean
//    public String serializeToJson(ExtendedStatsAggregate extendedStats) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.writeValueAsString(extendedStats);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // Handle serialization error
//            return null;
//        }
//    }
//}
