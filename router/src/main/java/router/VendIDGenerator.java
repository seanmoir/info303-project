package router;

public class VendIDGenerator {
    public String generateVendID(String group) {
        if(group.equals("Regular Customers")) {
            return "0afa8de1-147c-11e8-edec-2b197906d816";
        }
        return "0afa8de1-147c-11e8-edec-201e0f00872c";
    }
}
