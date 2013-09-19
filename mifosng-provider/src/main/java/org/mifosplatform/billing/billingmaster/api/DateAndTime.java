package org.mifosplatform.billing.billingmaster.api;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateAndTime {

    public static void main(String[] args) throws Exception {
        LocalDate date=new LocalDate();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
        String formattedDate = formatter.print(date);
        System.out.println(formattedDate);

    }
}