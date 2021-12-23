# Настройки добавленные в pom.xml для данного проекта
```xml
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
                <configuration>
                    <failIfNoTests>true</failIfNoTests>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.5</version>
                <executions>
                    <execution>
                        <id>agent-Smith</id>
                        <phase>initialize</phase>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report-agent-Smith</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.8.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>3.6.28</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
# Код Java находящийся в этом репозитории
```Java
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
```
```Java
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
  public Collection<Issue> filterByLabel(Collection<String> label) {
    return filterBy(issue -> issue.getLabel().containsAll(label));
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
    Issue tmp = repository.findById(id);
    if (tmp == null) {
      throw new NotFoundException("Element with id: " + id + " not found");
    }
      if (tmp.isStatus()) {
        repository.findById(id).setStatus(false);
      } else {
        repository.findById(id).setStatus(true);
      }
  }
}
```
```Java
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
```
```Java
package ru.netology.domain;

import java.util.Comparator;

public class OldestComparator implements Comparator<Issue> {
  public int compare(Issue t1, Issue t2) {
    return t2.getLifetime() - t1.getLifetime();
  }
}
```
```Java
package ru.netology.domain;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
```
```Java
package ru.netology.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.domain.Issue;
import ru.netology.domain.NotFoundException;
import ru.netology.domain.OldestComparator;
import ru.netology.repository.IssueRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class IssueManagerTest {
    @Nested     //Тесты без Issue
    public class Empty {
        //Общие данные:
        private final IssueRepository repository = new IssueRepository();
        private final IssueManager issueManager = new IssueManager(repository);

        @Test   //Тест на удаление по id - исключение
        public void shouldRemoveByIdException() {
            assertThrows(NotFoundException.class, () -> {
                repository.removeById(0);
            });
        }

        @Test   //Тест на отображения Issue по статусу - открыт(true) при отсутсвии найденных элементов
        public void shouldDisplayOpenWithNotFoundValue() {
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) issueManager.displayOpen();

            assertEquals(expected, actual, "Отображение Issue по статусу - открыт при отсутсвии найденных элементов");
        }

        @Test   //Тест на отображения Issue по статусу - закрыт(false) при отсутсвии найденных элементов
        public void shouldDisplayCloseWithNotFoundValue() {
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) issueManager.displayClose();

            assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при отсутсвии найденных элементов");
        }

        @Test   //Тест на отображения Issue по Author при отсутсвии найденных элементов
        public void shouldFilterByAuthorWithNotFoundValue() {
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) issueManager.filterByAuthor("e-ts");

            assertEquals(expected, actual, "Отображение Issue по Author при отсутсвии найденных элементов");
        }

        @Test   //Тест на отображения Issue по Label при отсутсвии найденных элементов
        public void shouldFilterByLabelWithNotFoundValue() {
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) issueManager.filterByLabel(Set.of("build"));

            assertEquals(expected, actual, "Отображение Issue по Label при отсутсвии найденных элементов");
        }

        @Test   //Тест на отображения Issue по Assignee при отсутсвии найденных элементов
        public void shouldFilterByAssigneeWithNotFoundValue() {
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) issueManager.filterByAssignee("No one assigned");

            assertEquals(expected, actual, "Отображение Issue по Assignee при отсутсвии найденных элементов");
        }

        @Test   //Тест на закрытие Issue по не существующему id - исключение
        public void shouldClosingAndOpeningIssueByIdWithNotFoundValue() {
            assertThrows(NotFoundException.class, () -> {
                issueManager.closingAndOpeningIssueById(0);
            });
        }
    }

    @Nested     //Тесты с одним Issue
    public class SingleIssue {
        //Общие данные:
        private final IssueRepository repository = new IssueRepository();
        private final IssueManager issueManager = new IssueManager(repository);

        private final Issue zero = new Issue(2775, "apiguardian-api version mismatch between source code and published POM", false,
                "e-ts", Set.of("theme: build"),
                "No one assigned", 54_720);

        @BeforeEach
        public void setUp() {
            issueManager.add(zero);
        }

        @Test   //Тест на удаление по id
        public void shouldRemoveById() {
            issueManager.removeById(2775);
            List<Issue> expected = List.of();
            List<Issue> actual = (List<Issue>) repository.findAll();

            assertEquals(expected, actual, "Удаление Issue по id");
        }

        @Test   //Тест на открытие и закрытие Issue по id
        public void shouldClosingAndOpeningIssueById() {
            issueManager.closingAndOpeningIssueById(2775);
            assertTrue(repository.findById(2775).isStatus(), "Открытие Issue по id");

            issueManager.closingAndOpeningIssueById(2775);
            assertFalse(repository.findById(2775).isStatus(), "Закрытие Issue по id");
        }

        @Test   //Тест на отображения Issue по статусу - открыт(true) при одном найденном элементе
        public void shouldDisplayOpenWithSingleValue() {
            issueManager.closingAndOpeningIssueById(2775);
            List<Issue> expected = List.of(zero);
            List<Issue> actual = (List<Issue>) issueManager.displayOpen();

            assertEquals(expected, actual, "Отображение Issue по статусу - открыт при одном найденном элементе");
        }

        @Test   //Тест на отображения Issue по статусу - закрыт(false) при одном найденном элементе
        public void shouldDisplayCloseWithSingleValue() {
            List<Issue> expected = List.of(zero);
            List<Issue> actual = (List<Issue>) issueManager.displayClose();

            assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при одном найденном элементе");
        }

        @Test   //Тест на отображения Issue по Author при отсутсвии найденных элементов
        public void shouldFilterByAuthorWithSingleValue() {
            List<Issue> expected = List.of(zero);
            List<Issue> actual = (List<Issue>) issueManager.filterByAuthor("e-ts");

            assertEquals(expected, actual, "Отображение Issue по Author при одном найденном элементе");
        }

        @Test   //Тест на отображения Issue по Label при отсутсвии найденных элементов
        public void shouldFilterByLabelWithSingleValue() {
            List<Issue> expected = List.of(zero);
            List<Issue> actual = (List<Issue>) issueManager.filterByLabel(Set.of("theme: build"));

            assertEquals(expected, actual, "Отображение Issue по Label при одном найденном элементе");
        }

        @Test   //Тест на отображения Issue по Assignee при отсутсвии найденных элементов
        public void shouldFilterByAssigneeWithSingleValue() {
            List<Issue> expected = List.of(zero);
            List<Issue> actual = (List<Issue>) issueManager.filterByAssignee("No one assigned");

            assertEquals(expected, actual, "Отображение Issue по Assignee при одном найденном элементе");
        }
    }

    @Nested     //Тесты с десятью Issue
    public class MultipleIssues {
        //Общие данные:
        private final IssueRepository repository = new IssueRepository();
        private final IssueManager issueManager = new IssueManager(repository);
        private final OldestComparator comparator = new OldestComparator();

        private final Issue zero = new Issue(2789, "List observable test engines and show their properties", true,
                "sormuras", Set.of("component: Platform", "type: new feature"),
                "sormuras", 11520);
        private final Issue first = new Issue(2788, "Make console launcher --classpath argument parser work like java's when referring to a jar directory", true,
                "mbucc", Set.of("component: Platform", "status: waiting-for-feedback", "theme: documentation", "type: new feature"),
                "No one assigned", 15_840);
        private final Issue second = new Issue(2787, "Extend ParallelExecutionConfiguration to specify a predicate for determing if a pool is saturated", true,
                "klease", Set.of("component: Platform", "status: in progress", "type: enhancement"),
                "klease", 15_880);
        private final Issue third = new Issue(2779, "[reflection] ReflectionSupport to return Streams", true,
                "kriegfrj", Set.of("component: Jupiter", "component: Platform", "status: in progress", "theme: extensions", "theme: programming model", "type: enhancement"),
                "Attyuttam", 48_960);
        private final Issue fourth = new Issue(2778, "Using a single aggregator on the method level", false,
                "akruijff", Set.of("component: Jupiter", "status: declined", "theme: parameterized tests", "theme: programming model"),
                "No one assigned", 38_880);
        private final Issue fifth = new Issue(2775, "apiguardian-api version mismatch between source code and published POM", false,
                "e-ts", Set.of("theme: build"),
                "No one assigned", 54_720);
        private final Issue sixth = new Issue(2774, "[FR] Mark tests preconditions / dependencies", false,
                "Hu1buerger", Set.of("component: Jupiter", "status: duplicate", "theme: programming model"),
                "No one assigned", 56_160);
        private final Issue seventh = new Issue(2772, "Overview of shadowed dependencies in standalone JAR", false,
                "sormuras", Set.of("component: Platform", "theme: documentation"),
                "marcphilipp", 57_600);
        private final Issue eighth = new Issue(2771, "Support Kotlin top level functions as test methods", true,
                "mmerdes", Set.of("component: Jupiter", "component: Kotlin, theme: programming model"),
                "mmerdes", 62_640);
        private final Issue ninth = new Issue(2764, "Fix intermittent module-path ordering issue in MultiReleaseJarTests.checkDefault", false,
                "marcphilipp", Set.of("3rd-party: Maven Surefire", "status: new, type: task"),
                "sormuras", 64_800);

        @BeforeEach
        public void setUp() {
            issueManager.add(ninth);
            issueManager.add(seventh);
            issueManager.add(fifth);
            issueManager.add(third);
            issueManager.add(first);
            issueManager.add(zero);
            issueManager.add(second);
            issueManager.add(fourth);
            issueManager.add(sixth);
            issueManager.add(eighth);
        }

        @Test   //Тест на отображения Issue по статусу - открыт(true) при сортировке newest
        public void shouldDisplayOpenWithCompareTo() {
            List<Issue> expected = List.of(zero, first, second, third, eighth);
            List<Issue> actual = (List<Issue>) issueManager.displayOpen();
            issueManager.sortBy(actual, Issue::compareTo);

            assertEquals(expected, actual, "Отображение Issue по статусу - открыт при сортировке newest");
        }

        @Test   //Тест на отображения Issue по статусу - открыт(true) при сортировке oldest
        public void shouldDisplayOpenWithComparator() {
            List<Issue> expected = List.of(eighth, third, second, first, zero);
            List<Issue> actual = (List<Issue>) issueManager.displayOpen();
            issueManager.sortBy(actual, comparator);

            assertEquals(expected, actual, "Отображение Issue по статусу - открыт при сортировке oldest");
        }

        @Test   //Тест на отображения Issue по статусу - закрыт(false) при сортировке newest
        public void shouldDisplayCloseWithCompareTo() {
            List<Issue> expected = List.of(fourth, fifth, sixth, seventh, ninth);
            List<Issue> actual = (List<Issue>) issueManager.displayClose();
            issueManager.sortBy(actual, Issue::compareTo);

            assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при сортировке newest");
        }

        @Test   //Тест на отображения Issue по статусу - закрыт(false) при сортировке oldest
        public void shouldDisplayCloseWithComparator() {
            List<Issue> expected = List.of(ninth, seventh, sixth, fifth, fourth);
            List<Issue> actual = (List<Issue>) issueManager.displayClose();
            issueManager.sortBy(actual, comparator);

            assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при сортировке oldest");
        }

        @Test   //Тест фильтрации Issue по автору при сортировке newest
        public void shouldFilterByAuthorWithCompareTo() {
            List<Issue> expected = List.of(zero, seventh);
            List<Issue> actual = (List<Issue>) issueManager.filterByAuthor("sormuras");
            issueManager.sortBy(actual, Issue::compareTo);

            assertEquals(expected, actual, "Фильтрация Issue по author при сортировке newest");
        }

        @Test   //Тест фильтрации Issue по автору при сортировке oldest
        public void shouldFilterByAuthorWithComparator() {
            List<Issue> expected = List.of(seventh, zero);
            List<Issue> actual = (List<Issue>) issueManager.filterByAuthor("sormuras");
            issueManager.sortBy(actual, comparator);

            assertEquals(expected, actual, "Фильтрация Issue по author при сортировке oldest");
        }

        @Test   //Тест фильтрации Issue по label при сортировке newest
        public void shouldFilterByLabelWithCompareTo() {
            List<Issue> expected = List.of(zero, first, second, third, seventh);
            List<Issue> actual = (List<Issue>) issueManager.filterByLabel(Set.of("component: Platform"));
            issueManager.sortBy(actual, Issue::compareTo);

            assertEquals(expected, actual, "Фильтрация Issue по label при сортировке newest");
        }

        @Test   //Тест фильтрации Issue по label при сортировке oldest
        public void shouldFilterByLabelWithComparator() {
            List<Issue> expected = List.of(seventh, third, second, first, zero);
            List<Issue> actual = (List<Issue>) issueManager.filterByLabel(Set.of("component: Platform"));
            issueManager.sortBy(actual, comparator);

            assertEquals(expected, actual, "Фильтрация Issue по label при сортировке oldest");
        }

        @Test   //Тест фильтрации Issue по assignee при сортировке newest
        public void shouldFilterByAssigneeWithCompareTo() {
            List<Issue> expected = List.of(first, fourth, fifth, sixth);
            List<Issue> actual = (List<Issue>) issueManager.filterByAssignee("No one assigned");
            issueManager.sortBy(actual, Issue::compareTo);

            assertEquals(expected, actual, "Фильтрация Issue по assignee при сортировке newest");
        }

        @Test   //Тест фильтрации Issue по assignee при сортировке oldest
        public void shouldFilterByAssigneeWithComparator() {
            List<Issue> expected = List.of(sixth, fifth, fourth, first);
            List<Issue> actual = (List<Issue>) issueManager.filterByAssignee("No one assigned");
            issueManager.sortBy(actual, comparator);

            assertEquals(expected, actual, "Фильтрация Issue по assignee при сортировке oldest");
        }
    }
}
```