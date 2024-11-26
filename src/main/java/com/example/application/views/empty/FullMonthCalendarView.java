package com.example.application.views.empty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.entity.DailyActivity;
import com.example.application.entity.DailyNote;
import com.example.application.entity.Task;
import com.example.application.enums.TaskPriority;
import com.example.application.enums.TaskRecurrence;
import com.example.application.enums.TaskStatus;
import com.example.application.service.DailyActivityService;
import com.example.application.service.DailyNoteService;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("calendar")
@PageTitle("Calendar")
@Menu(icon = "line-awesome/svg/file.svg", order = 1)
@CssImport("./styles/styles.css")
@JsModule("https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js")

public class FullMonthCalendarView extends Div {

	private FlexLayout calendarLayout;
	private TaskService taskService;
	private DailyActivityService dailyActivityService;
	private DailyNoteService dailyNoteService;
	private LocalDate currentMonth;

	@Autowired
	public FullMonthCalendarView(TaskService taskService, DailyActivityService dailyActivityService,
			DailyNoteService dailyNoteService) {
		this.taskService = taskService;
		this.dailyActivityService = dailyActivityService;
		this.dailyNoteService = dailyNoteService;
		;
		this.currentMonth = LocalDate.now().withDayOfMonth(1);

		// Create navigation buttons
		Button prevMonthButton = new Button("Previous Month", e -> navigateToMonth(-1));
		Button nextMonthButton = new Button("Next Month", e -> navigateToMonth(1));
		Label monthYearLabel = new Label();
		updateMonthYearLabel(monthYearLabel);

		// Set up the header layout
		HorizontalLayout headerLayout = new HorizontalLayout(prevMonthButton, monthYearLabel, nextMonthButton);
		headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		headerLayout.setWidthFull();
		headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		// Setup layout to hold the full month calendar in a grid style
		calendarLayout = new FlexLayout();
		calendarLayout.setWidth("100%");
		calendarLayout.getStyle().set("display", "grid");
		calendarLayout.getStyle().set("grid-template-columns", "repeat(7, 1fr)");
		calendarLayout.getStyle().set("gap", "5px");
		// After initializing calendarLayout
		calendarLayout.addClassName("calendar-background");

		Div calendarContainer = new Div();
		calendarContainer.setWidthFull();
		calendarContainer.addClassName("calendar-container");
		calendarContainer.add(calendarLayout);
		// Add components to the main layout
		add(headerLayout, calendarContainer);

		// Display the initial calendar
		displayMonthCalendar(currentMonth);
	}

	private void navigateToMonth(int monthsToAdd) {
		currentMonth = currentMonth.plusMonths(monthsToAdd);
		updateMonthYearLabel(
				(Label) ((HorizontalLayout) getChildren().filter(c -> c instanceof HorizontalLayout).findFirst().get())
						.getComponentAt(1));
		displayMonthCalendar(currentMonth);
	}

	private void updateMonthYearLabel(Label label) {
		String monthYear = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
				+ currentMonth.getYear();
		label.setText(monthYear);
	}

	private void addTimeBasedGif(Div dayCell) {
	    LocalTime currentTime = LocalTime.now();
	    String gifPath;

	    if (currentTime.isAfter(LocalTime.of(5, 0)) && currentTime.isBefore(LocalTime.NOON)) {
	        // Morning: 5 AM - 12 PM
	        gifPath = "images/morning 1.json"; // Replace with your Lottie JSON file path or URL
	      //  gifPath = "images/afternoon.json"; // Replace with your Lottie JSON file path or URL
	      //  gifPath = "images/moon.json"; // Replace with your Lottie JSON file path or URL
	    } else if (currentTime.isAfter(LocalTime.NOON) && currentTime.isBefore(LocalTime.of(18, 0))) {
	        // Afternoon: 12 PM - 6 PM
	        gifPath = "images/afternoon.json"; // Replace with your Lottie JSON file path or URL
	    } else {
	        // Night: 6 PM - 5 AM
	        gifPath = "images/moon.json"; // Replace with your Lottie JSON file path or URL
	    }

	    // Create the Lottie Player
//	    String lottieAnimation = String.format(
//	        "<lottie-player src='%s' background='transparent' speed='1' style='position: absolute; top: 0; left: 0; width: 100%%; height: 100%%; ' loop autoplay></lottie-player>",
//	        gifPath
//	    );
	    
	    String lottieAnimation = String.format(
	            "<lottie-player src='%s'  speed='1' " +
	            "style='position: absolute; top: 0; left: 0; width: 100%%; height: 100%%; " +
	            " pointer-events: none; opacity: 0.3;' loop autoplay></lottie-player>",
	            gifPath
	        );
	    Html lottie = new Html(lottieAnimation);
	    dayCell.add(lottie);
	}


