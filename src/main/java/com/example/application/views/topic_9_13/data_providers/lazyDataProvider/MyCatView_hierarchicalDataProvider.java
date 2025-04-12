package com.example.application.views.topic_9_13.data_providers.lazyDataProvider;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.*;
import java.util.stream.Stream;

@Route("2-lazy-data-provider-hierarchical")
@Menu(order = 8, icon = LineAwesomeIconUrl.GAVEL_SOLID)
public class MyCatView_hierarchicalDataProvider extends Div {

  public record MyCatEntity(String name, int age) { }

  private static class MyCatDatasource {

    private final Map<MyCatEntity, List<MyCatEntity>> catFamily = new HashMap<>();

    public MyCatDatasource() {

      // Step 1.0: Define Root cats
      MyCatEntity fluffy = new MyCatEntity("Fluffy", 5);
      MyCatEntity whiskers = new MyCatEntity("Whiskers", 7);


      // Step 2.1: Define 'FluffyChildren' Kittens
      List<MyCatEntity> fluffyKittens = Arrays.asList(
           new MyCatEntity("FluffyJr", 1),
           new MyCatEntity("Snowball", 2)
      );


      // Step 2.2: Define 'WhiskersChildren' Kittens
      List<MyCatEntity> whiskersKittens = Arrays.asList(
           new MyCatEntity("Mittens", 1),
           new MyCatEntity("Boots", 2)
      );

      
      // Build hierarchy
      catFamily.put(null, Arrays.asList(fluffy, whiskers));
      catFamily.put(fluffy, fluffyKittens);
      catFamily.put(whiskers, whiskersKittens);
    }

    public List<MyCatEntity> getChildren(MyCatEntity parent) {

      return catFamily.getOrDefault(parent, Collections.emptyList());
    }

    public boolean hasChildren(MyCatEntity cat) {

      return catFamily.containsKey(cat);
    }
  }

  private static class MyCatService {
    private final MyCatDatasource datasource = new MyCatDatasource();

    public Stream<MyCatEntity> getChildren(MyCatEntity parent, int offset, int limit) {
      return datasource.getChildren(parent).stream().skip(offset).limit(limit);
    }

    public int countChildren(MyCatEntity parent) {

      return datasource.getChildren(parent).size();
    }

    public boolean hasChildren(MyCatEntity cat) {

      return datasource.hasChildren(cat);
    }
  }

  // Hierarchical lazy data provider
  private static class provider extends AbstractBackEndHierarchicalDataProvider<MyCatEntity, Void> {
    private final MyCatService service;

    public provider(MyCatService service) {      this.service = service;    }

    @Override
    protected Stream<MyCatEntity> fetchChildrenFromBackEnd(HierarchicalQuery<MyCatEntity, Void> query) {
      // Get parent from query and fetch children with pagination
      return service.getChildren(query.getParent(), query.getOffset(), query.getLimit());
    }

    @Override
    public int getChildCount(HierarchicalQuery<MyCatEntity, Void> query) {
      // Count immediate children of parent
      return service.countChildren(query.getParent());
    }

    @Override
    public boolean hasChildren(MyCatEntity item) {
      return service.hasChildren(item);
    }
  }

  public MyCatView_hierarchicalDataProvider() {
    MyCatService service = new MyCatService();
    provider dataProvider = new provider(service);

    // Step 1: Create TreeGrid
    TreeGrid<MyCatEntity> catGrid = new TreeGrid<>();
    catGrid.addColumn(MyCatEntity::age).setHeader("Age");

    // Step 2: Set the Hierarchical Provider/Column
    catGrid.setDataProvider(dataProvider);
    catGrid.addHierarchyColumn(MyCatEntity::name).setHeader("Cat Name");

    add(catGrid);
  }
}