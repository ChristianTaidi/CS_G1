Feature: Play to Space invaders
  As a user
  I want play game

  Scenario: Do login in the app
    Given  We are a user
    And  We enter to app
    When we enter name and age
    Then We start game

  Scenario: Move space ship
    Given A new game of S.I
    When  We press left buttom
    Then  Space Ship move left

  Scenario: Shot space ship
    Given A new game of S.I
    When  We press shot buttom
    Then  Space Ship shot a laser

  Scenario: Move space ship up
    Given A new game of S.I
    When  We press up buttom
    Then  Space Ship move up

  Scenario: Collide with barrier
    Given A new game of S.I
    When  The ship collides with barrier
    Then  The barrier disapears