	private void displayMonthCalendar(LocalDate month) {
		// Clear the existing components
		calendarLayout.removeAll();

		// Fetch all tasks for the current month
		List<Task> tasksForMonth = taskService.findByMonth(month);
		Map<LocalDate, List<Task>> tasksByDate = tasksForMonth.stream().collect(Collectors.groupingBy(Task::getDate));

		// Add headers for days of the week
		String[] weekdays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		for (String dayName : weekdays) {
			Div headerCell = new Div();
			headerCell.setText(dayName);
			headerCell.getStyle().set("font-weight", "bold");
			headerCell.getStyle().set("text-align", "center");
			calendarLayout.add(headerCell);
		}

		// Determine the day of the week the month starts on (1=Monday, 7=Sunday)
		int firstDayOfWeek = month.withDayOfMonth(1).getDayOfWeek().getValue(); // 1=Monday, 7=Sunday
		int emptyCells = firstDayOfWeek - 1; // Calculate the number of empty cells

		// Add empty cells for days before the first day
		for (int i = 0; i < emptyCells; i++) {
			Div emptyCell = new Div();
			calendarLayout.add(emptyCell);
		}

		// Fill in the days of the month
		for (int day = 1; day <= month.lengthOfMonth(); day++) {
			LocalDate dayDate = month.withDayOfMonth(day);

			Div dayCell = new Div();
			dayCell.addClassName("calendar-cell");
			dayCell.getStyle().set("padding", "5px");
			dayCell.getStyle().set("border", "1px solid lightgray");
			dayCell.getStyle().set("min-height", "100px");
		//	dayCell.getStyle().set("background-color", "#f9f9f9"); // Light background color
			dayCell.getStyle().set("display", "flex");
			dayCell.getStyle().set("flex-direction", "column");
			dayCell.addClassName("calendar-cell");
			dayCell.addClassName("fade-in");

			// Highlight today's date

			if (dayDate.isBefore(LocalDate.now())) {
				// Previous days (light red)
			///	dayCell.getStyle().set("background-color", "#ffe6e6"); // Light red
			//	dayCell.addClassName("past-day");
			} else if (dayDate.equals(LocalDate.now())) {
				// Current day (light green)
				//dayCell.getStyle().set("background-color", "#e6ffe6"); // Light green
				dayCell.addClassName("today");
				
		
				
				
				
				
				  addTimeBasedGif(dayCell);
			} else {
				// Future days (default or you can set a different color)
				dayCell.getStyle().set("background-color", "#f9f9f9"); // Light gray background
			}
			// Container for day number and progress bar
			VerticalLayout headerContainer = new VerticalLayout();
			headerContainer.setPadding(false);
			headerContainer.setSpacing(false);
			headerContainer.setWidthFull();
			headerContainer.getStyle().set("align-items", "center");

			// Use Span for day number
			Span dayNumber = new Span(String.valueOf(day));
			dayNumber.getStyle().set("font-weight", "bold");

			// Add any existing tasks for this day
			List<Task> tasks = tasksByDate.get(dayDate);

			List<DailyActivity> activities = dailyActivityService.findByDate(dayDate);
			VerticalLayout activitiesContainer = new VerticalLayout();
			activitiesContainer.setPadding(false);
			activitiesContainer.setSpacing(false);

			if (activities != null && !activities.isEmpty()) {
				for (DailyActivity activity : activities) {
					Div activityCircle = new Div();
					activityCircle.setWidth("12px"); // Circle diameter
					activityCircle.setHeight("12px");
					activityCircle.getStyle().set("border-radius", "50%"); // Make it a circle
					activityCircle.getStyle().set("margin-right", "5px");
					Label activityLabel = new Label(activity.getDescription());
					activityLabel.getStyle().set("font-size", "10px");

					// Style based on completion status
					if (activity.isComplete()) {
						activityLabel.getStyle().set("color", "green"); // Completed in green
						activityCircle.getStyle().set("background-color", "green");
					} else {
						activityLabel.getStyle().set("color", "red"); // Not completed in red
						activityCircle.getStyle().set("background-color", "red");
					}
					HorizontalLayout activityLayout = new HorizontalLayout(activityCircle, activityLabel);
					activitiesContainer.add(activityLayout);
				}
			}
			dayCell.add(activitiesContainer);
			int totalTasks = 0;
			int completedTasks = 0;

			if (tasks != null) {
				totalTasks = tasks.size();
				for (Task task : tasks) {
					// Count completed tasks
					if (task.getStatus() == TaskStatus.COMPLETED) {
						completedTasks++;
					}
				}
			}

			// Calculate and add progress bar
			ProgressBar progressBar = null;
			if (totalTasks > 0) {
				double progress = (double) completedTasks / totalTasks;
				progressBar = new ProgressBar();
				progressBar.setValue(progress);
				progressBar.setWidthFull();
				progressBar.getElement().setAttribute("title",
						String.format("%d of %d tasks completed", completedTasks, totalTasks));
				progressBar.getStyle().set("transition", "all 0.5s ease");

				// Assign CSS class based on progress
				progressBar.addClassName(getProgressBarColorClass(progress));
			} else {
				// If no tasks, show an empty progress bar
				progressBar = new ProgressBar();
				progressBar.setValue(0);
				progressBar.setWidthFull();
				progressBar.getStyle().set("transition", "all 0.5s ease");
				progressBar.getElement().setAttribute("title", "No tasks");
				progressBar.addClassName("progress-neutral");
			}

			// Position progress bar at the top
			headerContainer.add(progressBar, dayNumber);

			// Container for tasks
			VerticalLayout tasksContainer = new VerticalLayout();
			tasksContainer.setPadding(false);
			tasksContainer.setSpacing(false);
			tasksContainer.getStyle().set("flex-grow", "1"); // Allow tasksContainer to grow

			if (tasks != null && !tasks.isEmpty()) {
				for (Task task : tasks) {
					String timeText = "";

					// Handle null priority
					TaskPriority priority = task.getPriority() != null ? task.getPriority() : TaskPriority.MEDIUM;
					String priorityText = "[" + priority.name().charAt(0) + "] "; // e.g., [H], [M], [L]
					String taskText = priorityText + timeText + task.getDescription();

					Label taskLabel = new Label(taskText);
					taskLabel.getStyle().set("font-size", "12px");

					// Style based on priority
					if (priority == TaskPriority.HIGH) {
						taskLabel.getStyle().set("color", "red");
					} else if (priority == TaskPriority.MEDIUM) {
						taskLabel.getStyle().set("color", "orange");
					} else if (priority == TaskPriority.LOW) {
						taskLabel.getStyle().set("color", "green");
					}

					// Style based on status
					if (task.getStatus() == TaskStatus.COMPLETED) {
						taskLabel.getStyle().set("text-decoration", "line-through");
						taskLabel.getStyle().set("color", "gray");
					} else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
						taskLabel.getStyle().set("font-weight", "bold");
					} else if (task.getStatus() == TaskStatus.ON_HOLD) {
						taskLabel.getStyle().set("color", "orange");
					} else if (task.getStatus() == TaskStatus.CANCELLED) {
						taskLabel.getStyle().set("color", "red");
					} else if (task.getStatus() == TaskStatus.NOT_COMPLETED) {
						taskLabel.getStyle().set("color", "red");
					}

					tasksContainer.add(taskLabel);
				}
			}

			// Add components to dayCell
			dayCell.add(headerContainer);

			// Add tasks to dayCell
			if ((tasks != null && !tasks.isEmpty())) {
				dayCell.add(tasksContainer);
			}

			// Add click listener to dayCell
			dayCell.addClickListener(e -> openDateDetailsDialog(dayDate));

			calendarLayout.add(dayCell);
		}

