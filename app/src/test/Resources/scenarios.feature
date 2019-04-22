Feature: Play to Space invaders
  As a user
  I want play game

  Scenario: Move space ship
    Given I want to move the ship down
    When We press down button
    Then Space Ship move down

  Scenario: Move space ship
    Given I want to move the ship to the left
    When  We press left button
    Then  Space Ship move left

  Scenario: Shot space ship
    Given I want to shot
    When  We press shot button
    Then  Space Ship shot a laser

  Scenario: Move space ship up
    Given I want to move the ship up
    When  We press up button
    Then  Space Ship move up

  Scenario: Collide with barrier
    Given I want to collide with the barrier
    When  The ship collides with barrier
    Then  The barrier disappears