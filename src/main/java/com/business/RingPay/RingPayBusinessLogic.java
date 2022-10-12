package com.business.RingPay;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.Datasheet.RingPay_TestData_DataProvider;
import com.android.RingPayPages.RingLoginPage;
import com.driverInstance.CommandBase;
import com.extent.ExtentReporter;
import com.propertyfilereader.PropertyFileReader;
import com.utility.LoggingUtils;
import com.utility.Utilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class RingPayBusinessLogic extends Utilities{
	
	
	public RingPayBusinessLogic(String Application) throws InterruptedException {
		new CommandBase(Application);
		init();
	}
    
	RingPay_TestData_DataProvider dataProvider = new RingPay_TestData_DataProvider();
	private int timeout;
	SoftAssert softAssertion = new SoftAssert();
	boolean launch = "" != null;
	/** Retry Count */
	private int retryCount;
	ExtentReporter extent = new ExtentReporter();

	/** The Constant logger. */
	static LoggingUtils logger = new LoggingUtils();

	/** The Android driver. */
	public AndroidDriver<AndroidElement> androidDriver;

	public static boolean relaunchFlag = false;
	public static boolean appliTools = false;

	public static boolean PopUp = false;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;

	}


	/**
	 * Initiate Property File.
	 *
	 * @param byLocator the by locator
	 */

	public void init() {

		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setTimeout(Integer.parseInt(handler.getproperty("TIMEOUT")));
		setRetryCount(Integer.parseInt(handler.getproperty("RETRY_COUNT")));
		logger.info(
				"Loaded the following properties" + " TimeOut :" + getTimeout() + " RetryCount :" + getRetryCount());
	}

	public void TearDown() {
		logger.info("App tear Down");
		getDriver().quit();
	}
	
	/**
	 * Business method for RingPay Application Launch
	 * 
	 */
	
	public void RingPayAppLaunch() throws Exception {
		extent.HeaderChildNode("RingPay App Launch");

		explicitWaitVisibility(RingLoginPage.objCamPermHeader,20);
		if(verifyElementPresent(RingLoginPage.objCamPermHeader, "Camera Permission required")) {
			String camPermHeaderTxt = getText(RingLoginPage.objCamPermHeader);
			Assert.assertEquals(camPermHeaderTxt, "Camera Permission required");
			logger.info("Camera Permission required popup");
			extent.extentLoggerPass("Camera Permission popup", "Camera Permission required popup");

			if(verifyElementPresent(RingLoginPage.objCamPermPopUp,"Enable Camera Permissions button")) {
				explicitWaitVisibility(RingLoginPage.objCamPermPopUp,10);
				Aclick(RingLoginPage.objCamPermPopUp, "Enable permissions button");

				if(verifyElementPresent(RingLoginPage.objAllowCamera,"Foreground allow camera permissions")) {
					logger.info("Foreground allow camera permissions");
					extent.extentLoggerPass("Foreground allow camera permissions", "Foreground allow camera permissions options");
					explicitWaitVisibility(RingLoginPage.objAllowCamera,10);
					Aclick(RingLoginPage.objAllowCamera, "While using the app foreground camera permission option");
				}

				else {
					logger.info("Foreground camera permission options are not displayed");
					extent.extentLoggerFail("Foreground camera permission options", "Foreground camera permission options are not displayed");
				}

			}

			else {
				logger.info("Enable Camera Permissions button is not displayed");
				extent.extentLoggerFail("Enable Camera Permissions button", "Enable Camera Permissions button is not Displayed");
			}

		}

		else {
			logger.info("Popup is not displayed");
			extent.extentLoggerFail("Allow popup", "Allow popup not displayed");
		}
	}
	
	/**
	 * Business method for RingPay Application Login
	 * @param = Mobile Number
	 * 
	 */
	public void ringPayLogin(String mobileNumber) throws Exception{
		extent.HeaderChildNode("RingPay App Login");
		
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader,10);
		if(verifyElementPresent(RingLoginPage.objQrCodeHeader, "Don't have QR Code header text")) {
			logger.info("Don't have QR Code header");
			extent.extentLoggerPass("Don't have QR Code header", "Don't have QR Code header is displayed");
			
			if(verifyElementPresent(RingLoginPage.objLoginLink,"Signup/Login link")) {
				logger.info("Signup/Login link");
				Aclick(RingLoginPage.objLoginLink, "Signup/Login link");
				
				explicitWaitVisibility(RingLoginPage.objLoginHeader,10);
				if(verifyElementPresent(RingLoginPage.objLoginHeader,"Signup/Login header")) {
					logger.info("Signup/Login Header");
					String loginHeaderTxt = getText(RingLoginPage.objLoginHeader);
					Assert.assertEquals(loginHeaderTxt, "Sign Up / Login");
					extent.extentLoggerPass("Signup/Login header", "Signup/Login header is displayed");
					
					explicitWaitVisibility(RingLoginPage.objLoginMobile,10);
					Aclick(RingLoginPage.objLoginMobile, "Continue with Mobile option");
					
					Wait(2000);
					if(verifyElementPresent(RingLoginPage.objTruSkipBtn,"Truecaller skip button")) {
						logger.info("True caller popup");
						extent.extentLoggerPass("True caller popup", "True caller popup is displayed");
						
						Aclick(RingLoginPage.objTruSkipBtn, "Truecaller skip button");
						explicitWaitVisibility(RingLoginPage.objNoneBtn,10);
						Aclick(RingLoginPage.objNoneBtn, "None of the above");
						
						explicitWaitVisibility(RingLoginPage.objVerifyMobHeader,10);
						String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
						Assert.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
						
						type(RingLoginPage.objMobTextField,mobileNumber,"Mobile text field");
						
						explicitWaitVisibility(RingLoginPage.objOtpHeader,10);
						if(verifyElementPresent(RingLoginPage.objOtpHeader,"OTP page")) {
							String otpHeaderTxt = getText(RingLoginPage.objOtpHeader);
							Assert.assertEquals(otpHeaderTxt, "Enter OTP");
							
							waitTime(3000);
							Aclick(RingLoginPage.objOtpTxtField1,"OTP Text Field");
							for(int i=1;i<=6;i++) {
								type(RingLoginPage.objOtpTxtField1,"8"," "+i+"th OTP text Field");
							}
							
							explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader,10);
							
							if(verifyElementPresent(RingLoginPage.objRingPermissionsHeader,"RingPay permissions")) {
								
								logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
								String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
								Assert.assertEquals(ringPermissionTxt, "Permissions");
								
								Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
								
								explicitWaitVisibility(RingLoginPage.objLocAccess,10);
							    
								Aclick(RingLoginPage.objLocAccess, "Location Access option");
								Aclick(RingLoginPage.objPhoneAccess,"Phone access option");
								Aclick(RingLoginPage.objSMSAccess,"SMS access option");
							}
							
							else {
								logger.info("RingPay permissions page is not displayed");
								extent.extentLoggerFail("RingPay permissions page", "RingPay permissions page is not Displayed");
								
							}
						}
						
						else {
							logger.info("OTP Page not displayed");
							extent.extentLoggerFail("OTP Page", "OTP Page not displayed");
						}
					}
					
					else {
						logger.info("User does not have TrueCaller application");
						explicitWaitVisibility(RingLoginPage.objNoneBtn,10);
						Aclick(RingLoginPage.objNoneBtn, "None of the above");
						
						explicitWaitVisibility(RingLoginPage.objVerifyMobHeader,10);
						String verifyMobHeaderTxt = getText(RingLoginPage.objVerifyMobHeader);
						Assert.assertEquals(verifyMobHeaderTxt, "Verify Mobile");
						
						if(verifyElementPresent(RingLoginPage.objOtpHeader,"OTP page")) {
							String otpHeaderTxt = getText(RingLoginPage.objOtpHeader);
							Assert.assertEquals(otpHeaderTxt, "Enter OTP");
							
							waitTime(3000);
							for(int i=1;i<=6;i++) {
								type(RingLoginPage.objOtpTxtField1,"8"," "+i+"st OTP text Field");
							}
							
							explicitWaitVisibility(RingLoginPage.objRingPermissionsHeader,10);
							if(verifyElementPresent(RingLoginPage.objRingPermissionsHeader,"RingPay permissions")) {
								
								logger.info("Ring Pay Permissions page (SMS, LOCATION & PHONE)");
								String ringPermissionTxt = getText(RingLoginPage.objRingPermissionsHeader);
								Assert.assertEquals(ringPermissionTxt, "Permissions");
								
								Aclick(RingLoginPage.objReadAcceptBtn, "Read & Accept button");
								
								explicitWaitVisibility(RingLoginPage.objLocAccess,10);
							    
								Aclick(RingLoginPage.objLocAccess, "Location Access option");
								Aclick(RingLoginPage.objPhoneAccess,"Phone access option");
								Aclick(RingLoginPage.objSMSAccess,"SMS access option");
							}
							
							else {
								logger.info("RingPay permissions page is not displayed");
								extent.extentLoggerFail("RingPay permissions page", "RingPay permissions page is not Displayed");
								
							}
							
						}
						
						else {
							logger.info("OTP Page not displayed");
							extent.extentLoggerFail("OTP Page", "OTP Page not displayed");
						}
					}
				}
			}
		}
		
		else {
			logger.info("Don't have QR Code header is not displayed");
			extent.extentLoggerFail("Don't have QR Code header", "Don't have QR Code header is not Displayed");
			
		}
		
	}
	
	/**
	 * Business method for RingPay payment to merchant
	 * @param merchant UPI ID, Above limit amount, Withing limit amount
	 * 
	 */
	public void ringPaymentMerchant(String merchantID,String exceedAmount, String withinLimitAmount) throws Exception {
		extent.HeaderChildNode("RingPay payment - Merchant");
		
		explicitWaitVisibility(RingLoginPage.objAdHeader,10);
		if(verifyElementPresent(RingLoginPage.objAdHeader,"AD popup")) {
			String adHeaderTxt = getText(RingLoginPage.objAdHeader);
			Assert.assertEquals(adHeaderTxt, "You’ve been chosen!");

			explicitWaitVisibility(RingLoginPage.objAdCloseBtn,10);
			Aclick(RingLoginPage.objAdCloseBtn,"AD Close button");
			
			explicitWaitVisibility(RingLoginPage.objAvailLimitHeader,10);
			if(verifyElementPresent(RingLoginPage.objAvailLimitHeader,"Available limit header")) {
				String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
				Assert.assertEquals(availLimitHeaderTxt,"Available Limit");
				
				explicitWaitVisibility(RingLoginPage.objScanQRBtn,10);
				Aclick(RingLoginPage.objScanQRBtn,"QR Code button");
				
				explicitWaitVisibility(RingLoginPage.objCreditLimitHeader,10);
				if(verifyElementPresent(RingLoginPage.objCreditLimitHeader,"Merchant payment page")) {
					String creditLimitHeader = getText(RingLoginPage.objCreditLimitHeader);
					Assert.assertEquals(creditLimitHeader, "Use your credit limit to complete this payment.");
					
					String receiverIDTxt = getText(RingLoginPage.objReceiverID);
					Assert.assertEquals(receiverIDTxt, merchantID);
					
					//Negative Test case - above credit limit
					logger.info("More than credit limit - Negative Test Case");
					type(RingLoginPage.objPaymentField,exceedAmount,"Amount text field");
					
					explicitWaitVisibility(RingLoginPage.objExceedLimitMsg,10);
					String exceedFailTxt = getText(RingLoginPage.objExceedLimitMsg);
					Assert.assertEquals(exceedFailTxt, "You have entered a higher amount than your available limit. Re-enter the amount.");
					logger.info(exceedFailTxt);
					extent.extentLoggerPass("Exceeded limit test case", "Exceeded limit failure text is displayed");
					extent.extentLogger("Exceeded Limit Error Message", exceedFailTxt);
					
					clearField(RingLoginPage.objPaymentField,"Payment text field");
					
					//Positive test case - within credit limit
					logger.info("Within credit limit - Positive Test Case");
					type(RingLoginPage.objPaymentField,withinLimitAmount,"Amount text field");
					
					Aclick(RingLoginPage.objPayNowBtn,"Pay now button");
					
					explicitWaitVisibility(RingLoginPage.objConfPayHeader,10);
					String confPayHeaderTxt = getText(RingLoginPage.objConfPayHeader);
					Assert.assertEquals(confPayHeaderTxt, "Confirm Payment");
					logger.info("Confirm payment page");
					extent.extentLoggerPass("Confirm payment page", "Confirm payment page is displayed");
					
					//Negative test case - Wrong MPIN
					logger.info("MPIN Negative Test Case");
					Aclick(RingLoginPage.objPinTxtField,"MPIN field");
					for(int i=1;i<=4;i++) {
						type(RingLoginPage.objPinTxtField,"2"," "+i+"th MPIN Text Field");
					}
					
					Aclick(RingLoginPage.objContinueBtn,"MPIN Continue Button");
					explicitWaitVisibility(RingLoginPage.objIncorrectPinTxt,10);
					String pinErrorMsg = getText(RingLoginPage.objIncorrectPinTxt);
					
                    logger.info(pinErrorMsg);				
					Assert.assertEquals(pinErrorMsg, "Incorrect PIN");
					extent.extentLoggerPass("Negative case - Incorrect case", "Incorrect PIN error message is successfully displayed");
					extent.extentLogger("Incorrect MPIN", pinErrorMsg);
					
					clearField(RingLoginPage.objPinTxtField,"MPIN text field");
					
					//Positive test case - Correct MPIN
					logger.info("MPIN Positive Test Case");
					Aclick(RingLoginPage.objPinTxtField,"MPIN field");
					for(int i=1;i<=4;i++) {
						type(RingLoginPage.objPinTxtField,"1"," "+i+"th MPIN Text Field");
					}
					
					Aclick(RingLoginPage.objContinueBtn,"MPIN Continue Button");
					
					explicitWaitVisibility(RingLoginPage.objPaymentDoneHeader,10);
					if(verifyElementPresent(RingLoginPage.objPaymentDoneHeader,"Payment Done Header")) {
						String paymentDoneTxt = getText(RingLoginPage.objPaymentDoneHeader);
						Assert.assertEquals(paymentDoneTxt, "Payment Done!");
						logger.info("Payment Done confirmation");
						extent.extentLoggerPass("Payment Done", "Payment Done confirmation");
						
						Aclick(RingLoginPage.objHomePageBtn,"Home page button");
						
						waitTime(3000);
						explicitWaitVisibility(RingLoginPage.objAvailLimitHeader,10);
						Assert.assertEquals(availLimitHeaderTxt,"Available Limit");
						logger.info("Back to ring pay homepage");
						extent.extentLoggerPass("RingPay homepage", "Back to ring pay homepage from Payment confirmation page");
						
						String currentSpendsTxt = getText(RingLoginPage.objCurrentSpends);
						logger.info("Current spends :: " + currentSpendsTxt);
						extent.extentLoggerPass("Current spends", "Currents spends is :: " + currentSpendsTxt);
						
					}
					
					else {
						logger.info("Payment Failed");
						extent.extentLoggerFail("Payment Failed", "Payment Failed message is displayed");
						
					}
				}
				
				else {
					logger.info("Invalid merchant QR Code");
					extent.extentLoggerFail("Invalid QR Code", "Invalid merchant QR Code - can't make payment");
					
				}
			}
			
			else {
				logger.info("User is not logged in");
				extent.extentLoggerFail("Available limit header", "Available limit header is not Displayed");
			}
		}
		
		else {
			explicitWaitVisibility(RingLoginPage.objAvailLimitHeader,10);
			if(verifyElementPresent(RingLoginPage.objAvailLimitHeader,"Available limit header")) {
				String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
				Assert.assertEquals(availLimitHeaderTxt,"Available Limit");
				
				explicitWaitVisibility(RingLoginPage.objScanQRBtn,10);
				Aclick(RingLoginPage.objScanQRBtn,"QR Code button");
				
				explicitWaitVisibility(RingLoginPage.objCreditLimitHeader,10);
				if(verifyElementPresent(RingLoginPage.objCreditLimitHeader,"Merchant payment page")) {
					String creditLimitHeader = getText(RingLoginPage.objCreditLimitHeader);
					Assert.assertEquals(creditLimitHeader, "Use your credit limit to complete this payment.");
				}
				
				else {
					logger.info("Invalid merchant QR Code");
					extent.extentLoggerFail("Invalid QR Code", "Invalid merchant QR Code - can't make payment");
					
				}
			}
			
			else {
				logger.info("User is not logged in");
				extent.extentLoggerFail("Available limit header", "Available limit header is not Displayed");
			}
		}
	}
	
	/**
	 * Business method for RingPay Transaction Details capturing
	 * 
	 */
	
	public void ringPayTransactionDetails() throws Exception{
		extent.HeaderChildNode("RingPay Transaction Details");
		
		explicitWaitVisibility(RingLoginPage.objTransacBtn,10);
		Aclick(RingLoginPage.objTransacBtn,"Transaction Button");
		waitTime(3000);
		explicitWaitVisibility(RingLoginPage.objRecentTransHeader,10);
		waitTime(3000);
		String recentTransTxt = getText(RingLoginPage.objRecentTransHeader);
		Assert.assertEquals(recentTransTxt, "Recent Transactions");
		logger.info("Recent Transaction Page is displayed");
		extent.extentLoggerPass("Recent Transactions", "Recent Transaction Page is displayed");
		
		explicitWaitVisibility(RingLoginPage.objMostRecentTrans,10);
		Aclick(RingLoginPage.objMostRecentTrans,"Most recent payment transaction");
		
		explicitWaitVisibility(RingLoginPage.objTransacDetailHeader,10);
		String transacDetailsTxt = getText(RingLoginPage.objTransacDetailHeader);
		Assert.assertEquals(transacDetailsTxt,"Transaction Details");
		logger.info("User is redirected to Transaction Details page");
		extent.extentLoggerPass("Transactions Details Page", "User is redirected to Transaction Details page");
		
		String transacNumber = getText(RingLoginPage.objTransacNumber);
		String payee = getText(RingLoginPage.objMostRecentTrans);
		logger.info("Most recent transaction number to payee " + payee + " is :: " + transacNumber);
		extent.extentLoggerPass("Transaction Number", "Most recent transaction number to payee " + payee + " is :: " + transacNumber);
		
		Aclick(RingLoginPage.objBackBtn,"Transaction Details Back Button");
		
		explicitWaitVisibility(RingLoginPage.objAvailLimitHeader,10);
		String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
		Assert.assertEquals(availLimitHeaderTxt,"Available Limit");
		logger.info("Back to ring pay homepage");
		extent.extentLoggerPass("RingPay homepage", "Back to ring pay homepage from Payment confirmation page");	
	}
	
	/**
	 * Business method for RingPay Application repayment
	 * @param CVV
	 * 
	 */
	
	public void ringRepayment(String cvv,String reloginMobNumber) throws Exception {
		extent.HeaderChildNode("RingPay Repayment");
		
		Aclick(RingLoginPage.objPayEarlyBtn,"Repayment button");
		
		explicitWaitVisibility(RingLoginPage.objRepaymentHeader,10);
		if(verifyElementPresent(RingLoginPage.objRepaymentHeader,"Repayment header")) {
			String repayHeaderTxt = getText(RingLoginPage.objRepaymentHeader);
			Assert.assertEquals(repayHeaderTxt, "Payment");
			logger.info("Repayment page");
			extent.extentLoggerPass("Repayment page", "Repayment header is displayed");
					
			Aclick(RingLoginPage.objCardOptionBtn,"Net banking & Debit Card");
			
			explicitWaitVisibility(RingLoginPage.objLoanPaymentHeader,10);
			if(verifyElementPresent(RingLoginPage.objLoanPaymentHeader,"Ring loan payment header")) {
				String loanPayHeaderTxt = getText(RingLoginPage.objLoanPaymentHeader);
				Assert.assertEquals(loanPayHeaderTxt, "Test Loan payment");
				

				Aclick(RingLoginPage.objCardOption,"Card option");
				
				if(verifyElementPresent(RingLoginPage.objOtpRepaymentHeader,"OTP Repayment header")) {
					//explicitWaitVisibility(RingLoginPage.objOtpRepaymentHeader,10);
					waitTime(3000);
					if(verifyElementPresent(RingLoginPage.objOtpForeAllow,"Foreground Allow Button")) {
						Aclick(RingLoginPage.objOtpForeAllow,"Foreground Allow Button");
						
						waitTime(5000);
						explicitWaitVisibility(RingLoginPage.objVerifyBtn,10);
						Aclick(RingLoginPage.objVerifyBtn,"OTP Verify Button");
					    
						waitTime(4000);
						if(verifyElementPresent(RingLoginPage.objSavedCardsHeader,"Saved Cards Header")) {
							explicitWaitVisibility(RingLoginPage.objSavedCardsHeader, 10);
						String cardHeaderTxt = getText(RingLoginPage.objSavedCardsHeader);
						Assert.assertEquals(cardHeaderTxt, "YOUR SAVED CARDS");
						logger.info("User is redirected to SAVED Cards page");
						extent.extentLoggerPass("Saved cards page", "User is redirected to SAVED Cards page");

						Aclick(RingLoginPage.objCardSelect, "Saved card ending with 1111");

						explicitWaitVisibility(RingLoginPage.objCVVField, 10);
						type(RingLoginPage.objCVVField, cvv, "CVV Text field");

						Aclick(RingLoginPage.objPayFooterBtn, "Repayment footer button");

						explicitWaitVisibility(RingLoginPage.objRazorPayHeader, 10);
						String razorHeaderTxt = getText(RingLoginPage.objRazorPayHeader);
						Assert.assertEquals(razorHeaderTxt, "Welcome to Razorpay Software Private Ltd Bank");
						logger.info("User is redirected to razor pay page");
						extent.extentLoggerPass("Razor pay", "User is redirected to razor pay page");

						Aclick(RingLoginPage.objSuccessBtn, "Payment success button");

						waitTime(21000);
						// explicitWaitVisibility(RingLoginPage.objRepaySuccessMsg,15);
						if (verifyIsElementDisplayed(RingLoginPage.objRepaySuccessMsg, "Your payment was successful")) {
							logger.info("App is not stuck");
							extent.extentLoggerPass("Application Readiness",
									"App is not stuck, proceeding further....");
							// explicitWaitVisibility(RingLoginPage.objRepaySuccessMsg,15);
							// verifyIsElementDisplayed(RingLoginPage.objRepaySuccessMsg,"Your payment was
							// successful");
							String repaySuccessTxt = getText(RingLoginPage.objRepaySuccessMsg);
							Assert.assertEquals(repaySuccessTxt, "Your payment was successful");
							logger.info("User is redirected to repayment success page");
							extent.extentLoggerPass("Repayment success",
									"User is redirected to repayment success page");

							Aclick(RingLoginPage.objGoHomePageBtn, "Go to homepage footer button");

							explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
							String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
							Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
							logger.info("Back to ring pay homepage");
							extent.extentLoggerPass("RingPay homepage",
									"Back to ring pay homepage from transaction details page");
						} else {
							System.out.println("App is stuck");
							logger.info("App is stuck");
							extent.extentLoggerWarning("Application Readiness",
									"Application is hung in repayment success page, killing and relaunching the app...");
							closeAndroidApp();
							RingPayAppLaunch();
							ringPayLogin(reloginMobNumber);
							explicitWaitVisibility(RingLoginPage.objAdCloseBtn, 10);
							Aclick(RingLoginPage.objAdCloseBtn, "AD Close button");
							explicitWaitVisibility(RingLoginPage.objAvailLimitHeader, 10);
							String availLimitHeaderTxt = getText(RingLoginPage.objAvailLimitHeader);
							Assert.assertEquals(availLimitHeaderTxt, "Available Limit");
							logger.info("Back to ring pay homepage");
							extent.extentLoggerPass("RingPay homepage",
									"Back to ring pay homepage from transaction details page");
//					Aclick(RingLoginPage.objTopMenu,"Top left menu button");
//					
//					explicitWaitVisibility(RingLoginPage.objTopMenu,10);
//					Aclick(RingLoginPage.objProfileSelect,"Profile Select Button");

						}
						}
					}
					else {
						waitTime(5000);
						String msgOtp = fetchOtp();
						System.out.println(msgOtp);
						
						explicitWaitVisibility(RingLoginPage.objOtpTextField,10);
						type(RingLoginPage.objOtpTextField,msgOtp,"OTP Text field");
						
						explicitWaitVisibility(RingLoginPage.objVerifyBtn,10);
						Aclick(RingLoginPage.objVerifyBtn,"OTP Verify Button");
						
						explicitWaitVisibility(RingLoginPage.objSavedCardsHeader,10);
						String cardHeaderTxt = getText(RingLoginPage.objSavedCardsHeader);
						Assert.assertEquals(cardHeaderTxt,"YOUR SAVED CARDS");
						logger.info("User is redirected to SAVED Cards page");
						extent.extentLoggerPass("Saved cards page","User is redirected to SAVED Cards page");
					}
						
					}
				else {
					waitTime(5000);
					logger.info("User is filling card details again");
					explicitWaitVisibility(RingLoginPage.objRepayFailCardNo,10);
					type(RingLoginPage.objRepayFailCardNo,"4111111111111111","Card Number");
					type(RingLoginPage.objRepayFailCardExpiry,"10/25","Card Expiry");
					type(RingLoginPage.objRepayFailHolderName,"shakir","Card holder's name");
					type(RingLoginPage.objRepayFailCVV,"123","Card CVV");
					
					Aclick(RingLoginPage.objPayFooterBtn,"Repayment footer button");
					
					explicitWaitVisibility(RingLoginPage.objRepayFailPopup,15);
					String repayFailTxt = getText(RingLoginPage.objRepayFailPopup);
					Assert.assertEquals(repayFailTxt,"Something went wrong, please try again after sometime.");
					logger.info("Repayment Failed");
					closeAndroidApp();
					extent.extentLoggerWarning("Repayment Failed", "Repayment is failed logging out...");
					//extent.extentLoggerWarning(loanPayHeaderTxt, repayFailTxt)
					
				}
				
				
			}
		
				
			else {
				logger.info("User not redirected to loan payment page");
				extent.extentLoggerFail("Loan payment page", "User not redirected to loan payment page");
			}
		}
		
		else {
			logger.info("User not redirected to repayment page");
			extent.extentLoggerFail("Repayment page", "User not redirected to repayment page");
			
		}
		
	}
	
	/**
	 * Business method for RingPay Application Logout
	 * 
	 */
	
	public void ringPayLogout() throws Exception{
		extent.HeaderChildNode("RingPay Logout");
		
		explicitWaitVisibility(RingLoginPage.objTopMenu,10);
		Aclick(RingLoginPage.objTopMenu,"Top left menu button");
		
		explicitWaitVisibility(RingLoginPage.objProfileSelect,10);
		Aclick(RingLoginPage.objProfileSelect,"Profile Select Button");
		
		explicitWaitVisibility(RingLoginPage.objLogoutBtn,10);
		
		Aclick(RingLoginPage.objLogoutBtn,"Logout Button");
		
		explicitWaitVisibility(RingLoginPage.objLogoutTxt,10);
		String logoutTxt = getText(RingLoginPage.objLogoutTxt);
		Assert.assertEquals(logoutTxt, "Are you sure you want to logout?");
		logger.info("Logout popup comes up");
		extent.extentLoggerPass("Logout popup", "Logout popup comes up");
		
		Aclick(RingLoginPage.objYesBtn,"Yes confirmation button");	
		
		explicitWaitVisibility(RingLoginPage.objQrCodeHeader,10);
		String logoutConfTxt = getText(RingLoginPage.objQrCodeHeader);
		Assert.assertEquals(logoutConfTxt,"Don't have a QR code?");
		logger.info("User is successfully logged out");
		extent.extentLoggerPass("Logout confirmation", "User is successfully logged out");
		
	}
	
	/**
	 * Business method for RingPay OTP API positive scenario
	 * 
	 */
	
	public void valid_otp_200(String url) throws Exception {
		extent.HeaderChildNode("OTP API - Positive");
		
        Object[][] data = dataProvider.RingPayAPIData("otp_200");
        ValidatableResponse response = RingPayAPI(data , url);

        //Status Code Validation
        logger.info("Status Code Validation for OTP API - Positive Scenario...");
        Assert.assertEquals(response.extract().statusCode(), 200);
        extent.extentLoggerPass("Status Code Validation", "OTP API Status Code Validation for positive scenario is successful");
        String otpStatus = String.valueOf(response.extract().statusCode());
        logger.info("OTP API response status code Validation is Successful");
        extent.extentLoggerPass("OTP API Status Code", "Repsonse status code is: " + otpStatus);

        //Body Validation
        logger.info("Response Body Validation for OTP API - Postive Scenario...");
        assertNotNull("'request_id' is not null", response.extract().body().jsonPath().get("request_id"));
        String requestId = response.extract().body().jsonPath().get("request_id");
        extent.extentLogger("Request ID", "Request ID from response body: " + requestId);
        Assert.assertTrue(response.extract().body().jsonPath().get("success"));
        assertNotNull(response.extract().body().jsonPath().get("response_code"));
        Assert.assertEquals(response.extract().body().jsonPath().get("message"), "Success");
        Assert.assertTrue(response.extract().body().jsonPath().get("data.user_exists"));
        logger.info("Response Body Validation for OTP API positive scenario is successful");
        extent.extentLoggerPass("Body Validation", "Response Body Validation for positive scenario is successful");

        //Schema Validation
        logger.info("Schema Validation for OTP API - Positive Scenario...");
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("otp_200_schema.json"));
        logger.info("Schema Validation for positive scenario is successful");
        extent.extentLoggerPass("Schema Validation","Schema Validation for positive scenario is successful");
    }

	/**
	 * Business method for RingPay AUTH API positive scenario
	 * 
	 */
	
    public void valid_auth_200(String url) throws Exception {
    	extent.HeaderChildNode("AUTH API - Positive");
    	
        Object[][] data = dataProvider.RingPayAPIData("auth_200");
        ValidatableResponse response = RingPayAPI(data, url);

        logger.info("Status Code Validation for AUTH API - Positive Scenario...");
        Assert.assertEquals(response.extract().statusCode(), 200);
        extent.extentLoggerPass("Status Code Validation", "AUTH API Status Code Validation for positive scenario is successful");
        String authStatus = String.valueOf(response.extract().statusCode());
        logger.info("AUTH API response status code Validation is Successful");
        extent.extentLoggerPass("AUTH API Status Code", "Repsonse status code is: " +authStatus);

        logger.info("Response Body Validation for AUTH API - Postive Scenario...");
        assertNotNull("'request_id' is not null", response.extract().body().jsonPath().get("request_id"));
        String requestId = response.extract().body().jsonPath().get("request_id");
        extent.extentLogger("Request ID", "Request ID from response body: " + requestId);
        Assert.assertTrue(response.extract().body().jsonPath().get("success"));
        assertNotNull(response.extract().body().jsonPath().get("response_code"));
        Assert.assertEquals(response.extract().body().jsonPath().get("message"), "Success");
        assertNotNull(response.extract().body().jsonPath().get("data.user_token"));
        String userToken = response.extract().body().jsonPath().get("data.user_token");
        extent.extentLogger("User Token", "User token from response body: " + userToken);
        assertNotNull(response.extract().body().jsonPath().get("data.encrypted_user_reference_number"));
        logger.info("Response Body Validation for AUTH API positive scenario is successful");
        extent.extentLoggerPass("Body Validation", "Response Body Validation for positive scenario is successful");

        //Schema Validation
        logger.info("Schema Validation for AUTH API - Positive Scenario...");
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("auth_200_schema.json"));
        logger.info("Schema Validation for positive scenario is successful");
        extent.extentLoggerPass("Schema Validation","Schema Validation for positive scenario is successful");
    }

    /**
	 * Business method for RingPay AUTH API Negative scenario
	 * 
	 */
    
    public void invalid_auth_400(String url) throws IOException {
    	extent.HeaderChildNode("AUTH API - Negative");
    	
        Object[][] data = dataProvider.RingPayAPIData("auth_400");
        ValidatableResponse response = RingPayAPI(data, url);

        //Status Code Validation
        logger.info("Status Code Validation for AUTH API - Negative Scenario...");
        Assert.assertEquals(response.extract().statusCode(), 400);
        extent.extentLoggerPass("Status Code Validation", "AUTH API Status Code Validation for negative scenario is successful");
        String authStatus = String.valueOf(response.extract().statusCode());
        logger.info("AUTH API response status code Validation is Successful");
        extent.extentLoggerPass("AUTH API Status Code", "Repsonse status code is: " +authStatus);

        //Body Validation
        logger.info("Response Body Validation for AUTH API - Negative Scenario...");
        assertNotNull("'request_id' is not null", response.extract().body().jsonPath().get("request_id"));
        Assert.assertFalse(response.extract().body().jsonPath().get("success"));
        assertNotNull(response.extract().body().jsonPath().get("response_code"));
        assertNotNull(response.extract().body().jsonPath().get("message"));
        String message = response.extract().body().jsonPath().get("message");
        extent.extentLogger("Body Message", "Message from response body is: " + message);
        logger.info("Response Body Validation for AUTH API negative scenario is successful");
        extent.extentLoggerPass("Body Validation", "Response Body Validation for negative scenario is successful");

        //Schema Validation
        logger.info("Schema Validation for AUTH API - Negative Scenario...");
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("auth_400_schema.json"));
        logger.info("Schema Validation for negative scenario is successful");
        extent.extentLoggerPass("Schema Validation","Schema Validation for positive scenario is successful");
    }
    
    /**
	 * Business method for RingPay OTP API negative scenario
	 * 
	 */
    public void invalid_otp_400(String url) throws Exception {
    	extent.HeaderChildNode("OTP  API - Negative");
    	
        Object[][] data = dataProvider.RingPayAPIData("otp_400");
        ValidatableResponse response = RingPayAPI(data, url);

        logger.info("Status Code Validation for OTP API - Negative Scenario...");
        Assert.assertEquals(response.extract().statusCode(), 400);
        String otpStatus = String.valueOf(response.extract().statusCode());
        logger.info("OTP API response status code Validation is Successful");
        extent.extentLoggerPass("OTP API Status Code", "Repsonse status code is: " + otpStatus);

        logger.info("Response Body Validation for OTP API - Negative Scenario...");
        assertNotNull("'request_id' is not null", response.extract().body().jsonPath().get("request_id"));
        Assert.assertFalse(response.extract().body().jsonPath().get("success"));
        assertNotNull(response.extract().body().jsonPath().get("response_code"));
        assertNotNull(response.extract().body().jsonPath().get("message"));
        String message = response.extract().body().jsonPath().get("message");
        extent.extentLogger("Body Message", "Message from response body is: " + message);
        logger.info("Response Body Validation for OTP API Negative scenario is successful");
        extent.extentLoggerPass("Body Validation", "Response Body Validation for Negative scenario is successful");


        //Schema Validation
        logger.info("Schema Validation for OTP API - Negative Scenario...");
        assertThat(response.extract().body().asString(), JsonSchemaValidator.matchesJsonSchemaInClasspath("otp_400_schema.json"));
        logger.info("Schema Validation for negative scenario is successful");
        extent.extentLoggerPass("Schema Validation","Schema Validation for negative scenario is successful");
    }

}
