import com.razorpay.Utils;
import org.json.JSONObject;

public class TestRazorpay {
    public static void main(String[] args) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", "order_T1EnLkk1z8QrEL");
            attributes.put("razorpay_payment_id", "pay_dummy123");
            
            // Generate valid signature manually
            String payload = "order_T1EnLkk1z8QrEL|pay_dummy123";
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(new javax.crypto.spec.SecretKeySpec("CEjQ3Hy1UEiRYYO97eBYl7C6".getBytes(), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String signature = hexString.toString();
            attributes.put("razorpay_signature", signature);
            
            boolean isValid = Utils.verifyPaymentSignature(attributes, "CEjQ3Hy1UEiRYYO97eBYl7C6");
            System.out.println("Signature Valid: " + isValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
