package com.example.application.views.topic_9_13.grid_selection; // Necessary imports

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;
import java.util.Set;

@Route("1-grid-selection-example")
@Menu(order = 1, icon = LineAwesomeIconUrl.GET_POCKET)
public class GridSelectionView extends VerticalLayout {

  public GridSelectionView() {

    // Create a new grid with the Person class
    Grid<Person> grid = new Grid<>(Person.class);

    // Configure the grid data
    List<Person> people = getPeople(); // method that returns a list of people
    grid.setItems(people);

    // Switch to multi-selection
    grid.setSelectionMode(Grid.SelectionMode.MULTI);

    // Example 3: Using asMultiSelect() to set multiple values
    Set<Person> personsToSelect = Set.of(people.get(0),people.get(2));
    grid.asMultiSelect().setValue(personsToSelect);

    // Add listener for selection
    grid.addSelectionListener(event -> {
      // Get selected items
      Set<Person> selectedItems = event.getAllSelectedItems();
      System.out.println("Selected: " + selectedItems.size() + " items");
    });

    add(grid);
  }

  // Using a Java Record instead of a class
  public record Person(String name, int age) {
    @Override
    public String toString() {
      return name + " (" + age + ")";
    }
  }

  private List<Person> getPeople() {

    return List.of(
         new Person("John", 25),
         new Person("Mary", 30),
         new Person("Carlos", 42)
    );
  }
}