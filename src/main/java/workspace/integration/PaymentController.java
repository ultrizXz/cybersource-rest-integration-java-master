package workspace.integration;

import Api.CaptureApi;
import Api.PaymentsApi;
import Api.RefundApi;
import Api.ReversalApi;
import org.springframework.web.bind.annotation.*;
import workspace.data.Configuration;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.*;
import com.cybersource.authsdk.core.MerchantConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@RestController

@RequestMapping("/payment/rest")
public class PaymentController {

    private static String responseCode = null;
    private static String status = null;
    private static Properties merchantProp;
    public static boolean userCapture = false;

    public static void WriteLogAudit(int status) {
        String filename = MethodHandles.lookup().lookupClass().getSimpleName();
        System.out.println("[Sample Code Testing] [" + filename + "] " + status);
    }

    @GetMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public PtsV2PaymentsPost201Response paymentAuth() {

        System.out.println("rest auth payment request");

        CreatePaymentRequest requestObj = new CreatePaymentRequest();

        // Merchant details
        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code("test_crr_mx_1744172384");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsProcessingInformation processingInformation = new Ptsv2paymentsProcessingInformation();
        processingInformation.capture(false);
        if (userCapture) {
            processingInformation.capture(true);
        }
        requestObj.processingInformation(processingInformation);

        // Card details
        Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
        Ptsv2paymentsPaymentInformationCard paymentInformationCard = new Ptsv2paymentsPaymentInformationCard();
        paymentInformationCard.number("4111111111111110");
        paymentInformationCard.expirationMonth("12");
        paymentInformationCard.expirationYear("2031");
        paymentInformation.card(paymentInformationCard);
        requestObj.paymentInformation(paymentInformation);

        // price & currency
        Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
        Ptsv2paymentsOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("102.21");
        orderInformationAmountDetails.currency("USD");
        orderInformation.amountDetails(orderInformationAmountDetails);
        requestObj.orderInformation(orderInformation);

        // billing address
        Ptsv2paymentsOrderInformationBillTo orderInformationBillTo = new Ptsv2paymentsOrderInformationBillTo();
        orderInformationBillTo.firstName("John");
        orderInformationBillTo.lastName("Doe");
        orderInformationBillTo.address1("1 Market St");
        orderInformationBillTo.locality("san francisco");
        orderInformationBillTo.administrativeArea("CA");
        orderInformationBillTo.postalCode("94105");
        orderInformationBillTo.country("US");
        orderInformationBillTo.email("test@cybs.com");
        orderInformationBillTo.phoneNumber("4158880000");
        orderInformation.billTo(orderInformationBillTo);
        requestObj.orderInformation(orderInformation);
        PtsV2PaymentsPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;
            System.out.println("requestObject :" + requestObj);
            PaymentsApi apiInstance = new PaymentsApi(apiClient);
            result = apiInstance.createPayment(requestObj);
            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);
            //WriteLogAudit(Integer.parseInt(responseCode));
        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "/capture", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public PtsV2PaymentsCapturesPost201Response paymentCapture(@PathVariable String id) {
        System.out.println("Capture === ");

        CapturePaymentRequest requestObj = new CapturePaymentRequest();

        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code("test_crr_mx_1744172384");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsidcapturesOrderInformation orderInformation = new Ptsv2paymentsidcapturesOrderInformation();
        Ptsv2paymentsidcapturesOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsidcapturesOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("102.21");
        orderInformationAmountDetails.currency("USD");
        orderInformation.amountDetails(orderInformationAmountDetails);

        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsCapturesPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            CaptureApi apiInstance = new CaptureApi(apiClient);
            System.out.println("requestObject :" + requestObj);
            result = apiInstance.capturePayment(requestObj, id);
            //result = apiInstance.capturePayment(requestObj, "7163189199856969903954");

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);
            WriteLogAudit(Integer.parseInt(responseCode));
        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @GetMapping(value = "/auth-capture", produces = MediaType.APPLICATION_JSON_VALUE)
    public PtsV2PaymentsPost201Response paymentAuthAndCapture() {
        System.out.println("rest auth and capture payment request");

        CreatePaymentRequest requestObj = new CreatePaymentRequest();

        // Merchant details
        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code("test_crr_mx_1744172384");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        // Processing information (Authorize and capture)
        Ptsv2paymentsProcessingInformation processingInformation = new Ptsv2paymentsProcessingInformation();
        processingInformation.capture(true); // Directly capture after authorization
        requestObj.processingInformation(processingInformation);

        // Card details
        Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
        Ptsv2paymentsPaymentInformationCard paymentInformationCard = new Ptsv2paymentsPaymentInformationCard();
        paymentInformationCard.number("4111111111111111");
        paymentInformationCard.expirationMonth("12");
        paymentInformationCard.expirationYear("2031");
        paymentInformation.card(paymentInformationCard);
        requestObj.paymentInformation(paymentInformation);

        // Price & currency
        Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
        Ptsv2paymentsOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("102.21");
        orderInformationAmountDetails.currency("USD");
        orderInformation.amountDetails(orderInformationAmountDetails);
        requestObj.orderInformation(orderInformation);

        // Billing address
        Ptsv2paymentsOrderInformationBillTo orderInformationBillTo = new Ptsv2paymentsOrderInformationBillTo();
        orderInformationBillTo.firstName("John");
        orderInformationBillTo.lastName("Doe");
        orderInformationBillTo.address1("1 Market St");
        orderInformationBillTo.locality("San Francisco");
        orderInformationBillTo.administrativeArea("CA");
        orderInformationBillTo.postalCode("94105");
        orderInformationBillTo.country("US");
        orderInformationBillTo.email("test@cybs.com");
        orderInformationBillTo.phoneNumber("4158880000");
        orderInformation.billTo(orderInformationBillTo);
        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            PaymentsApi apiInstance = new PaymentsApi(apiClient);
            System.out.println("requestObject :" + requestObj);
            result = apiInstance.createPayment(requestObj);

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode :" + responseCode);
            System.out.println("ResponseMessage :" + status);
            System.out.println(result);

        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping(value = "/authreversal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PtsV2PaymentsReversalsPost201Response paymentReverseAuth(@PathVariable String id) {
        System.out.println("Payment Reverse Auth === ");

        AuthReversalRequest requestObj = new AuthReversalRequest();

        Ptsv2paymentsidreversalsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsidreversalsClientReferenceInformation();
        clientReferenceInformation.code("test_crr_mx_1744172384");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsidreversalsReversalInformation reversalInformation = new Ptsv2paymentsidreversalsReversalInformation();
        Ptsv2paymentsidreversalsReversalInformationAmountDetails reversalInformationAmountDetails = new Ptsv2paymentsidreversalsReversalInformationAmountDetails();
        reversalInformationAmountDetails.totalAmount("102.21");
        reversalInformation.amountDetails(reversalInformationAmountDetails);
        reversalInformation.reason("testing");

        requestObj.reversalInformation(reversalInformation);

        PtsV2PaymentsReversalsPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            ReversalApi apiInstance = new ReversalApi(apiClient);
            System.out.println("requestObject :" + requestObj);
            result = apiInstance.authReversal(id, requestObj);
            //result = apiInstance.authReversal("7163184809226765403955", requestObj);

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("Response Code :" + responseCode);
            System.out.println("Response Message :" + status);
            System.out.println(result);
            WriteLogAudit(Integer.parseInt(responseCode));
        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public PtsV2PaymentsRefundPost201Response paymentRefund(@PathVariable String id) {
        RefundPaymentRequest requestObj = new RefundPaymentRequest();
        Ptsv2paymentsidrefundsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsidrefundsClientReferenceInformation();
        clientReferenceInformation.code("test_crr_mx_1744172384");
        requestObj.clientReferenceInformation(clientReferenceInformation);
        Ptsv2paymentsidrefundsOrderInformation orderInformation = new Ptsv2paymentsidrefundsOrderInformation();
        Ptsv2paymentsidcapturesOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsidcapturesOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("10");
        orderInformationAmountDetails.currency("USD");
        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsRefundPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;
            System.out.println("requestObject :" + requestObj);
            RefundApi apiInstance = new RefundApi(apiClient);
            //result = apiInstance.refundPayment(requestObj, "71631896525969823039");
            result = apiInstance.refundPayment(requestObj, id);

            responseCode = apiClient.responseCode;
            status = apiClient.status;

            System.out.println("Response Code :" + responseCode);
            System.out.println("Response Message :" + status);
            System.out.println(result);
            WriteLogAudit(Integer.parseInt(responseCode));
        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping(value = "/trigger-rejection", produces = MediaType.APPLICATION_JSON_VALUE)
    public PtsV2PaymentsPost201Response triggerRejection() {
        System.out.println("Triggering rejection via Decision Manager");

        CreatePaymentRequest requestObj = new CreatePaymentRequest();

        // Client Reference
        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        clientReferenceInformation.code("test_dm_reject_rule");
        requestObj.clientReferenceInformation(clientReferenceInformation);

        // Processing information
        Ptsv2paymentsProcessingInformation processingInformation = new Ptsv2paymentsProcessingInformation();
        processingInformation.capture(false);
        requestObj.processingInformation(processingInformation);

        // Payment Information
        Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
        Ptsv2paymentsPaymentInformationCard paymentInformationCard = new Ptsv2paymentsPaymentInformationCard();
        paymentInformationCard.number("4111111111111111");
        paymentInformationCard.expirationMonth("12");
        paymentInformationCard.expirationYear("2031");
        requestObj.paymentInformation(paymentInformation);

        // Order Information
        Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
        Ptsv2paymentsOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount("100.00");
        orderInformationAmountDetails.currency("USD");
        orderInformation.amountDetails(orderInformationAmountDetails);
        requestObj.orderInformation(orderInformation);

        // Billing Information with target rejection email
        Ptsv2paymentsOrderInformationBillTo orderInformationBillTo = new Ptsv2paymentsOrderInformationBillTo();
        orderInformationBillTo.firstName("John");
        orderInformationBillTo.lastName("Doe");
        orderInformationBillTo.address1("1 Market St");
        orderInformationBillTo.locality("San Francisco");
        orderInformationBillTo.administrativeArea("CA");
        orderInformationBillTo.postalCode("94105");
        orderInformationBillTo.country("US");
        orderInformationBillTo.email("assessment@reject.com.br");  // Email that should trigger rejection rule
        orderInformationBillTo.phoneNumber("4158880000");
        orderInformation.billTo(orderInformationBillTo);
        requestObj.orderInformation(orderInformation);

        PtsV2PaymentsPost201Response result = null;

        try {
            merchantProp = Configuration.getMerchantDetails();
            ApiClient apiClient = new ApiClient();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            apiClient.merchantConfig = merchantConfig;

            PaymentsApi apiInstance = new PaymentsApi(apiClient);
            System.out.println("Request Object: " + requestObj);
            result = apiInstance.createPayment(requestObj);

            responseCode = apiClient.responseCode;
            status = apiClient.status;
            System.out.println("ResponseCode: " + responseCode);
            System.out.println("ResponseMessage: " + status);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
            WriteLogAudit(e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}