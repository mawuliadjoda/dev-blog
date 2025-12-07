package com.esprit.batch.reader;

import com.esprit.persistence.entities.StgCustomerEntity;
import com.esprit.persistence.entities.StgOrderEntity;
import com.esprit.properties.ImportProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ImportReadersConfig {

    private final ImportProperties props;

    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ResourceLoader resourceLoader;

    public FlatFileItemReader<StgCustomerEntity> stgCustomerReader() {
        var reader = new FlatFileItemReader<StgCustomerEntity>();
        reader.setName("reader-customers");
        reader.setResource(resourceLoader.getResource(props.getCustomersFile()));
        reader.setLinesToSkip(1);

        var lineMapper = new DefaultLineMapper<StgCustomerEntity>();

        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("id","firstName","lastName","email","gender","contactNo","country","dob");
        lineMapper.setLineTokenizer(tokenizer);

        lineMapper.setFieldSetMapper(fs -> {
            var c = new StgCustomerEntity();
            c.setId(fs.readLong("id"));
            c.setFirstName(fs.readString("firstName"));
            c.setLastName(fs.readString("lastName"));
            c.setEmail(fs.readString("email"));
            c.setGender(fs.readString("gender"));
            c.setContactNo(fs.readString("contactNo"));
            c.setCountry(fs.readString("country"));
            c.setDob(LocalDate.parse(fs.readString("dob"), DMY));
            return c;
        });

        reader.setLineMapper(lineMapper);
        return reader;
    }

    public FlatFileItemReader<StgOrderEntity> stgOrderReader() {
        var reader = new FlatFileItemReader<StgOrderEntity>();
        reader.setName("reader-orders");
        reader.setResource(resourceLoader.getResource(props.getOrdersFile()));
        reader.setLinesToSkip(1);

        var lineMapper = new DefaultLineMapper<StgOrderEntity>();

        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("id","customerId","orderDate","amount","status");
        lineMapper.setLineTokenizer(tokenizer);

        lineMapper.setFieldSetMapper(fs -> {
            var o = new StgOrderEntity();
            o.setId(fs.readLong("id"));
            o.setCustomerId(fs.readLong("customerId"));
            o.setOrderDate(LocalDate.parse(fs.readString("orderDate"), DMY));
            o.setAmount(new BigDecimal(fs.readString("amount")));
            o.setStatus(fs.readString("status"));
            return o;
        });

        reader.setLineMapper(lineMapper);
        return reader;
    }
}
