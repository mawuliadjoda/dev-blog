package com.esprit.batch.reader;

import com.esprit.persistence.entities.StgCustomerEntity;
import com.esprit.persistence.entities.StgOrderEntity;
import com.esprit.persistence.repository.StgCustomerRepository;
import com.esprit.persistence.repository.StgOrderRepository;
import com.esprit.properties.ImportProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportCatalogImpl implements ImportCatalog {

    private final ImportProperties props;
    private final StgCustomerRepository stgCustomerRepository;
    private final StgOrderRepository stgOrderRepository;

    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public List<ImportDef<?>> catalog() {
        ImportDef<StgCustomerEntity> customers = new ImportDef<>(
                "customers",
                props.getCustomersFile(),
                new String[]{"id","firstName","lastName","email","gender","contactNo","country","dob"},
                fs -> {
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
                },
                stgCustomerRepository
        );

        ImportDef<StgOrderEntity> orders = new ImportDef<>(
                "orders",
                props.getOrdersFile(),
                new String[]{"id","customerId","orderDate","amount","status"},
                fs -> {
                    var o = new StgOrderEntity();
                    o.setId(fs.readLong("id"));
                    o.setCustomerId(fs.readLong("customerId"));
                    o.setOrderDate(LocalDate.parse(fs.readString("orderDate"), DMY));
                    o.setAmount(new BigDecimal(fs.readString("amount")));
                    o.setStatus(fs.readString("status"));
                    return o;
                },
                stgOrderRepository
        );

        return List.of(customers, orders);
    }
}
