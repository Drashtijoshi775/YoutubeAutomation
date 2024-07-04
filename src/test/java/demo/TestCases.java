package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.List;

import java.time.Duration;
import java.util.logging.Level;

import javax.swing.text.Utilities;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

         @BeforeMethod
    public void goToYT() throws InterruptedException{
        Wrappers.goToUrlAndWait(driver, "https://youtube.com");
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
    }

    @Test(enabled = true)
    public void testCase01() throws InterruptedException {
        System.out.println("Running Test Case 01");

        // Asserting correct URL
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(driver.getCurrentUrl().contains("youtube.com"), "URL does not contain 'youtube.com'");

        // Clicking on "About"
        Wrappers.findElementAndClick(driver, By.xpath("//div[@id='guide-links-primary']//a[text()='About']"));

        // Printing the message on the screen
        String message = Wrappers.findElementAndPrint(driver, By.tagName("h1"), 0);
        System.out.println("Message on the screen: " + message);

        sa.assertAll();
    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException {

        System.out.println("Running Test Case 02");

        SoftAssert softAssert = new SoftAssert();
      
        // Navigate to the "Films" tab
        WebElement movies = driver.findElement(By.xpath("//*[@id=\"endpoint\" and @title='Movies']"));
        movies.click();
          
        // Wait for the page to load completely
        Thread.sleep(2000);
          
        // Scroll to the extreme right in the "Top Selling" section
        WebElement topSellingSection = driver.findElement(By.xpath("(//ytd-button-renderer[@class='style-scope yt-horizontal-list-renderer arrow'])[2]"));
        
        while (topSellingSection.isDisplayed()) {
            topSellingSection.click();
            Thread.sleep(1000);
        }
    
      // Find the last movie in the "Top Selling" section
      List<WebElement> movieList = topSellingSection.findElements(By.xpath(".//ytd-grid-movie-renderer"));
      if (movieList.isEmpty()) {
        System.out.println("No movies found in the 'Top Selling' section.");
        return;
    }
    WebElement lastMovie = movieList.get(movieList.size() - 1);
      // Find the maturity rating (A or U/A) for the last movie
      //SoftAssert softAssert = new SoftAssert();
      boolean isRatedAorUA = false;
        try {
            WebElement ratingElement = lastMovie.findElement(By.xpath(".//p[@class='style-scope ytd-badge-supported-renderer']"));
            String ratingText = ratingElement.getText();
            System.out.println("Rating Text: " + ratingText);
            isRatedAorUA = ratingText.equalsIgnoreCase("A") || ratingText.equalsIgnoreCase("U/A");
        } catch (NoSuchElementException e) {
            System.out.println("Rating element not found: " + e.getMessage());
        }

        softAssert.assertTrue(isRatedAorUA, "Last movie is not rated 'A' or 'U/A'");
      // Apply Soft Assert on genre "Comedy" or "Animation"
        boolean isComedyOrAnimation = false;
        try {
            WebElement comedyElement = driver.findElement(By.xpath("//*[@id=\"items\"]/ytd-grid-movie-renderer[16]/a/span[contains(text(), 'Comedy')]"));
            WebElement animationElement = driver.findElement(By.xpath("//*[@id=\"items\"]/ytd-grid-movie-renderer[16]/a/span[contains(text(), 'Animation')]"));
            if (comedyElement.isDisplayed() || animationElement.isDisplayed()) {
                isComedyOrAnimation = true;
            }
        } catch (NoSuchElementException e) {
            System.out.println("Genre elements not found: " + e.getMessage());
        }
    
        softAssert.assertTrue(isComedyOrAnimation, "Movie is neither Comedy nor Animation");
    
        softAssert.assertAll(); // This will throw AssertionError if any soft assertion fails
    }
        //   WebElement matureMovie = driver.findElement(By.xpath("//*[@id=\"items\"]/ytd-grid-movie-renderer[6]/ytd-badge-supported-renderer/div[2]/p"));

        //   String ratingMovieText = matureMovie.getText();
        //   softAssert.assertEquals(ratingMovieText,"A");

        //   System.out.println("verified the rating-"+ ratingMovieText);

          
        //   // Verify if the movie genre is either "Comedy" or "Animation"
        //   WebElement movieGenre = driver.findElement(By.xpath("(//span[contains(text(),'Comedy')])[3]"));

        //   String GenreMovieText = movieGenre.getText();
        //   softAssert.assertTrue(GenreMovieText.contains("Comedy"),"Unverified Genre");

        //   System.out.println("verified the GenreMovie-"+ GenreMovieText);

    
        //   softAssert.assertAll();
        //   System.out.println("End Test case: testCase02");
        // }

    @Test(enabled = true)
    public void testCase03() throws InterruptedException{
        System.out.println("Running Test Case 03");
        Wrappers.findElementAndClick(driver, By.xpath("//a[@title='Music']"));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        Wrappers.clickTillUnclickable(driver, By.xpath("(//span[contains(.,'Biggest Hits')]//ancestor::div[5]//button[@class='yt-spec-button-shape-next yt-spec-button-shape-next--text yt-spec-button-shape-next--mono yt-spec-button-shape-next--size-m yt-spec-button-shape-next--icon-only-default'])[2]/.."), 5);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        By locator_TrackCount = By.xpath("//span[contains(.,'Biggest Hits')]//ancestor::div[6]//div[@id='contents']//ytd-compact-station-renderer//p[@id='video-count-text']");
        String res = Wrappers.findElementAndPrint(driver, locator_TrackCount, driver.findElements(locator_TrackCount).size()-1);
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(Wrappers.convertToNumericValue(res.split(" ")[0])>50);
    }

    @Test(enabled = true)
    public void testCase04() throws InterruptedException {
        System.out.println("Running Test Case 04");
        Wrappers.findElementAndClick(driver, By.xpath("//*[@id=\"endpoint\" and @title='News']"));
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    
        // Wait for the element to be clickable
        WebElement contentCards = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='rich-shelf-header-container' and contains(.,'Latest news posts')]//ancestor::div[1]//div[@id='contents']")));
    
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        long sumOfVotes = 0;
        for (int i = 1; i <= 3; i++) {
            System.out.println(Wrappers.findElementAndPrintWE(driver, By.xpath("//div[@id='header']"), contentCards, i));
            System.out.println(Wrappers.findElementAndPrintWE(driver, By.xpath("//div[@id='body']"), contentCards, i));
            try {
                String res = Wrappers.findElementAndPrintWE(driver, By.xpath("//span[@id='vote-count-middle']"), contentCards, i);
                if (!res.isEmpty()) { // Check if res is not empty
                    sumOfVotes += Wrappers.convertToNumericValue(res);
                }
            } catch (NoSuchElementException e) {
                System.out.println("Vote not present - " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error converting to numeric value - " + e.getMessage());
            }
            System.out.println(sumOfVotes);
            Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
        }
        System.out.println("Ending Test Case 04");
    }

    @Test(enabled = true, dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testCase05(String searchWord) throws InterruptedException {
          System.out.println("Running Test Case 05 Flow for: " + searchWord);

    // Enter search term and perform search
    Wrappers.sendKeysWrapper(driver, By.xpath("//input[@id='search']"), searchWord);
    Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    long tally = 0;
    int iter = 1;
    while (tally < 100000000 && iter <= 5) {
        try {
            // Find the element based on the index 'iter'
            String viewsText = Wrappers.findElementAndPrint(driver, By.xpath("(//div[@class='style-scope ytd-video-renderer' and @id='meta']//span[@class='inline-metadata-item style-scope ytd-video-meta-block'][1])"), iter - 1);
            tally += Wrappers.convertToNumericValue(viewsText);
            System.out.println("Current tally: " + tally);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No more elements found at iteration " + iter);
            break; // Exit loop if no more elements are found
        }
        iter++;
        Thread.sleep((new java.util.Random().nextInt(3) + 2) * 1000);
    }

    System.out.println("Total views tally: " + tally);
}
        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}