		// Add empty cells to fill the last row if necessary
		int totalCells = calendarLayout.getComponentCount();
		int cellsToAdd = 7 - (totalCells % 7);
		if (cellsToAdd < 7 && cellsToAdd != 0) {
			for (int i = 0; i < cellsToAdd; i++) {
				Div emptyCell = new Div();
				calendarLayout.add(emptyCell);
			}
		}
	}

	private String getProgressBarColorClass(double progress) {
		if (progress >= 1.0) {
			return "progress-green";
		} else if (progress >= 0.5) {
			return "progress-orange";
		} else {
			return "progress-red";
		}
	}

	private void openUpdateActivityDialog(LocalDate selectedDate) {
		// Prevent updating activities for future dates
		if (selectedDate.isAfter(LocalDate.now())) {
			Notification.show("Cannot update activities for future dates", 3000, Notification.Position.MIDDLE);
			return;
		}

		Dialog updateActivityDialog = new Dialog();

		// Dialog animation and styling
		updateActivityDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out")
				.set("border-radius", "10px").set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)")
				.set("background-color", "#f9f9f9");

		// Title with Icon
		HorizontalLayout titleLayout = new HorizontalLayout();
		Icon updateActivityIcon = VaadinIcon.EDIT.create();
		updateActivityIcon.getStyle().set("color", "#FF9800"); // Orange color
		Label titleLabel = new Label("Update Activities for: " + selectedDate);
		titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold").set("color", "white");

		// Title layout with gradient background
		titleLayout.add(updateActivityIcon, titleLabel);
		titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		titleLayout.setWidthFull();
		titleLayout.setSpacing(false);
		titleLayout.getStyle().set("background", "linear-gradient(90deg, #1e3c72, #2a5298)").set("padding", "15px")
				.set("border-top-left-radius", "10px").set("border-top-right-radius", "10px");

		// Form Layout
		VerticalLayout activitiesContainer = new VerticalLayout();
		activitiesContainer.setSpacing(true);
		activitiesContainer.setPadding(false);

		// Fetch existing activities for the selected date
		List<DailyActivity> existingActivities = dailyActivityService.findByDate(selectedDate);
		if (existingActivities.isEmpty()) {
			Label noActivitiesLabel = new Label("No activities found for this date.");
			activitiesContainer.add(noActivitiesLabel);
		} else {
			for (DailyActivity activity : existingActivities) {
				// Create a layout for each activity
				HorizontalLayout activityLayout = new HorizontalLayout();
				TextField activityDescriptionField = new TextField();
				activityDescriptionField.setValue(activity.getDescription());
				activityDescriptionField.setWidthFull();

				// Checkbox for completion status
				Checkbox completionCheckbox = new Checkbox("Completed");
				completionCheckbox.setValue(activity.isComplete());

				// Add change listener to update activity completion status
				completionCheckbox.addValueChangeListener(event -> {
					activity.setComplete(completionCheckbox.getValue());
				});

				// Add the activity components to the layout
				activityLayout.add(activityDescriptionField, completionCheckbox);
				activitiesContainer.add(activityLayout);

				// Listener to update the activity when description changes
				activityDescriptionField.addValueChangeListener(event -> {
					activity.setDescription(event.getValue());
				});
			}
		}

		// Save Button
		Button saveButton = new Button("Save", event -> {
			try {
				// Save all activities to the service
				for (DailyActivity activity : existingActivities) {
					dailyActivityService.update(activity); // Assuming an update method exists
				}
				updateActivityDialog.close();
				Notification.show("Activities updated successfully!", 3000, Notification.Position.TOP_CENTER);
			} catch (Exception e) {
				Notification.show("An error occurred while updating the activities", 3000,
						Notification.Position.MIDDLE);
				e.printStackTrace();
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.getStyle().set("background-color", "#4CAF50") // Green for success
				.set("color", "white").set("border-radius", "8px").set("width", "100%").set("margin-top", "20px");

		// Cancel Button
		Button cancelButton = new Button("Cancel", event -> updateActivityDialog.close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		cancelButton.getStyle().set("background-color", "#F44336") // Red for cancel
				.set("color", "white").set("border-radius", "8px").set("width", "100%");

		// Buttons Layout
		VerticalLayout buttonsLayout = new VerticalLayout(saveButton, cancelButton);
		buttonsLayout.setSpacing(true);
		buttonsLayout.setPadding(false);
		buttonsLayout.setWidthFull();
		buttonsLayout.setAlignItems(Alignment.STRETCH); // Align buttons to stretch the full width

		// Dialog Layout
		VerticalLayout dialogLayout = new VerticalLayout(titleLayout, activitiesContainer, buttonsLayout);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH); // Align content to stretch

		// Add components to dialog
		updateActivityDialog.add(dialogLayout);
		updateActivityDialog.open();
	}

	private void openDateDetailsDialog(LocalDate selectedDate) {
		Dialog dateDialog = new Dialog();

		// Dialog animation (fade-in effect) and rounded corners
		dateDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out").set("border-radius", "10px")
				.set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)").set("background-color", "#f9f9f9");

		// Inject CSS for fadeIn animation
		dateDialog.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJs("const style = document.createElement('style');"
					+ "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';"
					+ "document.head.appendChild(style);");
		});

		// Title with gradient background
		Label titleLabel = new Label("Details for " + selectedDate);
		titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold").set("color", "white");

		// Title layout with gradient background
		HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
		titleLayout.setWidthFull();
		titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		titleLayout.getStyle().set("background", "linear-gradient(90deg, #1e3c72, #2a5298)").set("padding", "15px")
				.set("border-top-left-radius", "10px").set("border-top-right-radius", "10px");

		// Buttons with updated styling
		Button viewTasksButton = new Button("View Tasks", event -> {
			dateDialog.close();
			openViewTasksDialog(selectedDate);
		});
		viewTasksButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		viewTasksButton.getStyle().set("background-color", "#4CAF50").set("color", "white").set("border-radius", "8px");

		Button addTaskButton = new Button("Add Task", event -> {
			dateDialog.close();
			openAddTaskDialog(selectedDate);
		});
		addTaskButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		addTaskButton.getStyle().set("background-color", "#1976D2").set("color", "white").set("border-radius", "8px");

		Button viewActivitiesButton = new Button("Review My Day", event -> {
			dateDialog.close();
			openReviewMyDayDialogBox(selectedDate);
		});
		viewActivitiesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		viewActivitiesButton.getStyle().set("background-color", "#FFC107") // Yellow color for contrast
				.set("color", "white").set("border-radius", "8px");

		Button addActivityButton = new Button("Add My DAY", event -> {
			dateDialog.close();
			openAddReviewMyDay(selectedDate);
		});
		addActivityButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		addActivityButton.getStyle().set("background-color", "#F44336") // Red color for emphasis
				.set("color", "white").set("border-radius", "8px");

		Button closeButton = new Button("Close", event -> dateDialog.close());
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		closeButton.getStyle().set("background-color", "#808080") // Grey color for neutral option
				.set("color", "white").set("border-radius", "8px");

		// New button to add activities for the current month
		Button addActivityForMonthButton = new Button("Add Activity for Month", event -> {
			dateDialog.close();
			openAddActivityForMonthDialog(selectedDate); // Open the new dialog for adding activities for the month
		});
		addActivityForMonthButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		addActivityForMonthButton.getStyle().set("background-color", "#3F51B5") // Indigo color for visibility
				.set("color", "white").set("border-radius", "8px");

		// Button for updating activity
		Button updateActivityButton = new Button("Update Activity", event -> {
			dateDialog.close();
			openUpdateActivityDialog(selectedDate); // Call the existing update activity dialog
		});
		updateActivityButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		updateActivityButton.getStyle().set("background-color", "#FF9800") // Orange color for visibility
				.set("color", "white").set("border-radius", "8px");

		// Layout for buttons
		HorizontalLayout tasksButtonsLayout = new HorizontalLayout(viewTasksButton, addTaskButton);
		tasksButtonsLayout.setSpacing(true);
		tasksButtonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		HorizontalLayout activitiesButtonsLayout = new HorizontalLayout(updateActivityButton, addActivityForMonthButton,
				viewActivitiesButton, addActivityButton);
		activitiesButtonsLayout.setSpacing(true);
		activitiesButtonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		VerticalLayout buttonsLayout = new VerticalLayout(tasksButtonsLayout, activitiesButtonsLayout, closeButton);
		buttonsLayout.setSpacing(true);
		buttonsLayout.setAlignItems(Alignment.CENTER);
		buttonsLayout.getStyle().set("padding-top", "20px");

		// Dialog layout with proper spacing and alignment
		VerticalLayout dialogLayout = new VerticalLayout(titleLayout, buttonsLayout);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH); // Align items to stretch the full width
		dialogLayout.getStyle().set("padding", "20px");

		dateDialog.add(dialogLayout);
		dateDialog.open();
	}

	// New dialog method to add activity for the month
	private void openAddActivityForMonthDialog(LocalDate selectedDate) {
		Dialog activityDialog = new Dialog();

		// Dialog animation (fade-in effect) and rounded corners
		activityDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out").set("border-radius", "10px")
				.set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)").set("background-color", "#f9f9f9");

		// Inject CSS for fadeIn animation
		activityDialog.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJs("const style = document.createElement('style');"
					+ "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';"
					+ "document.head.appendChild(style);");
		});

		// Title with gradient background
		Label titleLabel = new Label("Add Activity for the Month");
		titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold").set("color", "white");

		// Title layout with gradient background
		HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
		titleLayout.setWidthFull();
		titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		titleLayout.getStyle().set("background", "linear-gradient(90deg, #1e3c72, #2a5298)").set("padding", "15px")
				.set("border-top-left-radius", "10px").set("border-top-right-radius", "10px");

		// Form Layout
		FormLayout formLayout = new FormLayout();
		formLayout.setWidthFull();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		// Checkbox group for predefined activities
		CheckboxGroup<String> activityCheckboxGroup = new CheckboxGroup<>();
		activityCheckboxGroup.setLabel("Select Activities");
		activityCheckboxGroup.setItems("Gym", "Reading", "Meditation", "Running", "Work");
		activityCheckboxGroup.setWidthFull();

		// Button to add activities for the month
		Button addActivitiesButton = new Button("Add Activities", event -> {
			Set<String> selectedActivities = activityCheckboxGroup.getValue();
			if (selectedActivities.isEmpty()) {
				// refreshCalendar();
				// getPage().reload();

				// UI.getCurrent().getPage().setLocation(UI.getCurrent().getPage().getLocation().toString());

				// setLocation(UI.getCurrent().getPage().getLocation().toString());

				Notification.show("Please select at least one activity", 3000, Notification.Position.MIDDLE);
				return;
			}

			// Iterate through each day of the month and save the selected activities
			LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
			LocalDate lastDateOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth());

			LocalDate currentDate = selectedDate;
			// LocalDate lastOfMonth =
			// selectedDate.withDayOfMonth(firstOfMonth.lengthOfMonth());

			try {
				while (!currentDate.isAfter(lastDateOfMonth)) {
					for (String activity : selectedActivities) {
						// Check if the activity already exists for the current date
						if (!dailyActivityService.findByDateAndActivity(currentDate,
								activity.equals("Other") ? activity : activity)) {
							DailyActivity dailyActivity = new DailyActivity();
							dailyActivity.setDate(currentDate);
							dailyActivity.setDescription(activity.equals("Other") ? activity : activity);
							dailyActivity.setComplete(false); // Mark as not completed
							dailyActivityService.save(dailyActivity);
						} else {
							Notification.show("Activity '" + activity + "' already exists for " + currentDate, 3000,
									Notification.Position.MIDDLE);
						}
					}
					currentDate = currentDate.plusDays(1); // Move to the next day
				}

				// addActivityDialog.close();
				Notification.show("Activities added for the month!", 3000, Notification.Position.MIDDLE);

			} catch (Exception e) {
				Notification.show("Error adding activities: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
			}
			activityDialog.close();
		});

		// Layout for buttons
		HorizontalLayout buttonLayout = new HorizontalLayout(addActivitiesButton);
		buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

		// Combine layouts
		formLayout.add(activityCheckboxGroup);
		VerticalLayout dialogLayout = new VerticalLayout(titleLayout, formLayout, buttonLayout);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH); // Align items to stretch the full width
		dialogLayout.getStyle().set("padding", "20px");

		activityDialog.add(dialogLayout);
		activityDialog.open();

		// Clear the existing components

	}

	private void refreshCalendar() {
		// Logic to refresh your calendar goes here
		// This might involve re-fetching data from the service and updating the UI
		calendarLayout.getElement().callJsFunction("refresh"); // Example method call
	}
