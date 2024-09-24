package fon.njt.mockfon.service;

import com.paypal.api.payments.Refund;
import com.paypal.api.payments.Sale;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayPalService {

    private final APIContext apiContext;

    @Autowired
    public PayPalService(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    public Refund refundPayment(String saleId) {
        Refund refund = new Refund(); // Kreira prazan refund objekat
        Sale sale = new Sale();
        sale.setId(saleId); // Postavlja Sale ID na transakciju koju želite refundirati

        try {
            // Izvršava refundaciju transakcije i vraća odgovor
            Refund responseRefund = sale.refund(apiContext, refund);
            System.out.println("Refund status: " + responseRefund.getState());
            return responseRefund;
        } catch (PayPalRESTException e) {
            // Prikazuje grešku u slučaju neuspešne refundacije
            System.err.println(e.getDetails());
            throw new RuntimeException("Refund failed: " + e.getMessage());
        }
    }
}
