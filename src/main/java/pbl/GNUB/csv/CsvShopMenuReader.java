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
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.dto.ShopMenuDto;

@Configuration
@RequiredArgsConstructor
public class CsvShopMenuReader {

    @Bean(name = "csvShopMenuFlatFileReader")
    public FlatFileItemReader<ShopMenuDto> csvShopMenuReader(){
        // 파일 경로 지정
        FlatFileItemReader<ShopMenuDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("static/csv/shopMenu.csv")); // 읽어들일 csv file
        flatFileItemReader.setEncoding("UTF-8");

        // 데이터 내부에 개행있으면 추가
        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());

        // 읽으려는 데이터 linemapper를 통해 dto로 매핑
        DefaultLineMapper<ShopMenuDto> defaultLineMapper = new DefaultLineMapper<>();

        // csv 파일에서 구분자 지정하고 구분한 데이터 setNames를 통해 각 이름 설정
        defaultLineMapper.setLineTokenizer(new DelimitedLineTokenizer(){{
            setNames("restName", "menuName", "price", "imgUrl");
            setDelimiter(",");
        }});

        defaultLineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<ShopMenuDto>() {{
            setTargetType(ShopMenuDto.class);
        }});

        flatFileItemReader.setLineMapper(defaultLineMapper); // lineMapper 지정
        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }
}
