package com.testmaster.dummyapicaller;

import com.testmaster.dummyapicaller.Enum.Views;
import com.testmaster.dummyapicaller.View.SaveView;
import com.testmaster.dummyapicaller.View.ShowDataView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the username and a button
 * that shows a greeting message in a notification.
 */

@Route("")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css")
public class MainView extends AppLayout {

    public MainView() {
        createDrawer();
    }

    public void createDrawer() {
        DrawerToggle toggle = new DrawerToggle();

        H3 title = new H3("Dummy API Caller");
        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DOWNLOAD, "Save API Response", Views.SAVE_API),
                createTab(VaadinIcon.LIST, "Show Saved Responses", Views.SHOW_RESPONSE_LIST));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Views view) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        switch (view) {
            case SAVE_API:
                link.setRoute(SaveView.class);
                break;
            case SHOW_RESPONSE_LIST:
                link.setRoute(ShowDataView.class);
                break;
            default:
                break;
        }

        link.setTabIndex(-1);
        return new Tab(link);
    }

}
