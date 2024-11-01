package pbl.GNUB.csv;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopTagDto; // ShopTagDto import

@Configuration
@RequiredArgsConstructor
public class CsvShopTagReader {
    @Bean
    public FlatFileItemReader<ShopTagDto> csvTagReader() { 
        // 파일 경로 지정
        FlatFileItemReader<ShopTagDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("static/csv/shopTag.csv")); // 읽어들일 csv file
        flatFileItemReader.setEncoding("UTF-8");

        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        // 읽으려는 데이터 linemapper를 통해 dto로 매핑
        DefaultLineMapper<ShopTagDto> defaultLineMapper = new DefaultLineMapper<>();

        // csv 파일에서 구분자 지정하고 구분한 데이터 setNames를 통해 각 이름 설정
        defaultLineMapper.setLineTokenizer(new DelimitedLineTokenizer(){{
            setNames("name", "hygiene", "revisit", "recent", "delicious", "goodValue", "mood", "fresh", "kindness", "alone", "chilam_dong", "gajwa_dong");
            setDelimiter(",");
        }});

        defaultLineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<ShopTagDto>() {{
            setTargetType(ShopTagDto.class);
        }});

        flatFileItemReader.setLineMapper(defaultLineMapper); // lineMapper 지정
        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
        
    }
    
}