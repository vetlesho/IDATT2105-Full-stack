describe('Contact Form', () => {
  beforeEach(() => {
    cy.visit('/Contactform');
    cy.contains('Give feedback!').should('be.visible');
  });

  describe('Invalid form input', () => {
    it('should disable submit button if form is invalid', () => {
      cy.get('button').contains('Submit').should('be.disabled');

      cy.get('input[placeholder="Full Name"]').type('feil');
      cy.get('input[placeholder="E-mail"]').type('feilmail');
      cy.get('textarea[placeholder="Feedback"]').type('kort');

      cy.get('body').click(0, 0);

      cy.get('span').contains('This field should be at least 6 characters long').should('be.visible');
      cy.get('span').contains('Value is not a valid email').should('be.visible');
      cy.get('span').contains('This field should be at least 10 characters long').should('be.visible');

      cy.get('button').contains('Submit').should('be.disabled');
    });
  });

  describe('Successful Form Submission', () => {
    it('should submit the form successfully', () => {
      cy.intercept('POST', 'http://localhost:3000/feedback', {
        statusCode: 201,
        body: {
          messages: {
            success: 'From backend: Form submitted successfully.',
          },
        },
      }).as('submitForm');

      cy.get('input[placeholder="Full Name"]').clear()
      cy.get('input[placeholder="Full Name"]').type('Test navn');
      cy.get('input[placeholder="E-mail"]').clear();
      cy.get('input[placeholder="E-mail"]').type('testmail@gmail.com');
      cy.get('textarea[placeholder="Feedback"]').clear();
      cy.get('textarea[placeholder="Feedback"]').type('Dette her burde funke skikkelig bra!');

      cy.get('button').contains('Submit').should('not.be.disabled').click();
      cy.wait('@submitForm');

      cy.contains('From backend: Form submitted successfully.').should('be.visible');
      cy.get('.alert-content button').click();
      cy.get('.alert-popup').should('not.exist');
    });
  });

  describe('Navigation', () => {
    it('should navigate to calculator and back while preserving form data', () => {
      // Mock localStorage and store before starting
      cy.window().then((win) => {
        win.localStorage.setItem('userStore', JSON.stringify({
          name: '',
          email: ''
        }));
      });

      cy.get('input[placeholder="Full Name"]').type('Test User');
      cy.get('input[placeholder="E-mail"]').type('test@example.com');
      cy.get('textarea[placeholder="Feedback"]').type('My message testtest');
      cy.get('button').contains('Submit').should('not.be.disabled').click();
      cy.get('.alert-content button').click();
      cy.get('.alert-popup').should('not.exist');

      cy.get('.nav-link').contains('Calculator').click();
      cy.url().should('include', '/Calculator');

      cy.get('.nav-link').contains('Contact Form').click();
      cy.url().should('include', '/Contactform');

      cy.get('input[placeholder="Full Name"]').should('have.value', 'Test User');
      cy.get('input[placeholder="E-mail"]').should('have.value', 'test@example.com');
    });
  });
});
