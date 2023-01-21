import {selectors} from '@geogebra/web-test-harness/selectors'
/*global cy*/

describe('Mindmap tool test', () => {
    beforeEach(() => {
        cy.visit('notes.html');
        cy.get("body.application");
    });

    afterEach(cy.setSaved);

    it("Mindmap tool should insert mindmap", () => {
        selectors.mediaPanelButton.click();
        selectors.mindmapButton.click();
        selectors.euclidianView.get()
            .mouseEvent('down', 100, 100)
            .mouseEvent('up', 100, 100);

        cy.get('.mowWidget').should('be.visible');
    });
});