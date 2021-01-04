package Main;

import java.util.concurrent.atomic.AtomicInteger;

/** Helper class to generate part and product ID sequences */

public class IdGenerator {

    private static AtomicInteger partId = new AtomicInteger(12);
    private static AtomicInteger prodId = new AtomicInteger(1000);

    public String genProdId() {
        prodId.getAndIncrement();
        return String.valueOf(prodId);
    }
    public String genPartId() {
        partId.getAndIncrement();
        return String.valueOf(partId);
    }

}
