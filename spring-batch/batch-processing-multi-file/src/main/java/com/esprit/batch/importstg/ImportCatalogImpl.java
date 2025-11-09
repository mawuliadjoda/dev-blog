package com.esprit.batch.importstg;

import com.esprit.domain.model.StgCustomer;
import com.esprit.domain.model.StgOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ImportCatalogImpl implements ImportCatalog {

    private final ImportProperties props;

    @Override
    public List<ImportDef<?>> catalog() {
        DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ImportDef<StgCustomer> customers = new ImportDef<>(
                "customers",
                props.getCustomersFile(), // ex: "classpath:customers.csv" ou "src/main/resources/customers.csv"
                new String[] {"id","firstName","lastName","email","gender","contactNo","country","dob"},
                (Function<FieldSet, StgCustomer>) fs -> {
                    StgCustomer c = new StgCustomer();
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
                """
                INSERT INTO stg_customer (batch_id, customer_id, first_name, last_name, email, gender, contact, country, dob)
                VALUES (:batchId, :id, :firstName, :lastName, :email, :gender, :contactNo, :country, :dob)
                ON CONFLICT (batch_id, customer_id) DO UPDATE SET
                  first_name = EXCLUDED.first_name,
                  last_name  = EXCLUDED.last_name,
                  email      = EXCLUDED.email,
                  gender     = EXCLUDED.gender,
                  contact    = EXCLUDED.contact,
                  country    = EXCLUDED.country,
                  dob        = EXCLUDED.dob
                """
        );

        ImportDef<StgOrder> orders = new ImportDef<>(
                "orders",
                props.getOrdersFile(), // ex: "classpath:orders.csv" ou "src/main/resources/orders.csv"
                new String[] {"id","customerId","orderDate","amount","status"},
                (Function<FieldSet, StgOrder>) fs -> {
                    StgOrder o = new StgOrder();
                    o.setId(fs.readLong("id"));
                    o.setCustomerId(fs.readLong("customerId"));
                    o.setOrderDate(LocalDate.parse(fs.readString("orderDate"), DMY));
                    o.setAmount(new BigDecimal(fs.readString("amount")));
                    o.setStatus(fs.readString("status"));
                    return o;
                },
                """
                INSERT INTO stg_order (batch_id, order_id, customer_id, order_date, amount, status)
                VALUES (:batchId, :id, :customerId, :orderDate, :amount, :status)
                ON CONFLICT (batch_id, order_id) DO UPDATE SET
                  customer_id = EXCLUDED.customer_id,
                  order_date  = EXCLUDED.order_date,
                  amount      = EXCLUDED.amount,
                  status      = EXCLUDED.status
                """
        );

        return List.of(customers, orders);
    }
}

