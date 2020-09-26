package router;

import domain.Customer;
import domain.Sale;
import domain.Summary;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.swing.*;

public class SaleRouteBuilder extends RouteBuilder {

    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement
     * the routes using the Java fluent builder syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {

        //extract customer details from email
        /*from("imap://localhost?username=test@localhost"
                + "&port=3143"
                + "&password=password"
                + "&consumer.delay=5000"
                + "&searchTerm.subject=Vend:SaleUpdate")*/
        from("imaps://outlook.office365.com?username=<INSERT EMAIL HERE>"
                + "&password=" + getPassword()
                + "&searchTerm.subject=Vend:SaleUpdate"
                + "&debugMode=false"
                + "&folderName=INBOX")
                .convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Gson, Sale.class)
                .setProperty("customer").simple("${body.customer}")

                //send create sale on sale service
                .removeHeaders("*")
                .marshal().json(JsonLibrary.Gson)
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("http://localhost:8081/api/sales")

                //get sale summary from sale service
                .setBody().simple("null")
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .toD("http://localhost:8081/api/sales/customer/${exchangeProperty.customer.id}/summary")

                /*
                 * compare summary's caluclated customer group with current group, if different update account service
                 * and vend to have the same group as the summary produces
                 */
                .unmarshal().json(JsonLibrary.Gson, Summary.class)
                .setProperty("calculatedGroup").method(VendIDGenerator.class, "generateVendID(${body.group})")
                .choice()
                    .when().simple("${exchangeProperty.calculatedGroup} != ${exchangeProperty.customer.group}")

                        .setProperty("modifiedCustomer").method(Customer.class,
                    "changeCustomerGroup(${exchangeProperty.customer}, ${exchangeProperty.calculatedGroup})")

                        .setBody().simple("${exchangeProperty.modifiedCustomer}")
                        .marshal().json(JsonLibrary.Gson)
                        .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                        .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                        .toD("http://localhost:8086/api/accounts/account/${exchangeProperty.customer.id}")

                        .setBody().simple("${exchangeProperty.modifiedCustomer}")
                        .marshal().json(JsonLibrary.Gson)
                        .setHeader("Authorization", constant("Bearer <INSERT KEY HERE>"))
                        .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                        .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                        .toD("https://info303otago.vendhq.com/api/2.0/customers/${exchangeProperty.customer.id}");
    }

    public static String getPassword() {

        JPasswordField passwordField = new JPasswordField();

        int rsp = JOptionPane.showConfirmDialog(null, passwordField, "Enter E-Mail password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (rsp == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }
        return null;
    }
}
