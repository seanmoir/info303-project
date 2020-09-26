package router;

import domain.Account;
import domain.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class AccountRouteBuilder extends RouteBuilder {

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

        // Recieve acccount in JSON from Jetty endpoint, form suitable class for vend API and then convert back to JSON.
        from("jetty:http://localhost:9000/api/account?enableCORS=true")
                .setExchangePattern(ExchangePattern.InOnly)
                .unmarshal().json(JsonLibrary.Gson, Account.class)
                .bean(Customer.class, "generateVendCustomer(${body})")
                .marshal().json(JsonLibrary.Gson)
                .to("jms:queue:vend-req");

        // set API key in header and other meta data, then send off to vend and return response into queue
        from("jms:queue:vend-req")
                .removeHeaders("*")
                .setHeader("Authorization", constant("Bearer <INSERT KEY HERE>"))
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("https://info303otago.vendhq.com/api/2.0/customers")
                .to("jms:queue:vend-rsp");

        // extract customer JSON from vend response
        from("jms:queue:vend-rsp")
                .setBody().jsonpath("$.data")
                .marshal().json(JsonLibrary.Gson)
                .unmarshal().json(JsonLibrary.Gson, Customer.class)
                .bean(Account.class, "generateAccount(${body})")
                .marshal().json(JsonLibrary.Gson)
                .removeHeaders("*")
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("http://localhost:8086/api/accounts");
    }
}
