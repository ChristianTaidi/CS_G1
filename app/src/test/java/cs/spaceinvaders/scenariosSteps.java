package cs.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cs.spaceinvaders.entity.Bullet;
import cs.spaceinvaders.entity.Defence;
import cs.spaceinvaders.entity.SpaceShip;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import android.content.Context;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class scenariosSteps {
   private Context context;
   private SpaceShip spaceShip;
   private Bitmap spaceShipBitmap;
   private Bullet bullet;
   private Bitmap shootBitmap;
   private Defence barrier;
   private Bitmap barrierBitmap;
   private InvadersGameView spaceShipCollides;

   public int x, y, shelterNumber, row,  column;

   @Given("^I want to move the ship down$")
   public void iWantToMoveTheShipDown() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press down button$")
   public void wePressDownButtom() {
      spaceShip.setPositionY(25);
      spaceShip.setMovementState(3);
   }

   @Then("^Space Ship move down$")
   public void spaceShipMoveDown() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getPositionY(),0);
   }

   @Given("^I want to move the ship to the left$")
   public void iWantToMoveTheShipToTheLeft() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press left button$")
   public void wePressLeftButtom() {
      spaceShip.setPositionY(25);
      spaceShip.setMovementState(1);
   }

   @Then("^Space Ship move left$")
   public void spaceShipMoveLeft() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getPositionY(),0);
   }

   @Given("^I want to shot$")
   public void iWantToShot() {
      context = mock(Context.class);
      shootBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet1), x/20, y/20, false);
      this.bullet = new Bullet(20,20, shootBitmap);
   }

   @When("^We press shot button$")
   public void wePressShotButtom() {
      bullet.setShotDir(0);
      bullet.setPositionY(25);
   }

   @Then("^Space Ship shot a laser$")
   public void spaceShipShotALaser() {
      bullet.update(1000);
      assertEquals(25, bullet.getPositionY(),0);
   }

   @Given("^I want to move the ship up$")
   public void iWantToMoveTheShipUp() {
      context = mock(Context.class);
      spaceShipBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship), x/8, y/15, false);
      this.spaceShip = new SpaceShip(context,20,20, spaceShipBitmap);
   }

   @When("^We press up button$")
   public void wePressUpButtom() {
      spaceShip.setPositionY(25);
      spaceShip.setMovementState(4);
   }

   @Then("^Space Ship move up$")
   public void spaceShipMoveUp() {
      spaceShip.update(1000);
      assertEquals(25, spaceShip.getPositionY(),0);
   }

   @Given("^I want to collide with the barrier$")
   public void iWantToCollideWithTheBarrier() {
      context = mock(Context.class);
      barrierBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.rock), x/20, y/80, false);
      this.barrier = new Defence(row, column, shelterNumber, x, y);
   }

   @When("^The ship collides with barrier$")
   public void theShipCollidesWithBarrier() {
      spaceShipCollides = mock(InvadersGameView.class);
      spaceShipCollides.checkPlayerBlockCollision();
   }

   @Then("^The barrier disappears$")
   public void theBarrierDisapears() {
      barrier.destoyDefence();
   }
}
