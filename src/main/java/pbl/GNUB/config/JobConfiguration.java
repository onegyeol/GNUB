package pbl.GNUB.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.csv.CsvShopReader;
import pbl.GNUB.csv.CsvShopTagReader;
import pbl.GNUB.csv.CsvShopTagWriter;
import pbl.GNUB.csv.CsvShopWriter;
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.dto.ShopTagDto;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    private final CsvShopReader csvShopReader;
    private final CsvShopWriter csvShopWriter;
    private final CsvShopTagReader csvShopTagReader;
    private final CsvShopTagWriter csvShopTagWriter;

    @Bean
    public Job csvShopJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("csvShopJob", jobRepository)
            .start(csvShopReaderStep(jobRepository, platformTransactionManager))
            .next(csvShopTagReaderStep(jobRepository, platformTransactionManager))
            .build();
    }

    @Bean
    public Step csvShopReaderStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("csvShopReaderStep", jobRepository)
            .<ShopDto, ShopDto>chunk(100, platformTransactionManager)
            .reader(csvShopReader.csvScheduleReader())
            .writer(csvShopWriter)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step csvShopTagReaderStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("csvShopTagReaderStep", jobRepository)
            .<ShopTagDto, ShopTagDto>chunk(1000, platformTransactionManager)
            .reader(csvShopTagReader.csvTagReader())
            .writer(csvShopTagWriter)
            .allowStartIfComplete(true)
            .build();
    }
}