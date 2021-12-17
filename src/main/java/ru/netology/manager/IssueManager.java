package ru.netology.manager;

import ru.netology.domain.Issue;
import ru.netology.repository.IssueRepository;

import java.util.*;

public class IssueManager {
  //Добавление необходимыех полей, конструкторов и методов
  private IssueRepository repository;

  public IssueManager(IssueRepository repository) { this.repository = repository; }

  //Добавление Issue в репозиторий
  public void add(Issue item) { repository.save(item); }

  //Удаление Issue из репозитория по id
  public void removeById(int id) { repository.removeById(id); }

  //Отображение Issue по статусу - открыт(true)
  public Collection<Issue> displayOpen() {
    List<Issue> temps = (List<Issue>) repository.findAll();
    List<Issue> result = new ArrayList();
    for (Issue temp : temps) {
      if (temp.isStatus()) {
        result.add(temp);
      }
    }
    return result;
  }

  //Отображение Issue по статусу - закрыт(false)
  public Collection<Issue> displayClose() {
    List<Issue> temps = (List<Issue>) repository.findAll();
    List<Issue> result = new ArrayList();
    for (Issue temp : temps) {
      if (!temp.isStatus()) {
        result.add(temp);
      }
    }
    return result;
  }

  //Фильтрация Issue по заданному автору
  public Collection<Issue> filterByAuthor(String author) {
    List<Issue> result = (List<Issue>) repository.findAll();
    result.removeIf(el -> !Objects.equals(el.getAuthor(), author));
    return result;
  }

  //Фильтрация Issue по заданной метке
  public Collection<Issue> filterByLabel(String label) {
    List<Issue> result = (List<Issue>) repository.findAll();
    result.removeIf(el -> !Objects.equals(el.getLabel(), label));
    return result;
  }

  //Фильтрация Issue по заданному адресату
  public Collection<Issue> filterByAssignee(String assignee) {
    List<Issue> results = (List<Issue>) repository.findAll();
    results.removeIf(el -> !Objects.equals(el.getAssignee(), assignee));
    return results;
  }

  //Сортировка Issue по заданному критерию
  public Collection<Issue> sortBy(Collection<Issue> sort, Comparator<Issue> comparator) {
    List<Issue> result = (List<Issue>) sort;
    Collections.sort(result, comparator);
    return result;
  }

  //Сортировка Issue по заданному критерию
  public void closingAndOpeningIssueById(int id) {
    if (repository.findById(id).isStatus()) {
      repository.findById(id).setStatus(false);
    } else {
      repository.findById(id).setStatus(true);
    }
  }
}