package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue implements Comparable<Issue> {
  private int id;                   //Идентификатор
  private String name;              //Название
  private boolean status;           //Статус - открыт(true) или закрыт(false)
  private String author;            //Автор
  private Collection label = new HashSet<String>();  //Метка - тема
  private String assignee;          //Адресат или правоприемник
  private int lifetime;             //Время существования в минутах

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Issue issue = (Issue) o;
    return id == issue.id &&
            status == issue.status &&
            lifetime == issue.lifetime &&
            Objects.equals(name, issue.name) &&
            Objects.equals(author, issue.author) &&
            Objects.equals(label, issue.label) &&
            Objects.equals(assignee, issue.assignee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, status, author, label, assignee, lifetime);
  }

  @Override
  public String toString() {
    return "Issue{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", status=" + status +
            ", author='" + author + '\'' +
            ", label=" + label +
            ", assignee='" + assignee + '\'' +
            ", lifetime=" + lifetime +
            '}';
  }

  @Override
  public int compareTo(Issue o) {
    Issue p = (Issue) o;
    return this.lifetime - p.lifetime;
  }
}