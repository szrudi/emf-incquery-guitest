package demo
import org.eclipse.incquery.tooling.ui.tests.Calculator
Feature: Calculator
Background:
  Calculator calc
  Given a calculator
	calc = new Calculator
 
Scenario: Add two numbers
  int result
  Given I have entered "50" into the calculator
	calc.enter(args.first)
  And I have entered "70" into the calculator
     When I press "add"
	result = calc.press(args.first)
  Then the result should be "120" 
  	'''
  	asddsa
  	'''
  	result => args.first
  	
