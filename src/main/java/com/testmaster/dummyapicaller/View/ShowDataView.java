package com.testmaster.dummyapicaller.View;

import com.testmaster.dummyapicaller.Service.ApiResponseService;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("list-of-saved-responses")
public class ShowDataView extends VerticalLayout {

    public ShowDataView (@Autowired ApiResponseService apiResponseService) {
        H3 title = new H3("Show Responses");
//        ListView
        ListBox<String> box = new ListBox<>();
        box.setItems(apiResponseService.getListOfSavedResponses());
//        Grid<String> grid = new Grid<>();
//        Grid.Column<String> nameColumn = grid.addColumn("API Name");
//        GridListDataView<String> dataView = grid.setItems();

        add(title, box);

//        grid.getHeaderRows().clear();
//        HeaderRow headerRow = grid.appendHeaderRow();
//
//        headerRow.getCell(nameColumn).setComponent(
//                createFilterHeader("Name", personFilter::setFullName));

    }

//    private Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
//        Label label = new Label(labelText);
//        label.getStyle().set("padding-top", "var(--lumo-space-m)")
//                .set("font-size", "var(--lumo-font-size-xs)");
//        TextField textField = new TextField();
//        textField.setValueChangeMode(ValueChangeMode.EAGER);
//        textField.setClearButtonVisible(true);
//        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
//        textField.setWidthFull();
//        textField.getStyle().set("max-width", "100%");
//        textField.addValueChangeListener(
//                e -> filterChangeConsumer.accept(e.getValue()));
//        VerticalLayout layout = new VerticalLayout(label, textField);
//        layout.getThemeList().clear();
//        layout.getThemeList().add("spacing-xs");
//
//        return layout;
//    }
}
