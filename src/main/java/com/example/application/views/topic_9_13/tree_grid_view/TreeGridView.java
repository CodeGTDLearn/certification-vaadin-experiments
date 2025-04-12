package com.example.application.views.topic_9_13.tree_grid_view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("4-tree-grid-view")
@Menu(order = 4, icon = LineAwesomeIconUrl.GULP)
public class TreeGridView extends VerticalLayout {

  public record MyClient(String name, String email) {}

  public TreeGridView() {
    // Step 1: Create sample clients
    MyClient parentClient = new MyClient("ABC Corporation", "contact@abc.com");
    MyClient childClient = new MyClient("ABC North Branch", "north@abc.com");

    // Step 2: Create hierarchy map to establish parent-child relationships
    Map<MyClient, List<MyClient>> childMap = new HashMap<>();
    childMap.put(parentClient, List.of(childClient));  // Parent has one child
    childMap.put(childClient, List.of());              // Child has no children

    // Step 3: Create the TreeGrid component
    TreeGrid<MyClient> treeGrid = new TreeGrid<>();

    // Step 4: Add columns to display client properties
    treeGrid.addHierarchyColumn(MyClient::name).setHeader("Name");
    treeGrid.addColumn(MyClient::email).setHeader("Email");

    // Step 5: Configure hierarchical data
    // First parameter: root items (top-level nodes)
    // Second parameter: function to fetch children for any item
    treeGrid.setItems(List.of(parentClient), childMap::get);

    // Step 6: Expand the parent node to show its children initially
    treeGrid.expand(parentClient);

    // Step 7: Add the TreeGrid to the layout
    add(treeGrid);
  }
}