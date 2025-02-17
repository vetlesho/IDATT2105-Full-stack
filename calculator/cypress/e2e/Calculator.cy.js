describe('Calculator', () => {
  beforeEach(() => {
    cy.visit('/Calculator')
    cy.contains('Calculator')
    cy.intercept('POST', 'http://localhost:8080/api/calculator/calculate').as('calcRequest')
  })

  it('performs basic calculations correctly', () => {
    // Addition
    cy.get('button').contains('7').click()
    cy.get('button').contains('+').click()
    cy.get('button').contains('8').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '15')

    // Clear for next calculation
    cy.get('button').contains('C').click()

    // Subtraction
    cy.get('button').contains('1').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains(/^-$/).click()
    cy.get('button').contains('7').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '8')
  })

  it('handles complex calculations with multiple numbers', () => {
    cy.get('button').contains('1').click()
    cy.get('button').contains('2').click()
    cy.get('button').contains('3').click()
    cy.get('button').contains('*').click()
    cy.get('button').contains('4').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains('6').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '56088')
  })

  it('handles multiple operators', () => {
    cy.get('button').contains('3').click()
    cy.get('button').contains('*').click()
    cy.get('button').contains('3').click()
    cy.get('button').contains('*').click()
    cy.get('button').contains('3').click()
    cy.get('button').contains('/').click()
    cy.get('button').contains('9').click()
    cy.get('button').contains('+').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains('/').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '4')
  })

  it('handles decimal numbers correctly', () => {
    cy.get('button').contains('5').click()
    cy.get('button').contains('.').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains('*').click()
    cy.get('button').contains('2').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '11')
  })

  it('maintains calculation history', () => {
    // First calculation
    cy.get('button').contains('7').click()
    cy.get('button').contains('+').click()
    cy.get('button').contains('8').click()
    cy.get('button').contains('=').click()

    // Second calculation
    cy.get('button').contains('*').click()
    cy.get('button').contains('2').click()
    cy.get('button').contains('=').click()

    // Check log
    cy.get('.outputLog').within(() => {
      cy.contains('15*2 = 30')
      cy.contains('7+8 = 15')
    })

    // Test clear log
    cy.get('button').contains('C-log').click()
    cy.get('.outputLog').should('be.empty')
  })

  it('handles error cases correctly', () => {
    // Division by zero
    cy.get('button').contains('5').click()
    cy.get('button').contains('/').click()
    cy.get('button').contains('0').click()
    cy.get('button').contains('=').click()
    cy.get('.alert-popup').should('contain', 'Cannot divide by zero')

    // Clear alert
    cy.get('.alert-content button').click()
    cy.get('.alert-popup').should('not.exist')

    // Clear input
    cy.get('button').contains('C').click()

    // Invalid operation (ending with operator)
    cy.get('button').contains('5').click()
    cy.get('button').contains('+').click()
    cy.get('button').contains('=').click()
    cy.get('.alert-popup').should('contain', 'Expression cannot end with an operator')
  })

  it('handles consecutive operations', () => {
    cy.get('button').contains('5').click()
    cy.get('button').contains('+').click()
    cy.get('button').contains('5').click()
    cy.get('button').contains('=').click()
    cy.get('button').contains('*').click()
    cy.get('button').contains('2').click()
    cy.get('button').contains('=').click()
    cy.get('input').should('have.value', '20')
  })
})
