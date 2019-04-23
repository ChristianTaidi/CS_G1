package cs.spaceinvaders;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/Resources", plugin={"pretty"})
public class cucumberTestRunner{
}