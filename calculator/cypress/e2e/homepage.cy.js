describe('Home Page', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('should display the correct title', () => {
    cy.contains('h1', 'This is my calculator!').should('be.visible');
  });

  it('should find the Calculator button and click it', () => {
    cy.get('.nav-button').should('contain', 'Calculator').click();

    cy.url().should('include', '/Calculator');
  });
});
