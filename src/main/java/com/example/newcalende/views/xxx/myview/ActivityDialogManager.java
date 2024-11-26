package com.example.newcalende.views.xxx.myview;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.application.xxx.entity.DailyActivityXX;
import com.example.application.xxx.entity.DailyMoodXX;
import com.example.application.xxx.entity.DailyNoteXX;
import com.example.application.xxx.entity.NamajEntry;
import com.example.application.xxx.enums.Mood;
import com.example.application.xxx.service.DailyActivityServiceXX;
import com.example.application.xxx.service.DailyMoodServiceXX;
import com.example.application.xxx.service.DailyNamajService;
import com.example.application.xxx.service.DailyNoteServiceXX;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ActivityDialogManager {

    private final DailyActivityServiceXX dailyActivityService;
    private final Runnable onActivityUpdate;
    private final DailyMoodServiceXX dailyMoodService;
    private final DailyNamajService dailyNamajService;
    private final DailyNoteServiceXX dailyNoteService;



    public ActivityDialogManager(DailyActivityServiceXX dailyActivityService, Runnable onActivityUpdate,DailyMoodServiceXX dailyMoodService,DailyNamajService dailyNamajService,DailyNoteServiceXX dailyNoteService) {
        this.dailyActivityService = dailyActivityService;
        this.onActivityUpdate = onActivityUpdate;
        this.dailyMoodService=dailyMoodService;
        this.dailyNamajService =dailyNamajService;
        this.dailyNoteService =dailyNoteService;
    }

    // Method to open the dialog for adding daily activities starting from today
    public void openAddDailyActivityDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Form Layout
        FormLayout formLayout = new FormLayout();

        // Unique activities set
        Set<String> existingActivitySet = new HashSet<>();
        Set<String> uniqueActivities = new HashSet<>();

        // Fetch existing activities from the database
        List<DailyActivityXX> existingActivities = dailyActivityService.findAll();

        // Create checkboxes for unique existing activities
        for (DailyActivityXX activity : existingActivities) {
            String activityDescription = activity.getDescription().toLowerCase(); // To handle case sensitivity
            if (!uniqueActivities.contains(activityDescription)) {
                uniqueActivities.add(activityDescription); // Add to unique activities set
                Checkbox activityCheckbox = new Checkbox(activity.getDescription());
                activityCheckbox.addValueChangeListener(event -> {
                    if (event.getValue()) {
                        existingActivitySet.add(activityDescription); // Add to selected set
                    } else {
                        existingActivitySet.remove(activityDescription); // Remove from selected set
                    }
                });
                formLayout.add(activityCheckbox);
            }
        }

        TextField newActivityField = new TextField("Add New Activity (optional)");
        formLayout.add(newActivityField);

        // DatePickers for selecting start and end dates
        DatePicker startDatePicker = new DatePicker("Start Date");
        startDatePicker.setMin(LocalDate.now()); // Start from today
        formLayout.add(startDatePicker);

        DatePicker endDatePicker = new DatePicker("End Date");
        endDatePicker.setMin(LocalDate.now()); // Start from today
        endDatePicker.setMax(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())); // End of current month
        formLayout.add(endDatePicker);

        // Buttons
        Button saveButton = new Button("Save", event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null) {
                Notification.show("Please select both start and end dates", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (endDate.isBefore(startDate)) {
                Notification.show("End date must be after start date", 3000, Notification.Position.MIDDLE);
                return;
            }

            boolean activityAdded = false;

            // Save the selected existing activities
            for (String activity : existingActivitySet) {
                boolean isActivityAdded = saveActivityForSelectedDays(startDate, endDate, activity);
                if (isActivityAdded) {
                    activityAdded = true;
                } else {
                    Notification.show("Activity '" + activity + "' already exists for the selected dates.", 3000, Notification.Position.MIDDLE);
                }
            }

            // Check and save the new activity if it's not empty and unique
            String newActivity = newActivityField.getValue().trim();
            if (!newActivity.isEmpty()) {
                String newActivityLower = newActivity.toLowerCase(); // Convert to lower case
                if (!uniqueActivities.contains(newActivityLower)) { // Check for uniqueness
                    saveActivityForSelectedDays(startDate, endDate, newActivity);
                    uniqueActivities.add(newActivityLower); // Add to the existing set
                    activityAdded = true;
                } else {
                    Notification.show("Activity '" + newActivity + "' already exists.", 3000, Notification.Position.MIDDLE);
                }
            }

            if (activityAdded) {
                dialog.close();
                Notification.show("Activities added successfully", 3000, Notification.Position.BOTTOM_START);
                onActivityUpdate.run(); // Refresh the calendar
            } else {
                Notification.show("No new activities were added.", 3000, Notification.Position.BOTTOM_START);
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, saveButton, cancelButton);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private boolean saveActivityForSelectedDays(LocalDate startDate, LocalDate endDate, String activityDescription) {
        boolean isActivityAdded = false;
        // Save the activity for all days from startDate to endDate
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!dailyActivityService.existsByDateAndDescription(date, activityDescription)) {
                DailyActivityXX activity = new DailyActivityXX();
                activity.setDate(date);
                activity.setDescription(activityDescription);
                activity.setComplete(false); // Set default to false
                dailyActivityService.save(activity);
                isActivityAdded = true; // Mark that at least one activity was added
            }
        }
        return isActivityAdded; // Return true if any activity was added
    }

    public void openDeleteDailyActivityDialog() {
        Dialog dialog = new Dialog(); 
        dialog.setWidth("400px");

        // Date Pickers for selecting the start and end dates
        DatePicker startDatePicker = new DatePicker("Select Start Date");
        DatePicker endDatePicker = new DatePicker("Select End Date");

        // Layout for existing activities
        VerticalLayout activityCheckboxLayout = new VerticalLayout();
        List<DailyActivityXX> existingActivities = dailyActivityService.findAll();

        // Set to keep track of unique activity descriptions
        Set<String> uniqueActivities = new HashSet<>();

        // Add checkboxes for each unique existing activity
        for (DailyActivityXX activity : existingActivities) {
            if (uniqueActivities.add(activity.getDescription().toLowerCase())) {
                Checkbox activityCheckbox = new Checkbox(activity.getDescription());
                activityCheckbox.setValue(false); // Initially unchecked
                activityCheckbox.setLabel(activity.getDescription());
                activityCheckboxLayout.add(activityCheckbox);
            }
        }

        // Button for deleting selected activities
        Button deleteButton = new Button("Delete Selected", event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            boolean activityDeleted = false;

            if (startDate != null && endDate != null) {
                for (Checkbox checkbox : activityCheckboxLayout.getChildren().map(Checkbox.class::cast).toList()) {
                    if (checkbox.getValue()) {
                        String description = checkbox.getLabel();
                        // Delete the activities for the selected date range
                        dailyActivityService.deleteActivitiesInRange(description, startDate, endDate);
                        activityDeleted = true;
                    }
                }
            } else {
                Notification.show("Please select both start and end dates", 3000, Notification.Position.MIDDLE);
            }

            if (activityDeleted) {
                Notification.show("Selected activities deleted successfully", 3000, Notification.Position.BOTTOM_START);
            } else {
                Notification.show("No activities were deleted", 3000, Notification.Position.BOTTOM_START);
            }

            dialog.close();
            onActivityUpdate.run();
        });

        // Button for cancelling the operation
        Button cancelButton = new Button("Cancel", event -> dialog.close());

        // Main layout for the dialog
        VerticalLayout dialogLayout = new VerticalLayout(startDatePicker, endDatePicker, activityCheckboxLayout, deleteButton, cancelButton);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        dialog.open();
    }

    

   


   
    private void updateActivityLabelColor(Checkbox checkbox, Label label) {
        if (checkbox.getValue()) {
            label.getStyle().set("color", "green"); // Green for completed
            onActivityUpdate.run();
        } else {
            label.getStyle().set("color", "red"); // Red for not completed
            onActivityUpdate.run();
        }
    }

    
    public void openUpdateActivityDialog1(LocalDate date) {
        Dialog dialog = new Dialog();
        dialog.setWidth("50%");
        dialog.setHeight("57%");

        LocalDate currentDate = LocalDate.now();

        // Ensure the date is today's date
        if (!date.isEqual(currentDate)) {
            Notification.show("You can only update activities for today.", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Custom header layout
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setPadding(true);
        headerLayout.getStyle()
            .set("background-color", "#f1f3f4")  // Light grey background
            .set("padding", "10px")
            .set("font-size", "18px")
            .set("color", "#333")                // Dark text for contrast
            .set("border-bottom", "1px solid #ddd");

        // Add header title
        Span headerTitle = new Span("Update Daily Activity, Mood, and Namaj Tracker");
        headerTitle.getStyle().set("font-weight", "bold");

       // headerLayout.add(headerTitle);
        
        // Add the custom header to the dialog
       // dialog.add(headerLayout);
        
        dialog.setHeaderTitle("Update Daily Activity, Mood, and Namaj Tracker");

        // Create TabSheet with tabs for Activity, Mood, and Namaj Tracker
        TabSheet tabSheet = new TabSheet();
        tabSheet.getElement().getStyle()
                .set("border-bottom", "1px solid #ddd")
                .set("padding", "8px 0");

        // Tabs
        Tab activityTab = new Tab("UPDATE ACTIVITY");
        Tab moodTab = new Tab("UPDATE MOOD");
        Tab namajTab = new Tab("NAMAJ TRACKER");
        Tab dailyViewTab = new Tab("DAILY VIEW TRACKER");

        // Layouts for each tab
        Button saveButton = new Button();
        VerticalLayout activityLayout = createActivityTab(date, dialog);
        VerticalLayout moodLayout = createMoodTab(date, dialog,saveButton);
        VerticalLayout namajLayout = createNamajTab(date, dialog,saveButton);
        VerticalLayout dailyViewLayout = createDailyViewTab(date, dialog);

        // Add the tabs and corresponding layouts to the TabSheet
        tabSheet.add(activityTab, activityLayout);
        tabSheet.add(moodTab, moodLayout);
        tabSheet.add(namajTab, namajLayout);
        tabSheet.add(dailyViewTab, dailyViewLayout);
        

        // Add the TabSheet to the dialog
        dialog.add(tabSheet);

        dialog.open();
    }


    private VerticalLayout createActivityTab(LocalDate date, Dialog dialog) {
        VerticalLayout activityLayout = new VerticalLayout();
        FormLayout activityFormLayout = new FormLayout();

        // Fetch existing activities for the selected date
        List<DailyActivityXX> activities = dailyActivityService.findByDate(date);

        // If there are no activities, show a notification
        if (activities.isEmpty()) {
            Notification.show("No activities found for the selected date.", 3000, Notification.Position.MIDDLE);
            return activityLayout;
        }

        // Create checkboxes for each activity
        for (DailyActivityXX activity : activities) {
            Checkbox activityCheckbox = new Checkbox(activity.getDescription());
            activityCheckbox.setValue(activity.isComplete()); // Set initial value based on current status
            Label activityLabel = new Label(activity.getDescription());
            updateActivityLabelColor(activityCheckbox, activityLabel);
            activityCheckbox.addValueChangeListener(event -> {
                // Update the activity's status based on the checkbox value
                activity.setComplete(event.getValue());
                dailyActivityService.save(activity); // Save the updated activity

                updateActivityLabelColor(activityCheckbox, activityLabel); // Update color based on new status
                Notification.show("Activity status updated successfully.", 3000, Notification.Position.MIDDLE);
            });
            activityFormLayout.add(activityCheckbox);
        }

        Button cancelButton = new Button("Close", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Button layout
        HorizontalLayout buttonLayout = new HorizontalLayout( cancelButton);
        
        buttonLayout.getElement().getStyle()
        //   .set("border-bottom", "1px solid #ddd")
           .set("display", "flex").set("width", "100%").set("justify-content", "flex-end");
        activityLayout.add(activityFormLayout,buttonLayout);
        return activityLayout;
    }

    private VerticalLayout createMoodTab(LocalDate date, Dialog dialog,Button saveButton) {
        VerticalLayout moodLayout = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        dialog.getFooter().removeAll();
        // Mood Radio Button Group
        RadioButtonGroup<Mood> moodGroup = new RadioButtonGroup<>();
        moodGroup.setLabel("Select Mood");
        moodGroup.setItems(Mood.values());
        moodGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        moodGroup.setItemLabelGenerator(Mood::getLabel); // Assuming getLabel returns a user-friendly name
        formLayout.add(moodGroup);
        moodLayout.add(formLayout);
        dialog.getFooter().removeAll();

        DailyMoodXX existingMood = dailyMoodService.findByDate(date);
        if (existingMood != null) {
            moodGroup.setValue(existingMood.getMood()); // Set existing mood as selected
        }

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        
         saveButton = new Button("Save", event -> {
            Mood selectedMood = moodGroup.getValue();
            if (selectedMood == null) {
                Notification.show("Please select a mood.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (existingMood != null) {
//                // Update existing mood
                existingMood.setMood(selectedMood);
                dailyMoodService.update(existingMood); // Update the mood
                Notification.show("Mood updated successfully!", 3000, Notification.Position.BOTTOM_START);
               onActivityUpdate.run();
                dialog.close();
            } else {
//                // Save new mood
                DailyMoodXX dailyMood = new DailyMoodXX();
                dailyMood.setDate(date);
                dailyMood.setMood(selectedMood);
               dailyMoodService.save(dailyMood); // Save the mood
                Notification.show("Mood added successfully!", 3000, Notification.Position.BOTTOM_START);
                onActivityUpdate.run();
                dialog.close();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST);

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.getElement().getStyle()
           .set("display", "flex").set("width", "100%").set("justify-content", "flex-end");
        
        buttonLayout.add(saveButton, cancelButton);
        moodLayout.add(buttonLayout);

        return moodLayout;
    }



    public VerticalLayout createDailyViewTab(LocalDate date ,Dialog dialog) {
      
    	
    	 int charLimit = 1000;
    	 
    	 VerticalLayout moodLayout = new VerticalLayout();
    	 
    	 FormLayout formLayout = new FormLayout();
       //  formLayout.setWidthFull();
         dialog.getFooter().removeAll();
         
         HorizontalLayout buttonLayout = new HorizontalLayout();
        // buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
//        
		// TextArea for Daily Note Content
		TextArea noteContentArea = new TextArea("Today's Experience");
		noteContentArea.setPlaceholder("Share your full-day experiences, reflections, or observations...");
		//noteContentArea.setWidthFull();
		noteContentArea.setMaxLength(charLimit);
		noteContentArea.setHeight("250px");
		noteContentArea.getStyle().set("font-size", "16px")
		.set("color", "#444");
		noteContentArea.setMinWidth("100%");
		
		noteContentArea.setValueChangeMode(ValueChangeMode.EAGER);
		noteContentArea.addValueChangeListener(e -> {
	            e.getSource()
	                    .setHelperText(e.getValue().length() + "/" + charLimit);
	        });
		//noteContentArea.getStyle().set("display", "flex");
		//noteContentArea.getStyle().set("width", "-webkit-fill-available");
		formLayout.add(noteContentArea);
		 moodLayout.add(formLayout);
		Button saveButton = new Button("Save", event -> {
			String content = noteContentArea.getValue();
			if (content == null || content.trim().isEmpty()) {
				Notification.show("Daily note cannot be empty", 3000, Notification.Position.MIDDLE);
			} else {
				try {
					DailyNoteXX dailyNote = new DailyNoteXX();
					dailyNote.setDate(date);
					dailyNote.setContent(content);
					dailyNote.setDone(false); // Default to not done
					dailyNoteService.save(dailyNote);
					dialog.close();
					//displayMonthCalendar(date);
					Notification.show("Daily note added successfully!", 3000, Notification.Position.TOP_CENTER);
				} catch (Exception e) {
					Notification.show("An error occurred while saving the daily note", 3000,
							Notification.Position.MIDDLE);
					e.printStackTrace();
				}
			}
		});
		 saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
	                ButtonVariant.LUMO_CONTRAST);

	        Button cancelButton = new Button("Cancel", event -> dialog.close());
	        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
	        buttonLayout.getElement().getStyle()
	           .set("display", "flex").set("width", "100%").set("justify-content", "flex-end");
		// Buttons Layout
	        buttonLayout.add(saveButton, cancelButton);
	        moodLayout.add(buttonLayout); // Align buttons to stretch the full width
		return moodLayout;
    }

    
    private VerticalLayout createNamajTab(LocalDate date, Dialog dialog, Button saveButton) {
        VerticalLayout namajLayout = new VerticalLayout();
       // namajLayout.setPadding(true);
        namajLayout.setSpacing(true);
        namajLayout.getElement().getStyle()
     //   .set("border-bottom", "1px solid #ddd")
        .set("padding", "0px 0");
        

        // Dropdown for selecting Namaz
        ComboBox<String> namazComboBox = new ComboBox<>("Select Namaz");
        namazComboBox.setItems("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha", "Tahajjud");
        namazComboBox.setPlaceholder("Choose a Namaz");
        
        namazComboBox.getElement().getStyle()
        //   .set("border-bottom", "1px solid #ddd")
           .set("padding", "0px 0");

        // Layout for selected Namaz options
        HorizontalLayout optionsLayout = new HorizontalLayout();

        // Radio buttons for performed with Jamaat or without
        RadioButtonGroup<String> jamaatGroup = new RadioButtonGroup<>();
        jamaatGroup.setLabel("Was this performed with Jamaat?");
        jamaatGroup.setItems("With Jamaat", "Without Jamaat");
        jamaatGroup.setValue("Without Jamaat"); // Default to "Without Jamaat"
        optionsLayout.add(jamaatGroup);

        // Radio buttons for missed
        RadioButtonGroup<String> missedGroup = new RadioButtonGroup<>();
        missedGroup.setLabel("Was this missed?");
        missedGroup.setItems("Yes", "No");
        missedGroup.setValue("Yes"); // Default to "No"
        optionsLayout.add(missedGroup);

        // Radio buttons for Kaza
        RadioButtonGroup<String> kazaGroup = new RadioButtonGroup<>();
        kazaGroup.setLabel("Kaza Status");
        kazaGroup.setItems("Kaza Done", "Kaza Not Done");
        kazaGroup.setEnabled(false); // Disable by default
        optionsLayout.add(kazaGroup);

        // Add listener to handle missed selection
        missedGroup.addValueChangeListener(event -> {
            boolean isMissed = event.getValue().equals("Yes");
            jamaatGroup.setEnabled(!isMissed); // Disable Jamaat options if missed
            kazaGroup.setEnabled(isMissed); // Enable Kaza options only if missed
            if (!isMissed) {
                kazaGroup.clear(); // Clear Kaza selection if not missed
                
            }
        });
        
        if(missedGroup.getValue().equalsIgnoreCase("YES")) {
        	jamaatGroup.setEnabled(false);
        	 kazaGroup.setEnabled(true);
        }
       // saveButton.setText("SAVE NAMAJ");
        // Add the dropdown and options to the layout
        namajLayout.add(namazComboBox, optionsLayout);

        // Save button
         saveButton = new Button("Save/Update", event -> {
            String selectedNamaz = namazComboBox.getValue();
            String jamaatStatus = jamaatGroup.getValue();
            String missed = missedGroup.getValue(); // Use "Yes" or "No"
            String kazaStatus = kazaGroup.getValue(); // Get Kaza selection

            // Validation
            if (selectedNamaz == null || selectedNamaz.isEmpty()) {
                Notification.show("Please select a Namaz.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (missed.equals("Yes") && !jamaatGroup.isEnabled()) {
                // Jamaat option is irrelevant if missed
                jamaatStatus = "N/A";
            }

            // Check if an entry already exists for the selected Namaz and date
            Optional<NamajEntry> existingEntryOpt = dailyNamajService.findByDateAndType(date, selectedNamaz);
            NamajEntry entry;
            if (existingEntryOpt.isPresent()) {
                // Update existing entry
                entry = existingEntryOpt.get();
                entry.setJamatType(jamaatStatus);
                entry.setMissed(missed); // Set missed to "Yes" or "No"
                missedGroup.setValue(existingEntryOpt.get().getMissed()); 
                
                if(kazaGroup.isEnabled()) {
                	 entry.setKaza(kazaStatus);
                }else{
                	 entry.setKaza("KAZA NOT REQUIRED");
                }
              // Set Kaza based on selection
                dailyNamajService.update(entry);
                Notification.show("Namaz Tracker updated successfully!", 3000, Notification.Position.BOTTOM_START);
                onActivityUpdate.run();
            } else {
                // Create new entry
                entry = new NamajEntry();
                entry.setNamazDate(date);
                entry.setNamazType(selectedNamaz);
                entry.setJamatType(jamaatStatus);
                entry.setMissed(missed); // Set missed to "Yes" or "No"
                if(kazaGroup.isEnabled()) {
               	 entry.setKaza(kazaStatus);
               }else{
               	 entry.setKaza("KAZA NOT REQUIRED");
               } // Set Kaza based on selection
                dailyNamajService.update(entry);
                onActivityUpdate.run();
                Notification.show("Namaz Tracker added successfully!", 3000, Notification.Position.BOTTOM_START);
            }

            dialog.close();
        });
        
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST);

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Button layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        
        buttonLayout.getElement().getStyle()
        //   .set("border-bottom", "1px solid #ddd")
           .set("display", "flex").set("width", "100%").set("justify-content", "flex-end");
        
       // dialog.getFooter().add(buttonLayout);
        namajLayout.add(buttonLayout);
        

        // If updating an existing entry, populate the fields
        namazComboBox.addValueChangeListener(event -> {
        	
            String selectedNamaz = event.getValue();
            if (selectedNamaz != null && !selectedNamaz.isEmpty()) {
                // Fetch existing entry for the selected Namaz and date
                Optional<NamajEntry> existingEntryOptInner = dailyNamajService.findByDateAndType(date, selectedNamaz);
                if (existingEntryOptInner.isPresent()) {
                    NamajEntry existingEntry = existingEntryOptInner.get();
                    jamaatGroup.setValue(existingEntry.getJamatType());
                    missedGroup.setValue(existingEntry.getMissed()); // Directly set the missed value ("Yes" or "No")
                    kazaGroup.setValue(existingEntry.getKaza()); // Set Kaza status
                } else {
                    // Reset to default values if no entry exists
                    jamaatGroup.setValue("Without Jamaat");
                    missedGroup.setValue("No");
                    kazaGroup.clear(); // Clear Kaza selection if no entry exists
                }
            }
        });

        return namajLayout;
    }

    
   
}