//    private void openDateDetailsDialog(LocalDate selectedDate) {
//        Dialog dateDialog = new Dialog();
//
//        // Dialog animation (fade-in effect) and rounded corners
//        dateDialog.getElement().getStyle()
//            .set("animation", "fadeIn 0.3s ease-out")
//            .set("border-radius", "10px")
//            .set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)")
//            .set("background-color", "#f9f9f9");
//
//        // Inject CSS for fadeIn animation
//        dateDialog.getElement().getNode().runWhenAttached(ui -> {
//            ui.getPage().executeJs(
//                "const style = document.createElement('style');" +
//                "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';" +
//                "document.head.appendChild(style);"
//            );
//        });
//
//        // Title with gradient background
//        Label titleLabel = new Label("Details for " + selectedDate);
//        titleLabel.getStyle()
//            .set("font-size", "24px")
//            .set("font-weight", "bold")
//            .set("color", "white");
//
//        // Title layout with gradient background
//        HorizontalLayout titleLayout = new HorizontalLayout(titleLabel);
//        titleLayout.setWidthFull();
//        titleLayout.setJustifyContentMode(JustifyContentMode.CENTER);
//        titleLayout.getStyle()
//            .set("background", "linear-gradient(90deg, #1e3c72, #2a5298)")
//            .set("padding", "15px")
//            .set("border-top-left-radius", "10px")
//            .set("border-top-right-radius", "10px");
//
//        // Buttons with updated styling
//        Button viewTasksButton = new Button("View Tasks", event -> {
//            dateDialog.close();
//            openViewTasksDialog(selectedDate);
//        });
//        viewTasksButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        viewTasksButton.getStyle()
//            .set("background-color", "#4CAF50")
//            .set("color", "white")
//            .set("border-radius", "8px");
//
//        Button addTaskButton = new Button("Add Task", event -> {
//            dateDialog.close();
//            openAddTaskDialog(selectedDate);
//        });
//        addTaskButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
//        addTaskButton.getStyle()
//            .set("background-color", "#1976D2")
//            .set("color", "white")
//            .set("border-radius", "8px");
//
//        Button viewActivitiesButton = new Button("Review My Day", event -> {
//            dateDialog.close();
//            openReviewMyDayDialogBox(selectedDate);
//        });
//        viewActivitiesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        viewActivitiesButton.getStyle()
//            .set("background-color", "#FFC107") // Yellow color for contrast
//            .set("color", "white")
//            .set("border-radius", "8px");
//
//        Button addActivityButton = new Button("Add My DAY", event -> {
//            dateDialog.close();
//            openAddReviewMyDay(selectedDate);
//        });
//        addActivityButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
//        addActivityButton.getStyle()
//            .set("background-color", "#F44336") // Red color for emphasis
//            .set("color", "white")
//            .set("border-radius", "8px");
//
//        Button closeButton = new Button("Close", event -> dateDialog.close());
//        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        closeButton.getStyle()
//            .set("background-color", "#808080") // Grey color for neutral option
//            .set("color", "white")
//            .set("border-radius", "8px");
//        
//        
//        Button addActivityButton2 = new Button("Update Activity", event -> {
//            dateDialog.close();
//            openUpdateActivityDialog(selectedDate); // Call the new dialog here
//        });
//        // Layout for buttons
//        HorizontalLayout tasksButtonsLayout = new HorizontalLayout(viewTasksButton, addTaskButton);
//        tasksButtonsLayout.setSpacing(true);
//        tasksButtonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
//
//        HorizontalLayout activitiesButtonsLayout = new HorizontalLayout(addActivityButton2,viewActivitiesButton, addActivityButton);
//        activitiesButtonsLayout.setSpacing(true);
//        activitiesButtonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
//
//        VerticalLayout buttonsLayout = new VerticalLayout(tasksButtonsLayout, activitiesButtonsLayout, closeButton);
//        buttonsLayout.setSpacing(true);
//        buttonsLayout.setAlignItems(Alignment.CENTER);
//        buttonsLayout.getStyle()
//            .set("padding-top", "20px");
//
//        // Dialog layout with proper spacing and alignment
//        VerticalLayout dialogLayout = new VerticalLayout(titleLayout, buttonsLayout);
//        dialogLayout.setPadding(true);
//        dialogLayout.setSpacing(true);
//        dialogLayout.setAlignItems(Alignment.STRETCH); // Align items to stretch the full width
//        dialogLayout.getStyle()
//            .set("padding", "20px");
//
//        dateDialog.add(dialogLayout);
//        dateDialog.open();
//    }

	private void openViewTasksDialog(LocalDate selectedDate) {
		Dialog tasksDialog = new Dialog();

		// Title
		Label titleLabel = new Label("Tasks for " + selectedDate);
		titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");

		// Fetch tasks
		List<Task> tasks = taskService.findByDate(selectedDate);

		VerticalLayout tasksLayout = new VerticalLayout();
		tasksLayout.setPadding(false);
		tasksLayout.setSpacing(false);

		if (tasks != null && !tasks.isEmpty()) {
			tasks.forEach(task -> {
				String timeText = "";

				// Handle null priority
				TaskPriority priority = task.getPriority() != null ? task.getPriority() : TaskPriority.MEDIUM;
				String priorityText = "[" + priority.name().charAt(0) + "] "; // e.g., [H], [M], [L]
				String taskText = priorityText + timeText + task.getDescription();

				Label taskLabel = new Label(taskText);
				taskLabel.getStyle().set("font-size", "14px");

				// Style based on priority
				if (priority == TaskPriority.HIGH) {
					taskLabel.getStyle().set("color", "red");
				} else if (priority == TaskPriority.MEDIUM) {
					taskLabel.getStyle().set("color", "orange");
				} else if (priority == TaskPriority.LOW) {
					taskLabel.getStyle().set("color", "green");
				}

				// Style based on status
				if (task.getStatus() == TaskStatus.COMPLETED) {
					taskLabel.getStyle().set("text-decoration", "line-through");
					taskLabel.getStyle().set("color", "gray");
				} else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
					taskLabel.getStyle().set("font-weight", "bold");
				}

				Button editButton = new Button("Edit", event -> {
					tasksDialog.close();
					openEditTaskDialog(task);
				});
				editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

				HorizontalLayout taskItemLayout = new HorizontalLayout(taskLabel, editButton);
				taskItemLayout.setAlignItems(Alignment.CENTER);
				taskItemLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
				taskItemLayout.setWidthFull();

				tasksLayout.add(taskItemLayout);
			});
		} else {
			tasksLayout.add(new Label("No tasks for this date."));
		}

		Button backButton = new Button("Back", event -> {
			tasksDialog.close();
			openDateDetailsDialog(selectedDate);
		});

		VerticalLayout dialogLayout = new VerticalLayout(titleLabel, tasksLayout, backButton);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.CENTER);

		tasksDialog.add(dialogLayout);
		tasksDialog.open();
	}

	private void openAddTaskDialog(LocalDate selectedDate) {
		// Prevent adding tasks to past dates
		if (selectedDate.isBefore(LocalDate.now())) {
			Notification.show("Cannot add tasks to past dates");
			return;
		}

		Dialog taskDialog = new Dialog();

		Label titleLabel = new Label("Add Task for " + selectedDate);
		titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");

		// Create form layout
		FormLayout formLayout = new FormLayout();

		TextField taskDescription = new TextField("Task Description");
		taskDescription.setPlaceholder("Enter task description");
		taskDescription.setWidthFull();

		// Status ComboBox
		ComboBox<TaskStatus> statusComboBox = new ComboBox<>("Status");
		statusComboBox.setItems(TaskStatus.values());
		statusComboBox.setValue(TaskStatus.NOT_STARTED);
		statusComboBox.setWidthFull();

		// Priority ComboBox
		ComboBox<TaskPriority> priorityComboBox = new ComboBox<>("Priority");
		priorityComboBox.setItems(TaskPriority.values());
		priorityComboBox.setValue(TaskPriority.MEDIUM);
		priorityComboBox.setWidthFull();

		formLayout.add(taskDescription, statusComboBox, priorityComboBox);
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		Button saveButton = new Button("Save", event -> {

			try {
				Task task = new Task();
				task.setDate(selectedDate);
				task.setDescription(taskDescription.getValue());
				task.setStatus(statusComboBox.getValue());
				task.setPriority(
						priorityComboBox.getValue() != null ? priorityComboBox.getValue() : TaskPriority.MEDIUM);

				taskService.save(task);

				taskDialog.close();
				displayMonthCalendar(currentMonth);
				Notification.show("Task added for " + selectedDate);
			} catch (Exception e) {
				Notification.show("An error occurred while saving the task");
				e.printStackTrace();
			}

		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button cancelButton = new Button("Cancel", e -> taskDialog.close());

		HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
		buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
		buttonsLayout.setWidthFull();

		VerticalLayout dialogLayout = new VerticalLayout(titleLabel, formLayout, buttonsLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH);

		taskDialog.add(dialogLayout);
		taskDialog.open();
	}

	private void openEditTaskDialog(Task task) {
		Dialog taskDialog = new Dialog();

		Label titleLabel = new Label("Edit Task");
		titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");

		// Create form layout
		FormLayout formLayout = new FormLayout();

		TextField taskDescription = new TextField("Task Description");
		taskDescription.setValue(task.getDescription());
		taskDescription.setWidthFull();

//        TimePicker startTimePicker = new TimePicker("Start Time");
//        startTimePicker.setValue(task.getStartTime());
//        startTimePicker.setWidthFull();

//        TimePicker endTimePicker = new TimePicker("End Time");
//        endTimePicker.setValue(task.getEndTime());
//        endTimePicker.setWidthFull();

		// Disable past times if the selected date is today
//        if (task.getDate().equals(LocalDate.now())) {
//            startTimePicker.setMin(LocalTime.now());
//            endTimePicker.setMin(LocalTime.now());
//        }

		// Status ComboBox
		ComboBox<TaskStatus> statusComboBox = new ComboBox<>("Status");
		statusComboBox.setItems(TaskStatus.values());
		statusComboBox.setValue(task.getStatus());
		statusComboBox.setWidthFull();

		// Priority ComboBox
		ComboBox<TaskPriority> priorityComboBox = new ComboBox<>("Priority");
		priorityComboBox.setItems(TaskPriority.values());
		priorityComboBox.setValue(task.getPriority() != null ? task.getPriority() : TaskPriority.MEDIUM);
		priorityComboBox.setWidthFull();

		// Recurrence ComboBox
//        ComboBox<TaskRecurrence> recurrenceComboBox = new ComboBox<>("Recurrence");
//        recurrenceComboBox.setItems(TaskRecurrence.values());
//        recurrenceComboBox.setValue(task.getRecurrence() != null ? task.getRecurrence() : TaskRecurrence.NONE);
//        recurrenceComboBox.setWidthFull();

		// Add components to form layout
		// formLayout.add(taskDescription, startTimePicker, endTimePicker,
		// statusComboBox, priorityComboBox, recurrenceComboBox);
		formLayout.add(taskDescription, statusComboBox, priorityComboBox);
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		Button saveButton = new Button("Save", event -> {
			// Validation checks
//            if (taskDescription.isEmpty() || taskDescription.getValue().trim().isEmpty()) {
//                Notification.show("Task description cannot be empty");
//            } else if (task.getDate().isBefore(LocalDate.now())) {
//                Notification.show("Cannot modify tasks in past dates");
//            } else if (startTimePicker.getValue() != null && endTimePicker.getValue() != null &&
//                       startTimePicker.getValue().isAfter(endTimePicker.getValue())) {
//                Notification.show("End time must be after start time");
//            } else if (task.getDate().equals(LocalDate.now()) &&
//                       (startTimePicker.getValue() != null && startTimePicker.getValue().isBefore(LocalTime.now()))) {
//                Notification.show("Start time cannot be in the past");
//            } else if (task.getDate().equals(LocalDate.now()) &&
//                       (endTimePicker.getValue() != null && endTimePicker.getValue().isBefore(LocalTime.now()))) {
//                Notification.show("End time cannot be in the past");
//            } else {
			try {
				task.setDescription(taskDescription.getValue());
				// task.setStartTime(startTimePicker.getValue());
				// task.setEndTime(endTimePicker.getValue());
				task.setStatus(statusComboBox.getValue());
				task.setPriority(
						priorityComboBox.getValue() != null ? priorityComboBox.getValue() : TaskPriority.MEDIUM);
				// task.setRecurrence(recurrenceComboBox.getValue() != null ?
				// recurrenceComboBox.getValue() : TaskRecurrence.NONE);

				taskService.save(task);

				// Optionally, handle recurrence changes here

				taskDialog.close();
				displayMonthCalendar(currentMonth);
				Notification.show("Task updated");
			} catch (Exception e) {
				Notification.show("An error occurred while updating the task");
				e.printStackTrace();
			}
			// }
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		Button deleteButton = new Button("Delete", event -> {
			try {
				taskService.delete(task);
				taskDialog.close();
				displayMonthCalendar(currentMonth);
				Notification.show("Task deleted");
			} catch (Exception e) {
				Notification.show("An error occurred while deleting the task");
				e.printStackTrace();
			}
		});
		deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

		Button cancelButton = new Button("Cancel", e -> taskDialog.close());

		HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);
		buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
		buttonsLayout.setWidthFull();

		VerticalLayout dialogLayout = new VerticalLayout(titleLabel, formLayout, buttonsLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH);

		taskDialog.add(dialogLayout);
		taskDialog.open();
	}

//    private void openReviewMyDayDialogBox(LocalDate selectedDate) {
//        Dialog activitiesDialog = new Dialog();
//
//        // Title
//        Label titleLabel = new Label("Review my Day for : " + selectedDate);
//        titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");
//
//        // Fetch activities
//        List<DailyActivity> activities = dailyActivityService.findByDate(selectedDate);
//
//        VerticalLayout activitiesLayout = new VerticalLayout();
//        activitiesLayout.setPadding(false);
//        activitiesLayout.setSpacing(false);
//
//        if (activities != null && !activities.isEmpty()) {
//            activities.forEach(activity -> {
//                Label activityLabel = new Label(activity.getDescription());
//                activityLabel.getStyle().set("font-size", "14px");
//
//                Button editButton = new Button("Edit", event -> {
//                    activitiesDialog.close();
//                    openEditDailyActivityDialog(activity);
//                });
//                editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//
//                HorizontalLayout activityItemLayout = new HorizontalLayout(activityLabel, editButton);
//                activityItemLayout.setAlignItems(Alignment.CENTER);
//                activityItemLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
//                activityItemLayout.setWidthFull();
//
//                activitiesLayout.add(activityItemLayout);
//            });
//        } else {
//            activitiesLayout.add(new Label("No activities for this date."));
//        }
//
//        Button backButton = new Button("Back", event -> {
//            activitiesDialog.close();
//            openDateDetailsDialog(selectedDate);
//        });
//
//        VerticalLayout dialogLayout = new VerticalLayout(titleLabel, activitiesLayout, backButton);
//        dialogLayout.setPadding(true);
//        dialogLayout.setSpacing(true);
//        dialogLayout.setAlignItems(Alignment.CENTER);
//
//        activitiesDialog.add(dialogLayout);
//        activitiesDialog.open();
//    }
//
//    private void openAddReviewMyDay(LocalDate selectedDate) {
//        // Prevent adding activities to future dates
//        if (selectedDate.isAfter(LocalDate.now())) {
//            Notification.show("Cannot add daily activities to future dates");
//            return;
//        }
//
//        Dialog activityDialog = new Dialog();
//
//        Label titleLabel = new Label("Add Daily Activity for " + selectedDate);
//        titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");
//
//        // Create form layout
//        FormLayout formLayout = new FormLayout();
//
//        TextArea activityDescription = new TextArea("Activity Description");
//        activityDescription.setPlaceholder("Describe your daily activity");
//        activityDescription.setWidthFull();
//        activityDescription.setHeight("150px");
//
//        formLayout.add(activityDescription);
//        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
//
//        Button saveButton = new Button("Save", event -> {
//            // Validation checks
//            if (activityDescription.isEmpty() || activityDescription.getValue().trim().isEmpty()) {
//                Notification.show("Activity description cannot be empty");
//            } else {
//                try {
//                    DailyActivity activity = new DailyActivity();
//                    activity.setDate(selectedDate);
//                    activity.setDescription(activityDescription.getValue());
//                    dailyActivityService.save(activity);
//                    activityDialog.close();
//                    Notification.show("Daily activity added for " + selectedDate);
//                } catch (Exception e) {
//                    Notification.show("An error occurred while saving the daily activity");
//                    e.printStackTrace();
//                }
//            }
//        });
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        Button cancelButton = new Button("Cancel", e -> activityDialog.close());
//
//        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
//        buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
//        buttonsLayout.setWidthFull();
//
//        VerticalLayout dialogLayout = new VerticalLayout(titleLabel, formLayout, buttonsLayout);
//        dialogLayout.setPadding(true);
//        dialogLayout.setSpacing(true);
//        dialogLayout.setAlignItems(Alignment.STRETCH);
//
//        activityDialog.add(dialogLayout);
//        activityDialog.open();
//    }

//    private void openReviewMyDayDialogBox(LocalDate selectedDate) {
//        Dialog activitiesDialog = new Dialog();
//        activitiesDialog.setWidth("500px");
//        activitiesDialog.setHeight("600px");
//        activitiesDialog.getElement().getStyle().set("background-color", "#f9f9f9");
//        
//        // Title with Icon
//        HorizontalLayout titleLayout = new HorizontalLayout();
//        Icon activitiesIcon = VaadinIcon.CALENDAR.create();
//        activitiesIcon.getStyle().set("color", "#4CAF50"); // Green color
//        Label titleLabel = new Label("Review My Day for: " + selectedDate);
//        titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");
//        titleLayout.add(activitiesIcon, titleLabel);
//        titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        titleLayout.setWidthFull();
//        titleLayout.setSpacing(false);
//        
//        // Fetch activities
//         List<DailyNote> activities = dailyNoteService.findByDate(selectedDate);
//        
//        VerticalLayout activitiesLayout = new VerticalLayout();
//        activitiesLayout.setPadding(false);
//        activitiesLayout.setSpacing(false);
//        activitiesLayout.setWidthFull();
//        
//        if (activities != null && !activities.isEmpty()) {
//            for (DailyNote activity : activities) {
//                HorizontalLayout activityItemLayout = new HorizontalLayout();
//                activityItemLayout.setWidthFull();
//                activityItemLayout.setPadding(true);
//                activityItemLayout.getStyle().set("border-bottom", "1px solid #ddd");
//                activityItemLayout.getStyle().set("background-color", "#ffffff");
//                activityItemLayout.getStyle().set("border-radius", "5px");
//                activityItemLayout.getStyle().set("margin-bottom", "10px");
//                activityItemLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
//        
//                // Activity Description
//                Label activityLabel = new Label(activity.getContent());
//                activityLabel.getStyle().set("font-size", "16px").set("color", "#333");
//                activityLabel.setWidthFull();
//        
//                // Edit Button
//                Button editButton = new Button(new Icon(VaadinIcon.EDIT));
//                editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//                editButton.getStyle().set("color", "#1976D2"); // Blue color
//                editButton.addClickListener(event -> {
//                    activitiesDialog.close();
//                    openEditDailyActivityDialog(activity);
//                });
//        
//                activityItemLayout.add(activityLabel, editButton);
//                activitiesLayout.add(activityItemLayout);
//            }
//        } else {
//            Label noActivitiesLabel = new Label("No activities for this date.");
//            noActivitiesLabel.getStyle().set("font-size", "16px").set("color", "#777");
//            activitiesLayout.add(noActivitiesLabel);
//        }
//        
//        // Back Button
//        Button backButton = new Button("Back", event -> activitiesDialog.close());
//        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        backButton.getStyle().set("margin-top", "20px").set("width", "100%");
//        
//        // Dialog Layout
//        VerticalLayout dialogLayout = new VerticalLayout(titleLayout, activitiesLayout, backButton);
//        dialogLayout.setPadding(true);
//        dialogLayout.setSpacing(true);
//        dialogLayout.setAlignItems(Alignment.CENTER);
//        
//        activitiesDialog.add(dialogLayout);
//        activitiesDialog.open();
//    }

	private void openReviewMyDayDialogBox(LocalDate selectedDate) {
		Dialog activitiesDialog = new Dialog();

		// Increase width and reduce height for better layout
		activitiesDialog.setWidth("700px"); // Increased width for better alignment
		activitiesDialog.setHeight("550px"); // Reduced height for content fit

		// Dialog animation (fade-in effect)
		activitiesDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out").set("border-radius", "10px")
				.set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)").set("background-color", "#f9f9f9");

		// Inject CSS for fadeIn animation
		activitiesDialog.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJs("const style = document.createElement('style');"
					+ "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';"
					+ "document.head.appendChild(style);");
		});

		// Title with Icon and Gradient Background
		HorizontalLayout titleLayout = new HorizontalLayout();
		Icon activitiesIcon = VaadinIcon.CALENDAR.create();
		activitiesIcon.getStyle().set("color", "white");

		Label titleLabel = new Label("Review My Day for: " + selectedDate);
		titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold").set("color", "white");

		titleLayout.add(activitiesIcon, titleLabel);
		titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		titleLayout.setWidthFull();
		titleLayout.setSpacing(true);
		titleLayout.getStyle().set("background", "linear-gradient(90deg, #1e3c72, #2a5298)") // Gradient background
				.set("padding", "15px").set("border-top-left-radius", "10px").set("border-top-right-radius", "10px");

		// Fetch activities
		List<DailyNote> activities = dailyNoteService.findByDate(selectedDate);

		VerticalLayout activitiesLayout = new VerticalLayout();
		activitiesLayout.setPadding(false);
		activitiesLayout.setSpacing(true); // Added space between activities
		activitiesLayout.setWidthFull();

		// Display activities or show a message if none
		if (activities != null && !activities.isEmpty()) {
			for (DailyNote activity : activities) {
				HorizontalLayout activityItemLayout = new HorizontalLayout();
				activityItemLayout.setWidthFull();
				activityItemLayout.setPadding(true);
				activityItemLayout.getStyle().set("border-bottom", "1px solid #ddd").set("background-color", "#ffffff")
						.set("border-radius", "8px").set("margin-bottom", "10px")
						.set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

				// Activity Description
				Label activityLabel = new Label(activity.getContent());
				activityLabel.getStyle().set("font-size", "16px").set("color", "#333");
				activityLabel.setWidthFull();

				// Edit Button with Icon
				Button editButton = new Button(new Icon(VaadinIcon.EDIT));
				editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
				editButton.getStyle().set("color", "#1976D2"); // Blue color
				editButton.addClickListener(event -> {
					activitiesDialog.close();
					openEditDailyActivityDialog(activity);
				});

				activityItemLayout.add(activityLabel, editButton);
				activitiesLayout.add(activityItemLayout);
			}
		} else {
			Label noActivitiesLabel = new Label("No activities for this date.");
			noActivitiesLabel.getStyle().set("font-size", "16px").set("color", "#777");
			activitiesLayout.add(noActivitiesLabel);
		}

		// Back Button with styling
		Button backButton = new Button("Back", event -> activitiesDialog.close());
		backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		backButton.getStyle().set("margin-top", "20px").set("width", "100%").set("background-color", "#4CAF50")
				.set("color", "white").set("border-radius", "8px");

		// Dialog Layout with proper alignment
		VerticalLayout dialogLayout = new VerticalLayout(titleLayout, activitiesLayout, backButton);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH); // Stretch to fill width
		dialogLayout.getStyle().set("padding", "20px");

		activitiesDialog.add(dialogLayout);
		activitiesDialog.open();
	}

	/*
	 * private void openAddReviewMyDay(LocalDate selectedDate) { // Prevent adding
	 * activities to future dates if (selectedDate.isAfter(LocalDate.now())) {
	 * Notification.show("Cannot add daily activities to future dates", 3000,
	 * Notification.Position.MIDDLE); return; }
	 * 
	 * Dialog addNoteDialog = new Dialog(); addNoteDialog.setWidth("600px");
	 * addNoteDialog.setHeight("700px");
	 * addNoteDialog.getElement().getStyle().set("background-color", "#f9f9f9");
	 * 
	 * // Title with Icon HorizontalLayout titleLayout = new HorizontalLayout();
	 * Icon addNoteIcon = VaadinIcon.FILE_TEXT_O.create();
	 * addNoteIcon.getStyle().set("color", "#FF9800"); // Orange color Label
	 * titleLabel = new Label("Add Daily Note for: " + selectedDate);
	 * titleLabel.getStyle().set("font-size", "20px").set("font-weight", "bold");
	 * titleLayout.add(addNoteIcon, titleLabel);
	 * titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
	 * titleLayout.setWidthFull(); titleLayout.setSpacing(false);
	 * 
	 * // Form Layout FormLayout formLayout = new FormLayout();
	 * formLayout.setWidthFull(); formLayout.setResponsiveSteps( new
	 * FormLayout.ResponsiveStep("0", 1) );
	 * 
	 * // TextArea for Daily Note Content TextArea noteContentArea = new
	 * TextArea("Today's Experience"); noteContentArea.
	 * setPlaceholder("Share your full-day experiences, reflections, or observations..."
	 * ); noteContentArea.setWidthFull(); noteContentArea.setHeight("300px");
	 * noteContentArea.getStyle().set("font-size", "16px").set("color", "#333");
	 * 
	 * // Save Button Button saveButton = new Button("Save", event -> { String
	 * content = noteContentArea.getValue(); if (content == null ||
	 * content.trim().isEmpty()) { Notification.show("Daily note cannot be empty",
	 * 3000, Notification.Position.MIDDLE); } else { try { DailyNote dailyNote = new
	 * DailyNote(); dailyNote.setDate(selectedDate); dailyNote.setContent(content);
	 * dailyNote.setDone(false); // Default to not done
	 * dailyNoteService.save(dailyNote); addNoteDialog.close();
	 * displayMonthCalendar(currentMonth);
	 * Notification.show("Daily note added successfully!", 3000,
	 * Notification.Position.TOP_CENTER); } catch (Exception e) {
	 * Notification.show("An error occurred while saving the daily note", 3000,
	 * Notification.Position.MIDDLE); e.printStackTrace(); } } });
	 * saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	 * saveButton.getStyle().set("width", "100%").set("margin-top", "20px");
	 * 
	 * // Cancel Button Button cancelButton = new Button("Cancel", event ->
	 * addNoteDialog.close());
	 * cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	 * cancelButton.getStyle().set("width", "100%");
	 * 
	 * // Buttons Layout VerticalLayout buttonsLayout = new
	 * VerticalLayout(saveButton, cancelButton); buttonsLayout.setSpacing(false);
	 * buttonsLayout.setPadding(false); buttonsLayout.setWidthFull();
	 * 
	 * // Dialog Layout VerticalLayout dialogLayout = new
	 * VerticalLayout(titleLayout, formLayout, buttonsLayout);
	 * dialogLayout.setPadding(true); dialogLayout.setSpacing(true);
	 * dialogLayout.setAlignItems(Alignment.CENTER);
	 * 
	 * // Add components to dialog formLayout.add(noteContentArea);
	 * addNoteDialog.add(dialogLayout); addNoteDialog.open(); }
	 */

	private void openAddReviewMyDay(LocalDate selectedDate) {
		// Prevent adding activities to future dates
		if (selectedDate.isAfter(LocalDate.now())) {
			Notification.show("Cannot add daily activities to future dates", 3000, Notification.Position.MIDDLE);
			return;
		}

		Dialog addNoteDialog = new Dialog();

		// Dialog animation (fade-in effect) and rounded corners
		addNoteDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out").set("border-radius", "10px")
				.set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)").set("background-color", "#f9f9f9");

		// Inject CSS for fadeIn animation
		addNoteDialog.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJs("const style = document.createElement('style');"
					+ "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';"
					+ "document.head.appendChild(style);");
		});

		// Title with Icon
		HorizontalLayout titleLayout = new HorizontalLayout();
		Icon addNoteIcon = VaadinIcon.FILE_TEXT_O.create();
		addNoteIcon.getStyle().set("color", "#FF9800"); // Orange color for the icon
		Label titleLabel = new Label("Add Daily Note for: " + selectedDate);
		titleLabel.getStyle().set("font-size", "24px").set("font-weight", "bold").set("color", "white");

		// Title layout with gradient background
		titleLayout.add(addNoteIcon, titleLabel);
		titleLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		titleLayout.setWidthFull();
		titleLayout.setSpacing(false);
		titleLayout.getStyle().set("background", "linear-gradient(90deg, #1e3c72, #2a5298)").set("padding", "15px")
				.set("border-top-left-radius", "10px").set("border-top-right-radius", "10px");

		// Form Layout
		FormLayout formLayout = new FormLayout();
		formLayout.setWidthFull();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

		// TextArea for Daily Note Content
		TextArea noteContentArea = new TextArea("Today's Experience");
		noteContentArea.setPlaceholder("Share your full-day experiences, reflections, or observations...");
		noteContentArea.setWidthFull();
		noteContentArea.setHeight("300px");
		noteContentArea.getStyle().set("font-size", "16px").set("color", "#333");

		// Save Button with styling
		Button saveButton = new Button("Save", event -> {
			String content = noteContentArea.getValue();
			if (content == null || content.trim().isEmpty()) {
				Notification.show("Daily note cannot be empty", 3000, Notification.Position.MIDDLE);
			} else {
				try {
					DailyNote dailyNote = new DailyNote();
					dailyNote.setDate(selectedDate);
					dailyNote.setContent(content);
					dailyNote.setDone(false); // Default to not done
					dailyNoteService.save(dailyNote);
					addNoteDialog.close();
					displayMonthCalendar(currentMonth);
					Notification.show("Daily note added successfully!", 3000, Notification.Position.TOP_CENTER);
				} catch (Exception e) {
					Notification.show("An error occurred while saving the daily note", 3000,
							Notification.Position.MIDDLE);
					e.printStackTrace();
				}
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.getStyle().set("background-color", "#4CAF50") // Green for success
				.set("color", "white").set("border-radius", "8px").set("width", "100%").set("margin-top", "20px");

		// Cancel Button with styling
		Button cancelButton = new Button("Cancel", event -> addNoteDialog.close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		cancelButton.getStyle().set("background-color", "#F44336") // Red for cancel
				.set("color", "white").set("border-radius", "8px").set("width", "100%");

		// Buttons Layout
		VerticalLayout buttonsLayout = new VerticalLayout(saveButton, cancelButton);
		buttonsLayout.setSpacing(true);
		buttonsLayout.setPadding(false);
		buttonsLayout.setWidthFull();
		buttonsLayout.setAlignItems(Alignment.STRETCH); // Align buttons to stretch the full width

		// Dialog Layout
		VerticalLayout dialogLayout = new VerticalLayout(titleLayout, formLayout, buttonsLayout);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.STRETCH); // Align content to stretch

		// Add components to dialog
		formLayout.add(noteContentArea);
		addNoteDialog.add(dialogLayout);
		addNoteDialog.open();
	}



	private void openEditDailyActivityDialog(DailyNote activity) {
		Dialog activityDialog = new Dialog();

		// Adjust dialog dimensions
		activityDialog.setWidth("700px");
		activityDialog.setHeight("550px"); // Reduced height for a more compact look

		// Dialog animation (fade-in effect)
		activityDialog.getElement().getStyle().set("animation", "fadeIn 0.3s ease-out").set("border-radius", "10px")
				.set("box-shadow", "0px 4px 20px rgba(0, 0, 0, 0.1)").set("background-color", "#f9f9f9");

		// Inject CSS for fadeIn animation
		activityDialog.getElement().getNode().runWhenAttached(ui -> {
			ui.getPage().executeJs("const style = document.createElement('style');"
					+ "style.innerHTML = '@keyframes fadeIn { from { opacity: 0; transform: scale(0.95); } to { opacity: 1; transform: scale(1); }}';"
					+ "document.head.appendChild(style);");
		});

		// Header with gradient background and adjusted alignment
		Label titleLabel = new Label("Edit Review May Day");
		titleLabel.getStyle().set("font-size", "10px").set("font-weight", "bold").set("color", "white")
				.set("padding", "15px").set("background", "linear-gradient(90deg, #1e3c72, #2a5298)")
				.set("border-top-left-radius", "10px").set("border-top-right-radius", "10px")
				.set("text-align", "center").set("margin-left", "21px").set("width", "100%");

		// Align header and editor
		FormLayout formLayout = new FormLayout();
		TextArea activityDescription = new TextArea("Today's Experience");
		activityDescription.setPlaceholder("Share your full-day experiences, reflections, or observations...");
		activityDescription.setWidthFull();
		activityDescription.setHeight("300px"); // Adjusted height
		activityDescription.getStyle().set("font-size", "16px").set("color", "#333").set("border-radius", "8px")
				.set("border", "1px solid #ccc").set("padding", "10px");

		formLayout.add(activityDescription);
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
		activityDescription.setValue(activity.getContent());

		// Save button with icon and new styling
		Button saveButton = new Button("Save", new Icon(VaadinIcon.CHECK), event -> {
			if (activityDescription.isEmpty() || activityDescription.getValue().trim().isEmpty()) {
				Notification.show("Activity description cannot be empty");
			} else {
				try {
					// activity.setDescription(activityDescription);
					activity.setContent(activityDescription.getValue());

					dailyNoteService.save(activity);
					activityDialog.close();
					Notification.show("Daily activity updated");
				} catch (Exception e) {
					Notification.show("An error occurred while updating the daily activity");
					e.printStackTrace();
				}
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.getStyle().set("background-color", "#4CAF50").set("color", "white").set("border-radius", "8px");

		// Delete button with icon and new styling
		Button deleteButton = new Button("Delete", new Icon(VaadinIcon.TRASH), event -> {
			try {
				dailyNoteService.delete(activity);
				activityDialog.close();
				Notification.show("Daily activity deleted");
			} catch (Exception e) {
				Notification.show("An error occurred while deleting the daily activity");
				e.printStackTrace();
			}
		});
		deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		deleteButton.getStyle().set("background-color", "#F44336").set("color", "white").set("border-radius", "8px");

		// Cancel button with icon and new styling
		Button cancelButton = new Button("Cancel", new Icon(VaadinIcon.CLOSE), e -> activityDialog.close());
		cancelButton.getStyle().set("background-color", "#808080").set("color", "white").set("border-radius", "8px");

		// Align buttons in the center
		HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);
		buttonsLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Center the buttons
		buttonsLayout.setWidthFull();
		buttonsLayout.getStyle().set("padding-top", "20px").set("padding-bottom", "10px");

		// Main layout with better spacing and alignment
		VerticalLayout dialogLayout = new VerticalLayout(titleLabel, formLayout, buttonsLayout);
		dialogLayout.setPadding(true);
		dialogLayout.setSpacing(true);
		dialogLayout.setAlignItems(Alignment.CENTER); // Center-align content
		dialogLayout.getStyle().set("padding", "20px");

		activityDialog.add(dialogLayout);
		activityDialog.open();
	}


	private LocalDate getNextRecurrenceDate(LocalDate date, TaskRecurrence recurrence) {
		switch (recurrence) {
		case DAILY:
			return date.plusDays(1);
		case WEEKLY:
			return date.plusWeeks(1);
		case MONTHLY:
			return date.plusMonths(1);
		default:
			return date;
		}
	}
}
