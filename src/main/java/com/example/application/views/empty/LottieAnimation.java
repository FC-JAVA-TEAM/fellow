package com.example.application.views.empty;


import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LottieAnimation extends VerticalLayout {

    public LottieAnimation(String src) {
        // Create the Lottie Player HTML
        String lottiePlayerHtml = String.format(
            "<lottie-player src='%s' background='transparent' speed='1' loop autoplay style='width: 100%%; height: 100%%; position: absolute; top: 0; left: 0; z-index: -1;'></lottie-player>",
            src
        );

        Html lottiePlayer = new Html(lottiePlayerHtml);
        add(lottiePlayer);

        // Ensure the layout does not affect other components
        setWidthFull();
        setHeightFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        getStyle().set("position", "relative");
    }
}
