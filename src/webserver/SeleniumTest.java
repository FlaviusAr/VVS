   package webserver;
    
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.*;

    
   public class SeleniumTest  {
	   public WebDriver driver;
	   
	   
	   @Before
	    public void startBrowser() {
		    System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\eclipse-workspace\\maven\\chromedriver\\chromedriver.exe");
	        driver = new ChromeDriver();
	    }
	       
	    @Test
	    public void test_title() { 
	      String title = "Welcome!";
	      driver.get("http://localhost:10008"); 
	      Assert.assertEquals(title, driver.getTitle()); 	
	    }
	    
	    @Test
	    public void test_firstlink() { 
	      String title = "Google";
	      driver.get("http://localhost:10008"); 
	      driver.findElement(By.xpath("//a[@href='http://google.com']")).click();
	      Assert.assertEquals(title,driver.getTitle()); 	
	    }
	    
	    @Test
	    public void test_inputfield() { 
	      String text = "validare";
	      driver.get("http://localhost:10008");
	      driver.findElement(By.id("fname")).sendKeys("validare");
	      Assert.assertEquals(text, driver.findElement(By.id("fname")).getAttribute("value")); 	
	    }
	    
	    @Test
	    public void test_button1() { 
	      String text = "Buton apasat";
	      driver.get("http://localhost:10008");
	      driver.findElement(By.name("clickButton")).click();
	      Assert.assertEquals(text, driver.findElement(By.id("fname")).getAttribute("value")); 	
	    }
	    
	    @Test
	    public void test_button2() { 
	      String text = "YouTube";
	      driver.get("http://localhost:10008");
	      driver.findElement(By.name("butonYoutube")).click();
	      Assert.assertEquals(text, driver.getTitle()); 	
	    }
	    
	    @After
	    public void stopBrowser() {
	        driver.close();
	    }
}