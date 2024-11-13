package pbl.GNUB.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.csv.CsvShopTagReader;
import pbl.GNUB.csv.CsvShopTagWriter;
import pbl.GNUB.dto.ShopTagDto;

@Configuration
@RequiredArgsConstructor
@Order(1) // 이 설정 클래스의 우선순위를 지정
public class TagConfiguration {
    
    private final CsvShopTagReader csvShopTagReader;
    private final CsvShopTagWriter csvShopTagWriter;
    private final JobRepository jobRepository;

    // @Primary 어노테이션을 추가하여 이 Job 빈을 기본으로 사용하도록 지정
    @Bean(name = "csvShopTagJob")
@Primary
public Job csvShopTagJob(@Qualifier("shopTagDataLoadStep") Step shopTagDataLoadStep) {
    return new JobBuilder("csvShopTagJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(shopTagDataLoadStep)
        .build();
}

    @Bean(name = "shopTagDataLoadStep")
    @Primary
    public Step shopTagDataLoadStep(PlatformTransactionManager transactionManager) {
        return new StepBuilder("shopTagDataLoadStep", jobRepository)
        .<ShopTagDto, ShopTagDto>chunk(100, transactionManager)
        .reader(csvShopTagReader.csvTagReader())
        .processor(shopTagProcessor())
        .writer(csvShopTagWriter)
        .faultTolerant()
        .skip(Exception.class)
        .skipLimit(10)
        .listener(new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                // 로깅 추가
            }
            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                // 로깅 추가
                return ExitStatus.COMPLETED;
            }
        })
        .build();
}

    @Bean
    public ItemProcessor<ShopTagDto, ShopTagDto> shopTagProcessor() {
        return item -> {
        // 데이터 변환 및 유효성 검사 로직
        return item;
        };
    }
}