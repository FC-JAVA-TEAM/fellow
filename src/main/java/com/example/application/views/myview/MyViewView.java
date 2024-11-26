package com.example.application.views.myview;


import com.example.application.service.NewbornBuffaloService;
import com.example.application.service.PurchasedBuffaloService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Buffalo Add View")
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 0)
@Route(value = "MainView")
public class MyViewView extends Composite<VerticalLayout> {

	  private final NewbornBuffaloService newbornBuffaloService;
	    private final PurchasedBuffaloService purchasedBuffaloService;
	    public MyViewView(NewbornBuffaloService newbornBuffaloService, PurchasedBuffaloService purchasedBuffaloService) {
	        this.newbornBuffaloService = newbornBuffaloService;
	        this.purchasedBuffaloService = purchasedBuffaloService;
        HorizontalLayout layoutRow = new HorizontalLayout();
        TabSheet tabSheet = new TabSheet();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Hr hr = new Hr();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        layoutRow.add(tabSheet);
        getContent().add(layoutRow2);
        layoutRow2.add(layoutColumn2);
        layoutColumn2.add(hr);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        tabSheet.add("Dashboard", new Div(new Text("This is the Dashboard tab content")));
        
//        PurchasedBuffaloFormView purchasedBuffaloFormView = new PurchasedBuffaloFormView(purchasedBuffaloService);
//        tabSheet.add("Purchased Buffalo", purchasedBuffaloFormView);
//        
//        NewbornBuffaloFormView newbornBuffaloFormView = new NewbornBuffaloFormView(newbornBuffaloService);
//        tabSheet.add("Newborn Buffalo", newbornBuffaloFormView);

      //  tabSheet.add("Purchased Bufflo", NewbornBuffaloFormView.class);
      //  tabSheet.add("Purchased Bufflo", new Div(new Text("Purchase from other")));
        //tabSheet.add("Born Bufflo", new Div(new Text("Born here")));
    }
}

