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
/* 
@Configuration
@RequiredArgsConstructor
public class TagConfiguration {
    private final CsvShopTagReader csvShopTagReader;
    private final CsvShopTagWriter csvShopTagWriter;

    @Bean
    public Job csvShopTagJob(JobRepository jobRepository, Step stepDataLoadStep){
        return new JobBuilder("csvShopTagJob", jobRepository)
            .start(stepDataLoadStep)
            .build();
    }

    @Bean
    public Step csvShopTagReaderStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("csvShopTagReaderStep", jobRepository)
            .<ShopTagDto, ShopTagDto>chunk(100, platformTransactionManager)
            .reader(csvShopTagReader.csvTagReader())
            .writer(csvShopTagWriter)
            .allowStartIfComplete(true)
            .build();
    }
}*/