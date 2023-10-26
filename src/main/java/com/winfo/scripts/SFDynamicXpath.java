package com.winfo.scripts;

import java.io.IOException;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.winfo.vo.CustomerProjectDto;
import com.winfo.vo.ELements;
import com.winfo.vo.FetchConfigVO;
import com.winfo.vo.ScriptDetailsDto;

@Service
public class SFDynamicXpath {
	private Document doc;
	private String pageSource;
	private WebElement previousEle;
//	private JavascriptExecutor js;
	private String currentUrl;
	private String copyNumber;
	private String sendKeysValue;

	@Autowired
	private XpathPerformance xpathPerformance;
	
	public void dynamics(WebDriver driver, FetchConfigVO fetchConfigVO, ScriptDetailsDto fetchMetadataVO, String param,
			CustomerProjectDto customerDetails) throws Exception {
		if (fetchMetadataVO.getInputParameter() != null)
			fetchMetadataVO.setInputParameter(fetchMetadataVO.getInputParameter().replace("(*)", ""));
		if (fetchMetadataVO.getAction().equals("Navigate")) {
			try {
				clickOnNavbar(fetchMetadataVO, customerDetails, driver);
				Thread.sleep(5000);
				updateDOM(driver);
			} catch (Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("tab")) {
			try {
				previousEle.sendKeys(Keys.TAB);
			} catch (Exception e) {
				JavascriptExecutor jsTab = (JavascriptExecutor) driver;
				jsTab.executeScript(
						"document.activeElement.dispatchEvent(new KeyboardEvent('keydown',{'key':'Tab'}));");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("keyEnter")) {
			try {
			previousEle.sendKeys(Keys.ENTER);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("pageLoad")) {
			// previousEle.sendKeys(Keys.TAB);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("waitTillLoad")) {
			int i = Integer.parseInt(fetchMetadataVO.getInputValue());
			try {
				Thread.sleep(i * 1000);
				updateDOM(driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
			
		} else if (fetchMetadataVO.getAction().equals("Dropdown Values")) {
			try {
			dropDownValues(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("copynumber")) {
			try {
			String[] copyNumberInputParameter = fetchMetadataVO.getInputParameter().split(">");
			if (copyNumberInputParameter[0].equals("Confirmation")) {
				Element confirmationElement = doc
						.selectFirst(":containsOwn('" + copyNumberInputParameter[0] + "')");

				// Find the parent div element of the "Confirmation" text
				Element confirmationParent = confirmationElement.parent();
				while (confirmationParent != null
						&& confirmationParent.select(":contains('" + copyNumberInputParameter[1] + "')")
								.isEmpty()) {
					confirmationParent = confirmationParent.parent();
				}
				// Find the label element containing the "Process" text among its siblings
				Element labelElement = confirmationParent
						.selectFirst("label:contains('" + copyNumberInputParameter[1] + "')");
				Pattern pattern = Pattern.compile("\\d+");

				// Create a matcher with the input text
				Matcher matcher = pattern.matcher(labelElement.text());

				// Find and print all matching numbers
				while (matcher.find()) {
					String number = matcher.group();
					System.out.println(number);
					copyNumber = number;
				}
			}
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("copy")) {
			try {
			copy(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("windowhandle")) {
			try {
			String parentWindowHandle = driver.getWindowHandle();
			Set<String> windowHandles = driver.getWindowHandles();

			for (String windowHandle : windowHandles) {
				if (!windowHandle.equals(parentWindowHandle)) {
					driver.switchTo().window(windowHandle);
					CompletableFuture<Object> updateDo = this.updateDOM(driver);
					try {
						Object update = updateDo.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("switchToparent")) {
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
			updateDOM(driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("switchToMainparent")) {
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
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}

		} else if (fetchMetadataVO.getAction().equals("switchToFrame")) {
			try {
			SwitchToFrame(fetchMetadataVO, customerDetails, driver);
			updateDOM(driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
			
		} else if (fetchMetadataVO.getAction().equals("switchToDefaultFrame")) {
			try {
			driver.switchTo().defaultContent();
			updateDOM(driver);
		}catch(Exception e) {
			xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
			throw e;
		}
		} else if (fetchMetadataVO.getAction().equals("clickExpandorcollapse")) {
			try {
			clickExpandorcollapse(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("login")) {
			login(fetchMetadataVO, customerDetails, driver);
		} else if (fetchMetadataVO.getAction().equals("SendKeys")) {
			try {
			sendKeys(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickCheckbox")) {
			try {
			clickCheckbox(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickRadioButton")) {
			try {
			clickRadioButton(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("DatePicker")) {
			try {
			datePicker(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("textarea")) {
			try {
			textArea(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickButton")) {
			try {
			clickLink(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickLink")) {
			try {
			clickLink(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clickImage")) {
			try {
			clickImage(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("paste")) {
			try {
			paste(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("clearText")) {
			try {
			clearText(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("selectByText")) {
			try {
			selectDropdownValues(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("tableRowSelect")) {
			try {
			tableRowSelect(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		} else if (fetchMetadataVO.getAction().equals("selectAValue")) {
			try {
				Thread.sleep(3000);
			updateDOM(driver);
			
			selectAvalue(fetchMetadataVO, customerDetails, driver);
			}catch(Exception e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
		}

		if (fetchMetadataVO.getAction().equals("SendKeys") || fetchMetadataVO.getAction().equals("paste")
				|| fetchMetadataVO.getAction().equals("selectByText") || fetchMetadataVO.getAction().equals("login")) {

		} else {
			try {
				Thread.sleep(3000);
				updateDOM(driver);
			} catch (InterruptedException e) {
				xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
				throw e;
			}
			
		}
	// driver.quit();
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
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					// if (getElementtoCheck == "dataValue") {
					// selElement = driver.findElement(By.xpath("//*[@data-value='" + ele.text() +
					// "']"));
					// } else if (getElementtoCheck == "placeholder") {
					// selElement = driver
					// .findElement(By.xpath("//*[@placeholder='" + splitInputParameter[0] + "']"));
					// } else {
					// selElement = driver.findElement(By.xpath("//*[text()='" + ele.text() +
					// "']"));
					// }
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
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
				copyNumber = elementWithTextNextToGivenElement.getText();
				break;
			}
		} else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					// if (getElementtoCheck == "dataValue") {
					// selElement = driver.findElement(By.xpath("//*[@data-value='" + ele.text() +
					// "']"));
					// } else if (getElementtoCheck == "placeholder") {
					// selElement = driver
					// .findElement(By.xpath("//*[@placeholder='" + splitInputParameter[0] + "']"));
					// } else {
					// selElement = driver.findElement(By.xpath("//*[text()='" + ele.text() +
					// "']"));
					// }
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() && !selElement.isDisplayed()) {
				continue;
			}
			selElement = findTheCopyElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				copyNumber = selElement.getText();
				break;
			}
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findTheCopyElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findTheElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						Selelement = driver.findElement(By.xpath(getXPath(element)));
					}
				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
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
	} else {
		return null;
	}

	return null;
}

private String clickOnOKMethod(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, String replace) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	CompletableFuture<Object> updateDo = this.updateDOM(driver);
	try {
		Object update = updateDo.get();
	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	if (doc.select(("*[id=" + replace + "]")).size() != 0) {
		Element parentele = doc.select("*[id=" + replace + "]").first();
		return parentele.cssSelector();

	} else {
		return "";
	}
}

private String clickonDropDownText(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, String replace, String input) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	CompletableFuture<Object> updateDo = this.updateDOM(driver);
	try {
		Object update = updateDo.get();
	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	if (doc.select(("*[id=" + replace + "]")).size() != 0) {
		Element parentele = doc.select("*[id=" + replace + "]").first();
		Element tdEle = parentele.select("*:matchesOwn(^" + input + "$)").first();
		return tdEle.cssSelector();

	} else {
		return "";
	}
}

private String searchByInput(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, String inputValue, String replace) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	CompletableFuture<Object> updateDo = this.updateDOM(driver);
	try {
		Object update = updateDo.get();
	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(doc.select(("*[id=" + replace + "]")));
	if (doc.select(("*[id=" + replace + "]")).size() != 0) {

		return doc.select(("*[id=" + replace + "]")).first().cssSelector();
	} else {
		return "";
	}
}

private String clickOnSearch(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, String outPut) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	CompletableFuture<Object> updateDo = this.updateDOM(driver);
	try {
		Object update = updateDo.get();
	} catch (InterruptedException | ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// doc.select(("*[id=" + outPut + "]"));
	System.out.println(doc.select(("*[id=" + outPut + "]")));
	if (doc.select(("*[id=" + outPut + "]")).size() != 0) {

		return doc.select(("*[id=" + outPut + "]")).first().cssSelector();
	} else {
		return "";
	}
}

private List<String> dropDownXpath(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Document doc2) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
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
	String forAtrr = parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").attr("for")
			.replaceAll("content", "cntnrSpan");
	Element clickdropDown = parent.select(("*[id=" + forAtrr + "]")).first();
	List<String> storingIds = new ArrayList<String>();
	storingIds.add(clickdropDown.cssSelector());
	storingIds.add(forAtrr);

	return storingIds;
}

private void clickOnNavbar(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) throws InterruptedException, ExecutionException {
	try {
	String param1 = "Navigator";
	this.currentUrl = driver.getCurrentUrl();
	WebElement webEle = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
	webEle.click();

		Thread.sleep(5000);
		updateDOM(driver);
		WebElement showMore = driver.findElement(By.xpath("//*[text()='Show More']"));
		showMore.click();
		Thread.sleep(5000);
		updateDOM(driver);
	
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	String welcomeText = splitInputParameter[splitInputParameter.length - 1].trim();
	Element xpathNav = doc.select("*:contains(" + welcomeText + ")").last();
	WebElement element = driver.findElement(By.cssSelector(xpathNav.cssSelector()));
	xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
	element.click();
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

@Async
private CompletableFuture<Object> updateDOM(WebDriver driver) {
	JavascriptExecutor js = (JavascriptExecutor) driver;

	try {
		Object obj = js.executeScript("return document.readyState");
		if (obj!= null && !obj.toString().equals("complete")) {
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
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return CompletableFuture.completedFuture(js);
}

private String getXpath(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Document doc, boolean CheckAll) {
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
			case "sendKeys":
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

		if ((fetchMetadataVO.getAction().equals("clickButton") || fetchMetadataVO.getAction().equals("clickLink"))) {
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

			String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
			if (splitInputParameter.length == 1) {
				if (element.tagName() == "svg") {
					if (element.select("title").text().equals(fetchMetadataVO.getInputParameter()) || element.select("title")
							.text().replaceAll(String.valueOf((char) 160), " ").equals(fetchMetadataVO.getInputParameter()) ||
							element.parent().attr("title").equals(fetchMetadataVO.getInputParameter())
							|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
									.equals(fetchMetadataVO.getInputParameter())) {
						return element.cssSelector();
					}

				} else {
					if (element.attr("title").equals(fetchMetadataVO.getInputParameter()) || element.attr("title")
							.replaceAll(String.valueOf((char) 160), " ").equals(fetchMetadataVO.getInputParameter())
							|| element.parent().attr("title").equals(fetchMetadataVO.getInputParameter())
							|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
									.equals(fetchMetadataVO.getInputParameter())) {
						flag = true;
						return element.cssSelector();
					}
				}
			} else {

				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)")
						.last();
				Element parent = headerEle.parent();
				while (parent != null
						&& parent.select(String.format("img[title=%s]", splitInputParameter[1])).isEmpty()) {
					parent = parent.parent();
				}
				Element clickbutton = parent.select(String.format("img[title=%s]", splitInputParameter[1])).first();
				return clickbutton.cssSelector();

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
			while (parent != null
					&& parent.select("th:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
				parent = parent.parent();
			}
			String strele = "_afrFilter_FOpt1_afr__FOr1_afr_0_afr__FONSr2_afr_0_afr_MAnt2_afr_1_afr_pm1_afr_r1_afr_0_afr_ap1_afr_r12_afr_1_afr_at1_afr__ATp_afr_ta1_afr_c4::content";
			Element ele = doc.select("input[id=" + strele + "]").first();
			return ele.cssSelector();

		} else if (fetchMetadataVO.getAction().equals("textArea") || fetchMetadataVO.getAction().equals("selectvaluesTable")
				|| fetchMetadataVO.getAction().equals("sendKeys") || fetchMetadataVO.getAction().equals("selectByText") ||
				fetchMetadataVO.getAction().equals("clearText") || fetchMetadataVO.getAction().equals("paste")) {
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
			if (fetchMetadataVO.getAction().equals("selectvaluesTable") || fetchMetadataVO.getAction().equals("Dropdown Values")
					|| fetchMetadataVO.getAction().equals("sendKeys") || fetchMetadataVO.getAction().equals("paste")) {
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
			while (parent != null
					&& parent.select(":matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
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
			if (element.attr("id").equals(fetchMetadataVO.getInputParameter())) {
				return element.cssSelector();

			} else if (element.attr("title").equals(fetchMetadataVO.getInputParameter())) {

			}
		}
	}

	return "";
}

private WebElement findTheElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findTheElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						Selelement = driver.findElement(By.xpath(getXPath(element)));
					}

				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				} else {
					Selelement = Selelement.findElement(By.xpath("./following::input[not(@type='hidden')]"));
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

private WebElement findTheSelectAVAlueElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange, String string) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findTheSelectAVAlueElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length), string);
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try{
					Selelement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					catch(Exception ex){
						Selelement=driver.findElement(By.xpath(getXPath(element)));
					}
				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				} else {
					Element parent1 = element.parent();
					Element splitedElement = null;
					while (parent1 != null) {

						if (!parent1.select(":matchesOwn(^" + string + "$)").isEmpty()) {

							break;
						}
						// else {

						// if(sendKeysValue.equals(string)){

						// String[] parts = string.split(" ");

						// String selector = Arrays.stream(parts)

						// .map(p -> ":matchesOwn(^" + p + "$)")

						// .collect(Collectors.joining(", "));

						// Element element2 = doc.select(selector).parents().stream()

						// .filter(e -> Arrays.stream(parts)

						// .allMatch(p -> e.select(":matchesOwn(^" + p + "$)").size() == 1))

						// .findFirst()

						// .orElse(null);

						// if (element2 != null) {

						// splitedElement = element2;

						// break;

						// }

						// }

						// else{

						// String parts1 = string.replaceAll(sendKeysValue,"");

						// Elements elements1 = parent1.select(":matchesOwn(^" + sendKeysValue + "$), "

						// + ":containsOwn(^" + parts1 + "$)");

						// Element element1 = elements1.parents().stream()

						// .filter(e -> e.select(":matchesOwn(^" + sendKeysValue + "$)").size() == 1

						// && e.select(":containsOwn(^" + parts1 + "$)").size() == 1)

						// .findFirst()

						// .orElse(null);

						// if (element1 != null) {

						// splitedElement = element1;

						// break;

						// }

						// }

						// }
						else if (!parent1.select("[title='" + string + "']").isEmpty()) {
							break;
						}
						parent1 = parent1.parent();

					}
					if (parent1 != null) {
						Elements selectAValueElement = null;
						if (!parent1.select(":matchesOwn(^" + string + "$)").isEmpty()) {
							selectAValueElement = parent.select(":matchesOwn(^" + string + "$)");
						} else if (!parent1.select("[title='" + string + "']").isEmpty()) {
							selectAValueElement = parent1.select("[title='" + string + "']");
						}
						if(selectAValueElement.size() == 0) {
							throw new NullPointerException();
						}
						for (Element element1 : selectAValueElement) {
							// if (!element.parent().text().equals(element.text())) {
							// continue;
							// }
							WebElement selele = null;
							try {
								selele = driver.findElement(By.cssSelector(element1.cssSelector()));
							} catch (Exception e) {
								try {
									selele = driver
											.findElement(
													By.xpath(("\\#root".equalsIgnoreCase(element1.cssSelector()))
															? null
															: CSS2XPath.css2xpath(element1.cssSelector(), true)));
								} catch (Exception ex) {
									selele = driver
											.findElement(By.xpath("//*[@class='" + element1.parent().attr("class")
													+ "']/child::*[@class='" + element1.attr("class") + "']"));
								}
							}
							if (!selele.isEnabled() || !selele.isDisplayed()) {
								continue;
							}
							return selele;
						}
					}
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

private List<WebElement> findDatePicker(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	List<WebElement> elemets = new ArrayList<>();
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findTheElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						Selelement = driver.findElement(By.xpath(getXPath(element)));
					}
				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				} else {
					WebElement Selelement1 = Selelement.findElement(By.xpath("./following::input"));
					elemets.add(Selelement1);
					elemets.add(Selelement);

					return elemets;
				}
			}
		}
	} else {
		return null;
	}

	return null;
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

// Method for Naviagte
private void Navigate(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	String param1 = "Navigator";
	WebElement clickOnNaviagtorIcon = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
	synchronized (clickOnNaviagtorIcon) {
		while (clickOnNaviagtorIcon == null) {
			try {
				clickOnNaviagtorIcon.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Found element1: " + clickOnNaviagtorIcon.getText());

	}
	clickOnNaviagtorIcon.click();
	try {
		Thread.sleep(5000);
		CompletableFuture<Object> updateDom = this.updateDOM(driver);
		try {
			Object update = updateDom.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	WebElement showMoreElement = driver.findElement(By.xpath("//*[text()='Show More']"));
	synchronized (showMoreElement) {
		while (showMoreElement == null) {
			try {
				showMoreElement.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Found element2: " + showMoreElement.getText());

	}
	showMoreElement.click();
	String[] NavigateInputParameter = fetchMetadataVO.getInputParameter().split(">");
	for (int i = NavigateInputParameter.length - 1; i >= 0; i--) {
		Elements inputParameterEle = this.doc.select(":matchesOwn(^" + NavigateInputParameter[i] + "$)");
		String parentEle = NavigateInputParameter[i - 1];
		inputParameterEle.forEach(ele -> {
			Element parent = ele.parent();
			while (parent != null && parent.select(":matchesOwn(^" + parentEle + "$)").isEmpty()) {
				parent = parent.parent();
			}
			if (parent != null) {

			}
		});
	}

}

// Method for logout
private void Logout(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {

}

// Method for login
private void login(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
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
		WebElement selElement = findTheElement(fetchMetadataVO, customerDetails, driver, ele,
				Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
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

// Method for Table Dropdownvalues
private void TableDropdownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {

}

// Method for paste
private void paste(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if(headerElements.size() == 0) {
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
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isDisplayed() || !selElement.isEnabled()) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
				break;
			}
		}

		else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
									: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isDisplayed() || !selElement.isEnabled()) {
				continue;
			}
			selElement = findTheElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
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
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

// Method for Table sendKeys
private void tableSendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {

}

// Method for selectDropdownValues
private void selectDropdownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement=null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
										: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath(getXPath(ele)));
					}
				}
				if (!selElement.isDisplayed() || !selElement.isEnabled()) {
					continue;
				}
			selElement = selElement.findElement(By.xpath("./following::select"));
			if (!selElement.isEnabled()) {
				continue;
			} else {
				((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;",
						selElement);
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
		else{
			WebElement selElement=null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
										: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath(getXPath(ele)));
					}
				}
				if (!selElement.isDisplayed() || !selElement.isEnabled()) {
					continue;
				}
		selElement = findselectDropdownElement(fetchMetadataVO, customerDetails, driver, ele,
				Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
		if (selElement == null) {
			continue;
		} else {
			((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;",
					selElement);
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
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findselectDropdownElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findselectDropdownElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement selElement=null;
				try {
					selElement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath(getXPath(element)));
					}
				}
				if (!selElement.isDisplayed() || !selElement.isEnabled()) {
					continue;
				}
				selElement = selElement.findElement(By.xpath("./following::select"));
				if (!selElement.isEnabled() && !selElement.isDisplayed()) {
					continue;
				} else {
					return selElement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

// Method for sendKeys
private void sendKeys(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
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
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			} else {
				if (getElementtoCheck != "placeholder")
					selElement = selElement.findElement(By.xpath("./following::input[not(@type='hidden')]"));
				previousEle = selElement;
				try {
					selElement.clear();
					selElement.sendKeys(fetchMetadataVO.getInputValue());
					previousEle = selElement;
					sendKeysValue = fetchMetadataVO.getInputValue();
				} catch (Exception ex) {
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					jsExecutor.executeScript("arguments[0].value = '';", selElement);
					previousEle = selElement;
					// jsExecutor.executeScript("arguments[0].click();", selElement);
					String script = "arguments[0].value = arguments[1]";
					// jsExecutor.executeScript("arguments[0].focus();", selElement);
					jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue() + " ");
					selElement.sendKeys(Keys.BACK_SPACE);
					sendKeysValue = fetchMetadataVO.getInputValue();
				}
				break;
			}
		} else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			}
			selElement = findTheElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				try {
					selElement.clear();
					selElement.sendKeys(fetchMetadataVO.getInputValue());
					previousEle = selElement;
					sendKeysValue = fetchMetadataVO.getInputValue();
				} catch (Exception ex) {
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					jsExecutor.executeScript("arguments[0].value = '';", selElement);
					previousEle = selElement;
					jsExecutor.executeScript("arguments[0].click();", selElement);
					String script = "arguments[0].value = arguments[1]";
					// jsExecutor.executeScript("arguments[0].focus();", selElement);
					jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue() + " ");
					selElement.sendKeys(Keys.BACK_SPACE);
					sendKeysValue = fetchMetadataVO.getInputValue();
				}

				break;
			}
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private void clickCheckbox(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if (headerElements.size() == 0) {
		headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
	}
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		WebElement Selelement = null;
		try {
			Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
		} catch (Exception e) {
			try {
				Selelement = driver
						.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
			} catch (Exception ex) {
				Selelement = driver.findElement(By.xpath(getXPath(ele)));
			}
		}
		if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
			continue;
		}
		WebElement selElement = findCheckBoxElement(fetchMetadataVO, customerDetails, driver, ele,
				fetchMetadataVO.getInputValue());
		if (selElement == null) {
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
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findCheckBoxElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String inputValue) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + inputValue + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {

			WebElement Selelement = null;
			try {
				Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
			} catch (Exception e) {
				try {
					Selelement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
				} catch (Exception ex) {
					Selelement = driver.findElement(By.xpath(getXPath(element)));
				}
			}
			if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
				continue;
			} else {
				Selelement = Selelement.findElement(By.xpath("./following::input"));
				return Selelement;
			}

		}
	} else {
		return null;
	}

	return null;
}

private void clickRadioButton(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if (headerElements.size() == 0) {
		headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
	}
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		WebElement Selelement = null;
		try {
			Selelement = driver.findElement(By.cssSelector(ele.cssSelector()));
		} catch (Exception e) {
			try {
				Selelement = driver
						.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
			} catch (Exception ex) {
				Selelement = driver.findElement(By.xpath(getXPath(ele)));
			}
		}
		if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
			continue;
		}
		WebElement selElement = findRadioButtonElement(fetchMetadataVO, customerDetails, driver, ele,
				fetchMetadataVO.getInputValue());
		if (selElement == null) {
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
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findRadioButtonElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String inputValue) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + inputValue + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {

			WebElement Selelement = null;
			try {
				Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
			} catch (Exception e) {
				try {
					Selelement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
				} catch (Exception ex) {
					Selelement = driver.findElement(By.xpath(getXPath(element)));
				}
			}
			if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
				continue;
			} else {
				Selelement = Selelement.findElement(By.xpath("preceding::input[1]"));
				return Selelement;
			}

		}
	} else {
		return null;
	}

	return null;
}

private void datePicker(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if (headerElements.size() == 0) {
		headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
	}
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				selElement.sendKeys(Keys.BACK_SPACE);
				break;
			}
		}

		else {
			WebElement selEle = null;
			try {
				selEle = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selEle = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selEle.isEnabled() || !selEle.isDisplayed()) {
				continue;
			}
			List<WebElement> selElement = findDatePicker(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.get(0).clear();
				previousEle = selElement.get(0);
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				// jsExecutor.executeScript("arguments[0].click();", selElement.get(0));
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement.get(0), fetchMetadataVO.getInputValue() + " ");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				selElement.get(0).sendKeys(Keys.BACK_SPACE);
				break;
			}
		}
	}

}

// method for selectAValue
// method for selectAValue

private void selectAvalue(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
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
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			boolean flag = false;
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement=driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			} else {
				// selElement = selElement.findElement(By.xpath("./following::input"));

				Element parent = ele.parent();
				while (parent != null && parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)").isEmpty()) {
					parent = parent.parent();
				}
				if (parent != null) {
					Elements selectAValueElement = parent.select(":matchesOwn(^" + fetchMetadataVO.getInputValue() + "$)");
					if(selectAValueElement.size() == 0) {
						throw new NullPointerException();
					}
					for (Element element : selectAValueElement) {
						// if (!element.parent().text().equals(element.text())) {
						// continue;
						// }
						WebElement selele = null;
						try {
							selele = driver.findElement(By.cssSelector(element.cssSelector()));
						} catch (Exception e) {
							try {
								selele = driver
										.findElement(
												By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
														: CSS2XPath.css2xpath(element.cssSelector(), true)));
							} catch (Exception ex) {
								selele = driver.findElement(By.xpath(getXPath(element)));
							}
						}
						if (!selele.isEnabled() || !selele.isDisplayed()) {
							continue;
						} else {
							try {
								xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
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
					// if (flag) {
					// break;
					// }
				}
			}
		} else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement=driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			}
			selElement = findTheSelectAVAlueElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), fetchMetadataVO.getInputValue());
			if (selElement == null) {
				continue;
			} else {
				try {
					xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					selElement.click();
					break;
				} catch (Exception e) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", selElement);
					break;

				}
			}
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

// Method for vertical Scroll
private void VerticalScroll(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {

}

// Method for Clear
private void clearText(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement=null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
										: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath(getXPath(ele)));
					}
				}
				if (!selElement.isDisplayed() || !selElement.isEnabled()) {
					continue;
				}
			else {
				selElement.clear();

			}
		}
		else{
			WebElement selElement=null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
										: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath(getXPath(ele)));
					}
				}
				if (!selElement.isDisplayed() || !selElement.isEnabled()) {
					continue;
				}
		selElement = findTheElement(fetchMetadataVO, customerDetails, driver, ele,
				Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
		if (selElement == null) {
			continue;
		} else {
			selElement.clear();
		}
	}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

// Method for click Link
private void dropDownValues(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {
			WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			if (!selElement.isEnabled() && !selElement.isDisplayed()
			// || !selElement.isClickable()
			) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
				break;
			}
		} else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception ex) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception e) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			}
			selElement = findDropDownEle(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), fetchMetadataVO.getInputValue());
			if (selElement == null) {
				continue;
			} else {
				try {
					selElement.click();
				} catch (Exception e) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", selElement);
				}

				break;
			}
		}
	}
}

private WebElement findDropDownEle(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange, String inputValue) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()
				// || !Selelement.isClickable()
				) {
					continue;
				}
				findTheElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception ex) {
					try {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception e) {
						Selelement = driver.findElement(By.xpath(getXPath(element)));
					}
				}
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()
				// || !Selelement.isClickable()
				) {
					continue;
				}
				// Selelement = Selelement.findElement(By.xpath("./following::input"));
				WebElement Selelement1 = Selelement.findElement(By.xpath("./following::button"));
				if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
					continue;
				} else {
					try {
						Selelement1.click();
					} catch (Exception ex) {
						try {
							Selelement1.click();
						} catch (Exception e) {
							JavascriptExecutor executor = (JavascriptExecutor) driver;
							executor.executeScript("arguments[0].click();", Selelement1);
						}
					}
					// JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					// CompletableFuture<Object> updateDo = this.updateDOM(jsExecutor);
					// try {
					// Object update = updateDo.get();
					// } catch (InterruptedException | ExecutionException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// findDropdownOption(element, inputValue);
					WebElement SearchSelelement = Selelement
							.findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
					return SearchSelelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

private WebElement findDropdownOption(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element element, String inputValue) {
	Element parent = element.parent();
	while (parent != null) {
		boolean gotElement = false;
		if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
			Elements eles = parent.select(":matchesOwn(^" + inputValue + "$)");
			if(eles.size() == 0) {
				throw new NullPointerException();
			}
			for (Element elem : eles) {
				WebElement selEle = null;
				try {
					selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					try {
						selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
								: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					} catch (Exception exe) {
						try {
							selEle = driver.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
									+ "']/child::*[@class='" + elem.attr("class") + "']"));
						} catch (Exception exep) {
							continue;
						}
						// continue;
					}
				}
				if (selEle.isDisplayed() && selEle.isEnabled()) {
					gotElement = true;
					return selEle;

				}
			}
		}
		if (!gotElement) {
			parent = parent.parent();
		}
	}
	return null;
}

// method for click Link
private void clickLink(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	updateDOM(driver);
	WebElement cssSelectorEle = null;
	if (splitInputParameter.length == 1) {
		Elements elements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
		if (elements.size() == 0) {
			elements = doc.select("*[title='" + splitInputParameter[0] + "']:not(img)");
		}
		if (elements.size() == 0) {

			elements = doc.select("input[value='" + splitInputParameter[0] + "']");
		}
		if (elements.size() == 0) {
			String pattern = "^" + splitInputParameter[0] + "[\\p{L}0-9@#$%^\\s].*";
			elements = doc.select(":matchesOwn(" + pattern + ")");
		}
		if(elements.size() == 0) {
			throw new NullPointerException();
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
					System.out.println(getXPath(element));
					ele = driver.findElement(By.xpath(getXPath(element)));
				}
			}
			if (!ele.isDisplayed() || !ele.isEnabled()) {
				continue;

			} else {
				cssSelectorEle = ele;
				break;
			}
		}
	} else {
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		if(headerElements.size() == 0) {
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
					} else {
						headerSelElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
										: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					}

				}

			}
			if (!headerSelElement.isDisplayed() || !headerSelElement.isEnabled()) {
				continue;
			} else {
				WebElement selElement = findLinkElement(fetchMetadataVO, customerDetails, driver, elem,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
				if (selElement == null) {
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
			xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			buttonElement.click();
		} catch (Exception e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", buttonElement);
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}
}

private WebElement findLinkElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	ELements seconElement = findSecondELement(fetchMetadataVO, customerDetails, driver, parent, copyOfRange[0]);
	if (copyOfRange.length > 1) {
		findLinkElement(fetchMetadataVO, customerDetails, driver, seconElement.getElement(), Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
	} else {
		WebElement Selelement = seconElement.getWebElement();
		return Selelement;
	}
	return null;
}

private ELements findSecondELement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element parent, String string) {
	ELements elements = new ELements();
	while (parent != null) {
		boolean gotElement = false;
		String pattern = "^" + string + "[\\p{L}0-9@#$%^\\s].*";
		if (!parent.select(":matchesOwn(^" + string + "$)").isEmpty()) {
			Elements eles = parent.select(":matchesOwn(^" + string + "$)");
			if(eles.size() == 0) {
				throw new NullPointerException();
			}
			for (Element elem : eles) {
				WebElement selEle = null;
				try {
					selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					try {
						selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
								: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					} catch (Exception exe) {
						try {
							selEle = driver.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
									+ "']/child::*[@class='" + elem.attr("class") + "']"));
						} catch (Exception exep) {
							parent = elem.parent();
							while (parent != null && parent.attr("class") == "") {
								parent = parent.parent();
							}
							selEle = driver.findElement(By.xpath("//*[@class='" + parent.attr("class")
									+ "']/following::*[@class='" + elem.attr("class") + "']"));

						}
						// continue;
					}
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
			if(eles.size() == 0) {
				throw new NullPointerException();
			}
			for (Element elem : eles) {
				WebElement selEle = null;
				try {
					selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
							: CSS2XPath.css2xpath(elem.cssSelector(), true)));
				}
				if (selEle.isDisplayed() && selEle.isEnabled()) {
					gotElement = true;
					elements.setWebElement(selEle);
					elements.setElement(elem);
					return elements;

				}
			}
		} else if (!parent.select("[title='" + string + "']").isEmpty()) {
			Elements eles = parent.select("[title='" + string + "']");
			if(eles.size() == 0) {
				throw new NullPointerException();
			}
			for (Element elem : eles) {
				WebElement selEle = null;
				try {
					selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
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

		else if (!parent.select(":matchesOwn(" + pattern + ")").isEmpty()) {
			Elements eles = parent.select(":matchesOwn(" + pattern + ")");
			if(eles.size() == 0) {
				throw new NullPointerException();
			}
			for (Element elem : eles) {
				WebElement selEle = null;
				try {
					selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
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

	return null;
}

// method for click Link
private void clickImage(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	Elements elements = GettingAllElements(fetchMetadataVO.getAction());
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	String cssSelectorEle = "";
	if (splitInputParameter.length == 1) {

		Elements imagElements = doc.select("*[title='" + splitInputParameter[0] + "']");
		boolean checkingWhereIamgeFOund = false;
		if (imagElements.size() == 0) {
			checkingWhereIamgeFOund = true;
			imagElements = doc.select(String.format("img[data-key='%s'], svg[data-key='%s']",
					splitInputParameter[0].toLowerCase(), splitInputParameter[0].toLowerCase()));
		}
		if(imagElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : imagElements) {
			WebElement imgElement = null;
			try {
				imgElement = driver.findElement(By.cssSelector(element.cssSelector()));
			} catch (Exception e) {
				try {
					imgElement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
									: CSS2XPath.css2xpath(element.cssSelector(), true)));
				} catch (Exception ex) {
					imgElement = driver.findElement(By.xpath(getXPath(element)));
				}
			}
			if (!imgElement.isDisplayed() || !imgElement.isEnabled()) {
				continue;
			} else {
				try {
					xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
					imgElement.click();
					break;
				} catch (Exception e) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", imgElement);
					break;
				}
			}
		}
	} else {
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		WebElement imgElement = null;
		if(headerElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element ele : headerElements) {
			try {
				imgElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					imgElement = driver
							.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
									: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					imgElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!imgElement.isDisplayed() || !imgElement.isEnabled()) {
				continue;
			}
			WebElement selElement = findImageElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
				selElement.click();
			}
		}

	}
	if (cssSelectorEle != "") {

		WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
		synchronized (buttonElement) {
			while (buttonElement == null) {
				try {
					buttonElement.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
		buttonElement.click();
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}
}

private WebElement findImageElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent
			.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0])).isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent
				.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0]));
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findImageElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement imgElement = null;
				try {
					imgElement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						imgElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						imgElement = driver.findElement(By.xpath(getXPath(ele)));
					}
				}
				if (!imgElement.isDisplayed() || !imgElement.isEnabled()) {
					continue;
				} else {
					return imgElement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

// Method for click button
// Method for click button
private void tableRowSelect(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	String cssSelectorEle = "";
	if (splitInputParameter.length == 1) {
		Elements elements = doc.select(String.format("table[summary='%s']", splitInputParameter[0]));
		if(elements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : elements) {
			WebElement tableElement = driver.findElement(By.cssSelector(element.cssSelector()));
			if (!tableElement.isDisplayed() || !tableElement.isEnabled()) {
				continue;
			} else {
				cssSelectorEle = element.cssSelector();
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
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		if(headerElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element ele : headerElements) {
			WebElement selElement = findtableRowSelectElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.click();
			}
		}

	}
	if (cssSelectorEle != "") {

		WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
		synchronized (buttonElement) {
			while (buttonElement == null) {
				try {
					buttonElement.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		buttonElement.click();
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}
}

private WebElement findtableRowSelectElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	if (copyOfRange.length == 1) {
		while (parent != null && parent.select(String.format("table[summary='%s']", copyOfRange[0])).isEmpty()) {
			parent = parent.parent();
		}
	} else {
		while (parent != null && (parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)").isEmpty())) {
			parent = parent.parent();
		}
	}
	if (parent != null) {
		Elements secondElements = null;
		if (copyOfRange.length == 1) {
			secondElements = parent.select(String.format("table[summary='%s']", copyOfRange[0]));
		} else {
			secondElements = parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)");
		}
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findtableRowSelectElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				} else {
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

private void clickButton(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	Elements elements = GettingAllElements(fetchMetadataVO.getAction());
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	String cssSelectorEle = "";
	if (splitInputParameter.length == 1) {
		for (Element element : elements) {
			if ((element.text().trim().equalsIgnoreCase(fetchMetadataVO.getInputParameter())
					|| element.attr("alt").trim().equals(fetchMetadataVO.getInputParameter())
					|| element.attr("value").equals(fetchMetadataVO.getInputParameter()))) {
				cssSelectorEle = element.cssSelector();
				break;

			}
		}
	} else {
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			WebElement selElement = findButtonElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.click();
			}
		}

	}
	if (cssSelectorEle != "") {

		WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
		synchronized (buttonElement) {
			while (buttonElement == null) {
				try {
					buttonElement.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		buttonElement.click();
	}
}

private WebElement findButtonElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()
			|| parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()
			|| parent.select("[title='" + copyOfRange[0] + "']").isEmpty())) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = null;
		if (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
			secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		}
		if (parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()) {
			secondElements = parent.select("[alt='" + copyOfRange[0] + "']");
		}
		if (parent.select("[title='" + copyOfRange[0] + "']").isEmpty()) {
			secondElements = parent.select("[title='" + copyOfRange[0] + "']");
		}
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findButtonElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
					continue;
				} else {
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

// Method for clickCheckBox
private void clickCheckBox(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {

}

// Method for SwitchToParentWindow
private void SwitchToParentWindow(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
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

}

// Method for SwitchToDefaultFrame
private void SwitchToDefaultFrame(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	driver.switchTo().defaultContent();
}

// Method for SwitchToFrame
private void SwitchToFrame(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	// Elements elements = doc.select("iframe");
	Elements elements = GettingAllElements(fetchMetadataVO.getAction());
	String CssSelector = "";
	for (Element element : elements) {
		if (element.attr("id").equals(fetchMetadataVO.getInputParameter())) {
			CssSelector = element.cssSelector();
			break;

		} else if (element.attr("title").equals(fetchMetadataVO.getInputParameter())) {
			CssSelector = element.cssSelector();
			break;
		} else if (element.attr("title").equals(fetchMetadataVO.getInputParameter())) {

		}
	}
	WebElement frameElement = null;
	try {
		frameElement = driver.findElement(By.cssSelector(CssSelector));
	} catch (Exception e) {
		frameElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(CssSelector)) ? null
				: CSS2XPath.css2xpath(CssSelector, true)));
	}
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

}

// Method for textArea
private void textArea(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {

			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			} else {
				selElement = selElement.findElement(By.xpath("./following::textarea"));
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
				break;
			}
		} else {
			WebElement selElement = null;
			try {
				selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			} catch (Exception e) {
				try {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(getXPath(ele)));
				}
			}
			if (!selElement.isEnabled() || !selElement.isDisplayed()) {
				continue;
			}
			selElement = findTheTextAreaElement(fetchMetadataVO, customerDetails, driver, ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
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
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findTheTextAreaElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findTheTextAreaElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = null;
				try {
					Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					try {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					} catch (Exception ex) {
						Selelement = driver.findElement(By.xpath(getXPath(element)));
					}
				}
				Selelement = Selelement.findElement(By.xpath("./following::textarea"));
				if (!Selelement.isEnabled()) {
					continue;
				} else {
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

// Method for Windowhandle
private void windowhandle(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	String parentWindowHandle = driver.getWindowHandle();
	Set<String> windowHandles = driver.getWindowHandles();

	for (String windowHandle : windowHandles) {
		if (!windowHandle.equals(parentWindowHandle)) {
			driver.switchTo().window(windowHandle);
			break;
		}
	}

}

// Method for clickExpandorcollapse

private void clickExpandorcollapse(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver) {
	try {
	String[] splitInputParameter = fetchMetadataVO.getInputParameter().split(">");
	Elements headerElements = null;
	headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
	if(headerElements.size() == 0) {
		throw new NullPointerException();
	}
	for (Element ele : headerElements) {
		if (splitInputParameter.length == 1) {

			WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
			if (!selElement.isEnabled()) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, fetchMetadataVO.getInputValue());
				break;
			}

		}
		WebElement selElement = findThecllopseExpandElement(fetchMetadataVO, customerDetails, driver, ele,
				Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
		if (selElement == null) {
			continue;
		} else {
			// selElement.clear();
			xpathPerformance.fullPagePassedScreenshot(driver, fetchMetadataVO, customerDetails);
			selElement.click();
			break;
		}
	}
	}catch(Exception e) {
		xpathPerformance.fullPageFailedScreenshot(driver, fetchMetadataVO, customerDetails);
		throw e;
	}

}

private WebElement findThecllopseExpandElement(ScriptDetailsDto fetchMetadataVO, CustomerProjectDto customerDetails, WebDriver driver, Element ele, String[] copyOfRange) {
	Element parent = ele.parent();
	while (parent != null && parent.select("*[title=" + copyOfRange[0] + "]").isEmpty()) {
		parent = parent.parent();
	}
	if (parent != null) {
		Elements secondElements = parent.select("*[title=" + copyOfRange[0] + "]");
		if(secondElements.size() == 0) {
			throw new NullPointerException();
		}
		for (Element element : secondElements) {
			if (copyOfRange.length > 1) {
				findThecllopseExpandElement(fetchMetadataVO, customerDetails, driver, element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
			} else {
				WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
				if (!Selelement.isEnabled()) {
					continue;
				} else {
					return Selelement;
				}
			}
		}
	} else {
		return null;
	}

	return null;
}

private Elements GettingAllElements(String actionElements) {
	Elements elements;
	switch (actionElements) {
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
		case "clickImage":
			elements = doc.select("img, svg");
			break;
		case "sendKeys":
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
		case "switchToFrame":
			elements = doc.select("iframe");
			break;
		default:
			elements = doc.select("*");
			break;
	}
	return elements;
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