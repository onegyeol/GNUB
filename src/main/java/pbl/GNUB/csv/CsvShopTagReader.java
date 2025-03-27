package pbl.GNUB.csv;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.dto.ShopTagDto;
import pbl.GNUB.entity.ShopTag;

@Configuration
@RequiredArgsConstructor
public class CsvShopTagReader {
    @Bean
    public FlatFileItemReader<ShopTagDto> csvTagReader() { 
        FlatFileItemReader<ShopTagDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("static/csv/shopTag.csv"));
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        DefaultLineMapper<ShopTagDto> defaultLineMapper = new DefaultLineMapper<>();

        // csv 파일에서 구분자 지정하고 구분한 데이터 setNames를 통해 각 이름 설정
        defaultLineMapper.setLineTokenizer(new DelimitedLineTokenizer(){{
            setNames("name", "restId", "alone", "date", "delicious", "fresh", "goodValue", "hygiene", "kindness", "many", "mood", "parking", "recent");
            setDelimiter(",");
        }});

        defaultLineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<ShopTagDto>() {{
            setTargetType(ShopTagDto.class);
        }});

        flatFileItemReader.setLineMapper(defaultLineMapper);
        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }

    
}