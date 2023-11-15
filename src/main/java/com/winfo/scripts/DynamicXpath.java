package com.winfo.scripts;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joox.selector.CSS2XPath;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.winfo.exception.PopupException;
import com.winfo.serviceImpl.AbstractSeleniumKeywords;
import com.winfo.serviceImpl.DynamicRequisitionNumber;
import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ELements;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public class DynamicXpath extends AbstractSeleniumKeywords {

	private Document doc;
	private String pageSource;
	private WebElement previousEle;
	private String copyNumber;
	private FetchConfigVO fetchConfigVO;

	@Autowired
	private XpathPerformance xpathPerformance;
	@Autowired
	DynamicRequisitionNumber dynamicnumber;

	public void dynamics(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails) throws Exception {
		updateDOM(driver);
		fetchConfigVO = fetchConfigVO;

		// to substitute the specified input parameters if they must be filled in
		if (fetchMetadataVO.getInputParameter() != null)
			fetchMetadataVO.setInputParameter(
					fetchMetadataVO.getInputParameter().replace("(*)", "").replace("(#)", "").replace("(#*)", ""));

		// Action-based Methods
		if (fetchMetadataVO.getAction().equals("Navigate")) {
			try {
				clickOnNavbar(fetchMetadataVO, customerDetails, fetchConfigVO, driver);
				Thread.sleep(2000);
				updateDOM(driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to execute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("tab")) {
			throw new Exception();
		} else if (fetchMetadataVO.getAction().equals("enter")) {
			throw new Exception();
		} else if (fetchMetadataVO.getAction().equals("pageLoad")) {
			// previousEle.sendKeys(Keys.TAB);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("waitTillLoad")) {
			try {
				int i = Integer.parseInt(fetchMetadataVO.getInputValue());
				Thread.sleep(i * 1000);
				updateDOM(driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to execute" + fetchMetadataVO.getLineNumber() + " in Dynamic Xpath");
				e.printStackTrace();
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("Dropdown Values")) {
			try {
				dropDownValues(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + " in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("copynumber")) {
			throw new NullPointerException();
			// try {
			// String[] copyNumberInputParameter =
			// fetchMetadataVO.getInputParameter().split(">");
			// if (copyNumberInputParameter[0].equals("Confirmation")) {
			// Element confirmationElement = doc
			// .selectFirst(":containsOwn('" + copyNumberInputParameter[0] + "')");

			// // Find the parent div element of the "Confirmation" text
			// Element confirmationParent = confirmationElement.parent();
			// while (confirmationParent != null && confirmationParent
			// .select(":contains('" + copyNumberInputParameter[1] + "')").isEmpty()) {
			// confirmationParent = confirmationParent.parent();
			// }
			// // Find the label element containing the "Process" text among its siblings
			// Element labelElement = confirmationParent
			// .selectFirst("label:contains('" + copyNumberInputParameter[1] + "')");
			// Pattern pattern = Pattern.compile("\\d+");

			// // Create a matcher with the input text
			// Matcher matcher = pattern.matcher(labelElement.text());

			// // Find and print all matching numbers
			// while (matcher.find()) {
			// String number = matcher.group();
			// System.out.println(number);
			// // String scripNumber = fetchMetadataVO.getScriptNumber();
			// // String testParamId = fetchMetadataVO.getTestScriptParamId();
			// // String testSetId = fetchMetadataVO.getTestSetLineId();
			// // dynamicnumber.saveCopyNumber(number, testParamId, testSetId);
			// // copyNumber = number;
			// String testParamId = fetchMetadataVO.getTestScriptParamId();
			// String testSetId = fetchMetadataVO.getTestSetLineId();
			// dynamicnumber.saveCopyNumber(number, testParamId, testSetId);
			// }
			// } else {
			// throw new NullPointerException();

			// }
			// } catch (Exception e) {
			// logger.error(e.getMessage());
			// logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + " in
			// Dynamic Xpath");
			// throw e;
			// }

		} else if (fetchMetadataVO.getAction().equals("windowhandle")) {
			throw new NullPointerException();
		} else if (fetchMetadataVO.getAction().equals("switchParentWindow")) {
			throw new NullPointerException();
			// try {
			//// String parentWindowHandle = driver.getWindowHandle();
			//
			// // Get the window handles of all open windows
			// Set<String> allWindowHandles = driver.getWindowHandles();
			// List<String> list = new ArrayList<>(allWindowHandles);
			// // Switch to the child window
			// if (allWindowHandles.size() == 1) {
			// driver.switchTo().window(list.get(0));
			//
			// } else {
			// for (String windowHandle : allWindowHandles) {
			// if (list.get(list.size() - 2).equals(windowHandle)) {
			// driver.switchTo().window(windowHandle);
			// break;
			// }
			// }
			// }
			// updateDOM(driver);
			// } catch (Exception e) {
			// logger.error(e.getMessage());
			// logger.error("Failed to exicute "+fetchMetadataVO.getScriptNumber()+" in
			// Dynamic Xpath");
			// throw e;
			// }

		} else if (fetchMetadataVO.getAction().equals("switchToParentWindow")) {
			try {
				String parentWindowHandle = driver.getWindowHandle();

				// Get the window handles of all open windows
				Set<String> allWindowHandles = driver.getWindowHandles();
				List<String> list = new ArrayList<>(allWindowHandles);
				// Switch to the child window
				for (String windowHandle : allWindowHandles) {
					if (list.get(0).equals(windowHandle)) {
						driver.switchTo().window(windowHandle);
						break;
					}
				}
				updateDOM(driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("switchToFrame")) {
			try {
				String cssSelector = getXpath(fetchMetadataVO, customerDetails, doc, false, driver);
				WebElement frameElement = driver.findElement(By.cssSelector(cssSelector));
				driver.switchTo().frame(frameElement);
				updateDOM(driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + " in Dynamic Xpath");
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("switchToDefaultFrame")) {
			try {
				driver.switchTo().defaultContent();
				updateDOM(driver);

			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickCheckbox")) {
			try {
				clickCheckBox(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickRadiobutton")) {
			try {
				clickRadioButton(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickExpandorcollapse")) {
			try {
				clickExpandorcollapse(fetchMetadataVO, driver, customerDetails, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("login")) {
			try {
				login(fetchMetadataVO, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("SendKeys")) {
			try {
				sendKeys(fetchMetadataVO, customerDetails, driver, false);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("datePicker")) {
			try {
				datePicker(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("textarea")) {
			try {
				textArea(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickButton")) {
			try {
				clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickMenu")) {
			try {
				clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickLink")) {
			try {
				clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickImage")) {
			try {
				clickImage(fetchMetadataVO, driver, customerDetails, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("paste")) {
			try {
				paste(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("copy")) {
			throw new NullPointerException();
			// try {
			// copy(fetchMetadataVO, customerDetails, driver);
			// } catch (Exception e) {
			// logger.error(e.getMessage());
			// logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + " in
			// Dynamic Xpath");
			// throw e;
			// }
		} else if (fetchMetadataVO.getAction().equals("clearText")) {
			try {
				clearText(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("selectByText")) {
			try {
				selectDropdownValues(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("tableRowSelect")) {
			try {
				tableRowSelect(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("openTask")) {
			try {
				clickImage(fetchMetadataVO, driver, customerDetails, fetchConfigVO);
				Thread.sleep(3000);
				updateDOM(driver);
				clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("selectAValue")) {
			try {
				Thread.sleep(3000);
				updateDOM(driver);
				selectAvalue(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("scrollUsingElement")) {
			try {
				scrollUsingElement(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("Table Dropdown Values")) {
			try {
				tableDropdownValues(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("Table SendKeys")) {
			try {
				tableSendKeys(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickTableLink")) {
			try {
				tableClickLink(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickLinkAction")) {
			try {
				clickLinkAction(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("tableRowSelect")) {
			try {
				tableRowSelect(fetchMetadataVO, customerDetails, driver);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("Logout")) {
			try {
				logOut(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickNotificationLink")) {
			try {
				clickNotificationLink(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("mousehover")) {
			try {
				mouseHover(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("multipleSendKeys")) {
			try {
				multipleSendKeys(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("compareValue")) {
			try {
				maxWaitTime(fetchMetadataVO, customerDetails, driver, fetchConfigVO);
			} catch (Exception e) {
				logger.error(e.getMessage());
				logger.error("Failed to exicute " + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw e;
			}
		} else {
			logger.error("cannot find given Action- " + fetchMetadataVO.getAction() + "  in Dynamic Xpath");
			throw new Exception();
		}

		if (fetchMetadataVO.getAction().equals("SendKeys") || fetchMetadataVO.getAction().equals("paste")
				|| fetchMetadataVO.getAction().equals("selectByText") || fetchMetadataVO.getAction().equals("login")) {

		} else {
			Thread.sleep(10000);
			updateDOM(driver);
		}
		// driver.quit();
	}

	private void maxWaitTime(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			FetchConfigVO fetchConfigVO2) throws Exception {
		try {
			String[] inputValue = fetchMetadataVO.getInputValue().split(">");
			if (inputValue.length < 3) {
				throw new NullPointerException();
			}
			if (!inputValue[inputValue.length - 1].equalsIgnoreCase("0")) {
				int i = Integer.parseInt(inputValue[inputValue.length - 1]);
				int seconds = (i - 1) * 2;
				Element Succeed = checkStatusSuccess(fetchMetadataVO.getInputParameter(), doc, driver, customerDetails,
						fetchMetadataVO);
				while (seconds > 0) {
					if (Succeed.text().equals(inputValue[0])) {
						break;
					} else if (Succeed.text().equals(inputValue[1])) {
						throw new PopupException(400, "Error failed status");
					} else {
						fetchMetadataVO.setInputParameter("Refresh");
						clickImage(fetchMetadataVO, driver, customerDetails, fetchConfigVO2);
						Thread.sleep(30000);
						updateDOM(driver);
					}
					seconds--;
				}
				if (seconds == 0) {
					throw new PopupException(400, "Error Waiting status");
				}

			} else {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	private Element checkStatusSuccess(String inputParameter, Document doc, WebDriver driver,
			CustomerProjectDto customerDetails, ScriptDetailsDto fetchMetadataVO) {
		try {
			String[] splitInputParameter = inputParameter.split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			boolean placeholder = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (splitInputParameter.length == 1) {
						headerElements = popupDIv.select("*[placeholder='" + splitInputParameter[0] + "']");
						placeholder = true;
					}
					if (headerElements == null || headerElements.size() == 0) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						placeholder = false;
					}
					if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						placeholder = false;
					}
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
						placeholder = false;
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				if (splitInputParameter.length == 1) {
					headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
					placeholder = true;
				}
				if (headerElements == null || headerElements.size() == 0) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					placeholder = false;
				}
				if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					placeholder = false;
				}
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
					placeholder = false;
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");

				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						if (!placeholder) {
							Element parent1 = ele.parent();
							boolean gotIt = false;
							while (parent1 != null) {
								if (parent1.tagName().equalsIgnoreCase("th")) {
									Elements thElements = parent1.select("th");
									for (Element thele : thElements) {
										WebElement Selelement1 = null;
										try {
											Selelement1 = driver
													.findElement(By.cssSelector(thele.cssSelector()));
										} catch (Exception e) {
											Selelement1 = driver.findElement(
													By.xpath(("\\fetchMetadataVO"
															.equalsIgnoreCase(thele.cssSelector())) ? null
																	: CSS2XPath.css2xpath(thele.cssSelector(),
																			true)));
										}
										if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
											continue;
										}
										Element trParent = parent1;
										int xPosition = Selelement1.getLocation().getX();
										while (trParent != null) {
											if (!trParent.select("td")
													.isEmpty()) {
												Elements tdElements = trParent
														.select("td");
												for (Element tdele : tdElements) {
													WebElement seltdEle = null;
													try {
														seltdEle = driver.findElement(
																By.cssSelector(tdele.cssSelector()));
													} catch (Exception e) {
														seltdEle = driver.findElement(
																By.xpath(("\\fetchMetadataVO"
																		.equalsIgnoreCase(tdele.cssSelector()))
																				? null
																				: CSS2XPath.css2xpath(
																						tdele.cssSelector(),
																						true)));
													}
													if (!seltdEle.isEnabled() || !seltdEle.isDisplayed()) {
														gotIt = false;
														continue;
													}
													int tdxPosition = seltdEle.getLocation().getX();
													if (tdxPosition == xPosition) {
														return tdele;
													}
												}
											}
											trParent = trParent.parent();
										}
									}

								}
								parent1 = parent1.parent();
							}

						} else {
							return ele;
						}
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					Element selElement1 = findTheStatusElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						return selElement1;
					}
				}

			}
		}
		// catch(StaleElementReferenceException ex) {
		// logger.info("get Stayle element " +fetchMetadataVO.getInputParameter());
		// }
		catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
		return null;
	}

	private Element findTheStatusElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheStatusElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								if (!placeholder) {
									Element parent1 = element.parent();
									boolean gotIt = false;
									while (parent1 != null) {
										if (parent1.tagName().equalsIgnoreCase("th")) {
											Elements thElements = parent1.select("th");
											for (Element thele : thElements) {
												WebElement Selelement1 = null;
												try {
													Selelement1 = driver
															.findElement(By.cssSelector(thele.cssSelector()));
												} catch (Exception e) {
													Selelement1 = driver.findElement(
															By.xpath(("\\fetchMetadataVO"
																	.equalsIgnoreCase(thele.cssSelector())) ? null
																			: CSS2XPath.css2xpath(thele.cssSelector(),
																					true)));
												}
												if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
													continue;
												}
												Element trParent = parent1;
												int xPosition = Selelement1.getLocation().getX();
												while (trParent != null) {
													if (!trParent.select("td")
															.isEmpty()) {
														Elements tdElements = trParent
																.select("td");
														for (Element tdele : tdElements) {
															WebElement seltdEle = null;
															try {
																seltdEle = driver.findElement(
																		By.cssSelector(tdele.cssSelector()));
															} catch (Exception e) {
																seltdEle = driver.findElement(
																		By.xpath(("\\fetchMetadataVO"
																				.equalsIgnoreCase(tdele.cssSelector()))
																						? null
																						: CSS2XPath.css2xpath(
																								tdele.cssSelector(),
																								true)));
															}
															if (!seltdEle.isEnabled() || !seltdEle.isDisplayed()) {
																gotIt = false;
																continue;
															}
															int tdxPosition = seltdEle.getLocation().getX();
															if (tdxPosition == xPosition) {
																return tdele;
															}
														}
													}
													trParent = trParent.parent();
												}
											}

										}
										parent1 = parent1.parent();
									}
								} else
									return element;
							}
						}
					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private void multipleSendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, FetchConfigVO fetchConfigVO) {

		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			String[] splitInputValue = fetchMetadataVO.getInputValue().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			boolean placeholder = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (splitInputParameter.length == 1) {
						headerElements = popupDIv.select("*[placeholder='" + splitInputParameter[0] + "']");
						placeholder = true;
					}
					if (headerElements == null || headerElements.size() == 0) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						placeholder = false;
					}
					if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						placeholder = false;
					}
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
						placeholder = false;
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				if (splitInputParameter.length == 1) {
					headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
					placeholder = true;
				}
				if (headerElements == null || headerElements.size() == 0) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					placeholder = false;
				}
				if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					placeholder = false;
				}
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
					placeholder = false;
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element " + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						selElement = findTheInputElement(fetchMetadataVO, customerDetails, ele,
								Arrays.copyOfRange(splitInputValue, 0, splitInputValue.length - 1), driver);
						previousEle = selElement;
						if (selElement == null) {
							if (ele == headerElements.get(headerElements.size() - 1)) {
								logger.error("Failed to get element for script number-"
										+ fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						}
						try {
							selElement.clear();
						} catch (Exception e) {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].value = '';", selElement);
						}
						// try {
						// selElement.sendKeys(fetchMetadataVO.getInputValue());
						// } catch(Exception exec) {
						// previousEle = selElement1;
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						// jsExecutor.executeScript("arguments[0].click();", selElement);

						String script = "arguments[0].value = arguments[1]";
						try {
							jsExecutor.executeScript("arguments[0].focus();", selElement);

							jsExecutor.executeScript(script, selElement, splitInputValue[splitInputValue.length - 1]);
							// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
							// customerDetails);

						} catch (Exception exe) {
							try {
								selElement.sendKeys(splitInputValue[splitInputValue.length - 1]);
								// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
								// customerDetails);

							} catch (Exception i) {
								jsExecutor.executeScript(
										"arguments[0].value =" + splitInputValue[splitInputValue.length - 1] + ";",
										selElement);
								// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
								// customerDetails);

							}
						}
						break;
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findMultipleSendKeys(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element for script number-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						try {
							selElement1.clear();
						} catch (Exception e) {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].value = '';", selElement1);
						}
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						String script = "arguments[0].value = arguments[1]";
						try {
							jsExecutor.executeScript(
									"arguments[0].scrollIntoView({block: 'center', inline: 'center'});", selElement1);
							jsExecutor.executeScript("arguments[0].focus();", selElement1);
							jsExecutor.executeScript(script, selElement1, splitInputValue[splitInputValue.length - 1]);
							// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
							// customerDetails);

						} catch (Exception exe) {
							jsExecutor.executeScript(
									"arguments[0].scrollIntoView({block: 'center', inline: 'center'});", selElement1);
							selElement1.sendKeys(splitInputValue[splitInputValue.length - 1]);
							// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
							// customerDetails);

						}
						previousEle = selElement1;
					}

					// jsExecutor.executeScript("arguments[0].click();", selElement);
					break;
					// }
				}

			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findTheInputElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {

		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, false);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								List<WebElement> Selelement1;
								if (!placeholder) {
									Selelement1 = Selelement
											.findElements(By.xpath("./following::input[not(@type='hidden')]"));
									for (WebElement ele1 : Selelement1) {
										if (!ele1.isEnabled() || !ele1.isDisplayed()) {
											continue;
										}
										Selelement = ele1;
										break;
									}
								}
								return Selelement;
							}
						}
					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element " + copyOfRange[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findMultipleSendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {

		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findMultipleSendKeys(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								// if (!placeholder) {
								//// Selelement = Selelement
								//// .findElement(By.xpath("./following::input[not(@type='hidden')]"));
								//
								// }
								String[] splitInputParameter = fetchMetadataVO.getInputValue().split(">");
								return findTheInputElement(fetchMetadataVO, customerDetails, element,
										Arrays.copyOfRange(splitInputParameter, 0, splitInputParameter.length - 1),
										driver);

								// return multiElement;
							}
						}
					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element " + copyOfRange[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void mouseHover(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			FetchConfigVO fetchConfigVO) throws Exception {

		// Elements elements = GettingAllElements(xpath.getAction());;;;
		try {

			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			updateDOM(driver);
			WebElement cssSelectorEle = null;
			if (splitInputParameter.length == 1) {
				boolean specialCase = false;
				Elements elements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Thread.sleep(2000);
					updateDOM(driver);
					Elements popupElements = doc.select("*[data-afr-popupid]");
					Element popupDiv = null;
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						popupDiv = popupElements.get(i);
						if (!popupDiv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDiv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						elements = popupDiv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (elements.size() != 0) {
							toolTip = true;
							break;
						}
					}
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = popupDiv
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
							// toolTip=true;
						}
					}
				}
				if (toolTip == false) {
					elements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
					boolean notThere = false;
					for (Element element : elements) {
						WebElement ele = null;
						try {
							ele = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
						}
						notThere = true;

					}
					if (elements.size() == 0 || !notThere) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = doc
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
						}
					}
				}
				if (elements.size() == 0) {
					logger.error("Failed to get element " + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element element : elements) {
					WebElement ele = null;
					try {
						ele = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
								: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (specialCase) {
						Element parent = element.parent();
						if (parent.text().equals(splitInputParameter[0])
								|| element.text().equals(splitInputParameter[0])) {
							cssSelectorEle = ele;
							break;
						} else {
							continue;
						}
					}
					cssSelectorEle = ele;
					break;
				}

			} else {
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (headerElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					headerElements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if (headerElements.size() == 0) {
					logger.error("Failed to get element " + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element elem : headerElements) {
					WebElement headerSelElement = null;
					try {
						headerSelElement = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							headerSelElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
											: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception exe) {
							if (elem.attr("id") != "") {
								headerSelElement = driver.findElement(By.id(elem.attr("id")));
							}

						}

					}

					WebElement selElement = findmouseHoverElement(fetchMetadataVO, customerDetails, elem,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver,
							fetchConfigVO);
					if (selElement == null) {
						if (elem == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element for scritp Number-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						cssSelectorEle = selElement;
						break;
					}
				}
			}
			if (cssSelectorEle != null) {

				// WebElement buttonElement =
				// driver.findElement(By.cssSelector(cssSelectorEle));
				WebElement buttonElement = cssSelectorEle;
				try {
					Actions actions = new Actions(driver);
					actions.moveToElement(cssSelectorEle).perform();
					takeScreenshot(driver, fetchMetadataVO, customerDetails);
					cssSelectorEle.click();
					return;
				} catch (Exception e) {
					Actions actions = new Actions(driver);
					actions.moveToElement(cssSelectorEle).perform();
					takeScreenshot(driver, fetchMetadataVO, customerDetails);
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", cssSelectorEle);
					return;
				}
			}
		} catch (StaleElementReferenceException se) {

		} catch (Exception e) {

			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findmouseHoverElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element elem, String[] copyOfRange, WebDriver driver, FetchConfigVO fetchConfigVO) {
		try {
			Element parent = elem.parent();
			ELements seconElement = findSecondMouseHoverELement(fetchMetadataVO, customerDetails, parent,
					copyOfRange[0], driver, elem, fetchConfigVO);
			if (copyOfRange.length > 1) {
				return findmouseHoverElement(fetchMetadataVO, customerDetails, seconElement.getElement(),
						Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, fetchConfigVO);
			} else {
				WebElement Selelement = seconElement.getWebElement();
				return Selelement;
			}
		} catch (Exception e) {
			logger.error("Failed to get element for scritp Number-" + fetchMetadataVO.getScriptNumber()
					+ "  in Dynamic Xpath");
			throw e;
		}

	}

	private ELements findSecondMouseHoverELement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element parent, String string, WebDriver driver, Element ele2, FetchConfigVO fetchConfigVO) {

		try {
			ELements elements = new ELements();
			Element SpecialCaseParent = parent;
			Elements popupElements = doc.select("*[data-afr-popupid]");
			boolean getElementInPopup = false;
			if (popupElements.size() != 0) {
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element SpecialCaseParent1 = parent;
					Element parent1 = parent;
					while (parent1 != null && parent1 != popupElements.get(i)) {
						boolean gotElement = false;
						if (!parent1.select(":matchesOwn(^" + string + "$)").isEmpty()) {
							Elements eles = parent1.select(":matchesOwn(^" + string + "$)");
							if (eles.size() == 1 && eles.get(0) == ele2) {
								parent1 = parent1.parent();
								continue;
							}

							if (eles.size() == 0) {
								logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								if (elem == ele2) {
									continue;
								}
								try {
									WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle = driver
													.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
															+ "']/child::*[@class='" + elem.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								getElementInPopup = true;
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;
							}
						} else if (!parent1.select("[alt='" + string + "']").isEmpty()) {
							Elements eles = parent1.select("[alt='" + string + "']");
							if (eles.size() == 0) {
								logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								try {
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								}
								getElementInPopup = true;
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;
							}
						} else if (!parent1.select("[title='" + string + "']:not([type=text])").isEmpty()) {
							Elements eles = parent1.select("[title='" + string + "']");
							if (eles.size() == 0) {
								logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								try {
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								}

								getElementInPopup = true;
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;

							}
						}
						if (!gotElement) {
							parent1 = parent1.parent();
						}
					}
					if (getElementInPopup == false) {
						while (SpecialCaseParent1 != null && SpecialCaseParent1 != popupElements.get(i)) {
							for (int j = 0; j < string.length(); j++) {
								Elements eachCharacter = SpecialCaseParent
										.select(":matchesOwn(^" + string.charAt(j) + "$)");
								if (!eachCharacter.isEmpty()) {
									for (Element ele : eachCharacter) {
										WebElement SelEle = driver.findElement(By.cssSelector(ele.cssSelector()));
										if (!SelEle.isEnabled() || !SelEle.isDisplayed()) {
											continue;
										}
										if (ele.text().equals(string)) {
											elements.setWebElement(SelEle);
											elements.setElement(ele);
											return elements;
										}
										Element parent12 = ele.parent();
										if (parent12.text().equals(string)) {
											getElementInPopup = true;
											elements.setWebElement(SelEle);
											elements.setElement(ele);
											return elements;
										}
									}

									break;

								}
							}
							SpecialCaseParent = SpecialCaseParent.parent();
						}
					}

				}
			}
			if (!getElementInPopup) {
				while (parent != null) {
					boolean gotElement = false;
					if (!parent.select(":matchesOwn(^" + string + "$)").isEmpty()) {
						Elements eles = parent.select(":matchesOwn(^" + string + "$)");
						if (eles.size() == 1 && eles.get(0) == ele2) {
							parent = parent.parent();
							continue;
						}

						if (eles.size() == 0) {
							logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							if (elem == ele2) {
								continue;
							}
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								try {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								} catch (Exception exe) {
									try {
										selEle = driver
												.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
														+ "']/child::*[@class='" + elem.attr("class") + "']"));
									} catch (Exception exep) {
										continue;
									}
									// continue;
								}
							}
							gotElement = true;
							elements.setWebElement(selEle);
							elements.setElement(elem);
							return elements;
						}
					} else if (!parent.select("[alt='" + string + "']").isEmpty()) {
						Elements eles = parent.select("[alt='" + string + "']");
						if (eles.size() == 0) {
							logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							try {
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							}

							gotElement = true;
							elements.setWebElement(selEle);
							elements.setElement(elem);
							return elements;
						}
					} else if (!parent.select("[title='" + string + "']:not([type=text])").isEmpty()) {
						Elements eles = parent.select("[title='" + string + "']");
						if (eles.size() == 0) {
							logger.error("Failed to get element-" + string + "  in Dynamic Xpath");
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							try {
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							}
							gotElement = true;
							elements.setWebElement(selEle);
							elements.setElement(elem);
							return elements;
						}
					}
					if (!gotElement) {
						parent = parent.parent();
					}
				}
			}
			if (parent == null) {
				while (SpecialCaseParent != null) {
					for (int i = 0; i < string.length(); i++) {
						Elements eachCharacter = SpecialCaseParent.select(":matchesOwn(^" + string.charAt(i) + "$)");
						if (!eachCharacter.isEmpty()) {
							for (Element ele : eachCharacter) {
								WebElement SelEle = driver.findElement(By.cssSelector(ele.cssSelector()));
								if (!SelEle.isEnabled() || !SelEle.isDisplayed()) {
									continue;
								}
								if (ele.text().equals(string)) {
									elements.setWebElement(SelEle);
									elements.setElement(ele);
									return elements;
								}
								Element parent1 = ele.parent();
								if (parent1.text().equals(string)) {
									elements.setWebElement(SelEle);
									elements.setElement(ele);
									return elements;
								}
							}

							break;

						}
					}
					SpecialCaseParent = SpecialCaseParent.parent();
				}
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void clickNotificationLink(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			if (splitInputParameter.length == 1) {
				WebElement waittext = driver
						.findElement(By.xpath("//*[@placeholder=\"" + splitInputParameter[0] + "\"]/following::a[1]"));
				try {
					highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
					takeScreenshot(driver, fetchMetadataVO, customerDetails);
					// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
					// customerDetails);
					waittext.click();
					return;
				} catch (Exception e) {
					highlightElement(driver, fetchMetadataVO, waittext, fetchConfigVO);
					takeScreenshot(driver, fetchMetadataVO, customerDetails);
					// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
					// customerDetails);
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", waittext);
					return;
				}
			} else {
				clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void logOut(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			FetchConfigVO fetchConfigVO) throws Exception {
		fetchMetadataVO.setInputParameter("Settings and Actions");
		clickImage(fetchMetadataVO, driver, customerDetails, fetchConfigVO);
		Thread.sleep(5000);
		updateDOM(driver);
		fetchMetadataVO.setInputParameter("Sign Out");
		clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);
		Thread.sleep(5000);
		updateDOM(driver);
		fetchMetadataVO.setInputParameter("Confirm");
		clickLink(fetchMetadataVO, driver, customerDetails, false, fetchConfigVO);

	}

	private void clickLinkAction(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver)
			throws Exception {

		// Elements elements = GettingAllElements(xpath.getAction());
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			updateDOM(driver);
			WebElement cssSelectorEle = null;
			if (splitInputParameter.length == 1) {
				boolean specialCase = false;
				Elements elements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Thread.sleep(2000);
					updateDOM(driver);
					Elements popupElements = doc.select("*[data-afr-popupid]");
					Element popupDiv = null;
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						popupDiv = popupElements.get(i);
						if (!popupDiv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDiv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						elements = popupDiv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (elements.size() != 0) {
							toolTip = true;
							break;
						}
					}
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = popupDiv
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
							// toolTip=true;
						}
					}
				}
				if (toolTip == false) {
					elements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = doc
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
						}
					}
				}
				if (elements.size() == 0) {
					logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element element : elements) {
					WebElement ele = null;
					try {
						ele = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
								: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!ele.isEnabled() || !ele.isDisplayed()) {
						continue;

					} else {
						if (specialCase) {
							Element parent = element.parent();
							if (parent.text().equals(splitInputParameter[0])
									|| element.text().equals(splitInputParameter[0])) {
								cssSelectorEle = ele;
								break;
							}
						}
						String[] actionParam = "Actions".split(">");
						WebElement selElement = findClickLinkActionElement(fetchMetadataVO, customerDetails, element,
								Arrays.copyOfRange(actionParam, 0, actionParam.length), driver, false);
						if (selElement == null) {
							if (element == elements.get(elements.size() - 1)) {
								logger.error("Failed to get element for Scritp number-"
										+ fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						} else {
							cssSelectorEle = selElement;
							break;
						}
					}
				}
			} else {
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (headerElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					headerElements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if (headerElements.size() == 0) {
					logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element elem : headerElements) {
					WebElement headerSelElement = null;
					try {
						headerSelElement = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							headerSelElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
											: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception exe) {
							if (elem.attr("id") != "") {
								headerSelElement = driver.findElement(By.id(elem.attr("id")));
							}

						}

					}
					if (!headerSelElement.isEnabled() || !headerSelElement.isDisplayed()) {
						continue;
					} else {
						WebElement selElement = findClickLinkActionElement(fetchMetadataVO, customerDetails, elem,
								Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver, true);
						if (selElement == null) {
							if (elem == headerElements.get(headerElements.size() - 1)) {
								logger.error("Failed to get element for Scritp number-"
										+ fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						} else {
							cssSelectorEle = selElement;
							break;
						}
					}
				}

			}
			if (cssSelectorEle != null) {

				// WebElement buttonElement =
				// driver.findElement(By.cssSelector(cssSelectorEle));
				WebElement buttonElement = cssSelectorEle;
				try {
					highlightElement(driver, fetchMetadataVO, cssSelectorEle, fetchConfigVO);
					xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					cssSelectorEle.click();
					return;
				} catch (Exception e) {

					xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", cssSelectorEle);
					return;
				}
			}
		} catch (StaleElementReferenceException se) {

		} catch (Exception e) {

			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findClickLinkActionElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element elem, String[] copyOfRange, WebDriver driver, boolean secondinputParameter) {
		String input = fetchMetadataVO.getInputValue();
		Element parent = elem.parent();
		while (parent != null) {
			if (!parent.select(":matchesOwn(^" + input + "$)").isEmpty()) {
				Elements secondEle = parent.select(":matchesOwn(^" + input + "$)");
				if (secondEle.size() == 1 && secondEle.get(0) == elem) {
					parent = parent.parent();
				}
				for (Element element : secondEle) {
					WebElement selEle = null;
					if (element == elem) {
						continue;
					}
					try {
						selEle = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception ex) {
						try {
							selEle = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
											: CSS2XPath.css2xpath(element.cssSelector(), true)));
						} catch (Exception exe) {
							try {
								selEle = driver.findElement(By.xpath("//*[@class='" + element.parent().attr("class")
										+ "']/child::*[@class='" + element.attr("class") + "']"));
							} catch (Exception exep) {
								continue;
							}
							// continue;
						}
					}
					if (!selEle.isEnabled() || !selEle.isDisplayed()) {
						if (element == secondEle.get(secondEle.size() - 1)) {
							break;
						}
						continue;
					}
					Element parent1 = element.parent();
					String inputValueClickTitle = input + " Actions";
					while (parent1 != null) {

						if (!parent1.select("*[title='" + copyOfRange[0] + "']").isEmpty()) {

							Elements thirdEle = parent1.select("*[title='" + copyOfRange[0] + "']");
							if (thirdEle.size() == 1 && thirdEle.get(0) == elem) {
								parent1 = parent1.parent();
								continue;
							}
							for (Element thirdelement : thirdEle) {
								WebElement selEle1 = null;
								if (thirdelement == element) {
									continue;
								}
								try {
									selEle1 = driver.findElement(By.cssSelector(thirdelement.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle1 = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(thirdelement.cssSelector())) ? null
														: CSS2XPath.css2xpath(thirdelement.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle1 = driver.findElement(By.xpath("//*[@class='"
													+ thirdelement.parent().attr("class") + "']/child::*[@class='"
													+ thirdelement.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle1.isEnabled() || !selEle1.isDisplayed()) {
									if (thirdelement == thirdEle.get(secondEle.size() - 1)) {
										break;
									}
									continue;
								}
								return selEle1;
							}

						}
						if (!secondinputParameter
								&& !parent1.select("*[title='" + inputValueClickTitle + "']").isEmpty()) {

							Elements thirdEle = parent1.select("*[title='" + inputValueClickTitle + "']");
							if (thirdEle.size() == 1 && thirdEle.get(0) == elem) {
								parent1 = parent1.parent();
								continue;
							}
							for (Element thirdelement : thirdEle) {
								WebElement selEle1 = null;
								if (thirdelement == element) {
									continue;
								}
								try {
									selEle1 = driver.findElement(By.cssSelector(thirdelement.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle1 = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(thirdelement.cssSelector())) ? null
														: CSS2XPath.css2xpath(thirdelement.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle1 = driver.findElement(By.xpath("//*[@class='"
													+ thirdelement.parent().attr("class") + "']/child::*[@class='"
													+ thirdelement.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle1.isEnabled() || !selEle1.isDisplayed()) {
									if (thirdelement == thirdEle.get(secondEle.size() - 1)) {
										break;
									}
									continue;
								}
								return selEle1;
							}

						}
						parent1 = parent1.parent();
					}
					if (parent1 == null) {
						logger.error("Failed to get element-" + copyOfRange[0] + "  in Dynamic Xpath");
						throw new NullPointerException();
					}
				}
			}
			parent = parent.parent();
		}
		if (parent == null) {
			logger.error("Failed to get element-" + copyOfRange[0] + "  in Dynamic Xpath");
			throw new NullPointerException();
		}
		return null;
	}

	private void tableClickLink(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (splitInputParameter.length == 1) {
				try {
					WebElement sElement = driver.findElement(
							By.xpath("(//table[@summary='" + splitInputParameter[0] + "']//a[not (@title)])[1]"));
					sElement.click();
				} catch (Exception ex) {

				}
				return;
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						List<WebElement> Selelements = selElement.findElements(By.tagName("a"));
						for (WebElement selele : Selelements) {
							if (!selele.isEnabled() || !selele.isDisplayed()) {
								continue;
							}
							selele.click();
							break;
						}

					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findTheTableClickLinkElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element for ScriptNumber" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						highlightElement(driver, fetchMetadataVO, selElement1, fetchConfigVO);
						xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						selElement1.click();
					}

					// jsExecutor.executeScript("arguments[0].click();", selElement);
					break;
					// }
				}

			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findTheTableClickLinkElement(ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails, Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select("table[summary['" + copyOfRange[0] + "']").isEmpty()) {
					break;
				} else if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					break;
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					break;
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					break;
				}
				parent = parent.parent();
			}
			if (parent != null) {
				Elements secondElements = null;
				if (!parent.select("table[summary['" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("table[summary[" + copyOfRange[0] + "]");
				} else if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
				}
				if (secondElements.size() == 0) {
					logger.error("Failed to get element-" + copyOfRange[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element element : secondElements) {
					if (copyOfRange.length > 1) {
						return findTheTableClickLinkElement(fetchMetadataVO, customerDetails, element,
								Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
					} else {
						WebElement Selelement = null;
						try {
							Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							Selelement = driver.findElement(
									By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
											: CSS2XPath.css2xpath(element.cssSelector(), true)));
						}
						if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
							continue;
						} else {

							List<WebElement> Selelements = Selelement.findElements(By.tagName("a"));
							for (WebElement selele : Selelements) {
								if (!selele.isEnabled() || !selele.isDisplayed()) {
									continue;
								}
								return selele;
							}
							return null;
						}
					}
				}
			} else {
				return null;
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void copy(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			String getElementtoCheck = "";
			if (splitInputParameter.length == 1) {
				headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
				getElementtoCheck = "placeholder";
			}
			if (headerElements == null || headerElements.size() == 0) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				getElementtoCheck = "normal";
			}
			if (headerElements.size() == 0) {
				headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				getElementtoCheck = "dataValue";
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {

						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));

					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						// if (getElementtoCheck != "placeholder")
						List<WebElement> sWebElements = selElement.findElements(By.xpath("./following::*"));
						WebElement elementWithTextNextToGivenElement = null;
						for (WebElement element : sWebElements) {
							String elementText = element.getText();
							if (!elementText.isEmpty() && !element.isEnabled() && !element.isDisplayed()) {
								// The element has text
								elementWithTextNextToGivenElement = element;
								break;
							}
						}
						String testParamId = fetchMetadataVO.getTestScriptParamId();
						String testSetId = fetchMetadataVO.getTestSetLineId();
						dynamicnumber.saveCopyNumber(elementWithTextNextToGivenElement.getText(), testParamId,
								testSetId);
						break;
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));

					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					selElement = findTheCopyElement(fetchMetadataVO, customerDetails, driver, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element for ScriptNumber-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						String testParamId = fetchMetadataVO.getTestScriptParamId();
						String testSetId = fetchMetadataVO.getTestSetLineId();
						dynamicnumber.saveCopyNumber(selElement.getText(), testParamId, testSetId);
						// copyNumber = selElement.getText();
						break;
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findTheCopyElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null) {
			if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {

				Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
				if (secondElements.size() == 1 && secondElements.get(0) == ele) {
					parent = parent.parent();
					continue;
				}
				if (secondElements.size() == 0) {
					throw new NullPointerException();
				}
				for (Element element : secondElements) {
					if (element == ele) {
						continue;
					}
					if (copyOfRange.length > 1) {
						WebElement Selelement = null;
						try {
							Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {

							Selelement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
											: CSS2XPath.css2xpath(element.cssSelector(), true)));

						}
						if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
							if (element == secondElements.get(secondElements.size() - 1)) {
								break;
							}
							continue;
						}

						return findTheCopyElement(fetchMetadataVO, customerDetails, driver, ele,
								Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
					} else {
						WebElement Selelement = null;
						try {
							Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {

							Selelement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
											: CSS2XPath.css2xpath(element.cssSelector(), true)));

						}
						if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
							if (element == secondElements.get(secondElements.size() - 1)) {
								break;
							}
							continue;
						} else {
							java.util.List<WebElement> followingElements = Selelement
									.findElements(By.xpath("following::*"));

							// Loop through the following elements to find the first one that has text
							WebElement elementWithTextNextToGivenElement = null;
							for (WebElement elem : followingElements) {
								String elementText = elem.getText();
								if (!elementText.isEmpty() && elem.isEnabled() && elem.isDisplayed()) {
									// The element has text
									elementWithTextNextToGivenElement = elem;
									break;
								}
							}
							return elementWithTextNextToGivenElement;
						}
					}
				}

			}
			parent = parent.parent();
		}
		if (parent == null) {
			logger.error("Failed to get element for ScriptNumber-" + fetchMetadataVO.getScriptNumber()
					+ "  in Dynamic Xpath");
			throw new NullPointerException();
		}

		return null;
	}

	private void tableSendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			boolean placeholder = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (splitInputParameter.length == 1) {
						headerElements = popupDIv.select("*[placeholder='" + splitInputParameter[0] + "']");
						placeholder = true;
					}
					if (headerElements == null || headerElements.size() == 0) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						placeholder = false;
					}
					if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						placeholder = false;
					}
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
						placeholder = false;
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				if (splitInputParameter.length == 1) {
					headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
					placeholder = true;
				}
				if (headerElements == null || headerElements.size() == 0) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					placeholder = false;
				}
				if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					placeholder = false;
				}
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
					placeholder = false;
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");

				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						if (!placeholder) {
							selElement = selElement.findElement(By.xpath("./following::input[not(@type='hidden')]"));
						}
						previousEle = selElement;
						try {
							selElement.clear();
						} catch (Exception e) {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].value = '';", selElement);
						}
						// try {
						// selElement.sendKeys(fetchMetadataVO.getInputValue());
						// } catch(Exception exec) {
						// previousEle = selElement1;
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						// jsExecutor.executeScript("arguments[0].click();", selElement);

						String script = "arguments[0].value = arguments[1]";
						try {
							jsExecutor.executeScript("arguments[0].focus();", selElement);

							jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());

						} catch (Exception exe) {
							try {
								selElement.sendKeys(fetchMetadataVO.getInputValue());
							} catch (Exception i) {
								jsExecutor.executeScript("arguments[0].value =" + fetchMetadataVO.getInputValue() + ";",
										selElement);
							}
						}
						break;
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findThetableSendElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						try {
							selElement1.clear();
						} catch (Exception e) {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].value = '';", selElement1);
						}
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						String script = "arguments[0].value = arguments[1]";
						try {
							jsExecutor.executeScript("arguments[0].focus();", selElement1);
							jsExecutor.executeScript(script, selElement1, fetchMetadataVO.getInputValue());
						} catch (Exception exe) {
							selElement1.sendKeys(fetchMetadataVO.getInputValue());

						}
						previousEle = selElement1;
					}

					// jsExecutor.executeScript("arguments[0].click();", selElement);
					break;
				}

			}
		}
		// catch(StaleElementReferenceException ex) {
		// logger.info("get Stayle element " +fetchMetadataVO.getInputParameter());
		// }
		catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findThetableSendElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findThetableSendElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								if (!placeholder) {
									Element parent1 = element.parent();
									boolean gotIt = false;
									while (parent1 != null) {
										if (parent1.tagName().equalsIgnoreCase("th")) {
											Elements thElements = parent1.select("th");
											for (Element thele : thElements) {
												WebElement Selelement1 = null;
												try {
													Selelement1 = driver
															.findElement(By.cssSelector(thele.cssSelector()));
												} catch (Exception e) {
													Selelement1 = driver.findElement(
															By.xpath(("\\fetchMetadataVO"
																	.equalsIgnoreCase(thele.cssSelector())) ? null
																			: CSS2XPath.css2xpath(thele.cssSelector(),
																					true)));
												}
												if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
													continue;
												}
												Element trParent = parent1;
												int xPosition = Selelement1.getLocation().getX();
												while (trParent != null) {
													if (!trParent.select("td:has(input:not([type=hidden]))")
															.isEmpty()) {
														Elements tdElements = trParent
																.select("td:has(input:not([type=hidden]))");
														for (Element tdele : tdElements) {
															WebElement seltdEle = null;
															try {
																seltdEle = driver.findElement(
																		By.cssSelector(tdele.cssSelector()));
															} catch (Exception e) {
																seltdEle = driver.findElement(
																		By.xpath(("\\fetchMetadataVO"
																				.equalsIgnoreCase(tdele.cssSelector()))
																						? null
																						: CSS2XPath.css2xpath(
																								tdele.cssSelector(),
																								true)));
															}
															if (!seltdEle.isEnabled() || !seltdEle.isDisplayed()) {
																gotIt = false;
																continue;
															}
															int tdxPosition = seltdEle.getLocation().getX();
															if (tdxPosition == xPosition && !tdele
																	.select("input:not([type=hidden])").isEmpty()) {
																Elements inptElements = tdele
																		.select("input:not([type=hidden])");
																for (Element inptElement : inptElements) {
																	WebElement SelinptElement = null;
																	try {
																		SelinptElement = driver
																				.findElement(By.cssSelector(
																						inptElement.cssSelector()));
																	} catch (Exception e) {
																		SelinptElement = driver.findElement(
																				By.xpath(("\\fetchMetadataVO"
																						.equalsIgnoreCase(inptElement
																								.cssSelector())) ? null
																										: CSS2XPath
																												.css2xpath(
																														inptElement
																																.cssSelector(),
																														true)));
																	}
																	if (!SelinptElement.isEnabled()
																			|| !SelinptElement.isDisplayed()) {
																		gotIt = false;
																		continue;
																	}
																	gotIt = true;
																	return SelinptElement;
																}
															}
														}
													}
													trParent = trParent.parent();
												}

												// String index= thele.attr("_d_index");
												// if(index.equalsIgnoreCase("")) {
												// gotIt=false;
												// break;
												// }
												// else {
												// gotIt=true;
												// int index1 = Integer.parseInt(index)+1;
												// Selelement1=Selelement1.findElement(By.xpath("./following::td[" +
												// index1 + "]"));
												// Selelement1=Selelement1.findElement(By.tagName("input"));
												// return Selelement1;
												// }

											}

										}
										parent1 = parent1.parent();
									}
								} else
									return Selelement;
							}
						}
					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findThetableSendElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								if (!placeholder) {
									Element parent1 = element.parent();
									boolean gotIt = false;
									if (gotIt == false) {
										parent1 = element.parent();
										while (parent1 != null) {
											if (!parent1.select("input:not([type=hidden])").isEmpty()) {
												Elements tableSedkeysEle = parent1.select("input:not([type=hidden])");
												for (Element tableEle : tableSedkeysEle) {
													WebElement Selelement1 = null;
													try {
														Selelement1 = driver
																.findElement(By.cssSelector(tableEle.cssSelector()));
													} catch (Exception e) {
														Selelement1 = driver.findElement(
																By.xpath(("\\fetchMetadataVO"
																		.equalsIgnoreCase(tableEle.cssSelector()))
																				? null
																				: CSS2XPath.css2xpath(
																						tableEle.cssSelector(), true)));
													}
													if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
														continue;
													}
													return Selelement1;
												}

											}
											parent1 = parent1.parent();
										}
									}
								} else
									return Selelement;
							}
						}

					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// private WebElement findTheTableSendKeysElement(ScriptDetailsDto
	// fetchMetadataVO, CustomerProjectDto customerDetails,
	// Element ele, String[] copyOfRange, WebDriver driver) {
	// try {
	// Element parent = ele.parent();
	// while (parent != null) {
	// if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
	// break;
	// }
	// if (doc.select("*[data-afr-popupid]").size() > 0
	// && !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
	// break;
	// }
	// if (doc.select("*[data-afr-popupid]").size() > 0
	// && !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
	// break;
	// }
	// parent = parent.parent();
	// }
	// if (parent != null) {
	// Elements secondElements = null;
	// if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
	// secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
	// } else if (doc.select("*[data-afr-popupid]").size() > 0
	// && !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
	// secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
	// } else if (doc.select("*[data-afr-popupid]").size() > 0
	// && !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
	// secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
	// }
	// if (secondElements.size() == 0) {
	// throw new NullPointerException();
	//
	// }
	// for (Element element : secondElements) {
	// if (copyOfRange.length > 1) {
	// findTheTableSendKeysElement(fetchMetadataVO, customerDetails, element,
	// Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
	// } else {
	// WebElement Selelement = null;
	// try {
	// Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
	// } catch (Exception e) {
	// Selelement = driver.findElement(
	// By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
	// : CSS2XPath.css2xpath(element.cssSelector(), true)));
	// }
	// if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
	// continue;
	// } else {
	// Element trElement = element.closest("tr");
	// Element thElement = element.closest("th");
	// Elements thElements = trElement.select("th");
	// int indexofTh = 1;
	// for (Element elem : thElements) {
	// if (elem == thElement) {
	// break;
	// }
	// indexofTh++;
	// }
	// // int parentTrElement = trElement.indexOf(thElement);
	// // Selelement = Selelement.findElement(By.xpath("./following::input")) ;
	// WebElement tdElement = Selelement
	// .findElement(By.xpath("./following::td[" + indexofTh + "]"));
	//
	// Selelement = tdElement.findElement(By.tagName("input"));
	// return Selelement;
	// }
	// }
	// }
	// } else {
	// return null;
	// }
	//
	// return null;
	// } catch (Exception e) {
	// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
	// customerDetails);
	// throw e;
	// }
	//
	// }

	private void tableDropdownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						Element trElement = ele.closest("tr");
						Element thElement = ele.closest("th");
						Elements thElements = trElement.select("th");
						int indexofTh = 1;
						for (Element elem : thElements) {
							if (elem == thElement) {
								break;
							}
							indexofTh++;
						}
						// int parentTrElement = trElement.indexOf(thElement);
						// Selelement = Selelement.findElement(By.xpath("./following::input"));
						WebElement tdElement = selElement.findElement(By.xpath("./following::td[" + indexofTh + "]"));
						selElement = tdElement.findElement(By.tagName("a"));
						if (selElement == null) {
							if (ele == headerElements.get(headerElements.size() - 1)) {
								logger.error("Failed to get element-" + fetchMetadataVO.getScriptNumber()
										+ "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						} else {
							try {
								selElement.click();
							} catch (Exception ex) {

								JavascriptExecutor executor = (JavascriptExecutor) driver;
								executor.executeScript("arguments[0].click();", selElement);

							}
							// try {
							// selElement.sendKeys(fetchMetadataVO.getInputValue());
							// } catch(Exception exec) {
							previousEle = selElement;
							Thread.sleep(5000);
							CompletableFuture<Object> updateDo = this.updateDOM(driver);
							try {
								Object update = updateDo.get();
							} catch (InterruptedException | ExecutionException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
							Elements popupElements = doc.select("*[data-afr-popupid]");
							for (int i = popupElements.size() - 1; i >= 0; i--) {
								Element parent = doc.select("*[data-afr-popupid]").get(i);
								Elements selecElements = null;
								boolean gotElementWithhoutAdvSearch = false;
								while (parent != null) {
									if (!parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
											.isEmpty()) {
										selecElements = parent
												.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
										gotElementWithhoutAdvSearch = true;
										break;
									}
									if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
										if (!parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
												.isEmpty()) {
											selecElements = parent
													.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
											gotElementWithhoutAdvSearch = true;
										} else {
											selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
										}
										break;
									}
									parent = parent.parent();
								}
								if (parent != null) {
									for (Element elem : selecElements) {
										WebElement selElement12 = null;
										try {
											selElement12 = driver.findElement(By.cssSelector(elem.cssSelector()));
										} catch (Exception ex) {
											selElement12 = driver.findElement(
													By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
															: CSS2XPath.css2xpath(elem.cssSelector(), true)));

										}
										if (!selElement12.isEnabled() || !selElement12.isDisplayed()) {
											continue;
										}
										if (gotElementWithhoutAdvSearch)
											try {
												selElement12.click();
											} catch (Exception ex) {
												JavascriptExecutor executor = (JavascriptExecutor) driver;
												executor.executeScript("arguments[0].click();", selElement12);

											}
									}
								}
							}

						}

						break;
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findTableDropDownele(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						continue;
					} else {
						try {
							selElement1.click();
						} catch (Exception ex) {

							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", selElement1);

						}
						// try {
						// selElement.sendKeys(fetchMetadataVO.getInputValue());
						// } catch(Exception exec) {
						previousEle = selElement1;
						Thread.sleep(5000);
						CompletableFuture<Object> updateDo = this.updateDOM(driver);
						try {
							Object update = updateDo.get();
						} catch (InterruptedException | ExecutionException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						Elements popupElements = doc.select("*[data-afr-popupid]");
						for (int i = popupElements.size() - 1; i >= 0; i--) {
							Element parent = doc.select("*[data-afr-popupid]").get(i);
							Element mainparernt = parent;
							Elements selecElements = null;
							boolean gotElementWithhoutAdvSearch = false;
							String selector = "";
							String selector1 = "";
							boolean havingSinglequote = false;
							boolean havingBraces = false;
							if (fetchMetadataVO.getInputValue().contains("'")) {
								havingSinglequote = true;
								String[] splitItems = fetchMetadataVO.getInputValue().split("'");
								for (String item : splitItems) {
									selector += ":containsOwn(" + item + ")";
								}
								// System.out.println(doc.select(string));
							}
							Pattern pattern2 = Pattern.compile(".*[\\(\\)].*");
							Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
							if (matcher2.matches()) {
								havingBraces = true;
								String[] elementsb = fetchMetadataVO.getInputValue().split("\\s*[\\(\\)]\\s*");
								for (String item : elementsb) {
									if (item.contains("'")) {
										String[] splitItems = item.split("'");
										for (String it : splitItems) {
											selector1 += ":containsOwn(" + it + ")";
										}
										// System.out.println(doc.select(string));
									} else {
										selector1 += ":containsOwn(" + item + ")";
									}
								}
							}
							Pattern pattern1 = Pattern.compile("[^a-zA-Z0-9\\s]");
							Matcher matcher1 = pattern1.matcher(fetchMetadataVO.getInputValue());
							System.out.println(matcher1.find());
							System.out.println(parent.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")"));
							System.out.println(parent.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")"));
							while (parent != null) {
								if (havingSinglequote && !parent.select(selector).isEmpty()) {
									selecElements = parent.select(selector);
									gotElementWithhoutAdvSearch = true;
									break;
								}
								if (havingBraces && !parent.select(selector1).isEmpty()) {
									selecElements = parent.select(selector1);
									gotElementWithhoutAdvSearch = true;
									break;
								}
								if (!matcher1.find()
										&& !parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
												.isEmpty()) {
									selecElements = parent
											.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
									gotElementWithhoutAdvSearch = true;
									break;
								}
								if (matcher1.find()
										&& !parent.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")")
												.isEmpty()) {
									selecElements = parent
											.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")");
									gotElementWithhoutAdvSearch = true;
									break;
								}

								if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
									if (!parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
											.isEmpty()) {
										selecElements = parent
												.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
										gotElementWithhoutAdvSearch = true;
									} else {
										selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
									}
									break;
								}
								parent = parent.parent();
							}
							if (parent == null) {
								while (mainparernt != null) {
									if (!mainparernt.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")")
											.isEmpty()) {
										selecElements = mainparernt
												.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")");
										gotElementWithhoutAdvSearch = true;
										break;
									}
									mainparernt = mainparernt.parent();
								}
								// if(mainparernt == null) {
								// throw new NullPointerException();
								// }

							}
							if (parent != null || mainparernt != null) {
								for (Element elem : selecElements) {
									WebElement selElement12 = null;
									try {
										selElement12 = driver.findElement(By.cssSelector(elem.cssSelector()));
									} catch (Exception ex) {
										selElement12 = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));

									}
									if (!selElement12.isEnabled() || !selElement12.isDisplayed()) {
										continue;
									}
									if (gotElementWithhoutAdvSearch) {
										try {
											selElement12.click();
										} catch (Exception ex) {
											JavascriptExecutor executor = (JavascriptExecutor) driver;
											executor.executeScript("arguments[0].click();", selElement12);

										}
									} else {
										try {
											selElement.click();
										} catch (Exception ex) {
											JavascriptExecutor executor = (JavascriptExecutor) driver;
											executor.executeScript("arguments[0].click();", selElement);

										}
										// findSear
										Thread.sleep(5000);
										CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
										try {
											Object update = updateDo1.get();
										} catch (InterruptedException | ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										fetchMetadataVO.setInputParameter("Search>" + splitInputParameter[1]);
										sendKeys(fetchMetadataVO, customerDetails, driver, true);
										fetchMetadataVO.setInputParameter("Search>" + "Search");
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);
										Thread.sleep(5000);
										updateDOM(driver);
										fetchMetadataVO.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);
										fetchMetadataVO.setInputParameter("Search>" + "OK");
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);

									}

								}

							}
						}

					}

				}
			}

		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findTableDropDownele(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					break;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					break;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					break;
				}
				parent = parent.parent();
			}
			if (parent != null) {
				Elements secondElements = null;
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
				} else if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
				}
				if (secondElements.size() == 0) {
					throw new NullPointerException();

				}
				for (Element element : secondElements) {
					if (copyOfRange.length > 1) {
						return findTableDropDownele(fetchMetadataVO, customerDetails, element,
								Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
					} else {
						WebElement Selelement = null;
						try {
							Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							Selelement = driver.findElement(
									By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
											: CSS2XPath.css2xpath(element.cssSelector(), true)));
						}
						if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
							continue;
						} else {
							// Element trElement = element.closest("tr");
							// Element thElement = element.closest("th");
							// Elements thElements = trElement.select("th");
							// int indexofTh = 1;
							// for (Element elem : thElements) {
							// if (elem == thElement) {
							// break;
							// }
							// indexofTh++;
							// }
							// int parentTrElement = trElement.indexOf(thElement);
							// Selelement = Selelement.findElement(By.xpath("./following::input"));
							// WebElement tdElement = Selelement
							// .findElement(By.xpath("./following::td[" + indexofTh + "]"));

							Element parent1 = element.parent();
							boolean gotIt = false;
							while (parent1 != null) {
								if (parent1.tagName().equalsIgnoreCase("th")) {
									Elements thElements = parent1.select("th");
									for (Element thele : thElements) {
										WebElement Selelement1 = null;
										try {
											Selelement1 = driver.findElement(By.cssSelector(thele.cssSelector()));
										} catch (Exception e) {
											Selelement1 = driver.findElement(
													By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(thele.cssSelector()))
															? null
															: CSS2XPath.css2xpath(thele.cssSelector(), true)));
										}
										if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
											continue;
										}
										Element trParent = parent1;
										int xPosition = Selelement1.getLocation().getX();
										while (trParent != null) {
											if (!trParent.select("td:has(input:not([type=hidden]))").isEmpty()) {
												Elements tdElements = trParent
														.select("td:has(input:not([type=hidden]))");
												for (Element tdele : tdElements) {
													WebElement seltdEle = null;
													try {
														seltdEle = driver
																.findElement(By.cssSelector(tdele.cssSelector()));
													} catch (Exception e) {
														seltdEle = driver.findElement(
																By.xpath(("\\fetchMetadataVO"
																		.equalsIgnoreCase(tdele.cssSelector())) ? null
																				: CSS2XPath.css2xpath(
																						tdele.cssSelector(), true)));
													}
													if (!seltdEle.isEnabled() || !seltdEle.isDisplayed()) {
														gotIt = false;
														continue;
													}
													int tdxPosition = seltdEle.getLocation().getX();
													if (tdxPosition == xPosition
															&& !tdele.select("input:not([type=hidden])").isEmpty()) {
														Elements inptElements = tdele
																.select("input:not([type=hidden])");
														for (Element inptElement : inptElements) {
															WebElement SelinptElement = null;
															try {
																SelinptElement = driver.findElement(
																		By.cssSelector(inptElement.cssSelector()));
															} catch (Exception e) {
																SelinptElement = driver.findElement(
																		By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(
																				inptElement.cssSelector())) ? null
																						: CSS2XPath.css2xpath(
																								inptElement
																										.cssSelector(),
																								true)));
															}
															if (!SelinptElement.isEnabled()
																	|| !SelinptElement.isDisplayed()) {
																gotIt = false;
																continue;
															}
															gotIt = true;
															return SelinptElement
																	.findElement(By.xpath("./following::a"));
														}
													}
												}
											}
											trParent = trParent.parent();
										}

										// String index= thele.attr("_d_index");
										// if(index.equalsIgnoreCase("")) {
										// gotIt=false;
										// break;
										// }
										// else {
										// gotIt=true;
										// int index1 = Integer.parseInt(index)+1;
										// Selelement1=Selelement1.findElement(By.xpath("./following::td[" + index1 +
										// "]"));
										// Selelement1=Selelement1.findElement(By.tagName("input"));
										// return Selelement1;
										// }

									}

								}
								parent1 = parent1.parent();
							}

							// return tdElement.findElement(By.tagName("a"));
						}
					}
				}
			} else {
				return null;
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void scrollUsingElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, FetchConfigVO fetchConfigVO) throws Exception {

		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			updateDOM(driver);
			WebElement cssSelectorEle = null;
			if (splitInputParameter.length == 1) {
				boolean specialCase = false;
				Elements elements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Thread.sleep(2000);
					updateDOM(driver);
					Elements popupElements = doc.select("*[data-afr-popupid]");
					Element popupDiv = null;
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						popupDiv = popupElements.get(i);
						if (!popupDiv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDiv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						elements = popupDiv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (elements.size() != 0) {
							toolTip = true;
							break;
						}
					}
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = popupDiv
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
							// toolTip=true;
						}
					}
				}
				if (toolTip == false) {
					elements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = doc
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
							// for(Element ele: eachCharacter){
							// WebElement SelEle= driver.findElement(By.cssSelector(ele.cssSelector()));
							// if(!SelEle.isEnabled() && !SelEle.isDisplayed()){
							// Element parent= ele.parent();
							// if(parent.text().equals(splitInputParameter[0])){
							// elements=ele;
							// }
							// }
							// }

						}
					}
				}
				if (elements.size() == 0) {
					logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element element : elements) {
					WebElement ele = null;
					try {
						ele = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
								: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!ele.isEnabled() || !ele.isDisplayed()) {
						continue;

					} else {
						if (specialCase) {
							Element parent = element.parent();
							if (parent.text().equals(splitInputParameter[0])
									|| element.text().equals(splitInputParameter[0])) {
								cssSelectorEle = ele;
								break;
							}
						}
						cssSelectorEle = ele;
						break;
					}
				}
			} else {
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;

								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

								try {
									wait.until(
											ExpectedConditions
													.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (headerElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					headerElements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if (headerElements.size() == 0) {
					logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element elem : headerElements) {
					WebElement headerSelElement = null;
					try {
						headerSelElement = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							headerSelElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
											: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception exe) {
							if (elem.attr("id") != "") {
								headerSelElement = driver.findElement(By.id(elem.attr("id")));
							}

						}

					}
					if (!headerSelElement.isEnabled() || !headerSelElement.isDisplayed()) {
						continue;
					} else {
						WebElement selElement = findScrollIntoView(fetchMetadataVO, customerDetails, elem,
								Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver,
								fetchConfigVO);
						if (selElement == null) {
							if (elem == headerElements.get(headerElements.size() - 1)) {
								logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
										+ "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						} else {
							cssSelectorEle = selElement;
							break;
						}
					}
				}

			}
			if (cssSelectorEle != null) {
				Point location = cssSelectorEle.getLocation();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollTo(" + location.getX() + "," + location.getY() + ");");
				takeScreenshot(driver, fetchMetadataVO, customerDetails);

			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private WebElement findScrollIntoView(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element elem, String[] copyOfRange, WebDriver driver, FetchConfigVO fetchConfigVO2) {
		try {
			Element parent = elem.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				// System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == elem) {
						parent = parent.parent();
						continue;
					}
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == elem) {
						parent = parent.parent();
						continue;
					}
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == elem) {
						parent = parent.parent();
						continue;
					}
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == elem) {
						parent = parent.parent();
						continue;
					}
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == elem) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, false);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}

							return Selelement;

						}
					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private void clickOnNavbar(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			FetchConfigVO fetchConfigVO, WebDriver driver) throws InterruptedException, ExecutionException {
		try {
			String param1 = "Navigator";
			WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='" + param1 + "']")));
			WebElement webEle = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
			webEle.click();

			Thread.sleep(3000);
			updateDOM(driver);
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Show More']")));
				WebElement showMore = driver.findElement(By.xpath("//*[text()='Show More']"));
				showMore.click();
			} catch (Exception ex) {
				System.out.println("Show less");
			}

			Thread.sleep(3000);
			updateDOM(driver);

			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								try {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));
								} catch (Exception ex) {
									Selelement = driver.findElement(By.xpath(getXPath(ele)));
								}

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								try {
									wait.until(ExpectedConditions
											.presenceOfElementLocated(
													By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
															: CSS2XPath.css2xpath(ele.cssSelector(), true))));
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));
								} catch (Exception ex) {
									Selelement = driver.findElement(By.xpath(getXPath(ele)));
								}

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() == 0 && matcher.find()
							&& !popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
				if (headerElements.size() == 0 && matcher.find()
						&& !doc.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true))));
							selElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true)));
						} catch (Exception ex) {
							selElement = driver.findElement(By.xpath(getXPath(ele)));
						}
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {

						try {
							highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
							takeScreenshot(driver, fetchMetadataVO, customerDetails);
							selElement.click();
						} catch (Exception ex) {
							highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
							takeScreenshot(driver, fetchMetadataVO, customerDetails);
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", selElement);
						}

						break;
					}
				} else {
					WebElement selElement = null;
					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						try {
							wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true))));
							selElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true)));
						} catch (Exception ex) {
							selElement = driver.findElement(By.xpath(getXPath(ele)));
						}
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findTheNavElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						try {
							highlightElement(driver, fetchMetadataVO, selElement1, fetchConfigVO);
							takeScreenshot(driver, fetchMetadataVO, customerDetails);
							selElement1.click();
						} catch (Exception ex) {
							// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
							// customerDetails);
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", selElement);
						}
					}

					// jsExecutor.executeScript("arguments[0].click();", selElement);
					break;
					// }
				}

			}
		} catch (Exception e) {
			// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			e.printStackTrace();
			throw e;
		}

	}

	private WebElement findTheNavElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(copyOfRange[0]);
			while (parent != null) {
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					Elements NavElem = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (NavElem.size() == 1 && NavElem.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (NavElem.size() == 0) {
						throw new NullPointerException();
					}
					for (Element elem : NavElem) {

						WebElement selEle = null;
						if (elem == ele) {
							continue;
						}
						try {
							selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
						} catch (Exception ex) {
							try {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							} catch (Exception exe) {
								selEle = driver.findElement(By.xpath(getXPath(elem)));
								// continue;
							}
						}
						if (selEle.isDisplayed() && selEle.isEnabled()) {
							return selEle;

						}

					}
					break;
				}
				if (matcher.find() && !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					Elements NavElem = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (NavElem.size() == 1 && NavElem.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (NavElem.size() == 0) {
						throw new NullPointerException();
					}
					for (Element elem : NavElem) {

						WebElement selEle = null;
						if (elem == ele) {
							continue;
						}
						try {
							selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
						} catch (Exception ex) {
							try {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							} catch (Exception exe) {
								try {
									selEle = driver.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
											+ "']/child::*[@class='" + elem.attr("class") + "']"));
								} catch (Exception exep) {
									selEle = driver.findElement(By.xpath(getXPath(elem)));
								}
								// continue;
							}
						}
						if (selEle.isDisplayed() && selEle.isEnabled()) {
							return selEle;

						}

					}
					break;

				}
				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}

		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
		return null;

	}

	@Async
	private CompletableFuture<Object> updateDOM(WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			Object obj = js.executeScript("return document.readyState");
			if (obj != null && !obj.toString().equals("complete")) {
				return updateDOM(driver);
			} else {
				CompletableFuture<String> pageSourceFuture = this.getPageSource(driver);
				String pageSource = pageSourceFuture.get();
				CompletableFuture<Document> parsedHtmlFuture = this.parseHtml(pageSource);
				this.doc = parsedHtmlFuture.get();
				// System.out.println(obj.toString()+this.doc.html()+"hello");
				return CompletableFuture.completedFuture(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(js);

	}

	private String getXpath(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, Document doc,
			boolean CheckAll, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDom = this.updateDOM(driver);
		try {
			Object update = updateDom.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// checkingForPageLoaded(js);
		Elements elements;
		if (!CheckAll) {
			switch (fetchMetadataVO.getAction()) {
				case "clickTableLink":
				case "clickLink":
					elements = doc.select("a");
					break;
				case "clickButton":
					elements = doc.select("button, input[type=button],input[type=submit],a[role=button]");
					break;
				case "clickExpandorcollapse":
					elements = doc.select("a, button, input[type=button],input[type=submit]");
					break;
				case "selectByText":
					elements = doc.select("select");
					break;
				case "switchToFrame":
					elements = doc.select("iframe");
					break;
				case "clickImage":
					elements = doc.select("img, svg");
					break;
				case "":
					elements = doc.select("input");
					break;
				case "clearText":
					elements = doc.select("input");
					break;
				case "paste":
					elements = doc.select("input");
					break;
				case "login":
					elements = doc.select("input");
					break;
				case "textArea":
					elements = doc.select("textarea");
					break;
				case "tableRowSelect":
					elements = doc.select("table");
					break;
				default:
					elements = doc.select("*");
					break;
			}

		} else {
			elements = doc.select("*");
		}
		boolean flag = false;
		// Elements elements = doc.select("a");
		for (Element element : elements) {

			if ((fetchMetadataVO.getAction().equals("clickButton") || fetchMetadataVO.getAction().equals("clickLink")
					|| fetchMetadataVO.getAction().equals("clickTableLink"))) {
				String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");

				if (splitInputParameter.length == 1) {
					if ((element.text().trim().equalsIgnoreCase(fetchMetadataVO.getInputParameter())
							|| element.attr("alt").trim().equals(fetchMetadataVO.getInputParameter()))) {
						flag = true;
						return element.cssSelector();
					}
				} else {
					// Elements headerElements = null;
					// List<String> storingIds = new ArrayList<String>();
					Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label,button,a)")
							.last();
					Element parent = headerEle.parent();
					while (parent != null
							&& parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
						parent = parent.parent();
					}
					Element clickbutton = parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").first();
					return clickbutton.cssSelector();

				}

			} else if (fetchMetadataVO.getAction().equals("clickImage")) {

				try {
					String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
					if (splitInputParameter.length == 1) {
						if (element.tagName() == "svg") {
							if (element.select("title").text().equals(fetchMetadataVO.getInputParameter())
									|| element.select("title").text().replaceAll(String.valueOf((char) 160), " ")
											.equals(fetchMetadataVO.getInputParameter())
									|| element.parent().attr("title").equals(fetchMetadataVO.getInputParameter())
									|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
											.equals(fetchMetadataVO.getInputParameter())) {
								return element.cssSelector();
							}

						} else {
							if (element.attr("title").equals(fetchMetadataVO.getInputParameter())
									|| element.attr("title").replaceAll(String.valueOf((char) 160), " ")
											.equals(fetchMetadataVO.getInputParameter())
									|| element.parent().attr("title").equals(fetchMetadataVO.getInputParameter())
									|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
											.equals(fetchMetadataVO.getInputParameter())) {
								flag = true;
								return element.cssSelector();
							}
						}
					} else {

						Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)").last();
						Element parent = headerEle.parent();
						while (parent != null
								&& parent.select(String.format("img[title=%s]", splitInputParameter[1])).isEmpty()) {
							parent = parent.parent();
						}
						Element clickbutton = parent.select(String.format("img[title=%s]", splitInputParameter[1]))
								.first();
						return clickbutton.cssSelector();

					}
				} catch (Exception e) {
					// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
					// customerDetails);
					throw e;
				}

			} else if (fetchMetadataVO.getAction().equals("tableSendKeys")) {
				CompletableFuture<Object> updateDo = this.updateDOM(driver);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null && parent.select("th:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				String strele = "_afrFilter_FOpt1_afr__FOr1_afr_0_afr__FONSr2_afr_0_afr_MAnt2_afr_1_afr_pm1_afr_r1_afr_0_afr_ap1_afr_r12_afr_1_afr_at1_afr__ATp_afr_ta1_afr_c4::content";
				Element ele = doc.select("input[id=" + strele + "]").first();
				return ele.cssSelector();

			} else if (fetchMetadataVO.getAction().equals("textArea")
					|| fetchMetadataVO.getAction().equals("selectvaluesTable")
					|| fetchMetadataVO.getAction().equals("SendKeys")
					|| fetchMetadataVO.getAction().equals("selectByText")
					|| fetchMetadataVO.getAction().equals("clearText") || fetchMetadataVO.getAction().equals("paste")) {
				CompletableFuture<Object> updateDo = this.updateDOM(driver);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null
						&& parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				String forAtrr = parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").attr("for");
				if (fetchMetadataVO.getAction().equals("selectvaluesTable")
						|| fetchMetadataVO.getAction().equals("Dropdown Values")
						|| fetchMetadataVO.getAction().equals("") || fetchMetadataVO.getAction().equals("paste")) {
					Element textArea = parent.select(("input[id=" + forAtrr + "]")).first();
					if (fetchMetadataVO.getAction().equals("paste")) {
						textArea = parent
								.select(("input[id="
										+ "_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value10::content" + "]"))
								.first();
					}
					return textArea.cssSelector();
				} else if (fetchMetadataVO.getAction().equals("selectByText")) {
					Element textArea = parent.select(("select[id=" + forAtrr + "]")).first();
					return textArea.cssSelector();
				} else {

					Element textArea = parent.select(("textarea[id=" + forAtrr + "]")).first();
					return textArea.cssSelector();
				}

			} else if (fetchMetadataVO.getAction().equals("selectAvalue")) {
				Element textArea = doc.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)").last();
				return textArea.cssSelector();

			} else if (fetchMetadataVO.getAction().equals("clickExpandorcollapse")) {
				CompletableFuture<Object> updateDo = this.updateDOM(driver);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null && parent.select(":matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				Element ele = parent.select(":matchesOwn(^" + splitInputParameter[1] + "$)").first().parent().parent()
						.parent().select("a").first();
				System.out.println(ele.cssSelector());
				if (ele == element) {
					return ele.cssSelector();
				}

			} else if (fetchMetadataVO.getAction().equals("tableRowSelect")) {

				if (element.attr("summary").equals(fetchMetadataVO.getInputParameter())) {
					return element.cssSelector();
				}
			} else if (fetchMetadataVO.getAction().equals("switchToFrame")) {
				// System.out.println(element.attr("id")+
				// element.attr("id").equalsIgnoreCase(fetchMetadataVO.getInputParameter()));
				if (element.attr("id").contains(fetchMetadataVO.getInputParameter())) {
					return element.cssSelector();

				} else if (element.attr("title").contains(fetchMetadataVO.getInputParameter())) {
					return element.cssSelector();
				} else if (element.attr("name").contains(fetchMetadataVO.getInputParameter())) {
					return element.cssSelector();
				} else if (element.attr("src").contains(fetchMetadataVO.getInputParameter())) {
					return element.cssSelector();
				}
			}
		}

		return "";

	}

	private WebElement findTheElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, Element ele,
			String[] copyOfRange, WebDriver driver, boolean dropdown) {

		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				// System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (dropdown && doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (dropdown && doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, false);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								if (!placeholder) {
									List<WebElement> Selelements = Selelement
											.findElements(By.xpath("./following::input[not(@type='hidden')]"));
									for (WebElement Selem : Selelements) {
										if (!Selem.isEnabled() || !Selem.isDisplayed()) {
											continue;
										}
										return Selem;
									}
								}
								return Selelement;
							}
						}
					}
				}

				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private List<WebElement> findDatePicker(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			List<WebElement> elemets = new ArrayList<>();
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (secondElements.size() == 0) {
						logger.error("Failed to get element-" + copyOfRange[0] + "  in Dynamic Xpath");
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (ele == element) {
							continue;
						}
						if (copyOfRange.length > 1) {
							return findDatePicker(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							} else {
								WebElement Selelement1 = Selelement.findElement(By.xpath("./following::input"));
								elemets.add(Selelement1);
								elemets.add(Selelement);

								return elemets;
							}
						}
					}

				}
				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	@Async
	public CompletableFuture<String> getPageSource(WebDriver driver) {
		String pageSource = driver.getPageSource();
		return CompletableFuture.completedFuture(pageSource);
	}

	@Async
	public CompletableFuture<Document> parseHtml(String htmlString) throws IOException {
		Document parsedHtml = Jsoup.parse(htmlString);
		return CompletableFuture.completedFuture(parsedHtml);
	}

	// ___ Methods for every Actions

	// Method for login
	private void login(ScriptDetailsDto fetchMetadataVO, WebDriver driver) {
		String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement = selElement.findElement(By.xpath("./following::input"));
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
					break;
				}
			}
			WebElement selElement = findTheElement(fetchMetadataVO, null, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver, false);
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
				break;
			}
		}

	}

	// Method for paste
	private void paste(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver)
			throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0 && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(
								By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(ele.cssSelector())) ? null
										: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						Element parent1 = ele.parent();
						Elements pasteElements = null;
						while (parent1 != null) {
							if (!parent1.select("input").isEmpty()) {
								pasteElements = parent1.select("input");
								break;
							} else if (!parent1.select("textarea").isEmpty()) {
								pasteElements = parent1.select("textarea");
								break;
							}
							parent1 = parent1.parent();
						}
						if (parent1 != null) {
							for (Element elem : pasteElements) {
								WebElement selelem = driver.findElement(By.cssSelector(elem.cssSelector()));
								if (!selelem.isEnabled() || !selelem.isDisplayed()) {
									continue;
								}
								selelem.clear();
								String testParamId = fetchMetadataVO.getTestScriptParamId();
								String testSetId = fetchMetadataVO.getTestSetLineId();
								String copynumberValue;
								String inputValue = fetchMetadataVO.getInputValue();

								String[] arrOfStr = inputValue.split(">", 5);
								if (arrOfStr.length < 2) {
									copynumberValue = inputValue;
								} else {
									String Testrun_name = arrOfStr[0];
									String seq = arrOfStr[1];
									// String Script_num=arrOfStr[2];
									String line_number = arrOfStr[2];
									copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number,
											testParamId,
											testSetId);
								}
								try {
									JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
									jsExecutor.executeScript("arguments[0].focus();", selelem);
									selelem.sendKeys(Keys.chord(Keys.CONTROL, "a"));
								} catch (Exception e) {
									try {
										selelem.clear();
									} catch (Exception ex) {
										JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
										jsExecutor.executeScript("arguments[0].value = '';", selelem);
									}

								}
								// try {
								// selElement.sendKeys(fetchMetadataVO.getInputValue());
								// } catch(Exception exec) {
								// previousEle = selElement1;
								JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

								// jsExecutor.executeScript("arguments[0].click();", selElement);

								String script = "arguments[0].value = arguments[1]";
								try {
									// jsExecutor.executeScript("arguments[0].focus();", selElement);
									selelem.sendKeys(Keys.BACK_SPACE);
									jsExecutor.executeScript(script, selelem, copynumberValue);
									// String script1 = "arguments[0].value = arguments[0].value.slice(0, -1);";
									// jsExecutor.executeScript(script1, selElement);
									selelem.sendKeys(Keys.BACK_SPACE);
									script = "arguments[0].value += arguments[1];";
									jsExecutor.executeScript(script, selelem, copynumberValue
											.substring(copynumberValue.length() - 1));

								} catch (Exception exe) {
									try {
										selelem.sendKeys(fetchMetadataVO.getInputValue());
										selelem.sendKeys(Keys.BACK_SPACE);
										selelem.sendKeys(fetchMetadataVO.getInputValue()
												.substring(fetchMetadataVO.getInputValue().length() - 1));
									} catch (Exception i) {
										jsExecutor.executeScript("arguments[0].value =" + copynumberValue + ";",
												selelem);
									}
								}
								return;

							}
						}

					}
				} else {
					WebElement selElement1 = driver.findElement(By.xpath(ele.cssSelector()));
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					WebElement selElement = findThePasteElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in -" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						selElement.clear();
						String testParamId = fetchMetadataVO.getTestScriptParamId();
						String testSetId = fetchMetadataVO.getTestSetLineId();
						String copynumberValue;
						String inputValue = fetchMetadataVO.getInputValue();

						String[] arrOfStr = inputValue.split(">", 5);
						if (arrOfStr.length < 2) {
							copynumberValue = inputValue;
						} else {
							String Testrun_name = arrOfStr[0];
							String seq = arrOfStr[1];
							// String Script_num=arrOfStr[2];
							String line_number = arrOfStr[2];
							copynumberValue = dynamicnumber.getCopynumber(Testrun_name, seq, line_number, testParamId,
									testSetId);
						}

						try {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].focus();", selElement);
							selElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
						} catch (Exception e) {
							try {
								selElement.clear();
							} catch (Exception ex) {
								JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
								jsExecutor.executeScript("arguments[0].value = '';", selElement);
							}

						}
						// try {
						// selElement.sendKeys(fetchMetadataVO.getInputValue());
						// } catch(Exception exec) {
						// previousEle = selElement1;
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						// jsExecutor.executeScript("arguments[0].click();", selElement);

						String script = "arguments[0].value = arguments[1]";
						try {
							// jsExecutor.executeScript("arguments[0].focus();", selElement);
							selElement.sendKeys(Keys.BACK_SPACE);
							jsExecutor.executeScript(script, selElement, copynumberValue);
							// String script1 = "arguments[0].value = arguments[0].value.slice(0, -1);";
							// jsExecutor.executeScript(script1, selElement);
							selElement.sendKeys(Keys.BACK_SPACE);
							script = "arguments[0].value += arguments[1];";
							jsExecutor.executeScript(script, selElement, copynumberValue
									.substring(copynumberValue.length() - 1));

						} catch (Exception exe) {
							try {
								selElement.sendKeys(fetchMetadataVO.getInputValue());
								selElement.sendKeys(Keys.BACK_SPACE);
								selElement.sendKeys(fetchMetadataVO.getInputValue()
										.substring(fetchMetadataVO.getInputValue().length() - 1));
							} catch (Exception i) {
								jsExecutor.executeScript("arguments[0].value =" + copynumberValue + ";",
										selElement);
							}
						}
						return;
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for Table sendKeys
	// private void tableSendKeys(ScriptDetailsDto fetchMetadataVO, WebDriver
	// driver) {
	//
	// }

	private WebElement findThePasteElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(copyOfRange[0]);
			while (parent != null) {
				Elements secondElements = null;
				if (!parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				}
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (matcher.find() && !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {

									break;
								}
								continue;
							}
							return findThePasteElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {

									break;
								}
								continue;
							} else {
								Element parent1 = element.parent();
								Elements pasteElements = null;
								while (parent1 != null) {
									if (!parent1.select("input").isEmpty()) {
										pasteElements = parent1.select("input");
										break;
									} else if (!parent1.select("textarea").isEmpty()) {
										pasteElements = parent1.select("textarea");
										break;
									}
									parent1 = parent1.parent();
								}
								if (parent1 != null) {
									for (Element elem : pasteElements) {
										WebElement selelem = driver.findElement(By.cssSelector(elem.cssSelector()));
										if (!selelem.isEnabled() || !selelem.isDisplayed()) {
											continue;
										}
										return selelem;
									}
								}

								return null;
							}
						}
					}
				}
				parent = parent.parent();
			}

			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for selectDropdownValues
	private void selectDropdownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					selElement = selElement.findElement(By.xpath("./following::select"));
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;", selElement);
						List<WebElement> options = selElement.findElements(By.tagName("option"));
						for (WebElement option : options) {
							if (option.getText().equals(fetchMetadataVO.getInputValue())) {
								option.click();
								break;
							}
						}
						break;
					}
				} else {
					WebElement selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					WebElement selElement = findselectDropdownElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;", selElement);
						List<WebElement> options = selElement.findElements(By.tagName("option"));
						for (WebElement option : options) {
							if (option.getText().equals(fetchMetadataVO.getInputValue())) {
								option.click();
								break;
							}
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findselectDropdownElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {

					Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(secondElements.size() - 1) == ele) {
						parent = parent.parent();
						continue;
					}
					if (secondElements.size() == 0) {
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findselectDropdownElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							Selelement = Selelement.findElement(By.xpath("./following::select"));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								continue;
							} else {
								return Selelement;
							}
						}
					}

				}
				parent = parent.parent();
			}
			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}

			return null;
		} catch (Exception e) {
			// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// Method for sendKeys
	private void sendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			boolean dropdown) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			boolean placeholder = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (splitInputParameter.length == 1) {
						headerElements = popupDIv.select("*[placeholder='" + splitInputParameter[0] + "']");
						placeholder = true;
					}
					if (headerElements == null || headerElements.size() == 0) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						placeholder = false;
					}
					if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						placeholder = false;
					}
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
						placeholder = false;
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				if (splitInputParameter.length == 1) {
					headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
					placeholder = true;
				}
				if (headerElements == null || headerElements.size() == 0) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					placeholder = false;
				}
				if ((headerElements == null || headerElements.size() == 0) && matcher.find()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					placeholder = false;
				}
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
					placeholder = false;
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element " + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						if (!placeholder) {
							selElement = selElement.findElement(By.xpath("./following::input[not(@type='hidden')]"));
						}
						previousEle = selElement;
						try {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].focus();", selElement);
							selElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
						} catch (Exception e) {
							try {
								selElement.clear();
							} catch (Exception ex) {
								JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
								jsExecutor.executeScript("arguments[0].value = '';", selElement);
							}

						}
						// try {
						// selElement.sendKeys(fetchMetadataVO.getInputValue());
						// } catch(Exception exec) {
						// previousEle = selElement1;
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						// jsExecutor.executeScript("arguments[0].click();", selElement);

						String script = "arguments[0].value = arguments[1]";
						try {
							// jsExecutor.executeScript("arguments[0].focus();", selElement);
							selElement.sendKeys(Keys.BACK_SPACE);
							jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
							// String script1 = "arguments[0].value = arguments[0].value.slice(0, -1);";
							// jsExecutor.executeScript(script1, selElement);
							selElement.sendKeys(Keys.BACK_SPACE);
							script = "arguments[0].value += arguments[1];";
							jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue()
									.substring(fetchMetadataVO.getInputValue().length() - 1));

						} catch (Exception exe) {
							try {
								selElement.sendKeys(fetchMetadataVO.getInputValue());
								selElement.sendKeys(Keys.BACK_SPACE);
								selElement.sendKeys(fetchMetadataVO.getInputValue()
										.substring(fetchMetadataVO.getInputValue().length() - 1));
							} catch (Exception i) {
								jsExecutor.executeScript("arguments[0].value =" + fetchMetadataVO.getInputValue() + ";",
										selElement);
							}
						}
						break;
					}
				} else {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					}
					WebElement selElement1 = findTheElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver, dropdown);
					if (selElement1 == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						System.out.println(selElement1.getAttribute("aria-label"));
						if (selElement1.getAttribute("aria-label") != null
								&& !selElement1.getAttribute("aria-label").equalsIgnoreCase("")
								&& !selElement1.getAttribute("aria-label")
										.equalsIgnoreCase(splitInputParameter[splitInputParameter.length - 1])) {
							List<WebElement> selElems = selElement1
									.findElements(By.xpath("./following::input[not(@type='hidden')]"));
							for (WebElement selElem : selElems) {
								if (selElem.getAttribute("aria-label") != null
										&& selElem.getAttribute("aria-label") != ""
										&& selElem.getAttribute("aria-label").equalsIgnoreCase(
												splitInputParameter[splitInputParameter.length - 1])) {
									selElement1 = selElem;
									break;
								}
							}
						}
						try {
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							jsExecutor.executeScript("arguments[0].focus();", selElement1);
							selElement1.click();
							selElement1.sendKeys(Keys.chord(Keys.CONTROL, "a"));
						} catch (Exception e) {
							try {
								selElement1.clear();
							} catch (Exception ex) {
								JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
								jsExecutor.executeScript("arguments[0].value = '';", selElement1);
							}
						}
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

						String script = "arguments[0].value = arguments[1]";
						try {
							selElement1.sendKeys(Keys.BACK_SPACE);
							script = "arguments[0].value = arguments[1]";
							jsExecutor.executeScript(script, selElement1, fetchMetadataVO.getInputValue());
							// String script1 = "arguments[0].value = arguments[0].value.slice(0, -1);";
							// jsExecutor.executeScript(script1, selElement1);
							selElement1.sendKeys(Keys.BACK_SPACE);

							// Add the last character back using JavaScript
							script = "arguments[0].value += arguments[1];";
							jsExecutor.executeScript(script, selElement1, fetchMetadataVO.getInputValue()
									.substring(fetchMetadataVO.getInputValue().length() - 1));

						} catch (Exception exe) {
							selElement1.sendKeys(fetchMetadataVO.getInputValue());
							selElement1.sendKeys(Keys.BACK_SPACE);
							script = "arguments[0].value += arguments[1];";
							jsExecutor.executeScript(script, selElement1, fetchMetadataVO.getInputValue()
									.substring(fetchMetadataVO.getInputValue().length() - 1));

						}
						previousEle = selElement1;
					}

					// jsExecutor.executeScript("arguments[0].click();", selElement);
					break;
				}

			}
		}
		// catch(StaleElementReferenceException ex) {
		// logger.info("get Stayle element " +fetchMetadataVO.getInputParameter());
		// }
		catch (Exception e) {
			// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private void datePicker(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver)
			throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
			}

			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						selElement = selElement.findElement(By.xpath("./following::input"));
						previousEle = selElement;
						selElement.clear();
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						jsExecutor.executeScript("arguments[0].click();", selElement);
						String script = "arguments[0].value = arguments[1]";
						jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue() + " ");
						jsExecutor.executeScript(
								"var backspaceKeyEvent = new KeyboardEvent('keydown', { 'key': 'Backspace', 'code': 'Backspace', 'bubbles': true, 'cancelable': true }); document.dispatchEvent(backspaceKeyEvent);");
						break;
					}
				} else {

					WebElement selElement1 = null;
					try {
						selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement1 = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					List<WebElement> selElement = findDatePicker(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						selElement.get(0).clear();
						previousEle = selElement.get(0);
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						jsExecutor.executeScript("arguments[0].click();", selElement.get(0));
						String script = "arguments[0].value = arguments[1]";
						jsExecutor.executeScript(script, selElement.get(0), fetchMetadataVO.getInputValue());
						jsExecutor.executeScript("arguments[0].click();", selElement.get(0));
						jsExecutor.executeScript("arguments[0].click();", selElement.get(1));
						break;
					}
				}
			}
		} catch (Exception e) {
			// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// method for selectAValue
	// method for selectAValue

	private void selectAvalue(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver)
			throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean placeholder = false;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Pattern pattern1 = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher1 = pattern1.matcher(splitInputParameter[0]);
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (splitInputParameter.length == 1) {
						headerElements = popupDIv.select("*[placeholder='" + splitInputParameter[0] + "']");
						placeholder = true;
					}
					if (!matcher1.find() && (headerElements == null || headerElements.size() == 0)) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");

					}
					if (matcher1.find() && (headerElements == null || headerElements.size() == 0)) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (matcher1.find() && headerElements.size() == 0) {
						headerElements = popupDIv.select("*:containsOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() == 0 || headerElements == null) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				Pattern pattern1 = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher1 = pattern1.matcher(splitInputParameter[0]);
				if (splitInputParameter.length == 1) {
					headerElements = doc.select("*[placeholder='" + splitInputParameter[0] + "']");
					placeholder = true;
				}
				if (!matcher1.find() && (headerElements == null || headerElements.size() == 0)) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if (matcher1.find() && (headerElements == null || headerElements.size() == 0)) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
				if (matcher1.find() && headerElements.size() == 0) {
					headerElements = doc.select("*:containsOwn(" + splitInputParameter[0] + ")");
				}

				if (headerElements.size() == 0 || headerElements == null) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
			}

			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					boolean flag = false;
					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						// selElement = selElement.findElement(By.xpath("./following::input"));

						Element parent = ele.parent();
						Element mainParent = parent;
						Elements selectAValueElement = null;
						logger.error("Failed to get element in-" + doc
								.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
								+ "  in Dynamic Xpath");
						System.out.println(doc
								.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")"));
						while (parent != null) {
							Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9\\s]");
							Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
							if (!matcher2.find() && !parent
									.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)").isEmpty()) {
								selectAValueElement = parent
										.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
								break;
							}
							if (matcher2.find() && !parent
									.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")").isEmpty()) {
								selectAValueElement = parent
										.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")");
								break;
							}
							parent = parent.parent();
						}
						if (parent == null) {
							while (mainParent != null) {
								Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9\\s]");
								Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
								if (matcher2.find() && !mainParent
										.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")").isEmpty()) {
									selectAValueElement = mainParent
											.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")");
									break;
								}
								else if (!mainParent.select("*[title='" + fetchMetadataVO.getInputValue() + "']")
										.isEmpty()) {
											selectAValueElement = mainParent.select("*[title='" + fetchMetadataVO.getInputValue() + "']");
									break;
								}
								else if (!mainParent.select("*[alt='" + fetchMetadataVO.getInputValue() + "']")
										.isEmpty()) {
											selectAValueElement = mainParent.select("*[alt='" + fetchMetadataVO.getInputValue() + "']");
									break;
								}
								mainParent = mainParent.parent();
							}
							if (mainParent == null) {
								Element mainParent1 = ele.parent();
								while (mainParent1 != null) {
									Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9\\s]");
									Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
									if (!matcher2.find() && !mainParent1
											.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")")
											.isEmpty()) {
										selectAValueElement = mainParent1
												.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")");
										break;
									}
									mainParent1 = mainParent1.parent();
								}
								if (mainParent1 == null) {
									throw new NullPointerException();
								}
							}
						}
						if (parent != null || mainParent != null || selectAValueElement.size() != 0) {
							if (selectAValueElement.size() == 0) {
								throw new NullPointerException();

							}
							for (Element element : selectAValueElement) {
								if (selectAValueElement.last() != element) {
									if (!element.parent().text().equalsIgnoreCase(element.text())) {
										continue;
									}
								}
								WebElement selele = null;
								try {
									selele = driver.findElement(By.cssSelector(element.cssSelector()));
								} catch (Exception e) {
									selele = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
													: CSS2XPath.css2xpath(element.cssSelector(), true)));
								}
								if (!selele.isEnabled() || !selele.isDisplayed()) {
									continue;
								} else {
									try {
										selele.click();
										flag = true;
										break;
									} catch (Exception e) {
										JavascriptExecutor executor = (JavascriptExecutor) driver;
										executor.executeScript("arguments[0].click();", selele);
										flag = true;
										break;

									}
								}
							}
							if (flag) {
								break;
							}
						}
					}
				}

				else {
					WebElement selElement1 = null;
					try {
						selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						selElement1 = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					WebElement selElement = findTheSelectAvalueElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						// selElement.click();
						try {
							highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
							xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", selElement);
							break;
						} catch (Exception e) {
							// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
							// customerDetails);
							selElement.click();
							break;

						}
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findTheSelectAvalueElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {

		try {
			boolean placeholder = false;
			Element parent = ele.parent();
			Elements secondElements = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				// System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(copyOfRange[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !parent.select("*[placeholder='" + copyOfRange[0] + "']").isEmpty()) {
					secondElements = parent.select("*[placeholder='" + copyOfRange[0] + "']");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = true;
				} else if (!havingSinglequote && !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {
					secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingSinglequote && !parent.select(selector).isEmpty()) {
					secondElements = parent.select(selector);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				} else if (havingBraces && !parent.select(selector1).isEmpty()) {
					secondElements = parent.select(selector1);
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Value" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Value" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}
				if (doc.select("*[data-afr-popupid]").size() > 0
						&& !parent.select(":matchesOwn(^" + "Name" + "$)").isEmpty()) {
					secondElements = parent.select(":matchesOwn(^" + "Name" + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					placeholder = false;
				}

				if (secondElements != null && secondElements.size() > 0) {
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheSelectAvalueElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								Selelement = driver.findElement(
										By.xpath(("\\fetchMetadataVO".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								if (!placeholder) {
									Element parent1 = element.parent();
									Element mainParent = parent1;
									Elements selectAValueElement = null;
									while (parent1 != null) {
										Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9\\s]");
										Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
										if (!matcher2.find() && !parent1
												.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
												.isEmpty()) {
											selectAValueElement = parent1
													.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
											break;
										}
										if (matcher2.find() && !parent1
												.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")")
												.isEmpty()) {
											selectAValueElement = parent1
													.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")");
											break;
										}
										parent1 = parent1.parent();
									}
									if (parent1 == null) {
										while (mainParent != null) {
											Pattern pattern2 = Pattern.compile("[^a-zA-Z0-9\\s]");
											Matcher matcher2 = pattern2.matcher(fetchMetadataVO.getInputValue());
											if (matcher2.find() && !mainParent
													.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")")
													.isEmpty()) {
												selectAValueElement = mainParent
														.select(":containsOwn(" + fetchMetadataVO.getInputValue()
																+ ")");
												break;
											} else if (!mainParent.select("*[title='" + copyOfRange[0] + "']")
													.isEmpty()) {
												selectAValueElement = mainParent
														.select("*[title='" + copyOfRange[0] + "']");
												break;
											}
											else if (!mainParent.select("*[alt='" + copyOfRange[0] + "']")
													.isEmpty()) {
												selectAValueElement = mainParent
														.select("*[alt='" + copyOfRange[0] + "']");
												break;
											}
											mainParent = mainParent.parent();
										}
										if (mainParent == null) {
											throw new NullPointerException();
										}
									}
									if (parent1 != null || mainParent != null) {
										if (selectAValueElement.size() == 0) {
											throw new NullPointerException();

										}
										for (Element element1 : selectAValueElement) {
											if (!element1.parent().text().equals(element1.text())) {
												continue;
											}
											WebElement selele = null;
											try {
												selele = driver.findElement(By.cssSelector(element1.cssSelector()));
											} catch (Exception e) {
												selele = driver.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(element1.cssSelector()))
																? null
																: CSS2XPath.css2xpath(element1.cssSelector(), true)));
											}
											if (!selele.isEnabled() || !selele.isDisplayed()) {
												continue;
											} else {
												return selele;
											}
										}
									}

								} else
									return Selelement;
							}
						}
					}
				}

				parent = parent.parent();
			}

			if (parent == null) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for Clear
	private void clearText(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver)
			throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element -" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						selElement.clear();

					}
				} else {
					WebElement selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					WebElement selElement = findTheElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver, false);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						selElement.clear();
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for click Link
	private void dropDownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			FetchConfigVO fetchConfigVO) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {

				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					} else if (!matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					} else if ((headerElements == null || headerElements.size() == 0) && matcher.find()
							&& !popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					} else if ((headerElements == null || headerElements.size() == 0) && matcher.find()
							&& !popupDIv.select("*:containsOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:containsOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}

			if (toolTip == false) {
				if (!matcher.find()) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if ((headerElements == null || headerElements.size() == 0) && matcher.find()
						&& !doc.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}

			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element-" + splitInputParameter[0] + "  in Dynamic Xpath");
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {
					WebElement Selelement = null;
					try {
						WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
						Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception ex) {
						Selelement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}

					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					}

					WebElement Selelement2 = Selelement
							.findElement(By.xpath("./following::input[not(@type='hidden')]"));
					WebElement Selelement1 = Selelement2.findElement(By.xpath("./following::a"));
					Thread.sleep(1000);
					// previousEle = Selelement1;
					if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
						continue;
					} else {
						if (Selelement2.getAttribute("value").equalsIgnoreCase(fetchMetadataVO.getInputValue())) {
							return;
						}
						try {
							Selelement1.click();
						} catch (Exception ex) {

							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", Selelement1);

						}
						Thread.sleep(5000);
						CompletableFuture<Object> updateDo = this.updateDOM(driver);
						try {
							Object update = updateDo.get();
						} catch (InterruptedException | ExecutionException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						Elements popupElements = doc.select("*[data-afr-popupid]");
						for (int i = popupElements.size() - 1; i >= 0; i--) {
							Element parent = doc.select("*[data-afr-popupid]").get(i);
							Element mainParent = parent;
							Elements selecElements = null;
							boolean gotElementWithhoutAdvSearch = false;
							while (parent != null) {
								Pattern pattern1 = Pattern.compile("[^a-zA-Z0-9\\s]");
								Matcher matcher1 = pattern1.matcher(fetchMetadataVO.getInputValue());
								if (!matcher1.find() && !parent
										.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)").isEmpty()) {
									selecElements = parent
											.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
									gotElementWithhoutAdvSearch = true;
									break;
								}
								if (matcher1.find() && !parent
										.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")").isEmpty()) {
									selecElements = parent
											.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")");
									gotElementWithhoutAdvSearch = true;
									break;
								}
								if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
									if (!parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)")
											.isEmpty()) {
										selecElements = parent
												.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
										if (matcher1.find()) {
											selecElements = parent
													.select(":matchesOwn(" + fetchMetadataVO.getInputValue() + ")");
										}
										gotElementWithhoutAdvSearch = true;
									} else {
										selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
									}
									break;
								}
								parent = parent.parent();
							}
							if (parent == null) {
								while (mainParent != null) {
									if (!mainParent.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")")
											.isEmpty()) {
										selecElements = mainParent
												.select(":containsOwn(" + fetchMetadataVO.getInputValue() + ")");
										gotElementWithhoutAdvSearch = true;
										break;
									}
									mainParent = mainParent.parent();
								}

							}
							if (parent != null || mainParent != null) {
								for (Element elem : selecElements) {
									WebElement selElement = null;
									try {
										selElement = driver.findElement(By.cssSelector(elem.cssSelector()));
									} catch (Exception ex) {
										selElement = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));

									}
									if (!selElement.isEnabled() || !selElement.isDisplayed()) {
										continue;
									}
									if (gotElementWithhoutAdvSearch) {
										try {
											selElement.click();
										} catch (Exception e) {
											JavascriptExecutor executor = (JavascriptExecutor) driver;
											executor.executeScript("arguments[0].click();", selElement);
										}
										JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
										jsExecutor.executeScript("arguments[0].click();", Selelement2);
										((JavascriptExecutor) driver).executeScript("arguments[0].focus();",
												Selelement2);
										previousEle = Selelement2;
										break;
									} else {
										try {
											selElement.click();
										} catch (Exception ex) {
											JavascriptExecutor executor = (JavascriptExecutor) driver;
											executor.executeScript("arguments[0].click();", selElement);

										}
										// findSear
										Thread.sleep(5000);
										CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
										try {
											Object update = updateDo1.get();
										} catch (InterruptedException | ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										fetchMetadataVO.setInputParameter("Search>" + splitInputParameter[0]);
										sendKeys(fetchMetadataVO, customerDetails, driver, true);
										fetchMetadataVO.setInputParameter("Search>" + "Search");
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);
										Thread.sleep(5000);
										updateDOM(driver);
										fetchMetadataVO.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);
										Thread.sleep(5000);
										updateDOM(driver);
										fetchMetadataVO.setInputParameter("Search>" + "OK");
										clickLink(fetchMetadataVO, driver, customerDetails, true, fetchConfigVO);

										JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
										jsExecutor.executeScript("arguments[0].click();", Selelement2);
										((JavascriptExecutor) driver).executeScript("arguments[0].focus();",
												Selelement2);
										previousEle = Selelement2;
										break;

									}

								}

								// WebElement SearchSelelement = Selelement
								// .findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
								// return SearchSelelement;
							}
						}

					}

				} else {
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
					WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement.isEnabled() || !selElement.isDisplayed()
					// ___uncomment it
					// || !selElement.isClickable()
					// --comment this
					// || selElement.getTagName().equals("span")
					) {
						// s
						continue;
					}
					selElement = findDropDownEle(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length),
							fetchMetadataVO.getInputValue(), driver, fetchConfigVO);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						// jsExecutor.executeScript("arguments[0].click();", selElement);
						// ((JavascriptExecutor) driver).executeScript("arguments[0].focus();",
						// selElement);
						previousEle = selElement;
						break;
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private WebElement findDropDownEle(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, String inputValue, WebDriver driver, FetchConfigVO fetchConfigVO)
			throws Exception {
		try {
			Element parent = ele.parent();
			Element mainParent = ele.parent();
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (copyOfRange[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = copyOfRange[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern3 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher3 = pattern3.matcher(copyOfRange[0]);
			if (matcher3.matches()) {
				havingBraces = true;
				String[] elementsb = copyOfRange[0].split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			while (parent != null) {
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
				Matcher matcher = pattern.matcher(copyOfRange[0]);
				if (!havingSinglequote && !matcher.find()
						&& !parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
					Elements dropdownEle = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (dropdownEle.size() == 1 && dropdownEle.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (dropdownEle.size() == 0) {
						logger.error("Failed to get element-" + copyOfRange[0] + "  in Dynamic Xpath");
						throw new NullPointerException();

					}
					for (Element element : dropdownEle) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							return findDropDownEle(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length),
									fetchMetadataVO.getInputValue(), driver, fetchConfigVO);
						} else {
							WebElement Selelement = null;
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception ex) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}

							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							WebElement Selelementinput = Selelement
									.findElement(By.xpath("./following::input[not(@type='hidden')]"));
							WebElement Selelement1 = Selelementinput.findElement(By.xpath("./following::a"));
							Thread.sleep(1000);
							// previousEle = Selelement1;
							if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
								continue;
							} else {
								if (Selelementinput.getAttribute("value")
										.equalsIgnoreCase(fetchMetadataVO.getInputValue())) {
									return Selelementinput;
								}
								try {
									Selelement1.click();
								} catch (Exception ex) {

									JavascriptExecutor executor = (JavascriptExecutor) driver;
									executor.executeScript("arguments[0].click();", Selelement1);

								}
								Thread.sleep(5000);
								CompletableFuture<Object> updateDo = this.updateDOM(driver);
								try {
									Object update = updateDo.get();
								} catch (InterruptedException | ExecutionException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
								Elements popupElements = doc.select("*[data-afr-popupid]");
								for (int i = popupElements.size() - 1; i >= 0; i--) {
									parent = doc.select("*[data-afr-popupid]").get(i);
									Element mainparernt = parent;
									Elements selecElements = null;
									boolean gotElementWithhoutAdvSearch = false;
									Pattern pattern1 = Pattern.compile("[^a-zA-Z0-9\\s]");
									Matcher matcher1 = pattern1.matcher(inputValue);
									System.out.println(matcher1.find());
									System.out.println(parent.select(":matchesOwn(" + inputValue + ")"));
									System.out.println(parent.select(":containsOwn(" + inputValue + ")"));
									while (parent != null) {
										if (!matcher1.find()
												&& !parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
											selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
											gotElementWithhoutAdvSearch = true;
											break;
										}
										if (matcher1.find()
												&& !parent.select(":matchesOwn(" + inputValue + ")").isEmpty()) {
											selecElements = parent.select(":matchesOwn(" + inputValue + ")");
											gotElementWithhoutAdvSearch = true;
											break;
										}

										if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
											if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
												selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
												gotElementWithhoutAdvSearch = true;
											} else {
												selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
											}
											break;
										}
										parent = parent.parent();
									}
									if (parent == null) {
										while (mainparernt != null) {
											if (!mainparernt.select(":containsOwn(" + inputValue + ")").isEmpty()) {
												selecElements = mainparernt.select(":containsOwn(" + inputValue + ")");
												gotElementWithhoutAdvSearch = true;
												break;
											}
											mainparernt = mainparernt.parent();
										}
										// if(mainparernt == null) {
										// throw new NullPointerException();
										// }

									}
									if (parent != null || mainparernt != null) {
										for (Element elem : selecElements) {
											WebElement selElement = null;
											try {
												selElement = driver.findElement(By.cssSelector(elem.cssSelector()));
											} catch (Exception ex) {
												selElement = driver.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
																: CSS2XPath.css2xpath(elem.cssSelector(), true)));

											}
											if (!selElement.isEnabled() || !selElement.isDisplayed()) {
												continue;
											}
											if (gotElementWithhoutAdvSearch) {
												try {
													selElement.click();
												} catch (Exception e) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);
												}
												return Selelementinput;
											} else {
												try {
													selElement.click();
												} catch (Exception ex) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);

												}
												// findSear
												Thread.sleep(5000);
												CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
												try {
													Object update = updateDo1.get();
												} catch (InterruptedException | ExecutionException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												fetchMetadataVO.setInputParameter("Search>" + copyOfRange[0]);
												sendKeys(fetchMetadataVO, customerDetails, driver, true);
												fetchMetadataVO.setInputParameter("Search>" + "Search");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO
														.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO.setInputParameter("Search>" + "OK");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												return Selelementinput;

											}

										}
										break;

									}
								}

							}

						}

					}
				}
				if (!havingSinglequote && matcher.find()
						&& !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {

					Elements dropdownEle = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (dropdownEle.size() == 1 && dropdownEle.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (dropdownEle.size() == 0) {
						throw new NullPointerException();

					}
					for (Element element : dropdownEle) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							return findDropDownEle(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length),
									fetchMetadataVO.getInputValue(), driver, fetchConfigVO);
						} else {
							WebElement Selelement = null;
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception ex) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}

							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							WebElement Selelement2 = Selelement
									.findElement(By.xpath("./following::input[not(@type='hidden')]"));
							WebElement Selelement1 = Selelement2.findElement(By.xpath("./following::a"));
							Thread.sleep(1000);
							// previousEle = Selelement1;
							if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
								continue;
							} else {
								try {
									Selelement1.click();
								} catch (Exception ex) {

									JavascriptExecutor executor = (JavascriptExecutor) driver;
									executor.executeScript("arguments[0].click();", Selelement1);

								}
								Thread.sleep(5000);
								CompletableFuture<Object> updateDo = this.updateDOM(driver);
								try {
									Object update = updateDo.get();
								} catch (InterruptedException | ExecutionException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
								Elements popupElements = doc.select("*[data-afr-popupid]");
								for (int i = popupElements.size() - 1; i >= 0; i--) {
									parent = doc.select("*[data-afr-popupid]").get(i);
									Elements selecElements = null;
									boolean gotElementWithhoutAdvSearch = false;
									while (parent != null) {
										if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
											selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
											gotElementWithhoutAdvSearch = true;
											break;
										}
										if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
											if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
												selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
												gotElementWithhoutAdvSearch = true;
											} else {
												selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
											}
											break;
										}
										parent = parent.parent();
									}
									if (parent == null) {
										throw new NullPointerException();

									}
									if (parent != null) {
										for (Element elem : selecElements) {
											WebElement selElement = null;
											try {
												selElement = driver.findElement(By.cssSelector(elem.cssSelector()));
											} catch (Exception ex) {
												selElement = driver.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
																: CSS2XPath.css2xpath(elem.cssSelector(), true)));

											}
											if (!selElement.isEnabled() || !selElement.isDisplayed()) {
												continue;
											}
											if (gotElementWithhoutAdvSearch) {
												try {
													selElement.click();
												} catch (Exception e) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);
												}
												return Selelement2;
											} else {
												try {
													selElement.click();
												} catch (Exception ex) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);

												}
												// findSear
												Thread.sleep(5000);
												CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
												try {
													Object update = updateDo1.get();
												} catch (InterruptedException | ExecutionException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												fetchMetadataVO.setInputParameter("Search>" + copyOfRange[0]);
												sendKeys(fetchMetadataVO, customerDetails, driver, true);
												fetchMetadataVO.setInputParameter("Search>" + "Search");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO
														.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO.setInputParameter("Search>" + "OK");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												return Selelement2;

											}

										}

										// WebElement SearchSelelement = Selelement
										// .findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
										// return SearchSelelement;
									}
									break;
								}

							}

						}

					}

				}
				if (havingSinglequote && !parent.select(selector).isEmpty()) {
					Elements dropdownEle = parent.select(selector);
					if (dropdownEle.size() == 1 && dropdownEle.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (dropdownEle.size() == 0) {
						logger.error(
								"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
						throw new NullPointerException();

					}
					for (Element element : dropdownEle) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							return findDropDownEle(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length),
									fetchMetadataVO.getInputValue(), driver, fetchConfigVO);
						} else {
							WebElement Selelement = null;
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception ex) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}

							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									break;
								}
								continue;
							}

							WebElement Selelement2 = Selelement
									.findElement(By.xpath("./following::input[not(@type='hidden')]"));
							WebElement Selelement1 = Selelement2.findElement(By.xpath("./following::a"));
							Thread.sleep(1000);
							// previousEle = Selelement1;
							if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
								continue;
							} else {
								try {
									Selelement1.click();
								} catch (Exception ex) {

									JavascriptExecutor executor = (JavascriptExecutor) driver;
									executor.executeScript("arguments[0].click();", Selelement1);

								}
								Thread.sleep(5000);
								CompletableFuture<Object> updateDo = this.updateDOM(driver);
								try {
									Object update = updateDo.get();
								} catch (InterruptedException | ExecutionException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
								Elements popupElements = doc.select("*[data-afr-popupid]");
								for (int i = popupElements.size() - 1; i >= 0; i--) {
									parent = doc.select("*[data-afr-popupid]").get(i);
									Elements selecElements = null;
									boolean gotElementWithhoutAdvSearch = false;
									while (parent != null) {
										if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
											selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
											gotElementWithhoutAdvSearch = true;
											break;
										}
										if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
											if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
												selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
												gotElementWithhoutAdvSearch = true;
											} else {
												selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
											}
											break;
										}
										parent = parent.parent();
									}
									if (parent == null) {
										throw new NullPointerException();

									}
									if (selecElements.size() == 0) {
										logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
												+ "  in Dynamic Xpath");
										throw new NullPointerException();
									}
									if (parent != null) {
										for (Element elem : selecElements) {
											WebElement selElement = null;
											try {
												selElement = driver.findElement(By.cssSelector(elem.cssSelector()));
											} catch (Exception ex) {
												selElement = driver.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
																: CSS2XPath.css2xpath(elem.cssSelector(), true)));

											}
											if (!selElement.isEnabled() || !selElement.isDisplayed()) {
												continue;
											}
											if (gotElementWithhoutAdvSearch) {
												try {
													selElement.click();
													return Selelement2;
												} catch (Exception e) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);
													return Selelement2;
												}

											} else {
												// if()
												try {
													selElement.click();
												} catch (Exception ex) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);

												}
												// findSear
												Thread.sleep(5000);
												CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
												try {
													Object update = updateDo1.get();
												} catch (InterruptedException | ExecutionException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												fetchMetadataVO.setInputParameter("Search>" + copyOfRange[0]);
												sendKeys(fetchMetadataVO, customerDetails, driver, true);
												fetchMetadataVO.setInputParameter("Search>" + "Search");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO
														.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO.setInputParameter("Search>" + "OK");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												return Selelement2;

											}

										}

										// WebElement SearchSelelement = Selelement
										// .findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
										// return SearchSelelement;
									}
									break;
								}

							}

						}

					}

				}

				if (havingBraces && !parent.select(selector1).isEmpty()) {
					Elements dropdownEle = parent.select(selector1);
					if (dropdownEle.size() == 1 && dropdownEle.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (dropdownEle.size() == 0) {
						logger.error(
								"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
						throw new NullPointerException();

					}
					for (Element element : dropdownEle) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									parent = parent.parent();
									break;
								}
								continue;
							}
							return findDropDownEle(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length),
									fetchMetadataVO.getInputValue(), driver, fetchConfigVO);
						} else {
							WebElement Selelement = null;
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception ex) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}

							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == dropdownEle.get(dropdownEle.size() - 1)) {
									break;
								}
								continue;
							}

							WebElement Selelement2 = Selelement
									.findElement(By.xpath("./following::input[not(@type='hidden')]"));
							WebElement Selelement1 = Selelement2.findElement(By.xpath("./following::a"));
							Thread.sleep(1000);
							// previousEle = Selelement1;
							if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
								continue;
							} else {
								try {
									Selelement1.click();
								} catch (Exception ex) {

									JavascriptExecutor executor = (JavascriptExecutor) driver;
									executor.executeScript("arguments[0].click();", Selelement1);

								}
								Thread.sleep(5000);
								CompletableFuture<Object> updateDo = this.updateDOM(driver);
								try {
									Object update = updateDo.get();
								} catch (InterruptedException | ExecutionException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
								Elements popupElements = doc.select("*[data-afr-popupid]");
								for (int i = popupElements.size() - 1; i >= 0; i--) {
									parent = doc.select("*[data-afr-popupid]").get(i);
									Elements selecElements = null;
									boolean gotElementWithhoutAdvSearch = false;
									while (parent != null) {
										if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
											selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
											gotElementWithhoutAdvSearch = true;
											break;
										}
										if (!parent.select(":matchesOwn(^" + "Search..." + "$)").isEmpty()) {
											if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
												selecElements = parent.select(":matchesOwn(^" + inputValue + "$)");
												gotElementWithhoutAdvSearch = true;
											} else {
												selecElements = parent.select(":matchesOwn(^" + "Search..." + "$)");
											}
											break;
										}
										parent = parent.parent();
									}
									if (parent == null) {
										throw new NullPointerException();

									}
									if (selecElements.size() == 0) {
										logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
												+ "  in Dynamic Xpath");
										throw new NullPointerException();
									}
									if (parent != null) {
										for (Element elem : selecElements) {
											WebElement selElement = null;
											try {
												selElement = driver.findElement(By.cssSelector(elem.cssSelector()));
											} catch (Exception ex) {
												selElement = driver.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
																: CSS2XPath.css2xpath(elem.cssSelector(), true)));

											}
											if (!selElement.isEnabled() || !selElement.isDisplayed()) {
												continue;
											}
											if (gotElementWithhoutAdvSearch) {
												try {
													selElement.click();
													return Selelement2;
												} catch (Exception e) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);
													return Selelement2;
												}

											} else {
												// if()
												try {
													selElement.click();
												} catch (Exception ex) {
													JavascriptExecutor executor = (JavascriptExecutor) driver;
													executor.executeScript("arguments[0].click();", selElement);

												}
												// findSear
												Thread.sleep(5000);
												CompletableFuture<Object> updateDo1 = this.updateDOM(driver);
												try {
													Object update = updateDo1.get();
												} catch (InterruptedException | ExecutionException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												fetchMetadataVO.setInputParameter("Search>" + copyOfRange[0]);
												sendKeys(fetchMetadataVO, customerDetails, driver, true);
												fetchMetadataVO.setInputParameter("Search>" + "Search");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO
														.setInputParameter("Search>" + fetchMetadataVO.getInputValue());
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												Thread.sleep(5000);
												updateDOM(driver);
												fetchMetadataVO.setInputParameter("Search>" + "OK");
												clickLink(fetchMetadataVO, driver, customerDetails, true,
														fetchConfigVO);
												return Selelement2;

											}

										}

										// WebElement SearchSelelement = Selelement
										// .findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
										// return SearchSelelement;
									}
									break;
								}

							}

						}

					}

				}

				parent = parent.parent();
			}
			if (parent == null) {
				throw new NullPointerException();
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// method for click Link
	private void clickLink(ScriptDetailsDto fetchMetadataVO, WebDriver driver, CustomerProjectDto customerDetails,
			boolean dropdown, FetchConfigVO fetchConfigVO) throws Exception {

		// Elements elements = GettingAllElements(xpath.getAction());;;;
		try {

			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			// updateDOM(driver);
			WebElement cssSelectorEle = null;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";

			if (splitInputParameter[0].contains("'")) {
				havingSinglequote = true;
				String[] splitItems = splitInputParameter[0].split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				System.out.println(doc.select(selector));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)\\-].*");
			Matcher matcher1 = pattern1.matcher(splitInputParameter[0]);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = splitInputParameter[0].split("\\s*[\\(\\)\\-]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
			}
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			boolean findInToolTip = false;
			if (splitInputParameter.length == 1) {
				boolean specialCase = false;
				Elements elements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Thread.sleep(2000);
					updateDOM(driver);
					Elements popupElements = doc.select("*[data-afr-popupid]");
					Element popupDiv = null;
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						popupDiv = popupElements.get(i);
						if (!popupDiv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDiv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						if (havingSinglequote) {
							elements = popupDiv.select(selector);
						} else if (havingBraces) {
							elements = popupDiv.select(selector1);
						} else if (!havingSinglequote && matcher.find()) {
							elements = popupDiv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						} else {
							elements = popupDiv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						}
						if (elements.size() != 0) {
							toolTip = true;
							findInToolTip = true;
							break;
						}
					}
					if (elements.size() == 0) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = popupDiv
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
							// toolTip=true;
						}
					}
					if (elements.size() == 0) {
						elements = popupDiv.select("[title='" + splitInputParameter[0] + "']:not([type=text])");
					}
				}
				if (toolTip == false) {
					if (havingSinglequote) {
						elements = doc.select(selector);
					} else if (havingBraces) {
						elements = doc.select(selector1);
					} else if (!havingSinglequote && matcher.find()) {
						elements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					} else {
						elements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					}
					boolean notThere = false;
					for (Element element : elements) {
						WebElement ele = null;
						try {
							ele = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							try {
								ele = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							} catch (Exception ex) {
								ele = driver.findElement(By.xpath(getXPath(element)));
							}
						}

						if (!ele.isEnabled() || !ele.isDisplayed()) {
							continue;
						}
						if (element.tagName().equals("title")) {
							continue;
						}
						notThere = true;

					}
					if (elements.size() == 0 || !notThere) {
						for (int i = 0; i < splitInputParameter[0].length(); i++) {
							Elements eachCharacter = doc
									.select(":matchesOwn(^" + splitInputParameter[0].charAt(i) + "$)");
							if (!eachCharacter.isEmpty()) {
								for (Element ele : eachCharacter) {
									Element parent = ele.parent();
									if (parent.text().equals(splitInputParameter[0])
											|| ele.text().equals(splitInputParameter[0])) {
										elements = eachCharacter;
										specialCase = true;
										break;
									}
								}
								if (specialCase) {
									break;
								}
							}
						}
					}
				}
				for (Element element : elements) {
					WebElement ele = null;
					try {
						ele = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						try {
							ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
						} catch (Exception ex) {
							ele = driver.findElement(By.xpath(getXPath(element)));
						}
					}

					if (!ele.isEnabled() || !ele.isDisplayed()) {
						if (element.tagName().equals("title")) {
							continue;
						}
						if (elements.size() != 1) {
							continue;
						}

					}
					if (specialCase) {
						Element parent = element.parent();
						if (parent.text().equals(splitInputParameter[0])
								|| element.text().equals(splitInputParameter[0])) {
							cssSelectorEle = ele;
							break;
						} else {
							continue;
						}
					}

					cssSelectorEle = ele;
					break;
				}
				if (cssSelectorEle == null) {
					elements = doc.select("[title='" + splitInputParameter[0] + "']:not([type=text])");
					for (Element element : elements) {
						WebElement ele = null;
						try {
							ele = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							try {
								ele = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							} catch (Exception ex) {
								ele = driver.findElement(By.xpath(getXPath(element)));
							}
						}

						if (!ele.isEnabled() || !ele.isDisplayed()) {
							if (element.tagName().equals("title")) {
								if (element == elements.last()) {
									throw new NullPointerException();
								}
								continue;
							}
							if (elements.size() != 1 && !ele.isEnabled()) {
								continue;
							}

						}
						if (specialCase) {
							Element parent = element.parent();
							if (parent.text().equals(splitInputParameter[0])
									|| element.text().equals(splitInputParameter[0])) {
								cssSelectorEle = ele;
								break;
							} else {
								continue;
							}
						}

						cssSelectorEle = ele;
						break;
					}

				}
				if (elements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();

				}

			} else {
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									try {
										Selelement = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true)));
									} catch (Exception ex) {
										Selelement = driver.findElement(By.xpath(getXPath(ele)));
									}

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						if (havingSinglequote) {
							headerElements = popupDIv.select(selector);
						} else if (havingBraces) {
							headerElements = popupDIv.select(selector1);
						} else if (!havingSinglequote && matcher.find()) {
							headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
						} else {
							headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						}
						if (headerElements.size() != 0) {
							toolTip = true;
							findInToolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					if (havingSinglequote) {
						headerElements = doc.select(selector);
					} else if (havingBraces) {
						headerElements = doc.select(selector1);
					} else if (!havingSinglequote && matcher.find()) {
						headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					} else {
						headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					}
				}
				if (headerElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element elem : headerElements) {
					WebElement headerSelElement = null;
					try {
						headerSelElement = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							headerSelElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
											: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception e) {
							headerSelElement = driver.findElement(By.xpath(getXPath(elem)));
						}

					}
					if (!headerSelElement.isEnabled() || !headerSelElement.isDisplayed()) {
						continue;
					} else {
						WebElement selElement = findLinkElement(fetchMetadataVO, customerDetails, elem,
								Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver,
								fetchConfigVO);
						if (selElement == null) {
							if (elem == headerElements.get(headerElements.size() - 1)) {
								logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
										+ "  in Dynamic Xpath");
								throw new NullPointerException();
							}
							continue;
						} else {
							cssSelectorEle = selElement;
							break;
						}
					}
				}

			}
			if (cssSelectorEle != null) {

				// WebElement buttonElement =
				// driver.findElement(By.cssSelector(cssSelectorEle));
				WebElement buttonElement = cssSelectorEle;
				try {
					if (!dropdown) {

						// hard code for special case for arlo
						if (splitInputParameter.length == 1
								&& splitInputParameter[0].equalsIgnoreCase("Viewing plan")) {
							cssSelectorEle = driver.findElement(
									By.xpath("//*[text()='" + splitInputParameter[0] + "']/following::a[1]"));
						}
						if (splitInputParameter.length > 1
								&& splitInputParameter[1].equalsIgnoreCase("Performance Goals")) {
							cssSelectorEle = driver.findElement(
									By.xpath("//*[text()='" + splitInputParameter[1] + "']/following::a[1]"));
						}
						highlightElement(driver, fetchMetadataVO, cssSelectorEle, fetchConfigVO);
						if (findInToolTip)
							takeScreenshot(driver, fetchMetadataVO, customerDetails);
						else
							xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						// takeScreenshot(driver, fetchMetadataVO, customerDetails);
					}
					cssSelectorEle.click();
					return;
				} catch (Exception e) {
					if (!dropdown) {
						if (splitInputParameter.length == 1
								&& splitInputParameter[0].equalsIgnoreCase("Viewing plan")) {
							cssSelectorEle = driver.findElement(
									By.xpath("//*[text()='" + splitInputParameter[0] + "']/following::a[1]"));
						}
						if (splitInputParameter.length > 1
								&& splitInputParameter[1].equalsIgnoreCase("Performance Goals")) {
							cssSelectorEle = driver.findElement(
									By.xpath("//*[text()='" + splitInputParameter[1] + "']/following::a[1]"));
						}
						if (findInToolTip)
							takeScreenshot(driver, fetchMetadataVO, customerDetails);
						else
							xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					}
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", cssSelectorEle);
					return;
				}
			}
		} catch (Exception e) {

			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private WebElement findLinkElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver, FetchConfigVO fetchConfigVO) {
		try {
			Element parent = ele.parent();
			ELements seconElement = findSecondELement(fetchMetadataVO, customerDetails, parent, copyOfRange[0], driver,
					ele, fetchConfigVO);
			if (copyOfRange.length > 1) {
				return findLinkElement(fetchMetadataVO, customerDetails, seconElement.getElement(),
						Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, fetchConfigVO);
			} else {
				WebElement Selelement = seconElement.getWebElement();
				return Selelement;
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private ELements findSecondELement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element parent, String string, WebDriver driver, Element ele2, FetchConfigVO fetchConfigVO) {
		try {
			ELements elements = new ELements();
			Element SpecialCaseParent = parent;
			Elements popupElements = doc.select("*[data-afr-popupid]");
			boolean getElementInPopup = false;
			boolean havingSinglequote = false;
			boolean havingBraces = false;
			String selector = "";
			String selector1 = "";
			if (string.contains("'")) {
				havingSinglequote = true;
				String[] splitItems = string.split("'");
				for (String item : splitItems) {
					selector += ":containsOwn(" + item + ")";
				}
				// System.out.println(doc.select(string));
			}
			Pattern pattern1 = Pattern.compile(".*[\\(\\)].*");
			Matcher matcher1 = pattern1.matcher(string);
			if (matcher1.matches()) {
				havingBraces = true;
				String[] elementsb = string.split("\\s*[\\(\\)]\\s*");
				for (String item : elementsb) {
					if (item.contains("'")) {
						String[] splitItems = item.split("'");
						for (String it : splitItems) {
							selector1 += ":containsOwn(" + it + ")";
						}
						// System.out.println(doc.select(string));
					} else {
						selector1 += ":containsOwn(" + item + ")";
					}
				}
				// System.out.println(doc.select(string));
			}
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
			Matcher matcher = pattern.matcher(string);
			if (popupElements.size() != 0) {
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element SpecialCaseParent1 = parent;
					Element parent1 = parent;
					while (parent1 != null && parent1 != popupElements.get(i)) {
						boolean gotElement = false;
						if (havingBraces && !parent1.select(selector1).isEmpty()) {
							Elements eles = parent1.select(selector1);
							if (eles.size() == 1 && eles.get(0) == ele2) {
								parent1 = parent1.parent();
								continue;
							}

							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								if (elem == ele2) {
									continue;
								}
								try {
									WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle = driver
													.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
															+ "']/child::*[@class='" + elem.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle.isEnabled() || !selEle.isDisplayed()) {
									continue;
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;

								}
							}

						} else if (havingSinglequote && !parent1.select(selector).isEmpty()) {
							Elements eles = parent1.select(selector);
							if (eles.size() == 1 && eles.get(0) == ele2) {
								parent1 = parent1.parent();
								continue;
							}

							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								if (elem == ele2) {
									continue;
								}
								try {
									WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle = driver
													.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
															+ "']/child::*[@class='" + elem.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle.isEnabled() || !selEle.isDisplayed()) {
									continue;
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;

								}
							}

						} else if (!havingSinglequote && matcher.find()
								&& !parent1.select(":matchesOwn(" + string + ")").isEmpty()) {

							Elements eles = parent1.select(":matchesOwn(" + string + ")");
							if (eles.size() == 1 && eles.get(0) == ele2) {
								parent1 = parent1.parent();
								continue;
							}

							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								if (elem == ele2) {
									continue;
								}
								try {
									WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle = driver
													.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
															+ "']/child::*[@class='" + elem.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle.isEnabled() || !selEle.isDisplayed()) {
									continue;
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;

								}
							}

						} else if (!parent1.select(":matchesOwn(^" + string + "$)").isEmpty()) {
							Elements eles = parent1.select(":matchesOwn(^" + string + "$)");
							if (eles.size() == 1 && eles.get(0) == ele2) {
								parent1 = parent1.parent();
								continue;
							}

							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								if (elem == ele2) {
									continue;
								}
								try {
									WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									try {
										selEle = driver.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
														: CSS2XPath.css2xpath(elem.cssSelector(), true)));
									} catch (Exception exe) {
										try {
											selEle = driver
													.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
															+ "']/child::*[@class='" + elem.attr("class") + "']"));
										} catch (Exception exep) {
											continue;
										}
										// continue;
									}
								}
								if (!selEle.isEnabled() || !selEle.isDisplayed()) {
									continue;
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;

								}
							}
						} else if (!parent1.select("[alt='" + string + "']").isEmpty()) {
							Elements eles = parent1.select("[alt='" + string + "']");
							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								try {
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;

								}
							}
						} else if (!parent1.select("[title='" + string + "']:not([type=text])").isEmpty()) {
							Elements eles = parent1.select("[title='" + string + "']");
							if (eles.size() == 0) {
								throw new NullPointerException();

							}
							for (Element elem : eles) {
								WebElement selEle = null;
								try {
									selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
								} catch (Exception ex) {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								}
								if (selEle.isDisplayed() && selEle.isEnabled()) {
									getElementInPopup = true;
									gotElement = true;
									elements.setWebElement(selEle);
									elements.setElement(elem);
									return elements;
								}
							}
						}
						if (!gotElement) {
							parent1 = parent1.parent();
						}
					}
					if (getElementInPopup == false) {
						while (SpecialCaseParent1 != null && SpecialCaseParent1 != popupElements.get(i)) {
							for (int j = 0; j < string.length(); j++) {
								Elements eachCharacter = SpecialCaseParent
										.select(":matchesOwn(^" + string.charAt(j) + "$)");
								if (!eachCharacter.isEmpty()) {
									for (Element ele : eachCharacter) {
										WebElement SelEle = driver.findElement(By.cssSelector(ele.cssSelector()));
										if (!SelEle.isEnabled() || !SelEle.isDisplayed()) {
											continue;
										}
										if (ele.text().equals(string)) {
											elements.setWebElement(SelEle);
											elements.setElement(ele);
											return elements;
										}
										Element parent12 = ele.parent();
										if (parent12.text().equals(string)) {
											getElementInPopup = true;
											elements.setWebElement(SelEle);
											elements.setElement(ele);
											return elements;
										}
									}

									break;

								}
							}
							SpecialCaseParent = SpecialCaseParent.parent();
						}
					}

				}
			}
			if (!getElementInPopup) {
				while (parent != null) {
					boolean gotElement = false;
					if (havingSinglequote && !parent.select(selector).isEmpty()) {
						Elements eles = parent.select(selector);
						if (eles.size() == 1 && eles.get(0) == ele2) {
							parent = parent.parent();
							continue;
						}

						if (eles.size() == 0) {
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							if (elem == ele2) {
								continue;
							}
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								try {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								} catch (Exception exe) {
									try {
										selEle = driver
												.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
														+ "']/child::*[@class='" + elem.attr("class") + "']"));
									} catch (Exception exep) {
										continue;
									}
									// continue;
								}
							}
							if (!selEle.isEnabled() || !selEle.isDisplayed()) {
								continue;
							}
							if (selEle.isDisplayed() && selEle.isEnabled()) {
								getElementInPopup = true;
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;

							}
						}

					} else if (!havingSinglequote && matcher.find()
							&& !parent.select(":matchesOwn(" + string + ")").isEmpty()) {

						Elements eles = parent.select(":matchesOwn(" + string + ")");
						if (eles.size() == 1 && eles.get(0) == ele2) {
							parent = parent.parent();
							continue;
						}

						if (eles.size() == 0) {
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							if (elem == ele2) {
								continue;
							}
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								try {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								} catch (Exception exe) {
									try {
										selEle = driver
												.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
														+ "']/child::*[@class='" + elem.attr("class") + "']"));
									} catch (Exception exep) {
										continue;
									}
									// continue;
								}
							}
							if (!selEle.isEnabled() || !selEle.isDisplayed()) {
								continue;
							}
							if (selEle.isDisplayed() && selEle.isEnabled()) {
								getElementInPopup = true;
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;

							}
						}

					}

					else if (!parent.select(":matchesOwn(^" + string + "$)").isEmpty()) {
						Elements eles = parent.select(":matchesOwn(^" + string + "$)");
						if (eles.size() == 1 && eles.get(0) == ele2) {
							parent = parent.parent();
							continue;
						}

						if (eles.size() == 0) {
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							if (elem == ele2) {
								continue;
							}
							try {
								WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								try {
									selEle = driver.findElement(
											By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
													: CSS2XPath.css2xpath(elem.cssSelector(), true)));
								} catch (Exception exe) {
									try {
										selEle = driver
												.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
														+ "']/child::*[@class='" + elem.attr("class") + "']"));
									} catch (Exception exep) {
										continue;
									}
									// continue;
								}
							}
							if (!selEle.isEnabled() || !selEle.isDisplayed()) {
								continue;
							}
							if (selEle.isDisplayed() && selEle.isEnabled()) {
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;

							}
						}
					} else if (!parent.select("[alt='" + string + "']").isEmpty()) {
						Elements eles = parent.select("[alt='" + string + "']");
						if (eles.size() == 0) {
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							try {
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							}
							if (selEle.isDisplayed() && selEle.isEnabled()) {
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;

							}
						}
					} else if (!parent.select("[title='" + string + "']:not([type=text])").isEmpty()) {
						Elements eles = parent.select("[title='" + string + "']");
						if (eles.size() == 0) {
							throw new NullPointerException();

						}
						for (Element elem : eles) {
							WebElement selEle = null;
							try {
								selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
							} catch (Exception ex) {
								selEle = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
												: CSS2XPath.css2xpath(elem.cssSelector(), true)));
							}
							if (selEle.isDisplayed() && selEle.isEnabled()) {
								gotElement = true;
								elements.setWebElement(selEle);
								elements.setElement(elem);
								return elements;
							}
						}
					}
					if (!gotElement) {
						parent = parent.parent();
					}
				}
			}
			if (parent == null) {
				while (SpecialCaseParent != null) {
					for (int i = 0; i < string.length(); i++) {
						Elements eachCharacter = SpecialCaseParent.select(":matchesOwn(^" + string.charAt(i) + "$)");
						if (!eachCharacter.isEmpty()) {
							for (Element ele : eachCharacter) {
								WebElement SelEle = driver.findElement(By.cssSelector(ele.cssSelector()));
								if (!SelEle.isEnabled() || !SelEle.isDisplayed()) {
									continue;
								}
								if (ele.text().equals(string)) {
									elements.setWebElement(SelEle);
									elements.setElement(ele);
									return elements;
								}
								Element parent1 = ele.parent();
								if (parent1.text().equals(string)) {
									elements.setWebElement(SelEle);
									elements.setElement(ele);
									return elements;
								}
							}

							break;

						}
					}
					SpecialCaseParent = SpecialCaseParent.parent();
				}
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// method for click Link
	private void clickImage(ScriptDetailsDto fetchMetadataVO, WebDriver driver, CustomerProjectDto customerDetails,
			FetchConfigVO fetchConfigVO)
			throws Exception {
		String input[] = fetchMetadataVO.getInputParameter().split(">");
		String param1 = input[0];
		if (param1.equalsIgnoreCase("Export to Excel")) {
			try {
				logger.info("Dynamic Excel download");
				Thread.sleep(3000);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@title=\"Export to Excel\"]")));
				WebElement waittext = driver.findElement(By.xpath("//div[@title=\"Export to Excel\"]"));
				Actions actions = new Actions(driver);
				actions.moveToElement(waittext).build().perform();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", waittext);
				screenshot(driver, fetchMetadataVO, customerDetails);
				Thread.sleep(10000);
				driver.get("chrome://downloads/");
				// ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
				// driver.switchTo().window(tabs.get(1)).get("chrome://downloads");
				// Download Window Open
				// Thread.sleep(3000);
				String fileName = (String) js.executeScript(
						"return document.querySelector('downloads-manager').shadowRoot.querySelector('#downloadsList downloads-item').shadowRoot.querySelector('div#content #file-link').text");
				driver.navigate().back();
				// driver.close();
				// driver.switchTo().window(tabs.get(0));
				File file = new File(fetchConfigVO.getDOWNLOD_FILE_PATH() + fileName);
				File temp = new File(
						fetchConfigVO.getDOWNLOD_FILE_PATH() + fetchMetadataVO.getTestSetLineId() + "_" + "1" + ".xls");
				File rename = null;
				if (!temp.exists()) {
					String name = fetchMetadataVO.getTestSetLineId() + "_" + "1" + ".xls";
					rename = new File(fetchConfigVO.getDOWNLOD_FILE_PATH() + name);
					boolean flag = file.renameTo(rename);
				} else {
					String name1 = fetchMetadataVO.getTestSetLineId() + "_" + "2" + ".xls";
					rename = new File(fetchConfigVO.getDOWNLOD_FILE_PATH() + name1);
					file.renameTo(rename);

				}
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.info("Sucessfully Clicked clickImage " + scripNumber);
				return;
			} catch (Exception e) {
				String scripNumber = fetchMetadataVO.getScriptNumber();
				logger.error("Failed during clickImag " + scripNumber);
				logger.error(e.getMessage());
			}
		}
		try {
			updateDOM(driver);
			Elements elements = GettingAllElements(fetchMetadataVO.getAction());
			String[] splitInputParameter = null;
			if (fetchMetadataVO.getAction().equals("openTask")) {
				splitInputParameter = new String[] { "Tasks" };
			} else {
				splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			}
			String cssSelectorEle = "";
			if (splitInputParameter.length == 1) {
				Elements imagElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						imagElements = popupDIv.select("*[title='" + splitInputParameter[0] + "']");
						if (imagElements.size() == 0) {
							String regex = splitInputParameter[0] + "\\s?\\((.+)\\)";
							imagElements = doc.select("*[title~=^" + regex + "$]");

						}
						if (imagElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					imagElements = doc.select("*[title='" + splitInputParameter[0] + "']");
					if (imagElements.size() == 0) {
						String regex = splitInputParameter[0] + "\\s?\\((.+)\\)";
						imagElements = doc.select("*[title~=^" + regex + "$]");

					}
				}
				if (imagElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element element : imagElements) {
					WebElement imgElement = null;
					try {
						imgElement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						imgElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!imgElement.isEnabled() || !imgElement.isDisplayed()) {
						continue;
					} else {
						try {
							highlightElement(driver, fetchMetadataVO, imgElement, fetchConfigVO);
							xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							JavascriptExecutor js = (JavascriptExecutor) driver;
							js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
									imgElement);

							imgElement.click();
						} catch (Exception e) {
							xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", imgElement);
						}
						break;
					}
				}
			} else {
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						headerElements = popupDIv.select("*[title='" + splitInputParameter[0] + "']");
						if (headerElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					headerElements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = doc.select("*[title='" + splitInputParameter[0] + "']");

					}
				}
				if (headerElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element ele : headerElements) {
					WebElement imgElement = null;
					try {
						imgElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						imgElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					}
					if (!imgElement.isEnabled() || !imgElement.isDisplayed()) {
						continue;
					}
					WebElement selElement = findImageElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						// highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
						xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						selElement.click();
						break;
					}
				}

			}
			if (cssSelectorEle != "") {
				WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
				buttonElement.click();
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private WebElement findImageElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0]))
						.isEmpty()) {
					Elements secondElements = parent
							.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0]));
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						continue;
					}
					if (secondElements.size() == 0) {
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findImageElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								return Selelement;
							}
						}
					}

				}
				parent = parent.parent();
			}
			if (parent == null) {
				parent = ele.parent();
				while (parent != null) {
					if (!parent.select(String.format("img[alt='%s'], svg[alt='%s']", copyOfRange[0], copyOfRange[0]))
							.isEmpty()) {
						Elements secondElements = parent
								.select(String.format("img[alt='%s'], svg[alt='%s']", copyOfRange[0], copyOfRange[0]));
						if (secondElements.size() == 1 && secondElements.get(0) == ele) {
							continue;
						}
						if (secondElements.size() == 0) {
							throw new NullPointerException();

						}
						for (Element element : secondElements) {
							if (element == ele) {
								continue;
							}
							if (copyOfRange.length > 1) {
								WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
									if (element == secondElements.get(secondElements.size() - 1)) {
										break;
									}
									continue;
								}
								return findImageElement(fetchMetadataVO, customerDetails, element,
										Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
							} else {
								WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
									if (element == secondElements.get(secondElements.size() - 1)) {
										break;
									}
									continue;
								} else {
									return Selelement;
								}
							}
						}

					}
					parent = parent.parent();
				}
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// Method for click button
	// Method for click button
	private void tableRowSelect(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			WebElement cssSelectorEle = null;
			if (splitInputParameter.length == 1) {
				boolean gettingEleWoutSummary = false;
				Elements elements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						elements = popupDIv.select(String.format("table[summary='%s']", splitInputParameter[0]));
						if (elements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					elements = doc.select(String.format("table[summary='%s']", splitInputParameter[0]));
				}

				if (elements.size() == 0) {
					if (doc.select("*[data-afr-popupid]").size() > 0) {
						Elements popupElements = doc.select("*[data-afr-popupid]");
						for (int i = popupElements.size() - 1; i >= 0; i--) {
							Element popupDIv = popupElements.get(i);
							if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
								for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
									WebElement Selelement = null;
									try {
										Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
									} catch (Exception e) {
										Selelement = driver
												.findElement(
														By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
																: CSS2XPath.css2xpath(ele.cssSelector(), true)));

									}
									if (!Selelement.isEnabled() || !Selelement.isDisplayed()
											|| ele.tagName().equalsIgnoreCase("option")) {
										continue;
									} else {
										xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
												customerDetails);
										throw new PopupException(400, "Error popup message");
									}
								}

							}
							elements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
							if (elements.size() != 0) {
								toolTip = true;
								gettingEleWoutSummary = true;
								break;
							}
						}
					}
					if (elements.size() == 0) {
						elements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						gettingEleWoutSummary = true;
					}

				}
				if (elements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();
				}
				for (Element element : elements) {
					WebElement tableElement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!tableElement.isEnabled() || !tableElement.isDisplayed()) {
						continue;
					} else {
						if (gettingEleWoutSummary) {
							cssSelectorEle = tableElement.findElement(By.xpath("./following::table"));
						}
						cssSelectorEle = tableElement;
					}
				}

			} else {
				// Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] +
				// "$):not(label,button,a)")
				// .last();
				// Element parent = headerEle.parent();
				// while (parent != null
				// && parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty())
				// {
				// parent = parent.parent();
				// }
				// Element clickbutton = parent.select("*:matchesOwn(^" + splitInputParameter[1]
				// + "$)").first();
				// cssSelectorEle = clickbutton.cssSelector();
				Elements headerElements = null;
				boolean toolTip = false;
				if (doc.select("*[data-afr-popupid]").size() > 0) {
					Elements popupElements = doc.select("*[data-afr-popupid]");
					for (int i = popupElements.size() - 1; i >= 0; i--) {
						Element popupDIv = popupElements.get(i);
						if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
							for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
								WebElement Selelement = null;
								try {
									Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
								} catch (Exception e) {
									Selelement = driver
											.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
													: CSS2XPath.css2xpath(ele.cssSelector(), true)));

								}
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()
										|| ele.tagName().equalsIgnoreCase("option")) {
									continue;
								} else {
									xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
									throw new PopupException(400, "Error popup message");
								}
							}

						}
						headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
						if (headerElements.size() != 0) {
							toolTip = true;
							break;
						}
					}
				}
				if (toolTip == false) {
					headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				}
				if (headerElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();

				}
				for (Element ele : headerElements) {
					WebElement tableElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!tableElement.isEnabled() || !tableElement.isDisplayed()) {
						continue;
					}
					WebElement selElement = findtableRowSelectElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						cssSelectorEle = selElement;
						break;
						// selElement.click();
					}
				}

			}
			if (cssSelectorEle != null) {
				try {
					WebElement cssSelectorEle1 = cssSelectorEle.findElement(By.tagName("td"));
					cssSelectorEle1.click();
				} catch (Exception exe) {
					cssSelectorEle.click();
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private WebElement findtableRowSelectElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			Element parent = ele.parent();
			Elements secondElements = null;
			while (parent != null) {
				if (!parent.select(String.format("table[summary='%s']", copyOfRange[0])).isEmpty()) {
					secondElements = parent.select(String.format("table[summary='%s']", copyOfRange[0]));
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							return findtableRowSelectElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								continue;
							} else {
								return Selelement;
							}
						}
					}
				}
				parent = parent.parent();
			}
			if (parent == null) {
				parent = ele.parent();
				while (parent != null) {
					if (!parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
						Elements eles = parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)");
						if (eles.size() == 1 && eles.get(0) == ele) {
							parent = parent.parent();
							continue;
						}
						secondElements = parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)");
						for (Element element : secondElements) {
							if (element == ele) {
								continue;
							}
							if (copyOfRange.length > 1) {
								WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
									continue;
								} else {
									return findtableRowSelectElement(fetchMetadataVO, customerDetails, element,
											Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
								}

							} else {
								WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
								if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
									continue;
								} else {
									List<WebElement> selElems = Selelement.findElements(By.xpath("./following::table"));
									for (WebElement selElem : selElems) {
										if (!selElem.isEnabled() || !selElem.isDisplayed()) {
											continue;
										}

										return selElem;
									}
								}
							}
						}
					}
					parent = parent.parent();
				}
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// private void clickButton(ScriptDetailsDto fetchMetadataVO, WebDriver driver,
	// CustomerProjectDto customerDetails)
	// throws Exception {
	// try {
	// Elements elements = GettingAllElements(fetchMetadataVO.getAction());
	// if (elements.size() == 0) {
	// throw new NullPointerException();
	//
	// }
	// String[] splitInputParameter =
	// fetchMetadataVO.getInputParameter().split(">");
	// String cssSelectorEle = "";
	// if (splitInputParameter.length == 1) {
	// for (Element element : elements) {
	// if
	// ((element.text().trim().equalsIgnoreCase(fetchMetadataVO.getInputParameter())
	// || element.attr("alt").trim().equals(fetchMetadataVO.getInputParameter())
	// || element.attr("value").equals(fetchMetadataVO.getInputParameter()))) {
	// cssSelectorEle = element.cssSelector();
	// break;
	//
	// }
	// }
	// } else {
	// Elements headerElements = null;
	// headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] +
	// "$)");
	// if (headerElements.size() == 0) {
	// throw new NullPointerException();
	//
	// }
	// for (Element ele : headerElements) {
	// WebElement selElement = findButtonElement(fetchMetadataVO, customerDetails,
	// ele,
	// Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length),
	// driver);
	// if (selElement == null) {
	// continue;
	// } else {
	// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
	// customerDetails);
	// selElement.click();
	// }
	// }
	//
	// }
	// if (cssSelectorEle != "") {
	// WebElement buttonElement =
	// driver.findElement(By.cssSelector(cssSelectorEle));
	// xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO,
	// customerDetails);
	// buttonElement.click();
	// }
	// } catch (Exception e) {
	// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
	// customerDetails);
	// throw e;
	// }
	// }

	// private WebElement findButtonElement(ScriptDetailsDto fetchMetadataVO,
	// CustomerProjectDto customerDetails,
	// Element ele, String[] copyOfRange, WebDriver driver) {
	// try {
	// Element parent = ele.parent();
	// while (parent != null && (parent.select(":matchesOwn(^" + copyOfRange[0] +
	// "$)").isEmpty()
	// || parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()
	// || parent.select("[title='" + copyOfRange[0] + "']").isEmpty())) {
	// parent = parent.parent();
	// }
	// if (parent != null) {
	// Elements secondElements = null;
	// if (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
	// secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
	// }
	// if (parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()) {
	// secondElements = parent.select("[alt='" + copyOfRange[0] + "']");
	// }
	// if (parent.select("[title='" + copyOfRange[0] + "']").isEmpty()) {
	// secondElements = parent.select("[title='" + copyOfRange[0] + "']");
	// }
	// if (secondElements.size() == 0) {
	// throw new NullPointerException();
	//
	// }
	// for (Element element : secondElements) {
	// if (copyOfRange.length > 1) {
	// findButtonElement(fetchMetadataVO, customerDetails, element,
	// Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
	// } else {
	// WebElement Selelement =
	// driver.findElement(By.cssSelector(element.cssSelector()));
	// if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
	// continue;
	// } else {
	// return Selelement;
	// }
	// }
	// }
	// } else {
	// return null;
	// }
	//
	// return null;
	// } catch (Exception e) {
	// //xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
	// customerDetails);
	// throw e;
	// }
	// }

	// Method for clickCheckBox
	// Method for clickCheckBox
	private void clickCheckBox(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver,
			FetchConfigVO fetchConfigVO) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {

						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() == 0 && matcher.find()
							&& !popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}

					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
				if (headerElements.size() == 0 && matcher.find()
						&& !doc.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {
				WebElement Selelement = null;

				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
					Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
									: CSS2XPath.css2xpath(ele.cssSelector(), true))));
					Selelement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));

				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				}
				WebElement selElement = findCheckBoxElement(fetchMetadataVO, customerDetails, driver, ele,
						fetchMetadataVO.getInputValue(), fetchConfigVO);
				if (selElement == null) {
					if (ele == headerElements.get(headerElements.size() - 1)) {
						logger.error(
								"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
						throw new NullPointerException();
					}
					continue;
				} else {
					// selElement.clear();
					// previousEle = selElement;
					// JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					// jsExecutor.executeScript("arguments[0].click();", selElement);
					// String script = "arguments[0].value = arguments[1]";
					// jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					// jsExecutor.executeScript("arguments[0].click();", selElement);
					// break;
					try {
						selElement.click();
					} catch (Exception ex) {
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						jsExecutor.executeScript("arguments[0].click();", selElement);
					}
					break;
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findCheckBoxElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, Element ele, String inputValue, FetchConfigVO fetchConfigVO) {
		Element parent = ele.parent();
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

		Matcher matcher = pattern.matcher(inputValue);
		while (parent != null) {
			if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {

				Elements secondElements = parent.select(":matchesOwn(^" + inputValue + "$)");
				if (secondElements.size() == 1 && secondElements.get(0) == ele) {
					parent = parent.parent();
					continue;
				}
				if (secondElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();
				}
				for (Element element : secondElements) {
					if (element == ele) {
						continue;
					}
					WebElement Selelement = null;
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						wait.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true))));
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));

					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						if (element == secondElements.get(secondElements.size() - 1)) {
							parent = parent.parent();
							break;
						}
						continue;
					} else {
						Element parent1 = element.parent();
						Elements checkBoxelements = null;
						while (parent1 != null) {
							if (!parent1.select("input[type='checkbox']").isEmpty()) {
								checkBoxelements = parent1.select("input[type='checkbox']");
								for (Element elem : checkBoxelements) {
									wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
									wait.until(ExpectedConditions
											.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
									WebElement selelem = driver.findElement(By.cssSelector(elem.cssSelector()));
									// if (!selelem.isEnabled() || !selelem.isDisplayed()) {
									// continue;
									// }
									return selelem;
								}

							}
							parent1 = parent1.parent();
						}
						if (parent1 == null) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						break;
					}

				}
				break;

			}

			if (matcher.find() && !parent.select(":matchesOwn(" + inputValue + ")").isEmpty()) {

				Elements secondElements = parent.select(":matchesOwn(" + inputValue + ")");
				if (secondElements.size() == 1 && secondElements.get(0) == ele) {
					parent = parent.parent();
					continue;
				}
				if (secondElements.size() == 0) {
					logger.error(
							"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
					throw new NullPointerException();
				}
				for (Element element : secondElements) {
					if (element == ele) {
						continue;
					}
					WebElement Selelement = null;
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						wait.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true))));
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));

					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						if (element == secondElements.get(secondElements.size() - 1)) {
							parent = parent.parent();
							break;
						}
						continue;
					} else {
						Element parent1 = element.parent();
						Elements checkBoxelements = null;
						while (parent1 != null) {
							if (!parent1.select("input[type='checkbox']").isEmpty()) {
								checkBoxelements = parent1.select("input[type='checkbox']");
								break;
							}
						}
						if (parent1 == null) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						if (parent1 != null) {
							for (Element elem : checkBoxelements) {
								wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
								wait.until(ExpectedConditions
										.presenceOfElementLocated(By.cssSelector(elem.cssSelector())));
								WebElement selelem = driver.findElement(By.cssSelector(elem.cssSelector()));
								if (!selelem.isEnabled() || !selelem.isDisplayed()) {
									continue;
								}
								return selelem;
							}
						}
						break;
					}

				}
				break;

			}

			parent = parent.parent();
		}
		if (parent == null) {
			logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
			throw new NullPointerException();
		}
		return null;

	}

	private void clickRadioButton(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, FetchConfigVO fetchConfigVO) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0) {
						headerElements = popupDIv.select("*[data-value=" + splitInputParameter[0] + "]");
					}
					if (headerElements.size() == 0 && matcher.find()
							&& !popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0) {
					headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
				}
				if (headerElements.size() == 0 && matcher.find()
						&& !doc.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();
			}
			for (Element ele : headerElements) {

				WebElement Selelement = null;
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				try {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
					Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					wait.until(ExpectedConditions
							.presenceOfElementLocated(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
									: CSS2XPath.css2xpath(ele.cssSelector(), true))));
					Selelement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));

				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				}
				WebElement selElement = findRadioButtonElement(fetchMetadataVO, customerDetails, driver, ele,
						fetchMetadataVO.getInputValue(), fetchConfigVO);
				if (selElement == null) {
					if (ele == headerElements.get(headerElements.size() - 1)) {
						logger.error(
								"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
						throw new NullPointerException();
					}
					continue;
				} else {
					// selElement.clear();
					// previousEle = selElement;
					// JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					// jsExecutor.executeScript("arguments[0].click();", selElement);
					// String script = "arguments[0].value = arguments[1]";
					// jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					// jsExecutor.executeScript("arguments[0].click();", selElement);
					// break;
					try {
						selElement.click();
					} catch (Exception ex) {
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						jsExecutor.executeScript("arguments[0].click();", selElement);
					}
					break;
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findRadioButtonElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver, Element ele, String inputValue, FetchConfigVO fetchConfigVO) {
		Element parent = ele.parent();
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

		Matcher matcher = pattern.matcher(inputValue);
		while (parent != null) {
			if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
				Elements secondElements = parent.select(":matchesOwn(^" + inputValue + "$)");
				if (secondElements.size() == 1 && secondElements.get(0) == ele) {
					parent = parent.parent();
					continue;
				}
				if (secondElements.size() == 0) {
					throw new NullPointerException();
				}
				for (Element element : secondElements) {
					if (element == ele) {
						continue;
					}
					WebElement Selelement = null;
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						wait.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true))));
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));

					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						if (element == secondElements.get(secondElements.size() - 1)) {
							parent = parent.parent();
							break;
						}
						continue;
					} else {
						Selelement = Selelement.findElement(By.xpath("preceding::input[@type='radio'][1]"));
						return Selelement;
					}

				}

			} else if (matcher.find() && !parent.select(":matchesOwn(" + inputValue + ")").isEmpty()) {
				Elements secondElements = parent.select(":matchesOwn(" + inputValue + ")");
				if (secondElements.size() == 1 && secondElements.get(0) == ele) {
					parent = parent.parent();
					continue;
				}
				if (secondElements.size() == 0) {
					throw new NullPointerException();
				}
				for (Element element : secondElements) {
					if (element == ele) {
						continue;
					}
					WebElement Selelement = null;
					WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						wait.until(ExpectedConditions.presenceOfElementLocated(
								By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true))));
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));

					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						if (element == secondElements.get(secondElements.size() - 1)) {
							parent = parent.parent();
							break;
						}
						continue;
					} else {
						Selelement = Selelement.findElement(By.xpath("preceding::input[1]"));
						return Selelement;
					}

				}

			} else {
				parent = parent.parent();
			}
		}
		if (parent == null) {
			logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
			throw new NullPointerException();
		}
		return null;
	}

	// Method for SwitchToParentWindow
	private void SwitchToParentWindow(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			WebDriver driver) {
		try {
			String parentWindowHandle = driver.getWindowHandle();

			// Get the window handles of all open windows
			Set<String> allWindowHandles = driver.getWindowHandles();
			List<String> list = new ArrayList<>(allWindowHandles);
			// Switch to the child window
			for (String windowHandle : allWindowHandles) {
				if (list.get(list.size() - 2).equals(windowHandle)) {
					driver.switchTo().window(windowHandle);
					break;
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for SwitchToDefaultFrame
	private void SwitchToDefaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			throw e;
		}
	}

	// Method for SwitchToFrame
	private void SwitchToFrame(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
		// Elements elements = doc.select("iframe");
		try {
			Elements elements = GettingAllElements(fetchMetadataVO.getAction());
			String CssSelector = "";
			for (Element element : elements) {
				if (element.attr("id").equals(fetchMetadataVO.getInputParameter())) {
					CssSelector = element.cssSelector();
					break;

				} else if (element.attr("title").equals(fetchMetadataVO.getInputParameter())) {

				}
			}
			WebElement frameElement = driver.findElement(By.cssSelector(CssSelector));
			synchronized (frameElement) {
				while (frameElement == null) {
					try {
						frameElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			driver.switchTo().frame(frameElement);
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for textArea
	private void textArea(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = popupElements.get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;
							try {
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0 && matcher.find()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0 && matcher.find()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
			}
			if (headerElements.size() == 0) {
				throw new NullPointerException();

			}
			for (Element ele : headerElements) {
				if (splitInputParameter.length == 1) {

					WebElement selElement = null;
					try {
						selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						try {
							selElement = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true)));
						} catch (Exception ex) {
							selElement = driver.findElement(By.xpath("//*[text()='" + splitInputParameter[0] + "']"));
						}
					}
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						List<WebElement> list1 = new ArrayList<>();
						WebElement Selelement1 = null;
						boolean gotEle = false;
						Element parenttext = ele.parent();
						while (parenttext != null) {
							if (!parenttext.select("textarea").isEmpty()) {
								for (Element elem : parenttext.select("textarea")) {
									WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
									if (!sele.isEnabled() || !sele.isDisplayed()) {
										gotEle = false;
										continue;
									}
									gotEle = true;
									sele.clear();
									JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
									String script = "arguments[0].value = arguments[1]";
									jsExecutor.executeScript(script, sele, fetchMetadataVO.getInputValue());
									break;
								}

							}
							if (!gotEle && !parenttext.select("*[role=" + "textbox" + "]").isEmpty()) {
								for (Element elem : parenttext.select("*[role=" + "textbox" + "]")) {
									WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
									if (!sele.isEnabled() || !sele.isDisplayed()) {
										gotEle = false;
										continue;
									}
									Elements ptag = elem.select("p");
									for (Element PTag : ptag) {

										WebElement sele1 = driver.findElement(By.cssSelector(PTag.cssSelector()));
										if (!sele1.isEnabled() || !sele1.isDisplayed()) {
											gotEle = false;
											continue;
										}
										gotEle = true;
										sele.click();
										sele.sendKeys(fetchMetadataVO.getInputValue());
										break;

									}
									if (gotEle) {
										break;
									}
								}
								if (gotEle) {
									break;
								}

							}
							parenttext = parenttext.parent();
						}
						if (parenttext == null) {
							throw new NullPointerException();
						}
					}
				} else {
					WebElement selElement1 = null;
					try {
						selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					} catch (Exception e) {
						try {
							selElement1 = driver
									.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
											: CSS2XPath.css2xpath(ele.cssSelector(), true)));
						} catch (Exception ex) {
							selElement1 = driver.findElement(By.xpath("//*[text()='" + splitInputParameter[0] + "']"));
						}
					}
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					List<WebElement> selElement12 = findTheTextAreaElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver);
					if (selElement12.get(0) == null) {
						continue;
					} else {
						if (selElement12.get(1) != null) {
							selElement12.get(0).clear();
							JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
							String script = "arguments[0].value = arguments[1]";
							jsExecutor.executeScript(script, selElement12.get(0), fetchMetadataVO.getInputValue());
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private List<WebElement> findTheTextAreaElement(ScriptDetailsDto fetchMetadataVO,
			CustomerProjectDto customerDetails, Element ele, String[] copyOfRange, WebDriver driver) {
		try {
			List<WebElement> list1 = new ArrayList<>();
			Element parent = ele.parent();
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(copyOfRange[0]);
			while (parent != null) {
				if (!parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {

					Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (secondElements.size() == 0) {
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findTheTextAreaElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								WebElement Selelement1 = null;
								boolean gotEle = false;
								Element parenttext = element.parent();
								while (parenttext != null) {
									if (!parenttext.select("textarea").isEmpty()) {
										for (Element elem : parenttext.select("textarea")) {
											WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
											if (!sele.isEnabled() || !sele.isDisplayed()) {
												gotEle = false;
												continue;
											}
											gotEle = true;
											// list1.add(sele);
											// list1.add(sele);)

											list1.add(sele);
											list1.add(sele);
											return list1;
										}
										if (gotEle)
											break;

									}
									if (!gotEle && !parenttext.select("*[role=" + "textbox" + "]").isEmpty()) {
										for (Element elem : parenttext.select("*[role=" + "textbox" + "]")) {
											WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
											if (!sele.isEnabled() || !sele.isDisplayed()) {
												continue;
											}
											Elements ptag = elem.select("p");
											for (Element PTag : ptag) {
												WebElement sele1 = driver
														.findElement(By.cssSelector(PTag.cssSelector()));
												if (!sele1.isEnabled() || !sele1.isDisplayed()) {
													continue;
												}
												sele1.click();
												sele1.sendKeys(fetchMetadataVO.getInputValue());
												list1.add(sele);
												list1.add(null);
												return list1;
											}
											break;

										}
										break;

									}
									parenttext = parenttext.parent();
								}
								if (parenttext == null) {
									throw new NullPointerException();
								}
							}
						}
					}

				}

				if (matcher.find() && !parent.select(":matchesOwn(" + copyOfRange[0] + ")").isEmpty()) {

					Elements secondElements = parent.select(":matchesOwn(" + copyOfRange[0] + ")");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (secondElements.size() == 0) {
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							findTheTextAreaElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver);
						} else {
							WebElement Selelement1 = null;
							boolean gotEle = false;
							Element parenttext = element.parent();
							while (parenttext != null) {
								if (!parenttext.select("textarea").isEmpty()) {
									for (Element elem : parenttext.select("textarea")) {
										WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
										if (!sele.isEnabled() || !sele.isDisplayed()) {
											gotEle = false;
											continue;
										}
										gotEle = true;
										list1.add(sele);
										list1.add(sele);
										return list1;
									}

								}
								if (!gotEle && !parenttext.select("*[role=" + "textbox" + "]").isEmpty()) {
									for (Element elem : parenttext.select("*[role=" + "textbox" + "]")) {
										WebElement sele = driver.findElement(By.cssSelector(elem.cssSelector()));
										if (!sele.isEnabled() || !sele.isDisplayed()) {
											continue;
										}
										Elements ptag = elem.select("p");
										for (Element PTag : ptag) {
											WebElement sele1 = driver.findElement(By.cssSelector(PTag.cssSelector()));
											if (!sele1.isEnabled() || !sele1.isDisplayed()) {
												continue;
											}
											sele1.click();
											sele1.sendKeys(fetchMetadataVO.getInputValue());
											list1.add(sele);
											list1.add(null);
											return list1;
										}

									}

								}
								parenttext = parenttext.parent();
							}
							if (parenttext == null) {
								throw new NullPointerException();
							}
						}
					}

				}
				parent = parent.parent();
			}
			if (parent == null) {
				throw new NullPointerException();
			}
			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	// Method for Windowhandle
	private void windowhandle(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
		try {
			String parentWindowHandle = driver.getWindowHandle();
			Set<String> windowHandles = driver.getWindowHandles();

			for (String windowHandle : windowHandles) {
				if (!windowHandle.equals(parentWindowHandle)) {
					driver.switchTo().window(windowHandle);
					break;
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	// Method for clickExpandorcollapse

	private void clickExpandorcollapse(ScriptDetailsDto fetchMetadataVO, WebDriver driver,
			CustomerProjectDto customerDetails, FetchConfigVO fetchConfigVO) throws Exception {
		try {
			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");

			Matcher matcher = pattern.matcher(splitInputParameter[0]);
			Elements headerElements = null;
			boolean toolTip = false;
			if (doc.select("*[data-afr-popupid]").size() > 0) {
				Elements popupElements = doc.select("*[data-afr-popupid]");
				for (int i = popupElements.size() - 1; i >= 0; i--) {
					Element popupDIv = doc.select("*[data-afr-popupid]").get(i);
					if (!popupDIv.select("*:matchesOwn(^" + "Error" + "$)").isEmpty()) {
						for (Element ele : popupDIv.select("*:matchesOwn(^" + "Error" + "$)")) {
							WebElement Selelement = null;

							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());

							try {
								wait.until(
										ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
								Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
							} catch (Exception e) {
								wait.until(ExpectedConditions
										.presenceOfElementLocated(
												By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
														: CSS2XPath.css2xpath(ele.cssSelector(), true))));
								Selelement = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
												: CSS2XPath.css2xpath(ele.cssSelector(), true)));

							}
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()
									|| ele.tagName().equalsIgnoreCase("option")) {
								continue;
							} else {
								xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
								throw new PopupException(400, "Error popup message");
							}
						}

					}
					headerElements = popupDIv.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
					if (headerElements.size() == 0 && matcher.find()
							&& !popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
						headerElements = popupDIv.select("*:matchesOwn(" + splitInputParameter[0] + ")");
					}
					if (headerElements.size() != 0) {
						toolTip = true;
						break;
					}
				}
			}
			if (toolTip == false) {
				headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
				if (headerElements.size() == 0 && matcher.find()
						&& !doc.select("*:matchesOwn(" + splitInputParameter[0] + ")").isEmpty()) {
					headerElements = doc.select("*:matchesOwn(" + splitInputParameter[0] + ")");
				}
			}
			if (headerElements.size() == 0) {
				logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
				throw new NullPointerException();

			}

			for (Element ele : headerElements) {
				WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
				if (splitInputParameter.length == 1) {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
					WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement.isEnabled() || !selElement.isDisplayed()) {
						continue;
					} else {
						highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
						xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						selElement.click();
						break;
					}

				} else {
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(ele.cssSelector())));
					WebElement selElement1 = driver.findElement(By.cssSelector(ele.cssSelector()));
					if (!selElement1.isEnabled() || !selElement1.isDisplayed()) {
						continue;
					}
					WebElement selElement = findThecllopseExpandElement(fetchMetadataVO, customerDetails, ele,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), driver,
							fetchConfigVO);
					if (selElement == null) {
						if (ele == headerElements.get(headerElements.size() - 1)) {
							logger.error("Failed to get element in-" + fetchMetadataVO.getScriptNumber()
									+ "  in Dynamic Xpath");
							throw new NullPointerException();
						}
						continue;
					} else {
						// selElement.clear();
						highlightElement(driver, fetchMetadataVO, selElement, fetchConfigVO);
						xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
						selElement.click();
						break;
					}
				}
			}
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}

	}

	private WebElement findThecllopseExpandElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails,
			Element ele, String[] copyOfRange, WebDriver driver, FetchConfigVO fetchConfigVO) {
		try {
			Element parent = ele.parent();
			while (parent != null) {
				if (!parent.select("*[title=" + copyOfRange[0] + "]").isEmpty()) {

					Elements secondElements = parent.select("*[title=" + copyOfRange[0] + "]");
					if (secondElements.size() == 1 && secondElements.get(0) == ele) {
						parent = parent.parent();
						continue;
					}
					if (secondElements.size() == 0) {
						logger.error(
								"Failed to get element in-" + fetchMetadataVO.getScriptNumber() + "  in Dynamic Xpath");
						throw new NullPointerException();

					}
					for (Element element : secondElements) {
						if (element == ele) {
							continue;
						}
						if (copyOfRange.length > 1) {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							}
							return findThecllopseExpandElement(fetchMetadataVO, customerDetails, element,
									Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), driver, fetchConfigVO);
						} else {
							WebDriverWait wait = new WebDriverWait(driver, fetchConfigVO.getWait_time());
							wait.until(
									ExpectedConditions.presenceOfElementLocated(By.cssSelector(element.cssSelector())));
							WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
							if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
								if (element == secondElements.get(secondElements.size() - 1)) {
									break;
								}
								continue;
							} else {
								return Selelement;
							}
						}
					}

				}
				parent = parent.parent();
			}

			return null;
		} catch (Exception e) {
			// xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO,
			// customerDetails);
			throw e;
		}
	}

	private Elements GettingAllElements(String actionElements) {
		Elements elements;
		switch (actionElements) {
			case "clickTableLink":
			case "clickLink":
				elements = doc.select("a");
				break;
			case "clickButton":
				elements = doc.select("button, input[type=button],input[type=submit],a[role=button]");
				break;
			case "clickExpandorcollapse":
				elements = doc.select("a, button, input[type=button],input[type=submit]");
				break;
			case "Dropdown Values":
				elements = doc.select("select");
				break;
			case "openTask":
			case "clickImage":
				elements = doc.select("img, svg");
				break;
			case "":
				elements = doc.select("input");
				break;
			case "login":
				elements = doc.select("input");
				break;
			case "textArea":
				elements = doc.select("textarea");
				break;
			case "getAllElements":
				elements = doc.select("*");
				break;
			default:
				elements = doc.select("*");
				break;
		}
		return elements;
	}

	public void highlightElement(WebDriver driver, ScriptDetailsDto fetchMetadataVO, WebElement waittext,
			FetchConfigVO fetchConfigVO) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].style.border=\"6px solid yellow\"", waittext);
		} catch (Exception e) {
			logger.error("Failed during highlight Element " + fetchMetadataVO.getScriptNumber());
			e.printStackTrace();
		}
	}

	private static String getXPath(Element element) {
		StringBuilder xpath = new StringBuilder();
		while (element != null) {
			int index = getElementIndex(element);
			String tagName = element.tagName();
			xpath.insert(0, "/" + tagName + "[" + index + "]");
			element = element.parent();
			if (element.tagName().equals("body")) {
				xpath.insert(0, "/html/body");
				break;
			}
		}
		return xpath.toString();
	}

	private static int getElementIndex(Element element) {
		int index = 1;
		for (Element sibling : element.parent().children()) {
			if (sibling.tagName().equals(element.tagName())) {
				if (sibling == element) {
					return index;
				}
				index++;
			}
		}
		return index;
	}

}
