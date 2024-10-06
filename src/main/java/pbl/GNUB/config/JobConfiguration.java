package pbl.GNUB.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;

import pbl.GNUB.csv.CsvShopReader;
import pbl.GNUB.csv.CsvShopWriter;
import pbl.GNUB.dto.ShopDto;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {
    
    private final CsvShopReader csvShopReader;
    private final CsvShopWriter csvShopWriter;

    @Bean
    public Job csvShopJob(JobRepository jobRepository, Step shopDataLoadStep){
        return new JobBuilder("csvShopJob", jobRepository)
            .start(shopDataLoadStep)
            .build();
    }

    @Bean
    public Step csvShopReaderStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csvShopReaderStep", jobRepository)
            .<ShopDto, ShopDto>chunk(100, platformTransactionManager)
            .reader(csvShopReader.csvScheduleReader())
            .writer(csvShopWriter)
            .allowStartIfComplete(true) // step 다시 실행하고 싶을 때
            .build();
    }
}