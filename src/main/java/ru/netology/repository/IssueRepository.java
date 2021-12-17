package ru.netology.repository;

import ru.netology.domain.NotFoundException;
import ru.netology.domain.Issue;

import java.util.ArrayList;
import java.util.Collection;

public class IssueRepository {
  private Collection<Issue> items = new ArrayList<>();

  public void save(Issue item) {
    items.add(item);
  }

  public Collection<Issue> findAll() {
    return items;
  }

  public Issue findById(int id) {
    for (Issue item : items) {
      if (item.getId() == id) {
        return item;
      }
    }
    return null;
  }

  public void removeById(int id) {
    if (findById(id) == null) {
      throw new NotFoundException("Element with id: " + id + " not found");
    }
    items.removeIf(el -> el.getId() == id);
  }
}