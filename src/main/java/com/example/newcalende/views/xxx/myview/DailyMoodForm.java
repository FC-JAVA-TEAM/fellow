package com.example.newcalende.views.xxx.myview;

import java.time.LocalDate;

import com.example.application.xxx.entity.DailyMoodXX;
import com.example.application.xxx.enums.Mood;
import com.example.application.xxx.service.DailyMoodServiceXX;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

public class DailyMoodForm {

    private final DailyMoodServiceXX dailyMoodService;
    private final Runnable onMoodUpdate; // Runnable to refresh UI

    public DailyMoodForm(DailyMoodServiceXX dailyMoodService, Runnable onMoodUpdate) {
        this.dailyMoodService = dailyMoodService;
        this.onMoodUpdate = onMoodUpdate; // Set the runnable
    }

    public void openAddDailyMoodDialog(LocalDate date) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeight("400px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setPadding(true);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Header
        H3 header = new H3("Add Mood for: " + date);
        mainLayout.add(header);

        // Form Layout
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();

        // Mood Radio Button Group
        RadioButtonGroup<Mood> moodGroup = new RadioButtonGroup<>();
        moodGroup.setLabel("Select Mood");
        moodGroup.setItems(Mood.values());
        moodGroup.setItemLabelGenerator(Mood::getLabel); // Assuming getLabel returns a user-friendly name
        formLayout.add(moodGroup);

        // Check if a mood already exists for the date
        DailyMoodXX existingMood = dailyMoodService.findByDate(date);
        if (existingMood != null) {
            moodGroup.setValue(existingMood.getMood()); // Set existing mood as selected
        }

        mainLayout.add(formLayout);

        // Footer with buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button saveButton = new Button("Save", event -> {
            Mood selectedMood = moodGroup.getValue();
            if (selectedMood == null) {
                Notification.show("Please select a mood.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (existingMood != null) {
                // Update existing mood
                existingMood.setMood(selectedMood);
                dailyMoodService.update(existingMood); // Update the mood
                Notification.show("Mood updated successfully!", 3000, Notification.Position.BOTTOM_START);
                onMoodUpdate.run();
            } else {
                // Save new mood
                DailyMoodXX dailyMood = new DailyMoodXX();
                dailyMood.setDate(date);
                dailyMood.setMood(selectedMood);
                dailyMoodService.save(dailyMood); // Save the mood
                Notification.show("Mood added successfully!", 3000, Notification.Position.BOTTOM_START);
                onMoodUpdate.run();
                
            }

            dialog.close();
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Add buttons to the layout
        buttonLayout.add(saveButton, cancelButton);
        mainLayout.add(buttonLayout);

        // Add main layout to dialog
        dialog.add(mainLayout);
        dialog.open();
    }
}
