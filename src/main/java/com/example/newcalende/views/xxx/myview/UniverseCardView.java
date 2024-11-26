package com.example.newcalende.views.xxx.myview;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "universe-card") // Define the URL path for this view
@PageTitle("Universe of UI Card")
//@CssImport("./styles/universe-card-styles.css") // Import the custom CSS
@CssImport("./styles/styles.css")
public class UniverseCardView extends VerticalLayout {

    public UniverseCardView() {
        // Create the main card container
        Div card = new Div();
        card.addClassName("card");

        // Create top section
        Div topSection = new Div();
        topSection.addClassName("top-section");

        // Add the border div in the top section
        Div border = new Div();
        border.addClassName("border");
        topSection.add(border);

        // Add logo and social media icons
        Div icons = new Div(); 
        icons.addClassName("icons");

        // Create logo container
        Div logo = new Div();
        logo.addClassName("logo");
        // Here, instead of the SVG, you can use an HTML component or image component
        logo.setText("Logo");
        icons.add(logo);

        // Create social media icons container
        Div socialMedia = new Div();
        socialMedia.addClassName("social-media");
        socialMedia.setText("Icons"); // Placeholder for SVG or icons
        icons.add(socialMedia);

        topSection.add(icons);

        // Create the bottom section
        Div bottomSection = new Div();
        bottomSection.addClassName("bottom-section");

        // Add the title in the bottom section
        Div title = new Div();
        title.addClassName("title");
        title.setText("UNIVERSE OF UI");
        bottomSection.add(title);

        // Create rows and items in the bottom section
        Div row = new Div();
        row.addClassName("row");
        
        // Add individual items in the row
        row.add(createItem("2626", "UI elements"));
        row.add(createItem("100%", "Free for use"));
        row.add(createItem("38,631", "Contributors"));

        bottomSection.add(row);

        // Add both top and bottom sections to the card
        card.add(topSection, bottomSection);

        // Add the card to the main layout
        add(card);

        // Center the card
        setHorizontalComponentAlignment(Alignment.CENTER, card);
    }

    // Helper method to create an item div
    private Div createItem(String bigText, String regularText) {
        Div item = new Div();
        item.addClassName("item");

        Div bigTextDiv = new Div();
        bigTextDiv.addClassName("big-text");
        bigTextDiv.setText(bigText);

        Div regularTextDiv = new Div();
        regularTextDiv.addClassName("regular-text");
        regularTextDiv.setText(regularText);

        item.add(bigTextDiv, regularTextDiv);
        return item;
    }
}
