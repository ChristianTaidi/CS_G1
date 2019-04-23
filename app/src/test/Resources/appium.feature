Feature: Play to Space invaders
  As a user
  I want play game

  Scenario: Write the name
    Given I am in the MainActivity
    When We press the start button
    Then The game starts with my name saved

  Scenario: Move space ship
    Given I want to move the ship to the left side
    When  We press the left button
    Then  SpaceShip move left

  Scenario: Shot space ship
    Given I want to shot to the enemy
    When  We press the shot button
    Then  SpaceShip shot a laser

  Scenario: Move space ship up
    Given I want to move up
    When  We press the up button
    Then  SpaceShip move up

  Scenario: Write the age
    Given I am in the MainActivity
    When  We press the start button
    Then  The child or violent game starts