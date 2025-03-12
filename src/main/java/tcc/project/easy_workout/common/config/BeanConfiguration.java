package tcc.project.easy_workout.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
