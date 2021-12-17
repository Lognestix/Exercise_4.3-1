package ru.netology.manager;

import ru.netology.domain.Issue;
import ru.netology.domain.NotFoundException;
import ru.netology.repository.IssueRepository;

import java.util.*;
import java.util.function.Predicate;

public class IssueManager {
  private IssueRepository repository;

  public IssueManager(IssueRepository repository) { this.repository = repository; }

  //Добавление Issue в репозиторий
  public void add(Issue item) { repository.save(item); }

  //Удаление Issue из репозитория по id
  public void removeById(int id) { repository.removeById(id); }

  //Отображение Issue по статусу - открыт(true)
  public Collection<Issue> displayOpen() {
    List<Issue> result = new ArrayList();
    for (Issue tmp : repository.findAll()) {
      if (tmp.isStatus()) {
        result.add(tmp);
      }
    }
    return result;
  }

  //Отображение Issue по статусу - закрыт(false)
  public Collection<Issue> displayClose() {
    List<Issue> result = new ArrayList();
    for (Issue tmp : repository.findAll()) {
      if (!tmp.isStatus()) {
        result.add(tmp);
      }
    }
    return result;
  }

  //Внутренний метод фильтрации
  private Collection<Issue> filterBy(Predicate<Issue> filter) {
    List<Issue> result = new ArrayList<>();
    for (Issue tmp : repository.findAll()) {
      if (filter.test(tmp)) {
        result.add(tmp);
      }
    }
    return result;
  }

  //Фильтрация Issue по заданному автору
  public Collection<Issue> filterByAuthor(String author) {
    return filterBy(issue -> issue.getAuthor().equals(author));
  }

  //Фильтрация Issue по заданной метке
  public Collection<Issue> filterByLabel(String label) {
    return filterBy(issue -> issue.getLabel().contains(label));
  }

  //Фильтрация Issue по заданному адресату
  public Collection<Issue> filterByAssignee(String assignee) {
    return filterBy(issue -> issue.getAssignee().equals(assignee));
  }

  //Сортировка Issue по заданному критерию
  public Collection<Issue> sortBy(Collection<Issue> sort, Comparator<Issue> comparator) {
    List<Issue> result = (List<Issue>) sort;
    Collections.sort(result, comparator);
    return result;
  }

  //Открытие или закрытие Issue по заданному id
  public void closingAndOpeningIssueById(int id) {
    if (repository.findById(id) == null) {
      throw new NotFoundException("Element with id: " + id + " not found");
    }
    if (repository.findById(id).isStatus()) {
      repository.findById(id).setStatus(false);
    } else {
      repository.findById(id).setStatus(true);
    }
  }
}