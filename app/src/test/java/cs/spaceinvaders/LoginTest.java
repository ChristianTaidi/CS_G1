package cs.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cs.spaceinvaders.entity.SpaceShip;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import android.content.Context;
import org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;


public class LoginTest {
   public Context context;
   public SpaceShip spaceShip;
   public Bitmap spaceShipBitmap;

   @Given("^I want to move the ship down$")
   public void iWantToMoveTheShipDown() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press down buttom$")
   public void wePressDownButtom() {
      spaceShip.setMovementState(4);
      spaceShip.setY(25);
   }

   @Then("^Space Ship move down$")
   public void spaceShipMoveDown() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getY(),0);
   }

   @Given("^I want to move the ship to the left$")
   public void iWantToMoveTheShipToTheLeft() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press left buttom$")
   public void wePressLeftButtom() {
      spaceShip.setMovementState(4);
      spaceShip.setY(25);
   }

   @Then("^Space Ship move left$")
   public void spaceShipMoveLeft() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getY(),0);
   }

   @Given("^I want to shot$")
   public void iWantToShot() {

   }

   @When("^We press shot buttom$")
   public void wePressShotButtom() {

   }

   @Then("^Space Ship shot a laser$")
   public void spaceShipShotALaser() {

   }

   @Given("^I want to move the ship up$")
   public void iWantToMoveTheShipUp() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press up buttom$")
   public void wePressUpButtom() {
      spaceShip.setMovementState(4);
      spaceShip.setY(25);
   }

   @Then("^Space Ship move up$")
   public void spaceShipMoveUp() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getY(),0);
   }

   @Given("^I want to collide with the barrier$")
   public void iWantToCollideWithTheBarrier() {

   }

   @When("^The ship collides with barrier$")
   public void theShipCollidesWithBarrier() {

   }

   @Then("^The barrier disapears$")
   public void theBarrierDisapears() {

   }
}
