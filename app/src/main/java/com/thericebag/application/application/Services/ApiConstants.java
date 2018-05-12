package com.thericebag.application.application.Services;

/**
 * Created by abhilash on 01-09-2016.
 */

public class ApiConstants {
    public interface ApiName {

        //--------------------API URL---------------------------------------------//

//        public static final String BASE_API_URL = "https://www.thericebag.com/services/";    // Test Environment
        public static final String BASE_API_URL = "https://www.thericebag.com/services_production/";    // production URL

        //-------------------APIs Constants---------------------------------------//
        public static final String CHECKVERSION = BASE_API_URL + "checkVersion.php?";
        public static final String GETSUBCATEGORIES = BASE_API_URL + "getSubCategories.php?";
        public static final String GETPRODUCTDETAILS = BASE_API_URL + "getProductDetails.php?";
        public static final String GETFIRSTPAGEJSON = BASE_API_URL + "firstPageJson.php";
        public static final String CHECKLOGIN = BASE_API_URL + "login.php?";
        public static final String SIGNUP = BASE_API_URL + "signUp.php?";
        public static final String VERIFYOTP = BASE_API_URL + "OTPVerification.php?";
        public static final String UPDATECHECKOUTPRODUCTS = BASE_API_URL + "updateCheckOutProducts.php?";
        public static final String GETPROFILEDETAILS = BASE_API_URL + "getUserProfile.php?";
        public static final String RESENDOTP = BASE_API_URL + "resendOTP.php?";
        public static final String GETPAYMENTDETAILS = BASE_API_URL + "getPaymentDetails.php?";
        public static final String GET_MYORDERS = BASE_API_URL + "getMyOrders.php?";
        public static final String PLACE_ORDER = BASE_API_URL + "placeOrder.php";
        public static final String ORDER_CONFIRMED = BASE_API_URL + "confirmationToUser.php?";
        public static final String UPDATE_ORDER = BASE_API_URL + "updateOrderOnline.php";
        public static final String UPDATE_ADDRESS = BASE_API_URL + "updateAddress.php?";
        public static final String VERIFY_MAIL = BASE_API_URL + "verifyEmail.php?";
        public static final String UPDATE_PHONE_VERIFY_OTP = BASE_API_URL + "verifyOTPPhone.php?";
        public static final String VERIFY_PHONE_NUMBER = BASE_API_URL + "verifyPhoneNumber.php?";
        public static final String FORGOT_PASSWORD = BASE_API_URL + "forgotPassword.php?";
        public static final String VERIFY_PHONE_NUMBER_FORGOT_PASSWORD = BASE_API_URL + "verifyOTPForgotPassword.php?";
        public static final String RESET_PASSWORD = BASE_API_URL + "resetPassword.php?";
        public static final String RESEND_OTP_VERIFY_NUMBER = BASE_API_URL + "ResendOTPVerifyNumber.php?";
        public static final String RESEND_OTP_FORGOT_PASSWORD = BASE_API_URL + "ResendOTPForgotPassword.php?";
    }

    public interface StandardHeader {
        String HEADER_KEY_CONTENT_TYPE = "Content-Type";
        String HEADER_VALUE_APPLICATION_JSON = "application/json";
//        String HEADER_KEY_ANDROID_ID = "Android-Id";
//        String HEADER_KEY_COMPRESS_OUTPUT = "Compress-Output";
//        String HEADER_KEY_REQUEST_ID = "Request-Id";
//        String HEADER_KEY_AUTHORIZATION = "Authorization";
//        String HEADER_KEY_CLIENT_TIME = "Client-Time";
//        String MULTIPART_FORM_DATA = "multipart/form-data";
    }

}
