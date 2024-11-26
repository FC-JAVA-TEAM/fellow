package com.example.newcalende.views.xxx.myview;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.xxx.service.DailyActivityServiceXX;
import com.example.application.xxx.service.DailyMoodServiceXX;
import com.example.application.xxx.service.DailyNamajService;
import com.example.application.xxx.service.DailyNoteServiceXX;
import com.example.application.xxx.service.TaskServiceXX;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("calendar_4")
@PageTitle("Calendar_4")
@Menu(icon = "line-awesome/svg/file.svg", order = 1)
@CssImport("./styles/styles.css")
@JsModule("https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js")

public class FullMonthCalendarView1 extends Composite<VerticalLayout> {
    private LocalDate currentMonth;
    private CalendarHeader calendarHeader;
    
  
    private CalendarBody calendarBody;

   // private final TaskDialogManager taskDialogManager;
   private  ActivityDialogManager activityDialogManager;
  
   private DailyActivityServiceXX dailyActivityServicexx;
   
  //  private final DailyNoteDialogManager dailyNoteDialogManager;
  //  private final TaskService taskService;
   private DailyNoteServiceXX dailyNoteServiceXX;
   private DailyNoteForm dailyNoteForm;
   
   private  DailyMoodServiceXX dailyMoodService;
   private DailyMoodForm dailyMoodForm;
   private  DailyNamajService dailyNamajService;
   
    public FullMonthCalendarView1(DailyActivityServiceXX dailyActivityServicexx,DailyNoteServiceXX dailyNoteServiceXX,DailyMoodServiceXX dailyMoodService,
    		DailyNamajService dailyNamajService,DailyNamajService DailyNamajService) {
    	
    	this.dailyMoodService =dailyMoodService;
    	this.dailyNamajService =DailyNamajService;
    	
    	this.dailyActivityServicexx = dailyActivityServicexx;
    	this.dailyNamajService=dailyNamajService;
       
        this.currentMonth = LocalDate.now().withDayOfMonth(1);
this.dailyNoteServiceXX=dailyNoteServiceXX;
       
		
       // this.taskDialogManager = new TaskDialogManager(taskService, this::refreshCalendar);
        this.activityDialogManager = new ActivityDialogManager(dailyActivityServicexx, this::refreshCalendar,dailyMoodService,dailyNamajService, dailyNoteServiceXX);
        this.dailyNoteForm = new DailyNoteForm(dailyNoteServiceXX, this::refreshCalendar);
        
        this.dailyMoodForm = new DailyMoodForm(dailyMoodService,this::refreshCalendar);

        // Initialize Header and Body
        this.calendarHeader = new CalendarHeader(
        	    this::navigateToPreviousMonth,
        	    this::navigateToNextMonth,
        	 //   () -> taskDialogManager.openAddTaskDialog(LocalDate.now()), // Add Task
        	  //  () -> taskDialogManager.openViewTasksDialog(),              // View Tasks
        	 //   () -> dailyNoteDialogManager.openAddDailyNoteDialog(LocalDate.now()), // Add Daily Note
        	  //  () -> dailyNoteDialogManager.openViewDailyNotesDialog()             // View Daily Notes
        	                     // Add Activity
        	  //  () -> activityDialogManager.openViewActivitiesDialog(),               // View Activities
        	 //   () -> activityDialogManager.openViewActivitiesDialog()                    // View Summary
        	    
        	    null,null,() -> dailyNoteForm.openAddDailyNoteDialog(LocalDate.now()),() -> dailyNoteForm.openViewDailyNotesDialog(LocalDate.now()),
        	    () -> activityDialogManager.openAddDailyActivityDialog(),
        	    () -> activityDialogManager.openDeleteDailyActivityDialog(),
        	   // () -> activityDialogManager.openUpdateActivityDialog(LocalDate.now())
        	    () -> dailyMoodForm.openAddDailyMoodDialog(LocalDate.now())
        	    
        	);
 
//        this.calendarBody = new CalendarBody(taskService, activityService, dailyActivityService, dailyNoteService,
//                taskDialogManager, activityDialogManager);

       // this.calendarBody = new CalendarBody(taskService, activityService, dailyActivityService, dailyNoteService);
        this.calendarBody = new CalendarBody(dailyActivityServicexx,activityDialogManager,dailyMoodService,dailyNamajService);
       
        
        // Update header with the current month
        calendarHeader.updateMonthYearLabel(currentMonth);

        // Setup main view layout
        getContent().setSizeFull();
      //  getContent().add( calendarBody);
        getContent().add(calendarHeader, calendarBody);
     

        // Display the initial calendar for the current month
        refreshCalendar();
    }

    private void navigateToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
        calendarHeader.updateMonthYearLabel(currentMonth);
        refreshCalendar();
    }

    private void navigateToNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        calendarHeader.updateMonthYearLabel(currentMonth);
        refreshCalendar();
    }

    private void refreshCalendar() {
       calendarBody.displayMonthCalendar(currentMonth); 
    }
    
//    private void refreshCalendar() {
//        calendarBody.displayMonthCalendar(currentMonth, taskDialogManager, activityDialogManager);
//    }
}
