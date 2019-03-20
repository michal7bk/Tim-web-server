package pl.bak.timserver;

import org.modelmapper.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@Configuration
public class TimServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimServerApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {


        ModelMapper modelmapper = new ModelMapper();


        Provider<LocalDateTime> localDateProvider = new AbstractProvider<LocalDateTime>() {
            @Override
            public LocalDateTime get() {
                return LocalDateTime.now();
            }
        };

        Converter<String, LocalDateTime> toStringDate = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(source, format);
            }

        };
        modelmapper.createTypeMap(String.class, LocalDateTime.class);
        modelmapper.addConverter(toStringDate);
        modelmapper.getTypeMap(String.class, LocalDateTime.class).setProvider(localDateProvider);
        return modelmapper;
    }

}


