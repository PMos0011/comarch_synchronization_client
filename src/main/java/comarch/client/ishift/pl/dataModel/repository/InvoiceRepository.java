package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query ("from Invoice where typ = 2")
    List<Invoice> getAllSellingInvoices();

    @Query("select contactorId from Invoice where typ = 2 group by contactorId")
    List<Long> getAllBuyingContractorsIdList();
}
