package pbl.GNUB.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import pbl.GNUB.csv.*;
import pbl.GNUB.dto.*;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    private final CsvShopReader csvShopReader;
    private final CsvShopWriter csvShopWriter;
    private final CsvShopTagReader csvShopTagReader;
    private final CsvShopTagWriter csvShopTagWriter;
    private final CsvShopMenuReader csvMenuReader;
    private final CsvShopMenuWriter csvMenuWriter;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job csvShopJob() {
        return new JobBuilder("csvShopJob", jobRepository)
            //.start(csvShopReaderStep())
            .start(csvShopTagReaderStep())
            //.next(csvShopMenuReaderStep())
            .build();
    }

    @Bean
    public Step csvShopReaderStep() {
        return new StepBuilder("csvShopReaderStep", jobRepository)
            .<ShopDto, ShopDto>chunk(100, transactionManager)
            .reader(csvShopReader.csvScheduleReader())
            .writer(csvShopWriter)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step csvShopTagReaderStep() {
        return new StepBuilder("csvShopTagReaderStep", jobRepository)
            .<ShopTagDto, ShopTagDto>chunk(1000, transactionManager)
            .reader(csvShopTagReader.csvTagReader())
            .writer(csvShopTagWriter)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step csvShopMenuReaderStep() {
        return new StepBuilder("csvShopMenuReaderStep", jobRepository)
            .<ShopMenuDto, ShopMenuDto>chunk(1000, transactionManager)
            .reader(csvMenuReader.csvShopMenuReader())
            .writer(csvMenuWriter)
            .allowStartIfComplete(true)
            .build();
    }
}
