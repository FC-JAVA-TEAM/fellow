package com.example.newcalende.views.xxx.myview;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.application.xxx.entity.DailyActivityXX;
import com.example.application.xxx.entity.DailyMoodXX;
import com.example.application.xxx.entity.NamajEntry;
import com.example.application.xxx.service.DailyActivityServiceXX;
import com.example.application.xxx.service.DailyMoodServiceXX;
import com.example.application.xxx.service.DailyNamajService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Menu;


@CssImport("./styles/styles.css")

@JsModule("https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js")
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 0)
public class CalendarBody extends Div {

    private FlexLayout calendarLayout;
    private LocalDate currentMonth;
   private DailyActivityServiceXX dailyActivityServicexx;
   private ActivityDialogManager activityDialogManager;
   private DailyMoodServiceXX dailyMoodServiceXX;
   private DailyNamajService DailyNamajService;
    public CalendarBody(DailyActivityServiceXX dailyActivityServicexx,ActivityDialogManager activityDialogManager,
    		DailyMoodServiceXX dailyMoodServiceXX,DailyNamajService DailyNamajService) {
    	
    	this.dailyMoodServiceXX =dailyMoodServiceXX;
    	this.dailyActivityServicexx = dailyActivityServicexx; 
    	this.DailyNamajService = DailyNamajService; 
    	 
    	 this.activityDialogManager= activityDialogManager;
    	 setWidth("100%"); // Main Div width set to dailyActivityServicexx%
        this.currentMonth = LocalDate.now().withDayOfMonth(1);

        // Setup layout to hold the full month calendar in a grid style
        calendarLayout = new FlexLayout();
        calendarLayout.setWidth("100%");
        calendarLayout.getStyle().set("display", "grid");
      //  calendarLayout.getStyle().set("grid-template-columns", "repeat(7, 1fr)");
        calendarLayout.getStyle().set("gap", "3px");
        calendarLayout.addClassName("calendar-background");

        Div calendarContainer = new Div();
        calendarContainer.setWidthFull();
        calendarContainer.addClassName("calendar-container");
        calendarContainer.add(calendarLayout);

        // Add components to the main layout
        add(calendarContainer);

        // Display the initial calendar
        displayMonthCalendar(currentMonth);
    }

    
    
