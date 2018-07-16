import com.cy.framework.util.safe.RSA;
import org.testng.annotations.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TestRSA {
    @Test
    public void testRSA(){
        try {
            //===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
            //生成RSA公钥和私钥，并Base64编码
            KeyPair keyPair = RSA.getKeyPair();
            String publicKeyStr = RSA.getPublicKey(keyPair);
            String privateKeyStr = RSA.getPrivateKey(keyPair);
            System.out.println("RSA公钥Base64编码:" + publicKeyStr);
            System.out.println("RSA私钥Base64编码:" + privateKeyStr);

            //=================客户端=================
            //hello, i am infi, good night!加密
            String message = "hello, i am infi, good night!";
            //将Base64编码后的公钥转换成PublicKey对象
            PublicKey publicKey = RSA.string2PublicKey(publicKeyStr);
            //用公钥加密
            byte[] publicEncrypt = RSA.publicEncrypt(message.getBytes(), publicKey);
            //加密后的内容Base64编码
            String byte2Base64 = RSA.byte2Base64(publicEncrypt);
            System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);


            //##############	网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################



            //===================服务端================
            //将Base64编码后的私钥转换成PrivateKey对象
            PrivateKey privateKey = RSA.string2PrivateKey(privateKeyStr);
            //加密后的内容Base64解码
            byte[] base642Byte = RSA.base642Byte(byte2Base64);
            //用私钥解密
            byte[] privateDecrypt = RSA.privateDecrypt(base642Byte, privateKey);
            //解密后的明文
            System.out.println("解密后的明文: " + new String(privateDecrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testJs() throws Exception {
        String private_key="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBREhkYXMMt+hm9Y6nC1YlCaXA\n" +
                "lKcnAPujxem/czO03TswdIR/12R9oJu72aXR/xtShjOuZRiDgKFmX7DTkBbYqTpF07roLorcYxwU\n" +
                "YiGqlkjvdF8YnCYzvP+9VQeXJ2KEw6p95RUu0KgUm6x6K1/Q39wlaT0ZpDwF0NlnWZB2igUbySZ3\n" +
                "lOeie6Xo7FxgjdYoH+YhCVT83c+n1tPHKE8jg74G2lKeS4EDOhBuBSbbe7aXJ3mrqEYRdnys4hbK\n" +
                "o7YyCyT/t2JYctNyIr4BQade38o1tI4Dr4K+Kx/cDy0mE5dumOne3yGBYTnVvO1ge+3ZwJwdsxV6\n" +
                "fbCL6j4FKmM9AgMBAAECggEALgI9ueytZFN41lyEH5R7XsdmRXsjW1CQQCRLJCc+uhsb3A08s7vY\n" +
                "OpaVl3DKAkpIUUEO1/2HkhaQW72nVEWuvALPiNbpaYZXSkXEUsTysqK25vOGfV3mgve8FolTIEuW\n" +
                "VybFYfXNalI94MffxcP2YIOs+WvRoXTBIFjLaGuKbYYE7A/+W2b3Vj8ynejhgJUjV220knjyGZbK\n" +
                "lcS7UOuJMnKzEqFyMnPoA980p6kAi6zFo8V3XKHDvRkv3jhmq0DClxTjW0fFit+WrLJ7XI0b0Fmy\n" +
                "OOG1wl6fJk0b2b9UoCRVObCbmhGstT2n21/6yGpsQ6J2M+CCrhicu2Ts5n63AQKBgQC7AI8pmUBz\n" +
                "ALWxqKYz3bFQGlDby4PRO2hypbAqXoP7WYfF2mkIlu2otp4yXjpkstrt66j7c4U7e6SQcib7k3OB\n" +
                "LyRgS8LDe7QO77Kp5rI8iljhL7Hq12Gvq0BBJ0D5goP9mEjjPyOA3CcdIJByvt2fguxKlXJP+ucM\n" +
                "8EmGVdnY3QKBgQCw9kZoHyINBAlUk46Tsp33ubrNfMXjE964YQwc0wuDBmm+04S0o80loXQF2NsF\n" +
                "XIZXiO7dBOptyeWJBeUWIIU9/ZLIpu1NYLHYG4ZQH/+kdEhefgKm/c9rz+bq1chyYImucp5fUMQA\n" +
                "+QyTiEnNWwlkM1QiOg9mfkHcLCB2PKzd4QKBgEomn54MIc04U9O9nyHj34J23Oc63OsjlngXkTJL\n" +
                "xiPtzXO79ngseNK0jyhDyv3RGhTLpHcIyKTck1WfniuVvXBVRCRXBkTDkZN2oq7SRozRAwtQAUmw\n" +
                "3BBvZIwxR4dopHPzZbuOdsKLCYt4NS761iCBJrcwaWKjh+jOgykcc8SVAoGAJHoPV0Ubmr07QyDL\n" +
                "YeyOnVK+i77td3pQDYeLnKFwHQkEYR5um2GsVtZp+Q6TTw6cboy+V/a8b/cPBmidqSr47CdJy2yF\n" +
                "O87zuc0qqXd/FaIWjMvCtjNZPyryuXNpxxHZL4lW1eTrEhxEGGEefWMHk4z1Cb0YEZNy9fRumJC3\n" +
                "PWECgYBoCdt6VMUWiaHAA6wC3k1NPfLnpCQaqMut9guyPsOx83gh+mLhrx310EpRfDlGux1DO0pL\n" +
                "lknlLdGdHTGkGJ5x00em5qHQyqNOwg06EtUBzvNxJVRBIruhv7fUpm1jdbfzTgDWvtmE13jFFQc7\n" +
                "258l7wFfL6c8zqtQHvk2aBz5Kw==";
        String byte2Base64="D5BtrYS98z2dyG1X2ajHjjXCtYKyiFtW9ybI4jrBbmv72v72kRvTlWfzsbEUFkgA0kZEcRypZYN8mcpUDDeccUuH1lZFmozS0FoRce5u8nm1rxmc1gok8PvyIZKvQqSp3eNvCgq0Bn+yp2zajQbX386Knnf3xbJ17fJ1RAGRR969gcEcsjEdMlK+BnEioC4XEUsFxd3KwCku8soTqVal1p4alN5twmlCVJrY5mK7/jUGGzZYdNx07SuxR4dEVFLvaUGA+Qz/tJVQP966Tx9+l/2h368seVU/kgio+xV+YAmSQo1GARfCPbioldDwvpPgdM94c7gtYZCbsuFo9Do2vg==";
        //===================服务端================
        //将Base64编码后的私钥转换成PrivateKey对象
        PrivateKey privateKey = RSA.string2PrivateKey(private_key.trim());
        //加密后的内容Base64解码
        byte[] base642Byte = RSA.base642Byte(byte2Base64);
        //用私钥解密
        byte[] privateDecrypt = RSA.privateDecrypt(base642Byte, privateKey);
        //解密后的明文
        System.out.println("解密后的明文: " + new String(privateDecrypt));
    }

}
