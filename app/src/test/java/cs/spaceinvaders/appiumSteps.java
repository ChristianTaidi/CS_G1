package cs.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cs.spaceinvaders.entity.Bullet;
import cs.spaceinvaders.entity.Defence;
import cs.spaceinvaders.entity.SpaceShip;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.android.AndroidDriver;

import android.content.Context;


import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class appiumSteps {

    private Context context;
    private SpaceShip spaceShip;
    private Bitmap spaceShipBitmap;
    private Bullet bullet;
    private Bitmap shootBitmap;
    private AndroidDriver driver;

    public int x, y;

    @Given("^I am in the MainActivity$")
    public void iAmInTheMainActivity() throws Throwable {
        // Create object of DesiredCapabilities class
        DesiredCapabilities capabilities = new DesiredCapabilities();
        // Optional
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        // Specify the device name (any name)
        capabilities.setCapability("deviceName", "Probar Aqui");
        // Platform version
        capabilities.setCapability("platformVersion", "9");
        // platform name
        capabilities.setCapability("platformName", "Android");
        // specify the application package that we copied from appium
        capabilities.setCapability("appPackage", "cs.spaceinvaders");
        // specify the application activity that we copied from appium
        capabilities.setCapability("appActivity", ".MainActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        // Specify the implicit wait of 5 second
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @When("^We press the start button$")
    public void wePressTheStartButton() {
        spaceShip.setPositionY(25);
        spaceShip.setMovementState(3);
    }

    @Then("^The game starts with my name saved$")
    public void theGameStartsWithMyNameSaved() {
        spaceShip.update(1000);
        assertEquals(25, spaceShip.getPositionY(),0);
    }

    @Given("^I want to move the ship to the left side$")
    public void iWantToMoveTheShipToTheLeftSide() {
        context = mock(Context.class);
        spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
        this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
    }

    @When("^We press the left button$")
    public void wePressTheLeftButton() {
        spaceShip.setPositionY(25);
        spaceShip.setMovementState(1);
    }

    @Then("^SpaceShip move left$")
    public void spaceShipMoveLeft() {
        spaceShip.update(1000);
        assertEquals(25, spaceShip.getPositionY(),0);
    }

    @Given("^I want to shot to the enemy$")
    public void iWantToShotToTheEnemy() {
        context = mock(Context.class);
        shootBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1), x/20, y/20, false);
        this.bullet = new Bullet(20,20, shootBitmap);
    }

    @When("^We press the shot button$")
    public void wePressTheShotButton() {
        bullet.setShotDir(0);
        bullet.setPositionY(25);
    }

    @Then("^SpaceShip shot a laser$")
    public void spaceShipShotALaser() {
        bullet.update(1000);
        assertEquals(25, bullet.getPositionY(),0);
    }

    @Given("^I want to move up$")
    public void iWantToMoveUp() {
        context = mock(Context.class);
        spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
        this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
    }

    @When("^We press the up button$")
    public void wePressTheUpButton() {
        spaceShip.setPositionY(25);
        spaceShip.setMovementState(4);
    }

    @Then("^SpaceShip move up$")
    public void spaceShipMoveUp() {
        spaceShip.update(1000);
        assertEquals(25, spaceShip.getPositionY(),0);
    }

    @Then("^The child or violent game starts$")
    public void theChildOrViolentGameStarts() {
        //barrier.destoyDefence();
    }
}