    public void displayMonthCalendar(LocalDate month) {
        // Clear the existing components
        calendarLayout.removeAll();

        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        List<DailyActivityXX> activitiesForMonth = dailyActivityServicexx.findByDateBetween(startOfMonth, endOfMonth);

        // Map to store activities by date
        Map<LocalDate, List<DailyActivityXX>> activitiesByDate = activitiesForMonth.stream()
                .collect(Collectors.groupingBy(DailyActivityXX::getDate));

        // Add headers for days of the week
        String[] weekdays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String dayName : weekdays) {
            Div headerCell = new Div();
            headerCell.setText(dayName);
            headerCell.addClassName("day-name");
            
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
        
        List<DailyMoodXX> moodsForMonth = dailyMoodServiceXX.findByDateRange(startOfMonth, endOfMonth);
        Map<LocalDate, DailyMoodXX> moodsByDate = moodsForMonth.stream()
                .collect(Collectors.toMap(DailyMoodXX::getDate, mood -> mood));

        // Fill in the days of the month
        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate dayDate = month.withDayOfMonth(day);
            Div dayCell = createDayCell(dayDate);
            

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

    private Div createDayCell(LocalDate date) {
        Div dayCell = new Div();
        dayCell.addClassName("calendar-cell");
        dayCell.getStyle().set("padding", "5px");
       // dayCell.getStyle().set("width", "100%");
      
        dayCell.getStyle().set("border", "1px solid lightgray");
        dayCell.getStyle().set("display", "flex");
        dayCell.getStyle().set("flex-direction", "column");
        dayCell.getStyle().set("justify-content", "space-around");
        dayCell.getStyle().set("align-items", "center");
        dayCell.getStyle().set("align-content", "space-between");
      //  dayCell.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");
        
        // Highlight today's date
        if (date.equals(LocalDate.now())) {
            addTimeBasedGif(dayCell);
            dayCell.addClassName("today");
            dayCell.getStyle().set("background-color","#e0f7fa");
        }
        
        
        List<NamajEntry> namajEntries = DailyNamajService.findByDate(date);
        int totalNamaj = 6; // Total Namaj count
        int performedCount = 0; // Counter for performed Namaj
        for (NamajEntry entry : namajEntries) {
            if (!entry.getMissed().equalsIgnoreCase("YES")) { // Only count if it wasn't missed
                performedCount++;
            }
        }
        

        
         
    	ProgressBar progressBar = null;
		if (performedCount > 0) {
			double progress = (double) performedCount / totalNamaj;
			progressBar = new ProgressBar();
			progressBar.setValue(progress);
			progressBar.setWidthFull();
			progressBar.getElement().setAttribute("title",
					String.format("%d of %d tasks completed", performedCount, totalNamaj));
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
		

        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        
        List<DailyMoodXX> moodsForMonth = dailyMoodServiceXX.findByDateRange(startOfMonth, endOfMonth);
        Map<LocalDate, DailyMoodXX> moodsByDate = moodsForMonth.stream()
                .collect(Collectors.toMap(DailyMoodXX::getDate, mood -> mood));
        
        
      
        if (moodsByDate.get(date) != null) {
            String moodColor = getMoodColor(moodsByDate.get(date).getMood().getLabel()); // Assuming getMood returns the mood label
         //   dayCell.getStyle().set("background-color", moodColor); // Set background color based on mood
            
            Icon moodIcon = getMoodIcon(moodsByDate.get(date).getMood().getLabel());
            if (moodIcon != null) {
                moodIcon.getStyle().set("position", "absolute");
                moodIcon.getStyle().set("bottom", "5px"); // Position the icon
                moodIcon.getStyle().set("right", "5px");
                dayCell.add(moodIcon); // Add the icon to the cell
            }
        }
        
        

        
        // Create a container for the activities
        VerticalLayout activitiesContainer = new VerticalLayout();
        activitiesContainer.setPadding(false); // Minimize padding
        activitiesContainer.setSpacing(false); // Minimize spacing

        // Fetch activities for the current day
        List<DailyActivityXX> dailyActivities = dailyActivityServicexx.findByDate(date);
        if (dailyActivities != null && !dailyActivities.isEmpty()) {
            for (DailyActivityXX activity : dailyActivities) {
                Div activityLayout = new Div();
              //  activityLayout.setSpacing(false); // No spacing between activities
                activityLayout.setWidthFull(); // Ensure it takes full width
                
                // Create a colored dot for the activity
                Div activityDot = new Div();
                activityDot.setWidth("12px"); // Circle diameter
                activityDot.setHeight("12px");
                activityDot.getStyle().set("border-radius", "50%"); // Make it a circle
                activityDot.getStyle().set("margin-right", "5px"); // Space between dot and text

                // Set the color based on the activity status
                if (activity.isComplete()) {
                    activityDot.getStyle().set("background-color", "#123d12");
                } else {
                    activityDot.getStyle().set("background-color", "#ad3737");
                }

                // Create a label for the activity description
                Label activityLabel = new Label(activity.getDescription());
                activityLabel.getStyle().set("color", activity.isComplete() ? "#123d12" : "#ad3737"); // Set color based on status
               // activityLabel.getStyle().set("font-size", "12px"); // Set font size to small
                activityLabel.getStyle().set("margin-left", "9px");
                activityLayout.getStyle().set("font-size", "11px");
                activityLayout.getStyle().set("display", "flex");
               // activityLayout.getStyle().set("margin-left", "9px");
                activityLayout.getStyle().set("align-items", "baseline");
                // Add dot and label to the layout
                activityLayout.add(activityDot, activityLabel);
                activitiesContainer.add(activityLayout);
            }
        }

        // Add the activities container and the day number at the bottom
        dayCell.add(activitiesContainer);
        dayCell.add(progressBar);
        Label dayNumber = new Label(String.valueOf(date.getDayOfMonth()));
        dayNumber.getStyle().set("font-weight", "bold").set("font-size", "20px");
        dayCell.add(dayNumber);

        // Add click listener to open update activity dialog
        dayCell.addClickListener(event -> activityDialogManager.openUpdateActivityDialog1(date));

        return dayCell;
    }

    private Icon getMoodIcon(String mood) {
        switch (mood.toLowerCase()) {
            case "ok":
                return new Icon(VaadinIcon.STAR);
            case "good":
                return new Icon(VaadinIcon.SMILEY_O);
            case "very good":
                return new Icon(VaadinIcon.HEART_O);
            case "average":
                return new Icon(VaadinIcon.THUMBS_UP);
            case "bad":
                return new Icon(VaadinIcon.MEH_O);
            case "very bad":
                return new Icon(VaadinIcon.FROWN_O);
            default:
                return null; // Return null if no valid mood found
        }
    }

    private String getMoodColor(String mood) {
        switch (mood.toLowerCase()) {
            case "very bad":
                return "red"; // Color for very bad mood
            case "bad":
                return "orange"; // Color for bad mood
            case "average":
                return "yellow"; // Color for average mood
            case "ok":
                return "lightgreen"; // Color for ok mood
            case "good":
                return "green"; // Color for good mood
            case "very good":
                return "lightblue"; // Color for very good mood
            default:
                return "transparent"; // Default color if no mood is matched
        }
    }
    private void addTimeBasedGif(Div dayCell) {
	    LocalTime currentTime = LocalTime.now();
	    String gifPath;

	    if (currentTime.isAfter(LocalTime.of(5, 0)) && currentTime.isBefore(LocalTime.NOON)) {
//	        // Morning: 5 AM - 12 PM
	        gifPath = "images/afternoon.json"; // Replace with your Lottie JSON file path or URL
	      //  gifPath = "images/moon.json"; // Replace with your Lottie JSON file path or URL
	    } else if (currentTime.isAfter(LocalTime.NOON) && currentTime.isBefore(LocalTime.of(18, 0))) {
//	        // Afternoon: 12 PM - 6 PM
	        gifPath = "images/afternoon.json"; // Replace with your Lottie JSON file path or URL
	    } else {
//	        // Night: 6 PM - 5 AM
	        gifPath = "images/moon.json"; // Replace with your Lottie JSON file path or URL
    }
   
	    String lottieAnimation = String.format(
	            "<lottie-player src='%s'  speed='1' " +
	            "style='position: absolute; top: 0; left: 0; width: 100%%; height: 100%%; " +
	            " pointer-events: none; opacity: 0.3;' loop autoplay></lottie-player>",
	            gifPath
	        );
	    Html lottie = new Html(lottieAnimation);
	    dayCell.add(lottie);
	}
	    
  
	
}
