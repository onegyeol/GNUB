package pbl.GNUB.csv;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopTagDto;

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

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("name", "hygiene", "revisit", "recent", "delicious", "goodValue", "mood", "fresh", "kindness", "alone", "chilamDong", "gajwaDong");
        lineTokenizer.setDelimiter(",");

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(new ShopTagFieldSetMapper());

        flatFileItemReader.setLineMapper(defaultLineMapper);
        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }

    private static class ShopTagFieldSetMapper implements FieldSetMapper<ShopTagDto> {
        @Override
        public ShopTagDto mapFieldSet(FieldSet fieldSet) {
            ShopTagDto dto = new ShopTagDto();
            dto.setName(fieldSet.readString("name"));
            dto.setHygiene(fieldSet.readInt("hygiene"));
            dto.setRevisit(fieldSet.readInt("revisit"));
            dto.setRecent(fieldSet.readInt("recent"));
            dto.setDelicious(fieldSet.readInt("delicious"));
            dto.setGoodValue(fieldSet.readInt("goodValue"));
            dto.setMood(fieldSet.readInt("mood"));
            dto.setFresh(fieldSet.readInt("fresh"));
            dto.setKindness(fieldSet.readInt("kindness"));
            dto.setAlone(fieldSet.readInt("alone"));
            dto.setChilamDong(fieldSet.readInt("chilamDong"));
            dto.setGajwaDong(fieldSet.readInt("gajwaDong"));

            List<String> tags = new ArrayList<>();
            if (dto.getHygiene() == 1) tags.add("위생등급제 가게");
            if (dto.getRevisit() == 1) tags.add("재방문률이 높은");
            if (dto.getRecent() == 1) tags.add("최근에 자주가는");
            if (dto.getDelicious() == 1) tags.add("맛있는");
            if (dto.getGoodValue() == 1) tags.add("가성비");
            if (dto.getMood() == 1) tags.add("깔끔하고 분위기가 좋은");
            if (dto.getFresh() == 1) tags.add("신선한");
            if (dto.getKindness() == 1) tags.add("친절한");
            if (dto.getAlone() == 1) tags.add("혼밥");
            if (dto.getChilamDong() == 1) tags.add("칠암동");
            if (dto.getGajwaDong() == 1) tags.add("가좌동");

            dto.setTags(String.join(",", tags));
            return dto;
        }
    }
}