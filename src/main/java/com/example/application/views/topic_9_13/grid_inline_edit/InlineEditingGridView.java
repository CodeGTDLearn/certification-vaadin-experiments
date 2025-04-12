package com.example.application.views.topic_9_13.grid_inline_edit;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@Route("5-grid-edit")
@Menu(order = 5, icon = LineAwesomeIconUrl.FROWN_OPEN)
public class InlineEditingGridView extends VerticalLayout {

  public record MyClient(String id, String name) {
  }

  public InlineEditingGridView() {

    // Step 1: Create a grid with Client data
    Grid<MyClient> grid = new Grid<>();

    // Step 2: Add ID column (read-only)
    grid.addColumn(MyClient::id)
        .setHeader("ID");

    // Step 3: Add Name column with editor component
    Grid.Column<MyClient> nameColumn =
         grid.addColumn(MyClient::name)
             .setHeader("Client Name");

    // Step 4: Create text field for editing the name
    TextField clientInlineEditNameField = new TextField();
    clientInlineEditNameField.setWidthFull();
    nameColumn.setEditorComponent(clientInlineEditNameField);

    // Step 5: Get the editor instance from the grid
    Editor<MyClient> editor = grid.getEditor();

    // Step 6: Create binder for data binding
    Binder<MyClient> binder = new Binder<>(MyClient.class);
    editor.setBinder(binder);

    // Step 7: Bind editor field to the name property
    binder
         .forField(clientInlineEditNameField)
         .bind(
              MyClient::name,
              (myClient, newName) -> new MyClient(myClient.id(), newName)
         );

    // Step 8: Add double-click listener to start editing
    grid
         .addItemDoubleClickListener(
              doubleClick -> {
                editor.editItem(doubleClick.getItem());
                clientInlineEditNameField.focus();
              });

    // Step 9: Set close action to save changes when editor is closed
    editor.addCloseListener(e -> {
      if (editor.isOpen()) {
        Notification.show("Changes saved");
      }
    });

    // Step 10: Set sample data
    grid.setItems(List.of(new MyClient("C001", "John Smith")));

    // Step 11: Add the grid to the layout
    add(grid);
    setPadding(true);
  }
}